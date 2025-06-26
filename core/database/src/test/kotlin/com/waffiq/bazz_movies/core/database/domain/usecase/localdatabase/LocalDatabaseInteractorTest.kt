package com.waffiq.bazz_movies.core.database.domain.usecase.localdatabase

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.database.domain.repository.IDatabaseRepository
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.domain.Favorite
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LocalDatabaseInteractorTest {
  private val favListMovie = listOf(
    Favorite(
      id = 1,
      mediaId = 101,
      mediaType = "movie",
      genre = "Action",
      backDrop = "backdrop1",
      poster = "poster1",
      overview = "overview1",
      title = "Movie1",
      releaseDate = "2023-11-01",
      popularity = 8.5,
      rating = 4.5f,
      isFavorite = true,
      isWatchlist = false
    )
  )
  private val wtcListMovie = listOf(
    Favorite(
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
  val wtcListTv = listOf(
    Favorite(
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
  val favListTv = listOf(
    Favorite(
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

  private val mockRepository: IDatabaseRepository = mockk(relaxed = true)
  private lateinit var localDatabaseInteractor: LocalDatabaseInteractor

  @Before
  fun setup() {
    every { mockRepository.favoriteMoviesFromDB } returns flowOf(favListMovie)
    every { mockRepository.watchlistMovieFromDB } returns flowOf(wtcListMovie)
    every { mockRepository.favoriteTvFromDB } returns flowOf(favListTv)
    every { mockRepository.watchlistTvFromDB } returns flowOf(wtcListTv)
    localDatabaseInteractor = LocalDatabaseInteractor(mockRepository)
  }

  @Test
  fun favoriteMoviesFromDB_returnsListOfFavorites() = runTest {
    localDatabaseInteractor.favoriteMoviesFromDB.test {
      val result = awaitItem()
      assertEquals(1, result.size)
      assertEquals(101, result[0].mediaId)
      assertEquals("movie", result[0].mediaType)
      assertEquals(true, result[0].isFavorite)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun watchlistMovieFromDB_returnsListOfWatchlist() = runTest {
    localDatabaseInteractor.watchlistMovieFromDB.test {
      val result = awaitItem()
      assertEquals(1, result.size)
      assertEquals(102, result[0].mediaId)
      assertEquals("movie", result[0].mediaType)
      assertEquals(true, result[0].isWatchlist)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun favoriteTvFromDB_returnsListOfFavorites() = runTest {
    localDatabaseInteractor.favoriteTvFromDB.test {
      val result = awaitItem()
      assertEquals(1, result.size)
      assertEquals(104, result[0].mediaId)
      assertEquals("tv", result[0].mediaType)
      assertEquals(true, result[0].isFavorite)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun watchlistTvFromDB_returnListOfTvWatchlist() = runTest {
    localDatabaseInteractor.watchlistTvFromDB.test {
      val result = awaitItem()
      assertEquals(1, result.size)
      assertEquals(103, result[0].mediaId)
      assertEquals("tv", result[0].mediaType)
      assertEquals(true, result[0].isWatchlist)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun insertToDB_returnsSuccess() = runTest {
    val favorite = Favorite(
      id = 1,
      mediaId = 101,
      mediaType = "movie",
      genre = "Action",
      backDrop = "backdrop1",
      poster = "poster1",
      overview = "overview1",
      title = "Movie1",
      releaseDate = "2024-01-01",
      popularity = 8.5,
      rating = 4.5f,
      isFavorite = true,
      isWatchlist = false
    )

    coEvery { mockRepository.insertToDB(favorite) } returns DbResult.Success(1)

    val result = localDatabaseInteractor.insertToDB(favorite)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify { mockRepository.insertToDB(favorite) }
  }

  @Test
  fun deleteFromDB_returnsSuccess() = runTest {
    val favorite = Favorite(
      id = 1,
      mediaId = 101,
      mediaType = "movie",
      genre = "Action",
      backDrop = "backdrop1",
      poster = "poster1",
      overview = "overview1",
      title = "Movie1",
      releaseDate = "2025-01-11",
      popularity = 8.5,
      rating = 4.5f,
      isFavorite = true,
      isWatchlist = false
    )

    coEvery { mockRepository.deleteFromDB(favorite) } returns DbResult.Success(1)

    val result = localDatabaseInteractor.deleteFromDB(favorite)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify { mockRepository.deleteFromDB(favorite) }
  }

  @Test
  fun deleteAll_returnsSuccess() = runTest {
    coEvery { mockRepository.deleteAll() } returns DbResult.Success(5)

    val result = localDatabaseInteractor.deleteAll()

    assertTrue(result is DbResult.Success)
    assertEquals(5, (result as DbResult.Success).data)
    coVerify { mockRepository.deleteAll() }
  }

  @Test
  fun isFavoriteDB_returnsSuccess() = runTest {
    coEvery { mockRepository.isFavoriteDB(101, "movie") } returns DbResult.Success(true)

    val result = localDatabaseInteractor.isFavoriteDB(101, "movie")

    assertTrue(result is DbResult.Success)
    assertEquals(true, (result as DbResult.Success).data)
    coVerify { mockRepository.isFavoriteDB(101, "movie") }
  }

  @Test
  fun isWatchlistDB_returnsSuccess() = runTest {
    coEvery { mockRepository.isWatchlistDB(101, "movie") } returns DbResult.Success(true)

    val result = localDatabaseInteractor.isWatchlistDB(101, "movie")

    assertTrue(result is DbResult.Success)
    assertEquals(true, (result as DbResult.Success).data)
    coVerify { mockRepository.isWatchlistDB(101, "movie") }
  }

  @Test
  fun updateFavoriteItemDB_returnsSuccess() = runTest {
    val favorite = Favorite(
      id = 1,
      mediaId = 101,
      mediaType = "movie",
      genre = "Action",
      backDrop = "backdrop1",
      poster = "poster1",
      overview = "overview1",
      title = "Movie1",
      releaseDate = "2023-06-12",
      popularity = 8.5,
      rating = 4.5f,
      isFavorite = true,
      isWatchlist = false
    )

    coEvery { mockRepository.updateFavoriteItemDB(true, favorite) } returns DbResult.Success(1)

    val result = localDatabaseInteractor.updateFavoriteItemDB(true, favorite)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify { mockRepository.updateFavoriteItemDB(true, favorite) }
  }

  @Test
  fun updateWatchlistItemDB_returnsSuccess() = runTest {
    val favorite = Favorite(
      id = 1,
      mediaId = 101,
      mediaType = "movie",
      genre = "Action",
      backDrop = "backdrop1",
      poster = "poster1",
      overview = "overview1",
      title = "Movie1",
      releaseDate = "2022-04-02",
      popularity = 8.5,
      rating = 4.5f,
      isFavorite = true,
      isWatchlist = false
    )

    coEvery { mockRepository.updateWatchlistItemDB(false, favorite) } returns DbResult.Success(1)

    val result = localDatabaseInteractor.updateWatchlistItemDB(false, favorite)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify { mockRepository.updateWatchlistItemDB(false, favorite) }
  }
}
