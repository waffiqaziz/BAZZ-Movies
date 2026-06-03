package com.waffiq.bazz_movies.core.database.data.datasource

import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDao
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDatabase
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.favoriteMovieEntity
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.favoriteTvEntity
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.watchlistMovieEntity
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.watchlistTvEntity
import com.waffiq.bazz_movies.core.database.utils.DbResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FavoriteLocalDataSourceTest {

  private lateinit var database: FavoriteDatabase
  private lateinit var favoriteDao: FavoriteDao
  private lateinit var localDataSource: FavoriteLocalDataSource
  private val testDispatcher = StandardTestDispatcher()

  @Before
  fun setup() {
    database = inMemoryDatabaseBuilder(
      ApplicationProvider.getApplicationContext(),
      FavoriteDatabase::class.java,
    ).allowMainThreadQueries().build()

    favoriteDao = database.favoriteDao()
    localDataSource = FavoriteLocalDataSource(favoriteDao, testDispatcher)
  }

  @After
  fun teardown() {
    database.close()
  }

  @Test
  fun getFavoriteMovies_whenSuccessful_emitCorrectData() =
    runTest(testDispatcher) {
      favoriteDao.insert(favoriteMovieEntity.copy(isFavorite = true))

      localDataSource.getFavoriteMovies.test {
        val result = awaitItem()
        println(result.toString())
        assertTrue(result[0].isFavorite)
        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun getFavoriteTv_whenSuccessful_emitCorrectData() =
    runTest(testDispatcher) {
      favoriteDao.insert(favoriteTvEntity.copy(isFavorite = true))

      localDataSource.getFavoriteTv.test {
        runCurrent()
        val result = awaitItem()
        assertTrue(result[0].isFavorite)
        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun getWatchlistMovies_whenSuccessful_emitCorrectData() =
    runTest(testDispatcher) {
      favoriteDao.insert(watchlistMovieEntity)

      localDataSource.getWatchlistMovies.test {
        val result = awaitItem()
        assertEquals(watchlistMovieEntity.mediaId, result[0].mediaId)
        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun getWatchlistTv_whenSuccessful_emitCorrectData() =
    runTest(testDispatcher) {
      favoriteDao.insert(watchlistTvEntity)

      localDataSource.getWatchlistTv.test {
        val result = awaitItem()
        assertEquals(
          watchlistTvEntity.mediaId,
          result[0].mediaId,
        )
        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun deleteItemFromDB_whenSuccessful_dataShouldBeDeleted() =
    runTest(testDispatcher) {
      favoriteDao.insert(favoriteMovieEntity)

      val result =
        localDataSource.deleteItemFromDB(favoriteMovieEntity.mediaId, favoriteMovieEntity.mediaType)
      assert(result is DbResult.Success && result.data == 1)

      val remainingMovies = favoriteDao.getFavoriteMovies().first()
      assert(remainingMovies.isEmpty())
    }

  @Test
  fun deleteAll_whenSuccessful_databaseShouldBeEmpty() =
    runTest(testDispatcher) {
      val fakeMovies = listOf(
        favoriteMovieEntity.copy(mediaId = 1),
        favoriteMovieEntity.copy(mediaId = 2),
      )
      fakeMovies.forEach { favoriteDao.insert(it) }

      val result = localDataSource.deleteAll()
      assert(result is DbResult.Success && result.data == fakeMovies.size)

      val remainingMovies = favoriteDao.getFavoriteMovies().first()
      assert(remainingMovies.isEmpty())
    }

  @Test
  fun getByMedia_whenSuccessful_returnTrue() =
    runTest(testDispatcher) {
      favoriteDao.insert(favoriteMovieEntity.copy(isFavorite = true))

      val result = localDataSource.getByMedia(favoriteMovieEntity.mediaId, MOVIE_MEDIA_TYPE)
      assertEquals(true, result?.isFavorite)
    }

  @Test
  fun update_whenSuccessful_shouldUpdateTheData() =
    runTest(testDispatcher) {
      favoriteDao.insert(favoriteMovieEntity.copy(isFavorite = false, isWatchlist = false))

      val result = localDataSource.update(
        favoriteMovieEntity.copy(isFavorite = true, isWatchlist = true),
      )
      assert(result is DbResult.Success && result.data == Unit)

      val updatedMovie = favoriteDao.getFavoriteMovies().first().first()
      assertTrue(updatedMovie.isFavorite)
      assert(updatedMovie.isFavorite && updatedMovie.isWatchlist)
    }

  @Test
  fun insertMultipleFavorite_whenSameItem_returnCorrectSize() =
    runTest(testDispatcher) {
      // insert with same media id and same media type
      favoriteDao.insert(favoriteMovieEntity.copy(id = 3, isFavorite = true))
      favoriteDao.insert(favoriteMovieEntity.copy(id = 4, isFavorite = true))

      favoriteDao.getFavoriteMovies().test {
        assertEquals(1, awaitItem().size)
        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun insertMultipleFavorite_whenSuccessfulw_returnCorrectSize() =
    runTest(testDispatcher) {
      // insert with same media id and same media type
      favoriteDao.insert(favoriteMovieEntity.copy(id = 3, isFavorite = true))
      favoriteDao.insert(favoriteMovieEntity.copy(id = 4, mediaId = 231, isFavorite = true))

      favoriteDao.getFavoriteMovies().test {
        assertEquals(2, awaitItem().size)
        cancelAndIgnoreRemainingEvents()
      }
    }
}
