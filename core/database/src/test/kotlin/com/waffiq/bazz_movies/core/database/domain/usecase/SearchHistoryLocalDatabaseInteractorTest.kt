package com.waffiq.bazz_movies.core.database.domain.usecase

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.database.domain.repository.ISearchHistoryLocalDatabaseRepository
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.listSearchHistoryFlow
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.searchHistory
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchHistoryLocalDatabaseInteractorTest {

  private val mockRepository: ISearchHistoryLocalDatabaseRepository = mockk(relaxed = true)
  private lateinit var interactor: SearchHistoryLocalDatabaseInteractor

  @Before
  fun setup() {
    // mock all success
    every { mockRepository.getSearchHistory() } returns listSearchHistoryFlow
    coEvery { mockRepository.insert(any()) } just Runs
    coEvery { mockRepository.deleteByQuery(any()) } returns 1
    coEvery { mockRepository.delete(any()) } returns 1
    coEvery { mockRepository.deleteAll() } returns 1
    coEvery { mockRepository.trimHistory() } returns 1

    interactor = SearchHistoryLocalDatabaseInteractor(mockRepository)
  }

  @Test
  fun getSearchHistory_whenSuccessful_returnsListOfSearchHistory() =
    runTest {
      interactor.getSearchHistory().test {
        val result = awaitItem()
        assertEquals(1, result.size)
        assertEquals(searchHistory, result[0])
        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun insert_whenCalled_shouldInsertSearchHistory() =
    runTest {
      interactor.insert(searchHistory)
      coVerify { mockRepository.insert(any()) }
    }

  @Test
  fun deleteByQuery_whenSuccessful_returnsSuccess() =
    runTest {
      val result = interactor.deleteByQuery("query")

      assertEquals(1, result)

      coVerify { mockRepository.deleteByQuery(any()) }
    }

  @Test
  fun delete_whenSuccessful_returnsSuccess() =
    runTest {
      val result = interactor.delete(searchHistory)

      assertEquals(1, result)

      coVerify { mockRepository.delete(any()) }
    }

  @Test
  fun deleteAll_whenSuccessful_returnsSuccess() =
    runTest {
      val result = interactor.deleteAll()

      assertEquals(1, result)
      coVerify { mockRepository.deleteAll() }
    }

  @Test
  fun trimHistory_whenSuccessful_returnsSuccess() =
    runTest {
      val result = interactor.trimHistory()

      assertEquals(1, result)
      coVerify { mockRepository.trimHistory() }
    }
}
