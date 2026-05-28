package com.waffiq.bazz_movies.core.database.data.manager

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.database.data.model.DatabaseBackup
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDao
import com.waffiq.bazz_movies.core.database.utils.DbResult
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertIs

@RunWith(RobolectricTestRunner::class)
class DatabaseBackupManagerTest : BaseDatabaseBackupManagerTest() {

  private lateinit var manager: DatabaseBackupManager

  private val context = ApplicationProvider.getApplicationContext<Context>()
  private val mockFavoriteDao: FavoriteDao = mockk()

  private val testDispatcher = StandardTestDispatcher()

  @Before
  fun setup() {
    manager = DatabaseBackupManager(
      context = context,
      favoriteDao = mockFavoriteDao,
      ioDispatcher = testDispatcher,
      appVersion = "1.0.0",
    )
  }

  @Test
  fun backupToUri_withValidJSON_successWrites() =
    runTest(testDispatcher) {
      val entities = listOf<FavoriteEntity>(fakeEntity(id = 1), fakeEntity(id = 2))
      coEvery { mockFavoriteDao.getAllFavorites() } returns entities

      val file = context.tempFile()
      val uri = Uri.fromFile(file)

      val result = manager.backupToUri(uri)

      assertIs<DbResult.Success<Unit>>(result)
      val backup = gson.fromJson(file.readText(), DatabaseBackup::class.java)
      assertEquals(1, backup.version)
      assertEquals("1.0.0", backup.appVersion)
      assertEquals(2, backup.favorites.size)
    }

  @Test
  fun backupToUri_withEmptyFavorites_stillSucceed() =
    runTest(testDispatcher) {
      coEvery { mockFavoriteDao.getAllFavorites() } returns emptyList()

      val file = context.tempFile()
      val result = manager.backupToUri(Uri.fromFile(file))

      assertIs<DbResult.Success<Unit>>(result)
      val backup = gson.fromJson(file.readText(), DatabaseBackup::class.java)
      assertEquals(0, backup.favorites.size)
    }

  @Test
  fun backupToUri_whenDaoThrows_returnsError() =
    runTest(testDispatcher) {
      coEvery { mockFavoriteDao.getAllFavorites() } throws RuntimeException("db error")

      val result = manager.backupToUri(Uri.fromFile(context.tempFile()))

      assertIs<DbResult.Error>(result)
      assertEquals("db error", result.errorMessage)
    }

  @Test
  fun backupToUri_whenErrorMessageIsNull_returnsFallbackError() =
    runTest(testDispatcher) {
      coEvery { mockFavoriteDao.getAllFavorites() } throws Exception()

      val result = manager.backupToUri(Uri.fromFile(context.tempFile()))

      assertIs<DbResult.Error>(result)
      assertEquals("Backup failed", result.errorMessage)
    }

  @Test
  fun backupToUri_whenOutputStreamCannotBeOpened_returnsError() =
    runTest(testDispatcher) {
      coEvery { mockFavoriteDao.getAllFavorites() } returns listOf(fakeEntity())

      // non-existent directory,stream will be null
      val uri = Uri.parse("content://com.invalid.provider/no/such/path")

      val result = manager.backupToUri(uri)

      assertIs<DbResult.Error>(result)
    }

  @Test
  fun restoreFromUri_withValidBackup_successRestores() =
    runTest(testDispatcher) {
      coEvery { mockFavoriteDao.clearAndInsert(any()) } just Runs

      val file = context.writeBackupFile(favorites = listOf(fakeBackupEntry()))
      val result = manager.restoreFromUri(Uri.fromFile(file))

      assertIs<DbResult.Success<Unit>>(result)
      coVerify(exactly = 1) { mockFavoriteDao.clearAndInsert(any()) }
    }

  @Test
  fun restoreFromUri_onInvalidJSON_returnsError() =
    runTest(testDispatcher) {
      val file = context.tempFile().also { it.writeText("not json at all }{") }

      val result = manager.restoreFromUri(Uri.fromFile(file))

      assertIs<DbResult.Error>(result)
      assertEquals("Invalid backup file: not valid JSON", result.errorMessage)
    }

  @Test
  fun restoreFromUri_whenBackupVersionIsNewer_returnsError() =
    runTest(testDispatcher) {
      val file = context.writeBackupFile(version = 999)

      val result = manager.restoreFromUri(Uri.fromFile(file))

      assertIs<DbResult.Error>(result)
      assertTrue(result.errorMessage.contains("newer app version"))
    }

  @Test
  fun restoreFromUri_whenBackupVersionIsInvalid_returnsError() =
    runTest(testDispatcher) {
      val file = context.writeBackupFile(version = -1)

      val result = manager.restoreFromUri(Uri.fromFile(file))

      assertIs<DbResult.Error>(result)
      assertTrue(result.errorMessage.contains("Invalid backup version"))
    }

  @Test
  fun restoreFromUri_whenAllEntriesAreInvalid_returnsError() =
    runTest(testDispatcher) {
      coEvery { mockFavoriteDao.clearAndInsert(any()) } just Runs

      val file = context.writeBackupFile(
        favorites = listOf(
          fakeBackupEntry(mediaId = 0, mediaType = "", title = ""),
          fakeBackupEntry(mediaId = -1, mediaType = "", title = ""),
        ),
      )

      val result = manager.restoreFromUri(Uri.fromFile(file))

      assertIs<DbResult.Error>(result)
      assertEquals("Backup contains no valid entries", result.errorMessage)
      coVerify(exactly = 0) { mockFavoriteDao.clearAndInsert(any()) }
    }

  @Test
  fun restoreFromUri_entriesAndRestoresIsValid_skipsInvalid() =
    runTest(testDispatcher) {
      val captured = slot<List<FavoriteEntity>>()
      coEvery { mockFavoriteDao.clearAndInsert(capture(captured)) } just Runs

      val file = context.writeBackupFile(
        favorites = listOf(
          fakeBackupEntry(mediaId = 1, title = "Valid"),
          fakeBackupEntry(mediaId = -1, title = "Bad id"), // skipped
          fakeBackupEntry(mediaId = 2, title = "Also Valid"),
        ),
      )

      val result = manager.restoreFromUri(Uri.fromFile(file))

      assertIs<DbResult.Success<Unit>>(result)
      assertEquals(2, captured.captured.size)
    }

  @Test
  fun restoreFromUri_whenDaoThrowsDuringRestore_returnsError() =
    runTest(testDispatcher) {
      coEvery { mockFavoriteDao.clearAndInsert(any()) } throws RuntimeException("insert failed")

      val file = context.writeBackupFile(favorites = listOf(fakeBackupEntry()))

      val result = manager.restoreFromUri(Uri.fromFile(file))

      assertIs<DbResult.Error>(result)
      assertEquals("insert failed", result.errorMessage)
    }

  @Test
  fun restoreFromUri_messageErrorIsNull_returnsFallbackError() =
    runTest(testDispatcher) {
      coEvery { mockFavoriteDao.clearAndInsert(any()) } throws Exception()

      val file = context.writeBackupFile(favorites = listOf(fakeBackupEntry()))

      val result = manager.restoreFromUri(Uri.fromFile(file))

      assertIs<DbResult.Error>(result)
      assertEquals("Restore failed", result.errorMessage)
    }
}
