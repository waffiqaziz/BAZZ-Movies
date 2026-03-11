package com.waffiq.bazz_movies.core.movie.domain.usecase.listmovie

import com.waffiq.bazz_movies.core.movie.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.movieMediaItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetListMoviesInteractorTest : BaseInteractorTest() {

  private lateinit var getListMoviesInteractor: GetListMoviesInteractor

  @Before
  fun setup() {
    getListMoviesInteractor = GetListMoviesInteractor(mockMovieRepository)
  }

  @Test
  fun getTopRatedMovies_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockMovieRepository.getTopRatedMovies() },
      pagingData = fakeMoviePagingData,
      interactorCall = { getListMoviesInteractor.getTopRatedMovies() },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getPopularMovies_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockMovieRepository.getPopularMovies() },
      pagingData = fakeMoviePagingData,
      interactorCall = { getListMoviesInteractor.getPopularMovies() },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getTrendingThisWeek_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockMovieRepository.getTrendingThisWeek(region) },
      pagingData = fakeMoviePagingData,
      interactorCall = { getListMoviesInteractor.getTrendingThisWeek(region) },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getTrendingToday_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockMovieRepository.getTrendingToday(region) },
      pagingData = fakeMoviePagingData,
      interactorCall = { getListMoviesInteractor.getTrendingToday(region) },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getUpcomingMovies_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockMovieRepository.getUpcomingMovies(region) },
      pagingData = fakeMoviePagingData,
      interactorCall = { getListMoviesInteractor.getUpcomingMovies(region) },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getPlayingNowMovies_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockMovieRepository.getPlayingNowMovies(region) },
      pagingData = fakeMoviePagingData,
      interactorCall = { getListMoviesInteractor.getPlayingNowMovies(region) },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }
}
