package com.waffiq.bazz_movies.core.database.data.repository

import androidx.core.net.toUri
import com.waffiq.bazz_movies.core.database.data.datasource.DatabaseBackupDataSource
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
class DatabaseBackupRepositoryImplTest {

  private val mockSuccess = DbResult.Success(Unit)
  private lateinit var repository: DatabaseBackupRepositoryImpl
  private val mockBackupDataSource: DatabaseBackupDataSource = mockk()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    repository = DatabaseBackupRepositoryImpl(mockBackupDataSource)
  }

  @Test
  fun backupDatabase_whenSuccessful_returnsSuccessDbResult() =
    runTest {
      coEvery { mockBackupDataSource.backupDatabase(any()) } returns mockSuccess

      val result = repository.backupDatabase("test".toUri())
      assertTrue(result is DbResult.Success)
      assertEquals(Unit, (result as DbResult.Success).data)

      coVerify { mockBackupDataSource.backupDatabase(any()) }
    }

  @Test
  fun restoreDatabase_whenSuccessful_returnsSuccessDbResult() =
    runTest {
      coEvery { mockBackupDataSource.restoreDatabase(any()) } returns mockSuccess

      val result = repository.restoreDatabase("test".toUri())
      assertTrue(result is DbResult.Success)
      assertEquals(Unit, (result as DbResult.Success).data)

      coVerify { mockBackupDataSource.restoreDatabase(any()) }
    }
}
