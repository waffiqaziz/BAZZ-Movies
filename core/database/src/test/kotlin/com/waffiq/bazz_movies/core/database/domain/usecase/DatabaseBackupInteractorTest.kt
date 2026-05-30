package com.waffiq.bazz_movies.core.database.domain.usecase

import androidx.core.net.toUri
import com.waffiq.bazz_movies.core.database.domain.repository.IDatabaseBackupRepository
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
class DatabaseBackupInteractorTest {

  private val mockSuccess = DbResult.Success(Unit)
  private lateinit var interactor: DatabaseBackupInteractor
  private val mockRepository: IDatabaseBackupRepository = mockk()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    interactor = DatabaseBackupInteractor(mockRepository)
  }

  @Test
  fun backupDatabase_whenSuccessful_returnsSuccessDbResult() =
    runTest {
      coEvery { mockRepository.backupDatabase(any()) } returns mockSuccess

      val result = interactor.backupDatabase("test".toUri())
      assertTrue(result is DbResult.Success)
      assertEquals(Unit, (result as DbResult.Success).data)

      coVerify { mockRepository.backupDatabase(any()) }
    }

  @Test
  fun restoreDatabase_whenSuccessful_returnsSuccessDbResult() =
    runTest {
      coEvery { mockRepository.restoreDatabase(any()) } returns mockSuccess

      val result = interactor.restoreDatabase("test".toUri())
      assertTrue(result is DbResult.Success)
      assertEquals(Unit, (result as DbResult.Success).data)

      coVerify { mockRepository.restoreDatabase(any()) }
    }
}
