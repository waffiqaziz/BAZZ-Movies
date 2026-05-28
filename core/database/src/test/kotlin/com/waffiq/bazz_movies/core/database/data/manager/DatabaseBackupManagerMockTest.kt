package com.waffiq.bazz_movies.core.database.data.manager

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDao
import com.waffiq.bazz_movies.core.database.utils.DbResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.io.InputStream
import kotlin.test.assertIs

class DatabaseBackupManagerMockTest : BaseDatabaseBackupManagerTest() {

  private lateinit var manager: DatabaseBackupManager

  private val context: Context = mockk()
  private val contentResolver: ContentResolver = mockk(relaxed = true)
  private val mockFavoriteDao: FavoriteDao = mockk()

  private val testDispatcher = StandardTestDispatcher()

  @Before
  fun setup() {
    every { context.contentResolver } returns contentResolver
    coEvery { mockFavoriteDao.getAllFavorites() } returns listOf(fakeEntity())

    manager = DatabaseBackupManager(
      context = context,
      favoriteDao = mockFavoriteDao,
      ioDispatcher = testDispatcher,
      appVersion = "1.0.0",
    )
  }

  @Test
  fun backupToUri_whenOutputStreamCannotNull_returnsError() =
    runTest(testDispatcher) {
      val uri = mockk<Uri>()
      every { contentResolver.openOutputStream(uri) } returns null

      val result = manager.backupToUri(uri)
      assertIs<DbResult.Error>(result)
      assertEquals("Cannot open output stream", result.errorMessage)
    }

  @Test
  fun restoreFromUri_whenInputStreamCannotOpen_returnsError() =
    runTest(testDispatcher) {
      val uri = mockk<Uri>()

      every { contentResolver.openInputStream(uri) } returns null
      val result = manager.restoreFromUri(uri)

      assertIs<DbResult.Error>(result)
      assertEquals("Cannot open input stream", result.errorMessage)
    }

  @Test
  fun restoreFromUri_whenReadingStreamFails_returnsError() =
    runTest(testDispatcher) {
      val uri = mockk<Uri>()
      val inputStream = mockk<InputStream>()

      every { contentResolver.openInputStream(uri) } returns inputStream
      every { inputStream.read(any<ByteArray>(), any(), any()) } throws IOException("read failed")

      val result = manager.restoreFromUri(uri)
      assertIs<DbResult.Error>(result)
      assertEquals("read failed", result.errorMessage)
    }

  @Test
  fun restoreFromUri_whenClosingStreamFails_returnsError() =
    runTest(testDispatcher) {
      val uri = mockk<Uri>()
      val inputStream = mockk<InputStream>()

      every { contentResolver.openInputStream(uri) } returns inputStream
      every { inputStream.read(any<ByteArray>(), any(), any()) } returns -1
      every { inputStream.close() } throws IOException("close failed")

      val result = manager.restoreFromUri(uri)

      assertIs<DbResult.Error>(result)
      assertEquals("close failed", result.errorMessage)
    }
}
