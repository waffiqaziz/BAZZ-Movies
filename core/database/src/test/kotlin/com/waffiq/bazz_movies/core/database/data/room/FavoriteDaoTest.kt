package com.waffiq.bazz_movies.core.database.data.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

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
  fun insertAndGetFavoriteTv_success() = runTest {
    // Given
    val tvShow = FavoriteEntity(
      mediaId = 124324,
      mediaType = "tv",
      title = "Test Show",
      rating = 8.5f,
      isFavorite = true,
      isWatchlist = false,
      id = 1,
      genre = "Action, Adventure",
      backDrop = "backdrop",
      poster = "poster",
      overview = "overview",
      releaseDate = "release date",
      popularity = 12435.0
    )

    val insertResult = favoriteDao.insert(tvShow)
    assertEquals(1, insertResult)

    val favorites = favoriteDao.getFavoriteTv().first()
    assertEquals(1, favorites.size)
    assertEquals(tvShow.mediaId, favorites[0].mediaId)
  }

  @Test
  fun insertAndGetFavoriteMovies_success() = runTest {
    val movie = FavoriteEntity(
      mediaId = 2,
      mediaType = "movie",
      title = "Test Movie",
      rating = 9.0f,
      isFavorite = true,
      isWatchlist = false,
      id = 2,
      genre = "Action, Adventure",
      backDrop = "backdrop",
      poster = "poster",
      overview = "overview",
      releaseDate = "release date",
      popularity = 213.0
    )

    favoriteDao.insert(movie)

    val favorites = favoriteDao.getFavoriteMovies().first()
    assertEquals(1, favorites.size)
    assertEquals(movie.mediaId, favorites[0].mediaId)
  }

  @Test
  fun getWatchlistMovies_success() = runTest {
    val movie = FavoriteEntity(
      mediaId = 3,
      mediaType = "movie",
      title = "Watchlist Movie",
      rating = 7.5f,
      isFavorite = false,
      isWatchlist = true,
      id = 3,
      genre = "Comedy, Romance",
      backDrop = "backdrop",
      poster = "poster",
      overview = "overview",
      releaseDate = "release date",
      popularity = 56456.0
    )

    favoriteDao.insert(movie)

    val watchlist = favoriteDao.getWatchlistMovies().first()
    assertEquals(1, watchlist.size)
    assertEquals(movie.mediaId, watchlist[0].mediaId)
  }

  @Test
  fun getWatchlistTv_success() = runTest {
    val tvShow = FavoriteEntity(
      mediaId = 4,
      mediaType = "tv",
      title = "Watchlist Show",
      rating = 8.0f,
      isFavorite = false,
      isWatchlist = true,
      id = 4,
      genre = "Horror",
      backDrop = "backdrop",
      poster = "poster",
      overview = "overview",
      releaseDate = "release date",
      popularity = 5454.0
    )

    favoriteDao.insert(tvShow)

    val watchlist = favoriteDao.getWatchlistTv().first()
    assertEquals(1, watchlist.size)
    assertEquals(tvShow.mediaId, watchlist[0].mediaId)
  }

  @Test
  fun isFavorite_returnsTrueForFavorite() = runTest {
    // Given
    val tvShow = FavoriteEntity(
      mediaId = 5,
      mediaType = "tv",
      title = "Favorite Show",
      rating = 8.5f,
      isFavorite = true,
      isWatchlist = false,
      id = 5,
      genre = "Action, Adventure",
      backDrop = "backdrop",
      poster = "poster",
      overview = "overview",
      releaseDate = "release date",
      popularity = 43215.0
    )
    favoriteDao.insert(tvShow)

    val isFavorite = favoriteDao.isFavorite(5, "tv")
    assertTrue(isFavorite)
  }

  @Test
  fun isWatchlist_returnsTrueForWatchlist() = runTest {
    val movie = FavoriteEntity(
      mediaId = 6,
      mediaType = "movie",
      title = "Watchlist Movie",
      rating = 7.8f,
      isFavorite = false,
      isWatchlist = true,
      id = 6,
      genre = "Action, Adventure",
      backDrop = "backdrop",
      poster = "poster",
      overview = "overview",
      releaseDate = "release date",
      popularity = 6546.0
    )
    favoriteDao.insert(movie)

    val isWatchlist = favoriteDao.isWatchlist(6, "movie")
    assertTrue(isWatchlist)
  }

  @Test
  fun deleteItem_removesFromDatabase() = runTest {
    val tvShow = FavoriteEntity(
      mediaId = 7,
      mediaType = "tv",
      title = "Temporary Show",
      rating = 6.5f,
      isFavorite = true,
      isWatchlist = false,
      id = 7,
      genre = "Action, Adventure",
      backDrop = "backdrop",
      poster = "poster",
      overview = "overview",
      releaseDate = "release date",
      popularity = 325.0
    )
    favoriteDao.insert(tvShow)

    val deleteCount = favoriteDao.deleteItem(7, "tv")
    assertEquals(1, deleteCount)
    
    val favorites = favoriteDao.getFavoriteTv().first()
    assertEquals(0, favorites.size)
  }

  @Test
  fun deleteAll_removesAllItems() = runTest {
    // Given
    val tvShow = FavoriteEntity(
      mediaId = 8,
      mediaType = "tv",
      title = "Show 1",
      isFavorite = true,
      isWatchlist = false,
      id = 8,
      genre = "Thriller",
      backDrop = "backdrop",
      poster = "poster",
      overview = "overview",
      releaseDate = "release date",
      popularity = 2343.0,
      rating = 9.6f
    )
    val movie = FavoriteEntity(
      mediaId = 9,
      mediaType = "movie",
      title = "Movie 1",
      isFavorite = true,
      isWatchlist = false,
      id = 9,
      genre = "Action, Adventure",
      backDrop = "backdrop",
      poster = "poster",
      overview = "overview",
      releaseDate = "release date",
      popularity = 213.0,
      rating = 75.0f
    )
    favoriteDao.insert(tvShow)
    favoriteDao.insert(movie)

    val deleteCount = favoriteDao.deleteALl()

    assertEquals(2, deleteCount)
    assertEquals(0, favoriteDao.getFavoriteTv().first().size)
    assertEquals(0, favoriteDao.getFavoriteMovies().first().size)
  }

  @Test
  fun update_changesValues() = runTest {
    // Given
    val tvShow = FavoriteEntity(
      mediaId = 10,
      mediaType = "tv",
      title = "Update Test",
      isFavorite = false,
      isWatchlist = false,
      id = 2,
      genre = "Action, Adventure",
      backDrop = "backdrop",
      poster = "poster",
      overview = "overview",
      releaseDate = "release date",
      popularity = 543.0,
      rating = 45.0f
    )
    favoriteDao.insert(tvShow)

    // When
    val updateCount = favoriteDao.update(true, true, 10, "tv")

    // Then
    assertEquals(1, updateCount)
    val isFavorite = favoriteDao.isFavorite(10, "tv")
    val isWatchlist = favoriteDao.isWatchlist(10, "tv")
    assertTrue(isFavorite)
    assertTrue(isWatchlist)
  }

  @Test
  fun insert_ignoresOnConflict() = runTest {
    val original = FavoriteEntity(
      mediaId = 11,
      mediaType = "movie",
      title = "Original Title",
      isFavorite = true,
      isWatchlist = false,
      id = 11,
      genre = "Action, Adventure",
      backDrop = "backdrop",
      poster = "poster",
      overview = "overview",
      releaseDate = "release date",
      popularity = 3244.0,
      rating = 80.0f
    )
    val duplicate = FavoriteEntity(
      mediaId = 11,
      mediaType = "movie",
      title = "Updated Title", // Different title
      isFavorite = false,
      isWatchlist = true,
      id = 11,
      genre = "Action, Adventure",
      backDrop = "backdrop",
      poster = "poster",
      overview = "overview",
      releaseDate = "release date",
      popularity = 45353.0,
      rating = 7.5f
    )

    val firstInsert = favoriteDao.insert(original)
    val secondInsert = favoriteDao.insert(duplicate) // Should be ignored

    // Then
    assertEquals(11, firstInsert)
    assertEquals(-1, secondInsert) // -1 indicates insert was ignored

    // Original data should remain
    val movies = favoriteDao.getFavoriteMovies().first()
    assertEquals(1, movies.size)
    assertEquals("Original Title", movies[0].title)
    assertEquals(true, movies[0].isFavorite)
  }
}
