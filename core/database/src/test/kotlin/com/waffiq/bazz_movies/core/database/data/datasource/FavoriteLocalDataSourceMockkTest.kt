package com.waffiq.bazz_movies.core.database.data.datasource

import android.database.sqlite.SQLiteException
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDao
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.favoriteMovieEntity
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteLocalDataSourceMockkTest {

  private val testDispatcher = Dispatchers
  private lateinit var localDataSource: FavoriteLocalDataSource

  private val favoriteDao: FavoriteDao = mockk(relaxed = true)

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setUp() {
    localDataSource = FavoriteLocalDataSource(favoriteDao, testDispatcher.IO)
  }

  @Test
  fun insert_withValidValue_returnSuccess() =
    runTest {
      coEvery { favoriteDao.insert(favoriteMovieEntity) } returns 1L

      val result = localDataSource.insert(favoriteMovieEntity)

      assertTrue(result is DbResult.Success)
      assertEquals(1, (result as DbResult.Success).data)
    }

  @Test
  fun deleteItemFromDB_whenSuccessful_returnSuccess() =
    runTest {
      coEvery { favoriteDao.deleteItem(101, "movie") } returns 1

      val result = localDataSource.deleteItemFromDB(101, "movie")

      assertTrue(result is DbResult.Success)
      assertEquals(1, (result as DbResult.Success).data)
    }

  @Test
  fun deleteAll_whenSuccessful_returnSuccess() =
    runTest {
      coEvery { favoriteDao.deleteAll() } returns 1

      val result = localDataSource.deleteAll()

      assertTrue(result is DbResult.Success)
      assertEquals(1, (result as DbResult.Success).data)
    }

  @Test
  fun getByMedia_whenFavoriteExists_returnsTrue() =
    runTest {
      coEvery { favoriteDao.getByMedia(101, "movie") } returns favoriteMovieEntity

      val result = localDataSource.getByMedia(101, "movie")

      assertTrue(result is FavoriteEntity)
    }

  @Test
  fun update_whenSuccessful_returnSuccess() =
    runTest {
      coEvery { favoriteDao.update(favoriteMovieEntity) } returns 1

      val result = localDataSource.update(favoriteMovieEntity)

      assertTrue(result is DbResult.Success)
      assertEquals(Unit, (result as DbResult.Success).data)
    }

  private fun <T> runDbErrorTest(
    expectedMessage: String,
    mockDaoCall: () -> Unit,
    functionUnderTest: suspend () -> DbResult<T>,
  ) = runTest {
    mockDaoCall()

    val result = functionUnderTest()
    assertTrue(result is DbResult.Error)
    assertEquals(expectedMessage, (result as DbResult.Error).errorMessage)
  }

  @Test
  fun update_sQLiteExceptionNoMessage_throwsSQLiteException() =
    runDbErrorTest(
      "Database error",
      { coEvery { favoriteDao.insertOrUpdate(any()) } throws SQLiteException("SQLite exception") },
    ) {
      localDataSource.update(favoriteMovieEntity)
    }

  @Test
  fun insert_whenUnknownError_throwsException() =
    runDbErrorTest(
      "Unknown error",
      { coEvery { favoriteDao.insert(any()) } throws Exception("Unknown error") },
    ) {
      localDataSource.insert(favoriteMovieEntity)
    }
}
