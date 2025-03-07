package com.waffiq.bazz_movies.core.database.data.datasource

import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDao
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDatabase
import com.waffiq.bazz_movies.core.database.utils.DbResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
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
    val fakeMovie = FavoriteEntity(
      id = 1,
      mediaId = 101,
      mediaType = "movie",
      genre = "Action",
      backDrop = "",
      poster = "",
      overview = "",
      title = "Movie1",
      releaseDate = "",
      popularity = 0.0,
      rating = 0f,
      isFavorite = true,
      isWatchlist = false
    )
    favoriteDao.insert(fakeMovie)

    localDataSource.getFavoriteMovies.test {
      runCurrent()
      val result = awaitItem()
      assert(result.contains(fakeMovie))
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun getFavoriteTv_emitCorrectData() = runTest(testDispatcher) {
    val fakeTv = FavoriteEntity(
      id = 14324,
      mediaId = 43243242,
      mediaType = "tv",
      genre = "Action",
      backDrop = "",
      poster = "",
      overview = "",
      title = "Tv1",
      releaseDate = "",
      popularity = 0.0,
      rating = 0f,
      isFavorite = true,
      isWatchlist = false
    )
    favoriteDao.insert(fakeTv)

    localDataSource.getFavoriteTv.test {
      runCurrent()
      val result = awaitItem()
      assert(result.contains(fakeTv))
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun getWatchlistMovies_emitCorrectData() = runTest(testDispatcher) {
    val fakeMovie = FavoriteEntity(
      id = 1,
      mediaId = 101,
      mediaType = "movie",
      genre = "Action",
      backDrop = "",
      poster = "",
      overview = "",
      title = "Movie1",
      releaseDate = "",
      popularity = 0.0,
      rating = 0f,
      isFavorite = false,
      isWatchlist = true
    )
    favoriteDao.insert(fakeMovie)

    localDataSource.getWatchlistMovies.test {
      runCurrent()
      val result = awaitItem()
      assert(result.contains(fakeMovie))
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun getWatchlistTv_emitCorrectData() = runTest(testDispatcher) {
    val fakeTv = FavoriteEntity(
      id = 14324,
      mediaId = 43243242,
      mediaType = "tv",
      genre = "Action",
      backDrop = "",
      poster = "",
      overview = "",
      title = "Tv1",
      releaseDate = "",
      popularity = 0.0,
      rating = 0f,
      isFavorite = false,
      isWatchlist = true
    )
    favoriteDao.insert(fakeTv)

    localDataSource.getWatchlistTv.test {
      runCurrent()
      val result = awaitItem()
      assert(result.contains(fakeTv))
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun deleteItemFromDB_success() = runTest(testDispatcher) {
    val fakeMovie = FavoriteEntity(
      id = 1,
      mediaId = 101,
      mediaType = "movie",
      genre = "Action",
      backDrop = "",
      poster = "",
      overview = "",
      title = "Movie1",
      releaseDate = "",
      popularity = 0.0,
      rating = 0f,
      isFavorite = true,
      isWatchlist = false
    )
    favoriteDao.insert(fakeMovie)

    val result = localDataSource.deleteItemFromDB(fakeMovie.mediaId, fakeMovie.mediaType)
    assert(result is DbResult.Success && result.data == 1)

    val remainingMovies = favoriteDao.getFavoriteMovies().first()
    assert(remainingMovies.isEmpty())
  }

  @Test
  fun deleteAll_success() = runTest(testDispatcher) {
    val fakeMovies = listOf(
      FavoriteEntity(1, 101, "movie", "Action", "", "", "", "Movie1", "", 0.0, 0f, true, false),
      FavoriteEntity(2, 102, "movie", "Comedy", "", "", "", "Movie2", "", 0.0, 0f, true, false)
    )
    fakeMovies.forEach { favoriteDao.insert(it) }

    val result = localDataSource.deleteAll()
    assert(result is DbResult.Success && result.data == fakeMovies.size)

    val remainingMovies = favoriteDao.getFavoriteMovies().first()
    assert(remainingMovies.isEmpty())
  }

  @Test
  fun isFavorite_returnTrue() = runTest(testDispatcher) {
    val fakeMovie =
      FavoriteEntity(1, 101, "movie", "Action", "", "", "", "Movie1", "", 0.0, 0f, true, false)
    favoriteDao.insert(fakeMovie)

    val result = localDataSource.isFavorite(fakeMovie.mediaId, fakeMovie.mediaType)
    assert(result is DbResult.Success && result.data)
  }

  @Test
  fun isFavorite_returnFalse() = runTest(testDispatcher) {
    val result = localDataSource.isFavorite(999, "movie") // non-existent ID
    assert(result is DbResult.Success && !result.data)
  }

  @Test
  fun isWatchlist_returnTrue() = runTest(testDispatcher) {
    val fakeMovie =
      FavoriteEntity(1, 101, "movie", "Action", "", "", "", "Movie1", "", 0.0, 0f, false, true)
    favoriteDao.insert(fakeMovie)

    val result = localDataSource.isWatchlist(fakeMovie.mediaId, fakeMovie.mediaType)
    assert(result is DbResult.Success && result.data)
  }

  @Test
  fun isWatchlist_returnFalse() = runTest(testDispatcher) {
    val result = localDataSource.isWatchlist(999, "movie") // non-existent ID
    assert(result is DbResult.Success && !result.data)
  }

  @Test
  fun update_success() = runTest(testDispatcher) {
    val fakeMovie =
      FavoriteEntity(1, 101, "movie", "Action", "", "", "", "Movie1", "", 0.0, 0f, false, false)
    favoriteDao.insert(fakeMovie)

    val result = localDataSource.update(
      isFavorite = true,
      isWatchlist = true,
      id = fakeMovie.mediaId,
      mediaType = fakeMovie.mediaType
    )
    assert(result is DbResult.Success && result.data == 1)

    val updatedMovie = favoriteDao.getFavoriteMovies().first().first()
    assert(updatedMovie.isFavorite && updatedMovie.isWatchlist)
  }

  @Test
  fun insertMultipleFavorite_returnCorrectSize() = runTest(testDispatcher) {
    favoriteDao.insert(
      FavoriteEntity(1, 101, "movie", "Action", "", "", "", "Movie1", "", 0.0, 0f, true, false)
    )
    favoriteDao.insert(
      FavoriteEntity(2, 102, "movie", "Action", "", "", "", "Movie2", "", 0.0, 0f, true, false)
    )

    favoriteDao.getFavoriteMovies().test {
      assertEquals(awaitItem().size, 2)
      cancelAndIgnoreRemainingEvents()
    }
  }
}
