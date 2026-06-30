package com.waffiq.bazz_movies.core.database.data.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.database.testutils.DummyData.searchHistoryEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

// https://developer.android.com/training/data-storage/room/testing-db
@RunWith(RobolectricTestRunner::class)
class SearchHistoryDaoTest {

  private lateinit var database: SearchHistoryDatabase
  private lateinit var searchHistoryDao: SearchHistoryDao

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    database = Room.inMemoryDatabaseBuilder(
      context,
      SearchHistoryDatabase::class.java,
    ).allowMainThreadQueries() // allow for testing on main thread
      .build()

    searchHistoryDao = database.searchHistoryDao()
  }

  @After
  fun tearDown() {
    database.close()
  }

  @Test
  fun insert_whenCalled_shouldInsertSearchHistoryEntity() =
    runTest {
      insertData()

      searchHistoryDao.getSearchHistory().test {
        val result = awaitItem()
        assertEquals(1, result.size)
        assertEquals(searchHistoryEntity, result[0])
        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun deleteByQuery_whenSuccessful_returnsSuccess() =
    runTest {
      insertData()
      checkData()

      val result = searchHistoryDao.deleteByQuery(searchHistoryEntity.query)
      assertEquals(1, result)

      dataShouldBeZero()
    }

  @Test
  fun delete_whenSuccessful_returnsSuccess() =
    runTest {
      insertData()
      checkData()

      val result = searchHistoryDao.delete(searchHistoryEntity)
      assertEquals(1, result)

      dataShouldBeZero()
    }

  @Test
  fun deleteAll_whenSuccessful_returnsSuccess() =
    runTest {
      insertData()
      checkData()

      val result = searchHistoryDao.deleteAll()
      assertEquals(1, result)

      dataShouldBeZero()
    }

  @Test
  fun trimHistory_whenSuccessful_returnsSuccess() =
    runTest {
      insertData()
      checkData()

      val result = searchHistoryDao.trimHistory()
      assertEquals(0, result) // because the data does not exceed 20
    }

  private suspend fun insertData() {
    searchHistoryDao.insert(searchHistoryEntity)
  }

  private suspend fun checkData() {
    val result = searchHistoryDao.getSearchHistory().first()
    assertEquals(1, result.size)
    assertEquals(searchHistoryEntity, result[0])
  }

  private suspend fun dataShouldBeZero() {
    val result = searchHistoryDao.getSearchHistory().first()
    assertEquals(0, result.size)
  }
}
