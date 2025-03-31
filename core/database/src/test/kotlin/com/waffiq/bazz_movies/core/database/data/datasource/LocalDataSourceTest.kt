package com.waffiq.bazz_movies.core.database.data.datasource

import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDao
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDatabase
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.favoriteMovieEntity
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.favoriteTvEntity
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
class LocalDataSourceTest {

  private lateinit var database: FavoriteDatabase
  private lateinit var favoriteDao: FavoriteDao
  private lateinit var localDataSource: LocalDataSource
  private val testDispatcher = StandardTestDispatcher()

  @Before
  fun setup() {
    database = inMemoryDatabaseBuilder(
      ApplicationProvider.getApplicationContext(),
      FavoriteDatabase::class.java
    ).allowMainThreadQueries().build()

    favoriteDao = database.favoriteDao()
    localDataSource = LocalDataSource(favoriteDao, testDispatcher)
  }

  @After
  fun teardown() {
    database.close()
  }

  @Test
  fun getFavoriteMovies_emitCorrectData() = runTest(testDispatcher) {
    favoriteDao.insert(favoriteMovieEntity.copy(isFavorite = true))

    localDataSource.getFavoriteMovies.test {
      runCurrent()
      val result = awaitItem()
      assert(result.contains(favoriteMovieEntity.copy(isFavorite = true)))
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun getFavoriteTv_emitCorrectData() = runTest(testDispatcher) {
    favoriteDao.insert(favoriteTvEntity.copy(isFavorite = true))

    localDataSource.getFavoriteTv.test {
      runCurrent()
      val result = awaitItem()
      assert(result.contains(favoriteTvEntity.copy(isFavorite = true)))
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun getWatchlistMovies_emitCorrectData() = runTest(testDispatcher) {
    favoriteDao.insert(favoriteMovieEntity)

    localDataSource.getWatchlistMovies.test {
      runCurrent()
      val result = awaitItem()
      assert(result.contains(favoriteMovieEntity))
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun getWatchlistTv_emitCorrectData() = runTest(testDispatcher) {
    favoriteDao.insert(favoriteTvEntity)

    localDataSource.getWatchlistTv.test {
      runCurrent()
      val result = awaitItem()
      assert(result.contains(favoriteTvEntity))
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun deleteItemFromDB_success() = runTest(testDispatcher) {
    favoriteDao.insert(favoriteMovieEntity)

    val result =
      localDataSource.deleteItemFromDB(favoriteMovieEntity.mediaId, favoriteMovieEntity.mediaType)
    assert(result is DbResult.Success && result.data == 1)

    val remainingMovies = favoriteDao.getFavoriteMovies().first()
    assert(remainingMovies.isEmpty())
  }

  @Test
  fun deleteAll_success() = runTest(testDispatcher) {
    val fakeMovies = listOf(
      favoriteMovieEntity.copy(id = 1),
      favoriteMovieEntity.copy(id = 2)
    )
    fakeMovies.forEach { favoriteDao.insert(it) }

    val result = localDataSource.deleteAll()
    assert(result is DbResult.Success && result.data == fakeMovies.size)

    val remainingMovies = favoriteDao.getFavoriteMovies().first()
    assert(remainingMovies.isEmpty())
  }

  @Test
  fun isFavorite_returnTrue() = runTest(testDispatcher) {
    favoriteDao.insert(favoriteMovieEntity.copy(isFavorite = true))

    val result = localDataSource.isFavorite(favoriteMovieEntity.mediaId, "movie")
    assert(result is DbResult.Success && result.data)
  }

  @Test
  fun isFavorite_returnFalse() = runTest(testDispatcher) {
    val result = localDataSource.isFavorite(999, "movie") // non-existent ID
    assert(result is DbResult.Success && !result.data)
  }

  @Test
  fun isWatchlist_returnTrue() = runTest(testDispatcher) {
    favoriteDao.insert(favoriteMovieEntity)

    val result = localDataSource.isWatchlist(favoriteMovieEntity.mediaId, "movie")
    assert(result is DbResult.Success && result.data)
  }

  @Test
  fun isWatchlist_returnFalse() = runTest(testDispatcher) {
    val result = localDataSource.isWatchlist(999, "movie") // non-existent ID
    assert(result is DbResult.Success && !result.data)
  }

  @Test
  fun update_success() = runTest(testDispatcher) {
    favoriteDao.insert(favoriteMovieEntity.copy(isFavorite = false, isWatchlist = false))

    val result = localDataSource.update(
      isFavorite = true,
      isWatchlist = true,
      id = favoriteMovieEntity.mediaId,
      mediaType = favoriteMovieEntity.mediaType
    )
    assert(result is DbResult.Success && result.data == 1)

    val updatedMovie = favoriteDao.getFavoriteMovies().first().first()
    assertTrue(updatedMovie.isFavorite)
    assert(updatedMovie.isFavorite && updatedMovie.isWatchlist)
  }

  @Test
  fun insertMultipleFavorite_returnCorrectSize() = runTest(testDispatcher) {
    favoriteDao.insert(favoriteMovieEntity.copy(id = 3, isFavorite = true))
    favoriteDao.insert(favoriteMovieEntity.copy(id = 4, isFavorite = true))

    favoriteDao.getFavoriteMovies().test {
      assertEquals(awaitItem().size, 2)
      cancelAndIgnoreRemainingEvents()
    }
  }
}
