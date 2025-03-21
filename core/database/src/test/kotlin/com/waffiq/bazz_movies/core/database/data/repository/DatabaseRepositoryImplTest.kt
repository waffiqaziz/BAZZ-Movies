package com.waffiq.bazz_movies.core.database.data.repository

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.database.data.datasource.LocalDataSource
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DatabaseRepositoryImplTest {

  private lateinit var repository: DatabaseRepositoryImpl
  private val localDataSource: LocalDataSource = mockk()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setUp() {
    every { localDataSource.getFavoriteMovies } returns flowOf(listOf())
    every { localDataSource.getFavoriteTv } returns flowOf(listOf())
    every { localDataSource.getWatchlistMovies } returns flowOf(listOf())
    every { localDataSource.getWatchlistTv } returns flowOf(listOf())
    repository = DatabaseRepositoryImpl(localDataSource)
  }

  @Test
  fun watchlistTvFromDB_returnTransformedDataCorrectly() = runTest {
    val favoriteEntities = listOf(
      FavoriteEntity(
        id = 1,
        mediaId = 103,
        mediaType = "tv",
        genre = "Drama",
        backDrop = "backdrop3",
        poster = "poster3",
        overview = "overview3",
        title = "Show1",
        releaseDate = "2023-01-03",
        popularity = 9.0,
        rating = 4.8f,
        isFavorite = false,
        isWatchlist = true
      )
    )

    every { localDataSource.getWatchlistTv } returns flowOf(favoriteEntities)

    repository = DatabaseRepositoryImpl(localDataSource)

    repository.watchlistTvFromDB.test {
      val result = awaitItem()
      assertEquals(1, result.size)
      assertEquals(103, result[0].mediaId)
      assertEquals("tv", result[0].mediaType)
      assertEquals("Show1", result[0].title)
      assertEquals(false, result[0].isFavorite)
      assertEquals(true, result[0].isWatchlist)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun favoriteTvFromDB_returnTransformedDataCorrectly() = runTest {
    val favoriteEntities = listOf(
      FavoriteEntity(
        id = 1,
        mediaId = 104,
        mediaType = "tv",
        genre = "Sci-Fi",
        backDrop = "backdrop4",
        poster = "poster4",
        overview = "overview4",
        title = "Show2",
        releaseDate = "2023-01-04",
        popularity = 8.7,
        rating = 4.3f,
        isFavorite = true,
        isWatchlist = false
      )
    )

    every { localDataSource.getFavoriteTv } returns flowOf(favoriteEntities)

    repository = DatabaseRepositoryImpl(localDataSource)

    repository.favoriteTvFromDB.test {
      val result = awaitItem()
      assertEquals(1, result.size)
      assertEquals(104, result[0].mediaId)
      assertEquals("tv", result[0].mediaType)
      assertEquals("Show2", result[0].title)
      assertEquals("Sci-Fi", result[0].genre)
      assertEquals(true, result[0].isFavorite)
      assertEquals(false, result[0].isWatchlist)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun favoriteMoviesFromDB_transformsDataCorrectly() = runTest {
    val favoriteEntities = listOf(
      FavoriteEntity(
        id = 1,
        mediaId = 101,
        mediaType = "movie",
        genre = "Action",
        backDrop = "backdrop1",
        poster = "poster1",
        overview = "overview1",
        title = "Movie1",
        releaseDate = "2023-01-01",
        popularity = 8.5,
        rating = 4.5f,
        isFavorite = true,
        isWatchlist = false
      )
    )

    every { localDataSource.getFavoriteMovies } returns flowOf(favoriteEntities)

    repository = DatabaseRepositoryImpl(localDataSource)

    repository.favoriteMoviesFromDB.test {
      val result = awaitItem()
      assertEquals(1, result.size)
      assertEquals(101, result[0].mediaId)
      assertEquals("movie", result[0].mediaType)
      assertEquals("Movie1", result[0].title)
      assertEquals(true, result[0].isFavorite)
      assertEquals(false, result[0].isWatchlist)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun watchlistMovieFromDB_returnTransformedDataCorrectly() = runTest {
    val favoriteEntities = listOf(
      FavoriteEntity(
        id = 1,
        mediaId = 102,
        mediaType = "movie",
        genre = "Comedy",
        backDrop = "backdrop2",
        poster = "poster2",
        overview = "overview2",
        title = "Movie2",
        releaseDate = "2023-01-02",
        popularity = 7.5,
        rating = 3.5f,
        isFavorite = false,
        isWatchlist = true
      )
    )

    every { localDataSource.getWatchlistMovies } returns flowOf(favoriteEntities)

    repository = DatabaseRepositoryImpl(localDataSource)

    repository.watchlistMovieFromDB.test {
      val result = awaitItem()
      assertEquals(1, result.size)
      assertEquals(102, result[0].mediaId)
      assertEquals("Comedy", result[0].genre)
      assertEquals(false, result[0].isFavorite)
      assertEquals(true, result[0].isWatchlist)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun insertToDB_returnSuccessResult() = runTest {
    val favorite = Favorite(
      id = 0,
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

    val favoriteEntity = FavoriteEntity(
      id = 0,
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

    coEvery { localDataSource.insert(any()) } returns DbResult.Success(1)

    val result = repository.insertToDB(favorite)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify {
      localDataSource.insert(
        match {
          it.mediaId == favoriteEntity.mediaId &&
            it.mediaType == favoriteEntity.mediaType &&
            it.title == favoriteEntity.title
        }
      )
    }
  }

  @Test
  fun insertToDB_returnsSuccessDbResult() = runTest {
    val favoriteEntity = FavoriteEntity(
      id = 1344,
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
    val favorite = Favorite(
      id = 1344,
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
    coEvery { localDataSource.insert(favoriteEntity) } returns DbResult.Success(1)
    val result = repository.insertToDB(favorite)
    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)

    coVerify { localDataSource.insert(favoriteEntity) }
  }

  @Test
  fun deleteFromDB_returnSuccessResult() = runTest {
    val favorite = Favorite(
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

    coEvery { localDataSource.deleteItemFromDB(101, "movie") } returns DbResult.Success(1)

    val result = repository.deleteFromDB(favorite)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify { localDataSource.deleteItemFromDB(101, "movie") }
  }

  @Test
  fun deleteAll_returnSuccessResult() = runTest {
    coEvery { localDataSource.deleteAll() } returns DbResult.Success(5)

    val result = repository.deleteAll()

    assertTrue(result is DbResult.Success)
    assertEquals(5, (result as DbResult.Success).data)
    coVerify { localDataSource.deleteAll() }
  }

  @Test
  fun isFavoriteDB_returnSuccessResult() = runTest {
    coEvery { localDataSource.isFavorite(101, "movie") } returns DbResult.Success(true)

    val result = repository.isFavoriteDB(101, "movie")

    assertTrue(result is DbResult.Success)
    assertEquals(true, (result as DbResult.Success).data)
    coVerify { localDataSource.isFavorite(101, "movie") }
  }

  @Test
  fun isWatchlistDB_returnSuccessResult() = runTest {
    coEvery { localDataSource.isWatchlist(101, "movie") } returns DbResult.Success(true)

    val result = repository.isWatchlistDB(101, "movie")

    assertTrue(result is DbResult.Success)
    assertEquals(true, (result as DbResult.Success).data)
    coVerify { localDataSource.isWatchlist(101, "movie") }
  }

  @Test
  fun updateFavoriteItemDB_handlesDeleteCaseCorrectly() = runTest {
    val favorite = Favorite(
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
      isWatchlist = true
    )

    coEvery {
      localDataSource.update(false, true, 101, "movie")
    } returns DbResult.Success(1)

    val result = repository.updateFavoriteItemDB(isDelete = true, fav = favorite)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify { localDataSource.update(false, true, 101, "movie") }
  }

  @Test
  fun updateFavoriteItemDB_handlesAddCaseCorrectly() = runTest {
    val favorite = Favorite(
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

    coEvery {
      localDataSource.update(true, true, 101, "movie")
    } returns DbResult.Success(1)

    val result = repository.updateFavoriteItemDB(isDelete = false, fav = favorite)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify { localDataSource.update(true, true, 101, "movie") }
  }

  @Test
  fun updateWatchlistItemDB_handlesDeleteCaseCorrectly() = runTest {
    val favorite = Favorite(
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
      isWatchlist = true
    )

    coEvery {
      localDataSource.update(true, false, 101, "movie")
    } returns DbResult.Success(1)

    val result = repository.updateWatchlistItemDB(isDelete = true, fav = favorite)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify { localDataSource.update(true, false, 101, "movie") }
  }

  @Test
  fun updateWatchlistItemDB_handlesAddCaseCorrectly() = runTest {
    val favorite = Favorite(
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

    coEvery {
      localDataSource.update(true, true, 101, "movie")
    } returns DbResult.Success(1)

    val result = repository.updateWatchlistItemDB(isDelete = false, fav = favorite)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify { localDataSource.update(true, true, 101, "movie") }
  }
}
