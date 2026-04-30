package com.waffiq.bazz_movies.core.data.data.repository

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.data.testutils.BaseRepositoryTest
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.mediaStateResponse
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.postMovieResponseSuccess
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.mappers.MediaStateMapper.toMediaState
import com.waffiq.bazz_movies.core.network.data.remote.datasource.movie.MovieRemoteDataSource
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.testSuccessfulPagingData
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MovieRepositoryTest : BaseRepositoryTest() {

  private lateinit var movieRepository: MoviesRepositoryImpl
  private val mockMovieDataSource: MovieRemoteDataSource = mockk()

  @Before
  fun setup() {
    movieRepository = MoviesRepositoryImpl(mockMovieDataSource)
  }

  @Test
  fun getTopRatedMovies_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockMovieDataSource.getTopRatedMovies() },
      repositoryCall = { movieRepository.getTopRatedMovies() },
      verifyDataSourceCall = { verify { mockMovieDataSource.getTopRatedMovies() } },
    )
  }

  @Test
  fun getPopularMovies_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockMovieDataSource.getPopularMovies() },
      repositoryCall = { movieRepository.getPopularMovies() },
      verifyDataSourceCall = { verify { mockMovieDataSource.getPopularMovies() } },
    )
  }

  @Test
  fun getUpcomingMovies_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockMovieDataSource.getUpcomingMovies(region) },
      repositoryCall = { movieRepository.getUpcomingMovies(region) },
      verifyDataSourceCall = { verify { mockMovieDataSource.getUpcomingMovies(region) } },
    )
  }

  @Test
  fun getPlayingNowMovies_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockMovieDataSource.getPlayingNowMovies(region) },
      repositoryCall = { movieRepository.getPlayingNowMovies(region) },
      verifyDataSourceCall = { verify { mockMovieDataSource.getPlayingNowMovies(region) } },
    )
  }

  @Test
  fun getMovieRecommendation_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockMovieDataSource.getMovieRecommendation(id) },
      repositoryCall = { movieRepository.getMovieRecommendation(id) },
      verifyDataSourceCall = { verify { mockMovieDataSource.getMovieRecommendation(any()) } },
    )
  }

  @Test
  fun getStatedMovie_whenSuccessful_returnsMappedStatedMovie() =
    runTest {
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
  fun postMovieRate_whenSuccessful_returnsCorrectResponse() =
    runTest {
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
}
