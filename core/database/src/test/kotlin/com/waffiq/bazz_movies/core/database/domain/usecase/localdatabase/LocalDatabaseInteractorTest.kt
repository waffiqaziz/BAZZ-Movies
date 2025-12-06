package com.waffiq.bazz_movies.core.database.domain.usecase.localdatabase

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.database.domain.repository.IDatabaseRepository
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.favoriteMovie
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.favoriteTv
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.watchlistMovie
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.watchlistTv
import com.waffiq.bazz_movies.core.database.utils.DbResult
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

  private val favListMovie = listOf(favoriteMovie)
  private val wtcListMovie = listOf(watchlistMovie)
  val favListTv = listOf(favoriteTv)
  val wtcListTv = listOf(watchlistTv)

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
  fun favoriteMoviesFromDB_whenSuccessful_returnsListOfFavorites() = runTest {
    localDatabaseInteractor.favoriteMoviesFromDB.test {
      val result = awaitItem()
      assertEquals(1, result.size)
      assertEquals(favoriteMovie, result[0])
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun watchlistMovieFromDB_whenSuccessful_returnsListOfWatchlist() = runTest {
    localDatabaseInteractor.watchlistMovieFromDB.test {
      val result = awaitItem()
      assertEquals(1, result.size)
      assertEquals(watchlistMovie, result[0])
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun favoriteTvFromDB_whenSuccessful_returnsListOfFavorites() = runTest {
    localDatabaseInteractor.favoriteTvFromDB.test {
      val result = awaitItem()
      assertEquals(1, result.size)
      assertEquals(favoriteTv, result[0])
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun watchlistTvFromDB_whenSuccessful_returnListOfTvWatchlist() = runTest {
    localDatabaseInteractor.watchlistTvFromDB.test {
      val result = awaitItem()
      assertEquals(1, result.size)
      assertEquals(watchlistTv, result[0])
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun insertToDB_whenSuccessful_returnsSuccess() = runTest {
    coEvery { mockRepository.insertToDB(favoriteTv) } returns DbResult.Success(1)

    val result = localDatabaseInteractor.insertToDB(favoriteTv)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify { mockRepository.insertToDB(favoriteTv) }
  }

  @Test
  fun deleteFromDB_whenSuccessful_returnsSuccess() = runTest {
    coEvery { mockRepository.deleteFromDB(favoriteMovie) } returns DbResult.Success(1)

    val result = localDatabaseInteractor.deleteFromDB(favoriteMovie)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify { mockRepository.deleteFromDB(favoriteMovie) }
  }

  @Test
  fun deleteAll_whenSuccessful_returnsSuccess() = runTest {
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
  fun isWatchlistDB_whenSuccessful_returnsSuccess() = runTest {
    coEvery { mockRepository.isWatchlistDB(101, "movie") } returns DbResult.Success(true)

    val result = localDatabaseInteractor.isWatchlistDB(101, "movie")

    assertTrue(result is DbResult.Success)
    assertEquals(true, (result as DbResult.Success).data)
    coVerify { mockRepository.isWatchlistDB(101, "movie") }
  }

  @Test
  fun updateFavoriteItemDB_whenSuccessful_returnsSuccess() = runTest {
    coEvery { mockRepository.updateFavoriteItemDB(true, favoriteMovie) } returns DbResult.Success(1)

    val result = localDatabaseInteractor.updateFavoriteItemDB(true, favoriteMovie)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify { mockRepository.updateFavoriteItemDB(true, favoriteMovie) }
  }

  @Test
  fun updateWatchlistItemDB_whenSuccessful_returnsSuccess() = runTest {
    coEvery { mockRepository.updateWatchlistItemDB(false, favoriteMovie) } returns DbResult.Success(
      1
    )

    val result = localDatabaseInteractor.updateWatchlistItemDB(false, favoriteMovie)

    assertTrue(result is DbResult.Success)
    assertEquals(1, (result as DbResult.Success).data)
    coVerify { mockRepository.updateWatchlistItemDB(false, favoriteMovie) }
  }
}
