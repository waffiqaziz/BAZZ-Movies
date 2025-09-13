package com.waffiq.bazz_movies.core.database.data.repository

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.database.data.datasource.LocalDataSource
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.favoriteMovie
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.favoriteMovieEntity
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.favoriteTvEntity
import com.waffiq.bazz_movies.core.database.utils.DatabaseMapper.toFavoriteEntity
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

  private fun assertTvEquals(result: List<Favorite>) {
    assertEquals(1, result.size)
    assertEquals(103, result[0].mediaId)
    assertEquals("tv", result[0].mediaType)
    assertEquals("Show1", result[0].title)
    assertEquals("Drama", result[0].genre)
  }

  private fun assertMovieEquals(result: List<Favorite>) {
    assertEquals(1, result.size)
    assertEquals(101, result[0].mediaId)
    assertEquals("movie", result[0].mediaType)
    assertEquals("Movie1", result[0].title)
  }

  @Test
  fun watchlistTvFromDB_whenSuccessful_returnTransformedDataCorrectly() = runTest {
    val favoriteEntities = listOf(favoriteTvEntity)

    every { localDataSource.getWatchlistTv } returns flowOf(favoriteEntities)

    repository = DatabaseRepositoryImpl(localDataSource)

    repository.watchlistTvFromDB.test {
      val result = awaitItem()
      assertTvEquals(result)
      assertEquals(false, result[0].isFavorite)
      assertEquals(true, result[0].isWatchlist)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun favoriteTvFromDB_whenSuccessful_returnTransformedDataCorrectly() = runTest {
    val favoriteEntities = listOf(favoriteTvEntity.copy(isFavorite = true, isWatchlist = false))

    every { localDataSource.getFavoriteTv } returns flowOf(favoriteEntities)

    repository = DatabaseRepositoryImpl(localDataSource)

    repository.favoriteTvFromDB.test {
      val result = awaitItem()
      assertTvEquals(result)
      assertEquals(true, result[0].isFavorite)
      assertEquals(false, result[0].isWatchlist)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun favoriteMoviesFromDB_whenSuccessful_transformsDataCorrectly() = runTest {
    val favoriteEntities = listOf(favoriteMovieEntity.copy(isFavorite = true, isWatchlist = false))

    every { localDataSource.getFavoriteMovies } returns flowOf(favoriteEntities)

    repository = DatabaseRepositoryImpl(localDataSource)

    repository.favoriteMoviesFromDB.test {
      val result = awaitItem()
      assertMovieEquals(result)
      assertEquals(true, result[0].isFavorite)
      assertEquals(false, result[0].isWatchlist)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun watchlistMovieFromDB_whenSuccessful_returnTransformedDataCorrectly() = runTest {
    val favoriteEntities = listOf(favoriteMovieEntity)

    every { localDataSource.getWatchlistMovies } returns flowOf(favoriteEntities)

    repository = DatabaseRepositoryImpl(localDataSource)

    repository.watchlistMovieFromDB.test {
      val result = awaitItem()
      assertMovieEquals(result)
      assertEquals(false, result[0].isFavorite)
      assertEquals(true, result[0].isWatchlist)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun insertToDB_whenSuccessful_returnsSuccessDbResult() = runTest {
    coEvery { localDataSource.insert(favoriteMovie.toFavoriteEntity()) } returns DbResult.Success(1)
    val result = repository.insertToDB(favoriteMovie)
    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)

    coVerify { localDataSource.insert(favoriteMovie.toFavoriteEntity()) }
  }

  @Test
  fun deleteFromDB_whenSuccessful_returnSuccessResult() = runTest {
    coEvery { localDataSource.deleteItemFromDB(101, "movie") } returns DbResult.Success(1)

    val result = repository.deleteFromDB(favoriteMovie)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify { localDataSource.deleteItemFromDB(101, "movie") }
  }

  @Test
  fun deleteAll_whenSuccessful_returnSuccessResult() = runTest {
    coEvery { localDataSource.deleteAll() } returns DbResult.Success(5)

    val result = repository.deleteAll()

    assertTrue(result is DbResult.Success)
    assertEquals(5, (result as DbResult.Success).data)
    coVerify { localDataSource.deleteAll() }
  }

  @Test
  fun isFavoriteDB_whenSuccessful_returnSuccessResult() = runTest {
    coEvery { localDataSource.isFavorite(101, "movie") } returns DbResult.Success(true)

    val result = repository.isFavoriteDB(101, "movie")

    assertTrue(result is DbResult.Success)
    assertEquals(true, (result as DbResult.Success).data)
    coVerify { localDataSource.isFavorite(101, "movie") }
  }

  @Test
  fun isWatchlistDB_whenSuccessful_returnSuccessResult() = runTest {
    coEvery { localDataSource.isWatchlist(101, "movie") } returns DbResult.Success(true)

    val result = repository.isWatchlistDB(101, "movie")

    assertTrue(result is DbResult.Success)
    assertEquals(true, (result as DbResult.Success).data)
    coVerify { localDataSource.isWatchlist(101, "movie") }
  }

  @Test
  fun updateFavoriteItemDB_whenSuccessful_handlesDeleteCaseCorrectly() = runTest {
    coEvery {
      localDataSource.update(isFavorite = false, isWatchlist = true, id = 101, mediaType = "movie")
    } returns DbResult.Success(1)

    val result = repository.updateFavoriteItemDB(
      isDelete = true,
      fav = favoriteMovie.copy(isFavorite = false, isWatchlist = true)
    )

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify {
      localDataSource.update(
        isFavorite = false,
        isWatchlist = true,
        id = 101,
        mediaType = "movie"
      )
    }
  }

  @Test
  fun updateFavoriteItemDB_whenSuccessful_handlesAddCaseCorrectly() = runTest {
    coEvery {
      localDataSource.update(isFavorite = true, isWatchlist = true, id = 101, mediaType = "movie")
    } returns DbResult.Success(1)

    val result = repository.updateFavoriteItemDB(
      isDelete = false,
      fav = favoriteMovie.copy(isFavorite = true, isWatchlist = true)
    )

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify {
      localDataSource.update(
        isFavorite = true,
        isWatchlist = true,
        id = 101,
        mediaType = "movie"
      )
    }
  }

  @Test
  fun updateWatchlistItemDB_whenSuccessful_handlesDeleteCaseCorrectly() = runTest {
    coEvery {
      localDataSource.update(isFavorite = true, isWatchlist = false, id = 101, mediaType = "movie")
    } returns DbResult.Success(1)

    val result = repository.updateWatchlistItemDB(isDelete = true, fav = favoriteMovie)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify {
      localDataSource.update(
        isFavorite = true,
        isWatchlist = false,
        id = 101,
        mediaType = "movie"
      )
    }
  }

  @Test
  fun updateWatchlistItemDB_whenSuccessful_handlesAddCaseCorrectly() = runTest {
    coEvery {
      localDataSource.update(isFavorite = true, isWatchlist = true, id = 101, mediaType = "movie")
    } returns DbResult.Success(1)

    val result = repository.updateWatchlistItemDB(isDelete = false, fav = favoriteMovie)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify {
      localDataSource.update(
        isFavorite = true,
        isWatchlist = true,
        id = 101,
        mediaType = "movie"
      )
    }
  }
}
