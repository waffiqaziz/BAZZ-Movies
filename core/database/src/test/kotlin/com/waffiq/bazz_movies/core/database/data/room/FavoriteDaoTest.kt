package com.waffiq.bazz_movies.core.database.data.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
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
  // dump string
  private val releaseDate = "releaseDate"
  private val overview = "overview"
  private val backdrop = "backdrop"
  private val poster = "poster"

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
    val tvShow = FavoriteEntity(
      mediaId = 124324,
      mediaType = TV_MEDIA_TYPE,
      title = "Test Show",
      rating = 8.5f,
      isFavorite = true,
      isWatchlist = false,
      id = 1,
      genre = "Crime, War",
      backDrop = backdrop,
      poster = poster,
      overview = overview,
      releaseDate = releaseDate,
      popularity = 12435.0
    )

    val insertResult = favoriteDao.insert(tvShow)
    assertEquals(1, insertResult)

    val favorites = favoriteDao.getFavoriteTv().first()
    assertEquals(1, favorites.size)
    assertEquals(tvShow.mediaId, favorites[0].mediaId)
  }

  @Test
  fun insertAndGetFavoriteMovies_whenSuccessful_returnMovieDataCorrectly() = runTest {
    val movie = FavoriteEntity(
      mediaId = 2,
      mediaType = MOVIE_MEDIA_TYPE,
      title = "Test Movie",
      rating = 9.0f,
      isFavorite = true,
      isWatchlist = false,
      id = 2,
      genre = "Action, Crime",
      backDrop = backdrop,
      poster = poster,
      overview = overview,
      releaseDate = releaseDate,
      popularity = 213.0
    )

    favoriteDao.insert(movie)

    val favorites = favoriteDao.getFavoriteMovies().first()
    assertEquals(1, favorites.size)
    assertEquals(movie.mediaId, favorites[0].mediaId)
  }

  @Test
  fun getWatchlistMovies_whenSuccessful_returnDataCorrectly() = runTest {
    val movie = FavoriteEntity(
      mediaId = 3,
      mediaType = MOVIE_MEDIA_TYPE,
      title = "Watchlist Movie",
      rating = 7.5f,
      isFavorite = false,
      isWatchlist = true,
      id = 3,
      genre = "Comedy, Romance",
      backDrop = backdrop,
      poster = poster,
      overview = overview,
      releaseDate = releaseDate,
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
      mediaType = TV_MEDIA_TYPE,
      title = "Watchlist Show",
      rating = 8.0f,
      isFavorite = false,
      isWatchlist = true,
      id = 4,
      genre = "Horror",
      backDrop = backdrop,
      poster = poster,
      overview = overview,
      releaseDate = releaseDate,
      popularity = 5454.0
    )

    favoriteDao.insert(tvShow)

    val watchlist = favoriteDao.getWatchlistTv().first()
    assertEquals(1, watchlist.size)
    assertEquals(tvShow.mediaId, watchlist[0].mediaId)
  }

  @Test
  fun isFavorite_whenSuccessful_returnsTrueForFavorite() = runTest {
    val tvShow = FavoriteEntity(
      mediaId = 5,
      mediaType = TV_MEDIA_TYPE,
      title = "Favorite Show",
      rating = 8.5f,
      isFavorite = true,
      isWatchlist = false,
      id = 5,
      genre = "Science, War",
      backDrop = backdrop,
      poster = poster,
      overview = overview,
      releaseDate = releaseDate,
      popularity = 43215.0
    )
    favoriteDao.insert(tvShow)

    val isFavorite = favoriteDao.isFavorite(5, TV_MEDIA_TYPE)
    assertTrue(isFavorite)
  }

  @Test
  fun isWatchlist_whenSuccessful_returnsTrueForWatchlist() = runTest {
    val movie = FavoriteEntity(
      mediaId = 6,
      mediaType = MOVIE_MEDIA_TYPE,
      title = "Watchlist Movie",
      rating = 7.8f,
      isFavorite = false,
      isWatchlist = true,
      id = 6,
      genre = "Comedy, Thriller",
      backDrop = backdrop,
      poster = poster,
      overview = overview,
      releaseDate = releaseDate,
      popularity = 6546.0
    )
    favoriteDao.insert(movie)

    val isWatchlist = favoriteDao.isWatchlist(6, MOVIE_MEDIA_TYPE)
    assertTrue(isWatchlist)
  }

  @Test
  fun deleteItem_whenSuccessful_removesFromDatabase() = runTest {
    val tvShow = FavoriteEntity(
      mediaId = 7,
      mediaType = TV_MEDIA_TYPE,
      title = "Temporary Show",
      rating = 6.5f,
      isFavorite = true,
      isWatchlist = false,
      id = 7,
      genre = "Romance, War",
      backDrop = backdrop,
      poster = poster,
      overview = overview,
      releaseDate = releaseDate,
      popularity = 325.0
    )
    favoriteDao.insert(tvShow)

    val deleteCount = favoriteDao.deleteItem(7, TV_MEDIA_TYPE)
    assertEquals(1, deleteCount)

    val favorites = favoriteDao.getFavoriteTv().first()
    assertEquals(0, favorites.size)
  }

  @Test
  fun deleteAll_whenSuccessful_removesAllItems() = runTest {
    val tvShow = FavoriteEntity(
      mediaId = 8,
      mediaType = TV_MEDIA_TYPE,
      title = "Show 1",
      isFavorite = true,
      isWatchlist = false,
      id = 8,
      genre = "Thriller",
      backDrop = backdrop,
      poster = poster,
      overview = overview,
      releaseDate = releaseDate,
      popularity = 2343.0,
      rating = 9.6f
    )
    val movie = FavoriteEntity(
      mediaId = 9,
      mediaType = MOVIE_MEDIA_TYPE,
      title = "Movie 1",
      isFavorite = true,
      isWatchlist = false,
      id = 9,
      genre = "Action, War",
      backDrop = backdrop,
      poster = poster,
      overview = overview,
      releaseDate = releaseDate,
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
  fun update_whenSuccessful_changesValues() = runTest {
    val tvShow = FavoriteEntity(
      mediaId = 10,
      mediaType = TV_MEDIA_TYPE,
      title = "Update Test",
      isFavorite = false,
      isWatchlist = false,
      id = 2,
      genre = "Adventure",
      backDrop = backdrop,
      poster = poster,
      overview = overview,
      releaseDate = releaseDate,
      popularity = 543.0,
      rating = 45.0f
    )
    favoriteDao.insert(tvShow)

    val updateCount = favoriteDao.update(
      isFavorite = true,
      isWatchlist = true,
      id = 10,
      mediaType = TV_MEDIA_TYPE
    )

    assertEquals(1, updateCount)
    val isFavorite = favoriteDao.isFavorite(10, TV_MEDIA_TYPE)
    val isWatchlist = favoriteDao.isWatchlist(10, TV_MEDIA_TYPE)
    assertTrue(isFavorite)
    assertTrue(isWatchlist)
  }

  @Test
  fun insert_whenSuccessful_ignoresOnConflict() = runTest {
    val original = FavoriteEntity(
      mediaId = 11,
      mediaType = MOVIE_MEDIA_TYPE,
      title = "Original Title",
      isFavorite = true,
      isWatchlist = false,
      id = 11,
      genre = "Action",
      backDrop = backdrop,
      poster = poster,
      overview = overview,
      releaseDate = releaseDate,
      popularity = 3244.0,
      rating = 80.0f
    )
    val duplicate = FavoriteEntity(
      mediaId = 11,
      mediaType = MOVIE_MEDIA_TYPE,
      title = "Updated Title", // Different title
      isFavorite = false,
      isWatchlist = true,
      id = 11,
      genre = "Mystery",
      backDrop = backdrop,
      poster = poster,
      overview = overview,
      releaseDate = releaseDate,
      popularity = 45353.0,
      rating = 7.5f
    )

    val firstInsert = favoriteDao.insert(original)
    val secondInsert = favoriteDao.insert(duplicate) // Should be ignored

    assertEquals(11, firstInsert)
    assertEquals(-1, secondInsert) // -1 indicates insert was ignored

    val movies = favoriteDao.getFavoriteMovies().first()
    assertEquals(1, movies.size)
    assertEquals("Original Title", movies[0].title)
    assertEquals(true, movies[0].isFavorite)
  }
}
