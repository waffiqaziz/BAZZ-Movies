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
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.MediaStateResponse
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

  private lateinit var movieRepository: MoviesRepositoryImpl
  private val mockMovieDataSource: MovieDataSource = mockk()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    movieRepository = MoviesRepositoryImpl(mockMovieDataSource)
  }

  @Test
  fun getStatedMovie_whenSuccessful_returnsMappedStatedMovie() = runTest {
    val statedResponse = MediaStateResponse(
      id = 1234,
      favorite = true,
      ratedResponse = RatedResponse.Unrated,
      watchlist = false
    )

    coEvery { mockMovieDataSource.getMovieState("sessionId", 1234) } returns
      flowOf(NetworkResult.Success(statedResponse))

    movieRepository.getMovieState("sessionId", 1234).test {
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
  fun getStatedTv_whenSuccessful_returnsMappedStatedTv() = runTest {
    val statedResponse = MediaStateResponse(
      id = 8888,
      favorite = false,
      ratedResponse = RatedResponse.Value(9.0),
      watchlist = true
    )

    coEvery { mockMovieDataSource.getTvState("sessionId", 8888) } returns
      flowOf(NetworkResult.Success(statedResponse))

    movieRepository.getTvState("sessionId", 8888).test {
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
  fun postFavorite_whenSuccessful_returnsCorrectResponse() = runTest {
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
  fun postWatchlist_whenSuccessful_returnsCorrectResponse() = runTest {
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
  fun postMovieRate_whenSuccessful_returnsCorrectResponse() = runTest {
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
  fun postTvRate_whenSuccessful_returnsCorrectResponse() = runTest {
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
