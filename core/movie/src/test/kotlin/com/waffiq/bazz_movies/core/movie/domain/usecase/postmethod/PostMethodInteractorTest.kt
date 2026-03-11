package com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.movie.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.favoriteParams
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.postFavoriteWatchlistSuccess
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.postResult
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.watchlistParams
import io.mockk.coEvery
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PostMethodInteractorTest : BaseInteractorTest() {

  private lateinit var postMethodInteractor: PostMethodInteractor

  @Before
  fun setup() {
    postMethodInteractor = PostMethodInteractor(mockMovieRepository)
  }

  @Test
  fun postFavorite_whenSuccessful_returnsCorrectResponse() = runTest {
    coEvery {
      mockMovieRepository.postFavorite("sessionId", favoriteParams, 12345678)
    } returns flowOf(Outcome.Success(postFavoriteWatchlistSuccess))

    postMethodInteractor.postFavorite("sessionId", favoriteParams, 12345678).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertEquals("Success", result.data.statusMessage)
      assertEquals(201, result.data.statusCode)
      awaitComplete()
    }
  }

  @Test
  fun postWatchlist_whenSuccessful_returnsCorrectResponse() = runTest {
    coEvery {
      mockMovieRepository.postWatchlist("sessionId", watchlistParams, 666666)
    } returns flowOf(Outcome.Success(postFavoriteWatchlistSuccess))

    postMethodInteractor.postWatchlist("sessionId", watchlistParams, 666666).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertEquals("Success", result.data.statusMessage)
      assertEquals(201, result.data.statusCode)
      awaitComplete()
    }
  }

  @Test
  fun postMovieRate_whenSuccessful_returnsCorrectResponse() = runTest {
    coEvery {
      mockMovieRepository.postMovieRate("sessionId", 9.0f, 7777)
    } returns flowOf(Outcome.Success(postResult))

    postMethodInteractor.postMovieRate("sessionId", 9.0f, 7777).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertTrue(result.data.success == true)
      assertEquals("Success", result.data.statusMessage)
      assertEquals(201, result.data.statusCode)
      awaitComplete()
    }
  }

  @Test
  fun postTvRate_whenSuccessful_returnsCorrectResponse() = runTest {
    coEvery {
      mockMovieRepository.postTvRate("sessionId", 9.0f, 7777)
    } returns flowOf(Outcome.Success(postResult))

    postMethodInteractor.postTvRate("sessionId", 9.0f, 7777).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertTrue(result.data.success == true)
      assertEquals("Success", result.data.statusMessage)
      assertEquals(201, result.data.statusCode)
      awaitComplete()
    }
  }
}
