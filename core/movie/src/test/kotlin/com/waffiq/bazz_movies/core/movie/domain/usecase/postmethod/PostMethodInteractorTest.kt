package com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.UpdateFavoriteParams
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.PostResult
import com.waffiq.bazz_movies.core.domain.UpdateWatchlistParams
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PostMethodInteractorTest {
  private val mockRepository: IMoviesRepository = mockk()
  private lateinit var postMethodInteractor: PostMethodInteractor

  @Before
  fun setup() {
    postMethodInteractor = PostMethodInteractor(mockRepository)
  }

  @Test
  fun postFavorite_whenSuccessful_returnsCorrectResponse() = runTest {
    val postFavoriteWatchlist = PostFavoriteWatchlist(
      statusCode = 201,
      statusMessage = "Success"
    )
    val favoriteParams = UpdateFavoriteParams(
      mediaType = "movie",
      mediaId = 99999,
      favorite = false
    )

    coEvery {
      mockRepository.postFavorite(
        "sessionId",
        favoriteParams,
        12345678
      )
    } returns flowOf(Outcome.Success(postFavoriteWatchlist))

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
    val postFavoriteWatchlist = PostFavoriteWatchlist(
      statusCode = 201,
      statusMessage = "Success Add Watchlist"
    )
    val watchlistPostModel = UpdateWatchlistParams(
      mediaType = "tv",
      mediaId = 4444,
      watchlist = true
    )

    coEvery {
      mockRepository.postWatchlist(
        "sessionId",
        watchlistPostModel,
        666666
      )
    } returns flowOf(Outcome.Success(postFavoriteWatchlist))

    postMethodInteractor.postWatchlist("sessionId", watchlistPostModel, 666666).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertEquals("Success Add Watchlist", result.data.statusMessage)
      assertEquals(201, result.data.statusCode)
      awaitComplete()
    }
  }

  @Test
  fun postMovieRate_whenSuccessful_returnsCorrectResponse() = runTest {
    val postResult = PostResult(
      success = true,
      statusCode = 201,
      statusMessage = "Success Rating Movie"
    )

    coEvery {
      mockRepository.postMovieRate("sessionId", 9.0f, 7777)
    } returns flowOf(Outcome.Success(postResult))

    postMethodInteractor.postMovieRate("sessionId", 9.0f, 7777).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertTrue(result.data.success == true)
      assertEquals("Success Rating Movie", result.data.statusMessage)
      assertEquals(201, result.data.statusCode)
      awaitComplete()
    }
  }

  @Test
  fun postTvRate_whenSuccessful_returnsCorrectResponse() = runTest {
    val postResult = PostResult(
      success = true,
      statusCode = 201,
      statusMessage = "Success Rating Tv"
    )

    coEvery {
      mockRepository.postTvRate("sessionId", 9.0f, 7777)
    } returns flowOf(Outcome.Success(postResult))

    postMethodInteractor.postTvRate("sessionId", 9.0f, 7777).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertTrue(result.data.success == true)
      assertEquals("Success Rating Tv", result.data.statusMessage)
      assertEquals(201, result.data.statusCode)
      awaitComplete()
    }
  }
}
