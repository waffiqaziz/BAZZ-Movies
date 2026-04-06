package com.waffiq.bazz_movies.core.movie.domain.usecase.listmovie

import com.waffiq.bazz_movies.core.movie.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.MOVIE_ID
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.USER_REGION
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.movieMediaItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetListMoviesInteractorTest : BaseInteractorTest() {

  private lateinit var getListMoviesInteractor: GetListMoviesInteractor

  @Before
  override fun setup() {
    super.setup()
    getListMoviesInteractor = GetListMoviesInteractor(mockMoviesRepository, mockUserRepository)
  }

  @Test
  fun getTopRatedMovies_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockMoviesRepository.getTopRatedMovies() },
      pagingData = fakeMoviePagingData,
      interactorCall = { getListMoviesInteractor.getTopRatedMovies() },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getPopularMovies_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockMoviesRepository.getPopularMovies() },
      pagingData = fakeMoviePagingData,
      interactorCall = { getListMoviesInteractor.getPopularMovies() },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getTrendingThisWeek_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockMoviesRepository.getTrendingThisWeek(USER_REGION) },
      pagingData = fakeMoviePagingData,
      interactorCall = { getListMoviesInteractor.getTrendingThisWeek() },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getTrendingToday_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockMoviesRepository.getTrendingToday(USER_REGION) },
      pagingData = fakeMoviePagingData,
      interactorCall = { getListMoviesInteractor.getTrendingToday() },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getUpcomingMovies_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockMoviesRepository.getUpcomingMovies(USER_REGION) },
      pagingData = fakeMoviePagingData,
      interactorCall = { getListMoviesInteractor.getUpcomingMovies() },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getPlayingNowMovies_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockMoviesRepository.getPlayingNowMovies(USER_REGION) },
      pagingData = fakeMoviePagingData,
      interactorCall = { getListMoviesInteractor.getPlayingNowMovies() },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getMovieRecommendation_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockMoviesRepository.getMovieRecommendation(MOVIE_ID) },
      pagingData = fakeMoviePagingData,
      interactorCall = { getListMoviesInteractor.getMovieRecommendation(MOVIE_ID) },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }
}
