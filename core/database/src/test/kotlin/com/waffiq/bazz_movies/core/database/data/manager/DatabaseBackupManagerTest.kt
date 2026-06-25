package com.waffiq.bazz_movies.core.database.data.manager

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import com.waffiq.bazz_movies.core.database.data.model.BackupPayload
import com.waffiq.bazz_movies.core.database.data.model.DatabaseBackup
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDao
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.favoriteTvEntity
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.database.utils.FavoriteMapper.toBackupEntry
import com.waffiq.bazz_movies.core.database.utils.sha256
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

class DatabaseBackupManagerTest {

  private val context: Context = mockk(relaxed = true)
  private val contentResolver: ContentResolver = mockk()
  private val favoriteDao: FavoriteDao = mockk(relaxed = true)
  private val testDispatcher = StandardTestDispatcher()
  private val appVersion = "1.0.0"
  private val mockUri = mockk<Uri>()

  private lateinit var manager: DatabaseBackupManager

  private val jsonCompact = Json { ignoreUnknownKeys = true }
  private val jsonPretty = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
  }

  private fun createMockEntity(id: Int) = favoriteTvEntity.copy(id = id)
  private fun createMockBackupEntry(id: Int) = favoriteTvEntity.toBackupEntry().copy(mediaId = id)

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
    mockkStatic(Log::class)
    every { Log.e(any(), any(), any()) } returns 0

    every { context.contentResolver } returns contentResolver

    manager = DatabaseBackupManager(
      context = context,
      favoriteDao = favoriteDao,
      ioDispatcher = testDispatcher,
      appVersion = appVersion,
    )
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
    unmockkAll()
  }

  @Test
  fun backupToUri_whenDatabaseHasData_expectedSuccessAndCorrectJsonWritten() =
    runTest {
      val entities = listOf(createMockEntity(1), createMockEntity(2))
      coEvery { favoriteDao.getAllFavorites() } returns entities

      val outputStream = ByteArrayOutputStream()
      every { contentResolver.openOutputStream(mockUri) } returns outputStream

      val result = manager.backupToUri(mockUri)

      assertTrue(result is DbResult.Success)
      val writtenString = outputStream.toString(Charsets.UTF_8.name())

      // verify the validity
      val decodedBackup = jsonPretty.decodeFromString<DatabaseBackup>(writtenString)
      assertEquals(DatabaseBackup.BACKUP_VERSION, decodedBackup.version)
      assertEquals(appVersion, decodedBackup.appVersion)
      assertEquals(2, decodedBackup.favorites.size)

      // verify checksum integrity matches production logic
      val expectedPayload = BackupPayload(
        version = decodedBackup.version,
        createdAt = decodedBackup.createdAt,
        appVersion = decodedBackup.appVersion,
        favorites = decodedBackup.favorites,
      )
      val expectedChecksum = jsonCompact.encodeToString(expectedPayload).sha256()
      assertEquals(expectedChecksum, decodedBackup.checksum)
    }

  @Test
  fun backupToUri_whenOutputStreamCannotBeOpened_expectedErrorResult() =
    runTest {
      coEvery { favoriteDao.getAllFavorites() } returns emptyList()
      every { contentResolver.openOutputStream(mockUri) } returns null

      val result = manager.backupToUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertEquals("Cannot open output stream", (result as DbResult.Error).errorMessage)
    }

  @Test
  fun backupToUri_whenDaoThrowsException_expectedErrorResult() =
    runTest {
      coEvery {
        favoriteDao.getAllFavorites()
      } throws RuntimeException("Database disk image is malformed")

      val result = manager.backupToUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertEquals("Database disk image is malformed", (result as DbResult.Error).errorMessage)
    }

  @Test
  fun backupToUri_whenExceptionHasNullMessage_expectedErrorResultWithDefaultMessage() =
    runTest {
      coEvery { favoriteDao.getAllFavorites() } throws RuntimeException(null as String?)

      val result = manager.backupToUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertEquals("Backup failed", (result as DbResult.Error).errorMessage)
    }

  @Test
  fun restoreFromUri_whenValidBackupProvided_expectedSuccessAndDatabaseWipedAndInserted() =
    runTest {
      val favorites = listOf(createMockBackupEntry(10), createMockBackupEntry(20))

      val payload = BackupPayload(
        version = DatabaseBackup.BACKUP_VERSION,
        createdAt = System.currentTimeMillis(),
        appVersion = appVersion,
        favorites = favorites,
      )
      val validBackup = DatabaseBackup(
        version = payload.version,
        createdAt = payload.createdAt,
        appVersion = payload.appVersion,
        favorites = payload.favorites,
        checksum = jsonCompact.encodeToString(payload).sha256(),
      )

      val rawJson = jsonPretty.encodeToString(validBackup)
      val inputStream = ByteArrayInputStream(rawJson.toByteArray(Charsets.UTF_8))
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
      val badBackup = DatabaseBackup(
        version = 0, // invalid version
        createdAt = System.currentTimeMillis(),
        appVersion = appVersion,
        favorites = emptyList(),
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
      val badBackup = DatabaseBackup(
        version = DatabaseBackup.BACKUP_VERSION + 1, // newer unsupported framework
        createdAt = System.currentTimeMillis(),
        appVersion = appVersion,
        favorites = emptyList(),
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
      val badBackup = DatabaseBackup(
        version = DatabaseBackup.BACKUP_VERSION,
        createdAt = System.currentTimeMillis(),
        appVersion = appVersion,
        favorites = listOf(createMockBackupEntry(1)),
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
      val favorites = listOf(createMockBackupEntry(5))

      val legacyBackup = DatabaseBackup(
        version = DatabaseBackup.BACKUP_VERSION,
        createdAt = System.currentTimeMillis(),
        appVersion = appVersion,
        favorites = favorites,
        checksum = null, // Legacy condition
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
      val invalidEntry = favoriteTvEntity.toBackupEntry().copy(mediaId = -1, mediaType = "")

      val payload = BackupPayload(
        version = DatabaseBackup.BACKUP_VERSION,
        createdAt = System.currentTimeMillis(),
        appVersion = appVersion,
        favorites = listOf(invalidEntry),
      )
      val backup = DatabaseBackup(
        version = payload.version,
        createdAt = payload.createdAt,
        appVersion = payload.appVersion,
        favorites = payload.favorites,
        checksum = jsonCompact.encodeToString(payload).sha256(),
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
      val mockInputStream = mockk<java.io.InputStream>()

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
