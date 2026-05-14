package com.waffiq.bazz_movies.core.database.data.repository

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.database.data.datasource.SearchHistoryLocalDataSource
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.listSearchHistoryEntityFlow
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.searchHistory
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchHistoryLocalDatabaseRepositoryImplTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val mockDataSource: SearchHistoryLocalDataSource = mockk(relaxed = true)
  private lateinit var repository: SearchHistoryLocalDatabaseRepositoryImpl

  @Before
  fun setup() {
    // mock all success
    every { mockDataSource.getSearchHistory() } returns listSearchHistoryEntityFlow
    coEvery { mockDataSource.insert(any()) } just Runs
    coEvery { mockDataSource.deleteByQuery(any()) } returns 1
    coEvery { mockDataSource.delete(any()) } returns 1
    coEvery { mockDataSource.deleteAll() } returns 1
    coEvery { mockDataSource.trimHistory() } returns 1

    repository = SearchHistoryLocalDatabaseRepositoryImpl(mockDataSource)
  }

  @Test
  fun getSearchHistory_whenSuccessful_returnsListOfSearchHistory() =
    runTest {
      repository.getSearchHistory().test {
        val result = awaitItem()
        assertEquals(1, result.size)
        assertEquals(searchHistory, result[0])
        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun insert_whenCalled_shouldInsertSearchHistory() =
    runTest {
      repository.insert(searchHistory)
      coVerify { mockDataSource.insert(any()) }
    }

  @Test
  fun deleteByQuery_whenSuccessful_returnsSuccess() =
    runTest {
      val result = repository.deleteByQuery("query")

      assertEquals(1, result)

      coVerify { mockDataSource.deleteByQuery(any()) }
    }

  @Test
  fun delete_whenSuccessful_returnsSuccess() =
    runTest {
      val result = repository.delete(searchHistory)

      assertEquals(1, result)

      coVerify { mockDataSource.delete(any()) }
    }

  @Test
  fun deleteAll_whenSuccessful_returnsSuccess() =
    runTest {
      val result = repository.deleteAll()

      assertEquals(1, result)
      coVerify { mockDataSource.deleteAll() }
    }

  @Test
  fun trimHistory_whenSuccessful_returnsSuccess() =
    runTest {
      val result = repository.trimHistory()

      assertEquals(1, result)
      coVerify { mockDataSource.trimHistory() }
    }
}
