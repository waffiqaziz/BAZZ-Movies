package com.waffiq.bazz_movies.core.database.data.manager

import com.waffiq.bazz_movies.core.database.data.model.BackupPayload
import com.waffiq.bazz_movies.core.database.data.model.DatabaseBackup
import com.waffiq.bazz_movies.core.database.testutils.BaseDatabaseBackupManagerTest
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.database.utils.sha256
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayOutputStream

class DatabaseBackupManagerTest : BaseDatabaseBackupManagerTest() {

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
      assertEquals(APP_VERSION, decodedBackup.appVersion)
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
}
