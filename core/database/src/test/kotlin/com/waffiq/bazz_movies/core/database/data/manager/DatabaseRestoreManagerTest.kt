package com.waffiq.bazz_movies.core.database.data.manager

import android.net.Uri
import com.waffiq.bazz_movies.core.database.data.model.DatabaseBackup
import com.waffiq.bazz_movies.core.database.testutils.BaseDatabaseBackupManagerTest
import com.waffiq.bazz_movies.core.database.utils.DbResult
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream

class DatabaseRestoreManagerTest : BaseDatabaseBackupManagerTest() {

  @Test
  fun restoreFromUri_whenValidBackupProvided_expectedSuccessAndDatabaseWipedAndInserted() =
    runTest {
      val legacyBackup = generateBackupJson(
        payload = createPayload(
          favorites = listOf(validBackupEntry, secondBackupEntry),
        ),
      )
      val inputStream =
        ByteArrayInputStream(jsonPretty.encodeToString(legacyBackup).toByteArray(Charsets.UTF_8))
      every { contentResolver.openInputStream(mockUri) } returns inputStream

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Success)
      coVerify(exactly = 1) { favoriteDao.clearAndInsert(any()) }
    }

  @Test
  fun restoreFromUri_whenInputStreamCannotBeOpened_expectedErrorResult() =
    runTest {
      every { contentResolver.openInputStream(mockUri) } returns null

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertEquals("Cannot open input stream", (result as DbResult.Error).errorMessage)
    }

  @Test
  fun restoreFromUri_whenJsonIsMalformed_expectedErrorResult() =
    runTest {
      val malformedJson = "{ invalid json structure }"
      val inputStream = ByteArrayInputStream(malformedJson.toByteArray(Charsets.UTF_8))
      every { contentResolver.openInputStream(mockUri) } returns inputStream

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertTrue(
        (result as DbResult.Error).errorMessage.contains("Invalid backup file: not valid JSON"),
      )
    }

  @Test
  fun restoreFromUri_whenVersionIsInvalidZeroOrNegative_expectedErrorResult() =
    runTest {
      val badBackup = generateBackupJson(
        payload = createPayload(
          favorites = listOf(validBackupEntry),
          version = 0, // invalid version
        ),
      )
      val inputStream =
        ByteArrayInputStream(jsonPretty.encodeToString(badBackup).toByteArray(Charsets.UTF_8))
      every { contentResolver.openInputStream(mockUri) } returns inputStream

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertEquals("Invalid backup version", (result as DbResult.Error).errorMessage)
    }

  @Test
  fun restoreFromUri_whenVersionIsNewerThanSupported_expectedErrorResult() =
    runTest {
      val badBackup = generateBackupJson(
        payload = createPayload(
          favorites = listOf(validBackupEntry),
          version = DatabaseBackup.BACKUP_VERSION + 1, // newer unsupported framework
        ),
      )
      val inputStream =
        ByteArrayInputStream(jsonPretty.encodeToString(badBackup).toByteArray(Charsets.UTF_8))
      every { contentResolver.openInputStream(mockUri) } returns inputStream

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertEquals(
        "Backup is from a newer app version. Please update the app.",
        (result as DbResult.Error).errorMessage,
      )
    }

  @Test
  fun restoreFromUri_whenChecksumIsCorruptedOrTampered_expectedErrorResult() =
    runTest {
      val badBackup = generateBackupJson(
        payload = createPayload(favorites = listOf(validBackupEntry)),
        checksum = "wrong_or_manipulated_checksum_hash",
      )
      val inputStream =
        ByteArrayInputStream(jsonPretty.encodeToString(badBackup).toByteArray(Charsets.UTF_8))
      every { contentResolver.openInputStream(mockUri) } returns inputStream

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertEquals(
        "Backup file is corrupted or has been modified",
        (result as DbResult.Error).errorMessage,
      )
    }

  @Test
  fun restoreFromUri_whenLegacyBackupHasNullChecksum_expectedSuccessWithoutVerifyingChecksum() =
    runTest {
      val legacyBackup = generateBackupJson(
        payload = createPayload(favorites = listOf(validBackupEntry)),
        checksum = null,
      )
      val inputStream =
        ByteArrayInputStream(jsonPretty.encodeToString(legacyBackup).toByteArray(Charsets.UTF_8))
      every { contentResolver.openInputStream(mockUri) } returns inputStream

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Success)
      coVerify(exactly = 1) { favoriteDao.clearAndInsert(any()) }
    }

  @Test
  fun restoreFromUri_whenBackupContainsNoValidEntries_expectedErrorResult() =
    runTest {
      // create invalid data that trigger`.isValid()` inside FavoriteBackupEntry whis was
      // the mediaType should not empty and mediaId should more that 0
      val backup = generateBackupJson(
        payload = createPayload(favorites = listOf(invalidBackupEntry)),
      )
      val inputStream =
        ByteArrayInputStream(jsonPretty.encodeToString(backup).toByteArray(Charsets.UTF_8))
      every { contentResolver.openInputStream(mockUri) } returns inputStream

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertEquals("Backup contains no valid entries", (result as DbResult.Error).errorMessage)
    }

  @Test
  fun restoreFromUri_whenClassicTryFinallyThrowsException_expectedStreamClosedAndError() =
    runTest {
      val mockUri = mockk<Uri>()
      val mockInputStream = mockk<InputStream>()

      every { contentResolver.openInputStream(mockUri) } returns mockInputStream
      // error during the reading phase
      every { mockInputStream.read(any<ByteArray>(), any(), any()) } throws
        IOException("Read crash")

      every { mockInputStream.close() } returns Unit

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertEquals("Read crash", (result as DbResult.Error).errorMessage)
      coVerify(exactly = 1) { mockInputStream.close() }
    }

  @Test
  fun restoreFromUri_whenExceptionHasNullMessage_expectedErrorResultWithDefaultMessage() =
    runTest {
      // exception with no message
      every { contentResolver.openInputStream(mockUri) } throws RuntimeException(null as String?)

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertEquals("Restore failed", (result as DbResult.Error).errorMessage)
    }
}
