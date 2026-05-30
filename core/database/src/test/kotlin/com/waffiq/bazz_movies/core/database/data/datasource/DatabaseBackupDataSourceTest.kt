package com.waffiq.bazz_movies.core.database.data.datasource

import androidx.core.net.toUri
import com.waffiq.bazz_movies.core.database.data.manager.DatabaseBackupManager
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DatabaseBackupDataSourceTest {

  private val mockSuccess = DbResult.Success(Unit)
  private lateinit var dataSource: DatabaseBackupDataSource
  private val mockBackupManager: DatabaseBackupManager = mockk()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    dataSource = DatabaseBackupDataSource(mockBackupManager)
  }

  @Test
  fun backupDatabase_whenSuccessful_returnsSuccessDbResult() =
    runTest {
      coEvery { mockBackupManager.backupToUri(any()) } returns mockSuccess

      val result = dataSource.backupDatabase("test".toUri())
      assertTrue(result is DbResult.Success)
      assertEquals(Unit, (result as DbResult.Success).data)

      coVerify { mockBackupManager.backupToUri(any()) }
    }

  @Test
  fun restoreDatabase_whenSuccessful_returnsSuccessDbResult() =
    runTest {
      coEvery { mockBackupManager.restoreFromUri(any()) } returns mockSuccess

      val result = dataSource.restoreDatabase("test".toUri())
      assertTrue(result is DbResult.Success)
      assertEquals(Unit, (result as DbResult.Success).data)

      coVerify { mockBackupManager.restoreFromUri(any()) }
    }
}
