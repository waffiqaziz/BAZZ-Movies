package com.waffiq.bazz_movies.core.database.data.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.favoriteMovieEntity
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.favoriteTvEntity
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.watchlistMovieEntity
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.watchlistTvEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

// https://developer.android.com/training/data-storage/room/testing-db
@RunWith(RobolectricTestRunner::class)
class FavoriteDaoTest {

  private lateinit var database: FavoriteDatabase
  private lateinit var favoriteDao: FavoriteDao

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    // use in-memory database for testing
    database = Room.inMemoryDatabaseBuilder(
      context,
      FavoriteDatabase::class.java
    ).allowMainThreadQueries() // allow for testing on main thread
      .build()

    favoriteDao = database.favoriteDao()
  }

  @After
  fun tearDown() {
    database.close()
  }

  @Test
  fun insertAndGetFavoriteTv_whenSuccessful_returnTvDataCorrectly() = runTest {
    val insertResult = favoriteDao.insert(favoriteTvEntity)
    assertEquals(favoriteTvEntity.id.toLong(), insertResult)

    val favorites = favoriteDao.getFavoriteTv().first()
    assertEquals(1, favorites.size)
    assertEquals(favoriteTvEntity.mediaId, favorites[0].mediaId)
  }

  @Test
  fun insertAndGetFavoriteMovies_whenSuccessful_returnMovieDataCorrectly() = runTest {
    favoriteDao.insert(favoriteMovieEntity)

    val favorites = favoriteDao.getFavoriteMovies().first()
    assertEquals(1, favorites.size)
    assertEquals(favoriteMovieEntity.mediaId, favorites[0].mediaId)
  }

  @Test
  fun getWatchlistMovies_whenSuccessful_returnDataCorrectly() = runTest {
    favoriteDao.insert(watchlistMovieEntity)

    val watchlist = favoriteDao.getWatchlistMovies().first()
    assertEquals(1, watchlist.size)
    assertEquals(watchlistMovieEntity.mediaId, watchlist[0].mediaId)
  }

  @Test
  fun getWatchlistTv_whenSuccessful_returnDataCorrectly() = runTest {
    favoriteDao.insert(watchlistTvEntity)

    val watchlist = favoriteDao.getWatchlistTv().first()
    assertEquals(1, watchlist.size)
    assertEquals(watchlistTvEntity.mediaId, watchlist[0].mediaId)
  }

  @Test
  fun isFavorite_whenSuccessful_returnsTrueForFavorite() = runTest {
    favoriteDao.insert(favoriteTvEntity)

    val isFavorite = favoriteDao.isFavorite(103, TV_MEDIA_TYPE)
    assertTrue(isFavorite)
  }

  @Test
  fun isWatchlist_whenSuccessful_returnsTrueForWatchlist() = runTest {
    favoriteDao.insert(watchlistMovieEntity)

    val isWatchlist = favoriteDao.isWatchlist(101, MOVIE_MEDIA_TYPE)
    assertTrue(isWatchlist)
  }

  @Test
  fun deleteItem_whenSuccessful_removesFromDatabase() = runTest {
    favoriteDao.insert(favoriteTvEntity)

    val deleteCount = favoriteDao.deleteItem(103, TV_MEDIA_TYPE)
    assertEquals(1, deleteCount)

    val favorites = favoriteDao.getFavoriteTv().first()
    assertEquals(0, favorites.size)
  }

  @Test
  fun deleteAll_whenSuccessful_removesAllItems() = runTest {
    favoriteDao.insert(favoriteTvEntity)
    favoriteDao.insert(favoriteMovieEntity)

    // check if the item is inserted
    val favoritesTv = favoriteDao.getFavoriteTv().first()
    assertEquals(1, favoritesTv.size)

    val favoritesMovie = favoriteDao.getFavoriteMovies().first()
    assertEquals(1, favoritesMovie.size)

    val deleteCount = favoriteDao.deleteALl()

    assertEquals(2, deleteCount)
    assertEquals(0, favoriteDao.getFavoriteTv().first().size)
    assertEquals(0, favoriteDao.getFavoriteMovies().first().size)
  }

  @Test
  fun update_whenSuccessful_changesValues() = runTest {
    favoriteDao.insert(favoriteTvEntity)

    val updateCount = favoriteDao.update(
      isFavorite = true,
      isWatchlist = true,
      id = 103,
      mediaType = TV_MEDIA_TYPE
    )

    assertEquals(1, updateCount)
    val isFavorite = favoriteDao.isFavorite(103, TV_MEDIA_TYPE)
    val isWatchlist = favoriteDao.isWatchlist(103, TV_MEDIA_TYPE)
    assertTrue(isFavorite)
    assertTrue(isWatchlist)
  }

  @Test
  fun insert_whenSuccessful_ignoresOnConflict() = runTest {
    val original = favoriteMovieEntity.copy(title = "Original Title")
    val duplicate = favoriteMovieEntity.copy(title = "Updated Title")

    val firstInsert = favoriteDao.insert(original)
    val secondInsert = favoriteDao.insert(duplicate) // Should be ignored

    assertEquals(1, firstInsert)
    assertEquals(-1, secondInsert) // -1 indicates insert was ignored

    val movies = favoriteDao.getFavoriteMovies().first()
    assertEquals(1, movies.size)
    assertEquals("Original Title", movies[0].title)
    assertEquals(true, movies[0].isFavorite)
  }
}
