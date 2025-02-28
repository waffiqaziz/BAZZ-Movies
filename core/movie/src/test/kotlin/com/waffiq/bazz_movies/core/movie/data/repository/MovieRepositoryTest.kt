package com.waffiq.bazz_movies.core.movie.data.repository

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.RatedResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.StatedResponse
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toFavoritePostModel
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toWatchlistPostModel
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieRepositoryTest {

  private lateinit var movieRepository: MoviesRepository
  private val mockMovieDataSource: MovieDataSource = mockk()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    movieRepository = MoviesRepository(mockMovieDataSource)
  }

  @Test
  fun getStatedMovieSuccess_returnMappedStatedMovie() = runTest {
    val statedResponse = StatedResponse(
      id = 1234,
      favorite = true,
      ratedResponse = RatedResponse.Unrated,
      watchlist = false
    )

    coEvery { mockMovieDataSource.getStatedMovie("sessionId", 1234) } returns
      flowOf(NetworkResult.Success(statedResponse))

    movieRepository.getStatedMovie("sessionId", 1234).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertEquals(1234, result.data.id)
      assertTrue(result.data.favorite)
      assertEquals(Rated.Unrated, result.data.rated)
      assertFalse(result.data.watchlist)
      awaitComplete()
    }
  }

  @Test
  fun getStatedTvSuccess_returnMappedStatedTv() = runTest {
    val statedResponse = StatedResponse(
      id = 8888,
      favorite = false,
      ratedResponse = RatedResponse.Value(9.0),
      watchlist = true
    )

    coEvery { mockMovieDataSource.getStatedTv("sessionId", 8888) } returns
      flowOf(NetworkResult.Success(statedResponse))

    movieRepository.getStatedTv("sessionId", 8888).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertEquals(8888, result.data.id)
      assertFalse(result.data.favorite)
      assertEquals(9.0, (result.data.rated as Rated.Value).value)
      assertTrue(result.data.watchlist)
      awaitComplete()
    }
  }

  @Test
  fun postFavoriteSuccess_returnCorrectResponse() = runTest {
    val postFavoriteWatchlistResponse = PostFavoriteWatchlistResponse(
      statusCode = 201,
      statusMessage = "Success"
    )
    val favoritePostModel = FavoriteModel(
      mediaType = "movie",
      mediaId = 99999,
      favorite = false
    )

    coEvery {
      mockMovieDataSource.postFavorite(
        "sessionId",
        favoritePostModel.toFavoritePostModel(),
        12345678
      )
    } returns flowOf(NetworkResult.Success(postFavoriteWatchlistResponse))

    movieRepository.postFavorite("sessionId", favoritePostModel, 12345678).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertEquals("Success", result.data.statusMessage)
      assertEquals(201, result.data.statusCode)
      awaitComplete()
    }
  }

  @Test
  fun postWatchlistSuccess_returnCorrectResponse() = runTest {
    val postFavoriteWatchlistResponse = PostFavoriteWatchlistResponse(
      statusCode = 201,
      statusMessage = "Successs Addd Watchlist"
    )
    val watchlistPostModel = WatchlistModel(
      mediaType = "tv",
      mediaId = 4444,
      watchlist = true
    )

    coEvery {
      mockMovieDataSource.postWatchlist(
        "sessionId",
        watchlistPostModel.toWatchlistPostModel(),
        666666
      )
    } returns flowOf(NetworkResult.Success(postFavoriteWatchlistResponse))

    movieRepository.postWatchlist("sessionId", watchlistPostModel, 666666).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertEquals("Successs Addd Watchlist", result.data.statusMessage)
      assertEquals(201, result.data.statusCode)
      awaitComplete()
    }
  }

  @Test
  fun postMovieRateSuccess_returnCorrectResponse() = runTest {
    val postRateResponse = PostResponse(
      success = true,
      statusCode = 201,
      statusMessage = "Success Rating Movie"
    )

    coEvery {
      mockMovieDataSource.postMovieRate("sessionId", 9.0f, 7777)
    } returns flowOf(NetworkResult.Success(postRateResponse))

    movieRepository.postMovieRate("sessionId", 9.0f, 7777).test {
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
  fun postTvRateSuccess_returnCorrectResponse() = runTest {
    val postRateResponse = PostResponse(
      success = true,
      statusCode = 201,
      statusMessage = "Success Rating Tv"
    )

    coEvery {
      mockMovieDataSource.postTvRate("sessionId", 9.0f, 7777)
    } returns flowOf(NetworkResult.Success(postRateResponse))

    movieRepository.postTvRate("sessionId", 9.0f, 7777).test {
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
