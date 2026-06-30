package com.waffiq.bazz_movies.core.database.data.manager

import com.waffiq.bazz_movies.core.database.testutils.BaseDatabaseBackupManagerTest
import com.waffiq.bazz_movies.core.database.utils.DbResult
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DatabaseRestoreBackupFileManagerTest : BaseDatabaseBackupManagerTest() {

  @Test
  fun restoreFromUri_whenValidBackupProvided_expectedSuccess() =
    runTest {
      every { contentResolver.openInputStream(mockUri) } returns loadBackup("v1_valid.json")

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Success)
      coVerify(exactly = 1) { favoriteDao.clearAndInsert(any()) }
    }

  @Test
  fun restoreFromUri_whenInputStreamCannotBeOpened_expectedError() =
    runTest {
      every { contentResolver.openInputStream(mockUri) } returns null

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertEquals(
        "Cannot open input stream",
        (result as DbResult.Error).errorMessage,
      )
    }

  @Test
  fun restoreFromUri_whenJsonIsMalformed_expectedError() =
    runTest {
      every { contentResolver.openInputStream(mockUri) } returns loadBackup("v1_malformed.json")

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertTrue(
        (result as DbResult.Error).errorMessage.contains("Invalid backup file: not valid JSON"),
      )
    }

  @Test
  fun restoreFromUri_whenVersionIsZero_expectedError() =
    runTest {
      every { contentResolver.openInputStream(mockUri) } returns
        loadBackup("v1_invalid_version.json")

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertEquals("Invalid backup version", (result as DbResult.Error).errorMessage)
    }

  @Test
  fun restoreFromUri_whenVersionIsNewerThanSupported_expectedError() =
    runTest {
      every { contentResolver.openInputStream(mockUri) } returns
        loadBackup("v1_newer_version.json")

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertEquals(
        "Backup is from a newer app version. Please update the app.",
        (result as DbResult.Error).errorMessage,
      )
    }

  @Test
  fun restoreFromUri_whenChecksumIsInvalid_expectedError() =
    runTest {
      every { contentResolver.openInputStream(mockUri) } returns
        loadBackup("v1_invalid_checksum.json")

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertEquals(
        "Backup file is corrupted or has been modified",
        (result as DbResult.Error).errorMessage,
      )
    }

  @Test
  fun restoreFromUri_whenNoChecksum_expectedSuccess() =
    runTest {
      every { contentResolver.openInputStream(mockUri) } returns loadBackup("v1_no_checksum.json")

      val result = manager.restoreFromUri(mockUri)

      // success without verifying checksum
      assertTrue(result is DbResult.Success)
      coVerify(exactly = 1) { favoriteDao.clearAndInsert(any()) }
    }

  @Test
  fun restoreFromUri_whenBackupContainsInvalidEntries_expectedError() =
    runTest {
      // create invalid data that trigger`.isValid()` inside FavoriteBackupEntry whis was
      // the mediaType should not empty and mediaId should more than 0
      every { contentResolver.openInputStream(mockUri) } returns
        loadBackup("v1_invalid_entries.json")

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertEquals("Backup contains no valid entries", (result as DbResult.Error).errorMessage)
    }

  @Test
  fun restoreFromUri_whenFavoritesAreEmpty_expectedError() =
    runTest {
      every { contentResolver.openInputStream(mockUri) } returns
        loadBackup("v1_empty_favorites.json")

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Error)
      assertEquals("Backup contains no valid entries", (result as DbResult.Error).errorMessage)
    }

  @Test
  fun restoreFromUri_whenBackupContainsUnknownFields_expectedSuccess() =
    runTest {
      // this to test `ignoreUnknownKeys = true`
      every { contentResolver.openInputStream(mockUri) } returns
        loadBackup("v1_unknown_fields.json")

      val result = manager.restoreFromUri(mockUri)

      assertTrue(result is DbResult.Success)
      coVerify(exactly = 1) {
        favoriteDao.clearAndInsert(any())
      }
    }
}
