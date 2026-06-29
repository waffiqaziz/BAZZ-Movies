package com.waffiq.bazz_movies.core.database.data.datasource

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.database.data.room.SearchHistoryDao
import com.waffiq.bazz_movies.core.database.testutils.DummyData.listSearchHistoryEntityFlow
import com.waffiq.bazz_movies.core.database.testutils.DummyData.searchHistoryEntity
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchHistoryLocalDataSourceTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val mockDao: SearchHistoryDao = mockk(relaxed = true)
  private lateinit var dataSource: SearchHistoryLocalDataSource

  @Before
  fun setup() {
    // mock all success
    every { mockDao.getSearchHistory() } returns listSearchHistoryEntityFlow
    coEvery { mockDao.insert(any()) } just Runs
    coEvery { mockDao.deleteByQuery(any()) } returns 1
    coEvery { mockDao.delete(any()) } returns 1
    coEvery { mockDao.deleteAll() } returns 1
    coEvery { mockDao.trimHistory() } returns 1

    dataSource = SearchHistoryLocalDataSource(mockDao, Dispatchers.IO)
  }

  @Test
  fun getSearchHistory_whenSuccessful_returnsListOfSearchHistory() =
    runTest {
      dataSource.getSearchHistory().test {
        val result = awaitItem()
        assertEquals(1, result.size)
        assertEquals(searchHistoryEntity, result[0])
        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun insert_whenCalled_shouldInsertSearchHistory() =
    runTest {
      dataSource.insert(searchHistoryEntity)
      coVerify { mockDao.insert(any()) }
    }

  @Test
  fun deleteByQuery_whenSuccessful_returnsSuccess() =
    runTest {
      val result = dataSource.deleteByQuery("query")

      assertEquals(1, result)

      coVerify { mockDao.deleteByQuery(any()) }
    }

  @Test
  fun delete_whenSuccessful_returnsSuccess() =
    runTest {
      val result = dataSource.delete(searchHistoryEntity)

      assertEquals(1, result)

      coVerify { mockDao.delete(any()) }
    }

  @Test
  fun deleteAll_whenSuccessful_returnsSuccess() =
    runTest {
      val result = dataSource.deleteAll()

      assertEquals(1, result)
      coVerify { mockDao.deleteAll() }
    }

  @Test
  fun trimHistory_whenSuccessful_returnsSuccess() =
    runTest {
      val result = dataSource.trimHistory()

      assertEquals(1, result)
      coVerify { mockDao.trimHistory() }
    }
}
