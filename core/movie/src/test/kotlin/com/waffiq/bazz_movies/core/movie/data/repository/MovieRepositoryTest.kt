package com.waffiq.bazz_movies.core.movie.data.repository

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.mappers.MediaStateMapper.toMediaState
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.createSampleMediaItemResponse
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.createSamplePagingData
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.favoriteParams
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.mediaStateResponse
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.postFavoriteWatchlistResponseSuccess
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.postMovieResponseSuccess
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.postTvRateResponseSuccess
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.watchlistParams
import com.waffiq.bazz_movies.core.movie.utils.Helper.getDateToday
import com.waffiq.bazz_movies.core.movie.utils.Helper.getDateTwoWeeksFromToday
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toFavoriteRequest
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toWatchlistRequest
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.testSuccessfulPagingData
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieRepositoryTest {

  private val id = 123
  private val region = "id"
  private val response = createSampleMediaItemResponse()
  private val fakePagingData = createSamplePagingData(response, response)

  private lateinit var movieRepository: MoviesRepositoryImpl
  private val mockMovieDataSource: MovieDataSource = mockk()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    movieRepository = MoviesRepositoryImpl(mockMovieDataSource)
  }

  @Test
  fun getTrendingToday_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockMovieDataSource.getTrendingToday(region) },
      repositoryCall = { movieRepository.getTrendingToday(region) },
      verifyDataSourceCall = { verify { mockMovieDataSource.getTrendingToday(region) } }
    )
  }

  @Test
  fun getTopRatedMovies_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockMovieDataSource.getTopRatedMovies() },
      repositoryCall = { movieRepository.getTopRatedMovies() },
      verifyDataSourceCall = { verify { mockMovieDataSource.getTopRatedMovies() } }
    )
  }

  @Test
  fun getPopularMovies_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockMovieDataSource.getPopularMovies() },
      repositoryCall = { movieRepository.getPopularMovies() },
      verifyDataSourceCall = { verify { mockMovieDataSource.getPopularMovies() } }
    )
  }

  @Test
  fun getUpcomingMovies_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockMovieDataSource.getUpcomingMovies(region) },
      repositoryCall = { movieRepository.getUpcomingMovies(region) },
      verifyDataSourceCall = { verify { mockMovieDataSource.getUpcomingMovies(region) } }
    )
  }

  @Test
  fun getPlayingNowMovies_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockMovieDataSource.getPlayingNowMovies(region) },
      repositoryCall = { movieRepository.getPlayingNowMovies(region) },
      verifyDataSourceCall = { verify { mockMovieDataSource.getPlayingNowMovies(region) } }
    )
  }

  @Test
  fun getPopularTv_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockMovieDataSource.getPopularTv(region, getDateTwoWeeksFromToday()) },
      repositoryCall = { movieRepository.getPopularTv(region) },
      verifyDataSourceCall = {
        verify { mockMovieDataSource.getPopularTv(region, getDateTwoWeeksFromToday()) }
      }
    )
  }

  @Test
  fun getAiringThisWeekTv_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = {
        mockMovieDataSource.getAiringTv(region, getDateTwoWeeksFromToday(), getDateToday())
      },
      repositoryCall = { movieRepository.getAiringThisWeekTv(region) },
      verifyDataSourceCall = {
        verify {
          mockMovieDataSource.getAiringTv(region, getDateTwoWeeksFromToday(), getDateToday())
        }
      }
    )
  }

  @Test
  fun getAiringTodayTv_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockMovieDataSource.getAiringTv(region, getDateToday(), getDateToday()) },
      repositoryCall = { movieRepository.getAiringTodayTv(region) },
      verifyDataSourceCall = {
        verify { mockMovieDataSource.getAiringTv(region, getDateToday(), getDateToday()) }
      }
    )
  }

  @Test
  fun getTopRatedTv_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockMovieDataSource.getTopRatedTv() },
      repositoryCall = { movieRepository.getTopRatedTv() },
      verifyDataSourceCall = { verify { mockMovieDataSource.getTopRatedTv() } }
    )
  }

  @Test
  fun getTrendingThisWeek_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockMovieDataSource.getTrendingThisWeek(region) },
      repositoryCall = { movieRepository.getTrendingThisWeek(region) },
      verifyDataSourceCall = { verify { mockMovieDataSource.getTrendingThisWeek(region) } }
    )
  }

  @Test
  fun getMovieRecommendation_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockMovieDataSource.getMovieRecommendation(id) },
      repositoryCall = { movieRepository.getMovieRecommendation(id) },
      verifyDataSourceCall = { verify { mockMovieDataSource.getMovieRecommendation(any()) } }
    )
  }

  @Test
  fun getTvRecommendation_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockMovieDataSource.getTvRecommendation(id) },
      repositoryCall = { movieRepository.getTvRecommendation(id) },
      verifyDataSourceCall = { verify { mockMovieDataSource.getTvRecommendation(any()) } }
    )
  }

  @Test
  fun getStatedMovie_whenSuccessful_returnsMappedStatedMovie() = runTest {
    coEvery { mockMovieDataSource.getMovieState("sessionId", 1234) } returns
      flowOf(NetworkResult.Success(mediaStateResponse))

    movieRepository.getMovieState("sessionId", 1234).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertEquals(mediaStateResponse.toMediaState(), result.data)
      awaitComplete()
    }
  }

  @Test
  fun getStatedTv_whenSuccessful_returnsMappedStatedTv() = runTest {
    coEvery { mockMovieDataSource.getTvState("sessionId", 8888) } returns
      flowOf(NetworkResult.Success(mediaStateResponse))

    movieRepository.getTvState("sessionId", 8888).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertEquals(mediaStateResponse.toMediaState(), result.data)
      awaitComplete()
    }
  }

  @Test
  fun postFavorite_whenSuccessful_returnsCorrectResponse() = runTest {
    coEvery {
      mockMovieDataSource.postFavorite(
        "sessionId",
        favoriteParams.toFavoriteRequest(),
        12345678
      )
    } returns flowOf(NetworkResult.Success(postFavoriteWatchlistResponseSuccess))

    movieRepository.postFavorite("sessionId", favoriteParams, 12345678).test {
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
      mockMovieDataSource.postWatchlist(
        "sessionId",
        watchlistParams.toWatchlistRequest(),
        666666
      )
    } returns flowOf(NetworkResult.Success(postFavoriteWatchlistResponseSuccess))

    movieRepository.postWatchlist("sessionId", watchlistParams, 666666).test {
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
      mockMovieDataSource.postMovieRate("sessionId", 9.0f, 7777)
    } returns flowOf(NetworkResult.Success(postMovieResponseSuccess))

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
    coEvery {
      mockMovieDataSource.postTvRate("sessionId", 9.0f, 7777)
    } returns flowOf(NetworkResult.Success(postTvRateResponseSuccess))

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
