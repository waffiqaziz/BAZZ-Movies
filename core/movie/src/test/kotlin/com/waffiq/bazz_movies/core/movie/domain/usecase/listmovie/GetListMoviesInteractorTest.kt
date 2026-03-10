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
    getListMoviesInteractor = GetListMoviesInteractor(mockRepository)
  }

  @Test
  fun getTopRatedMovies_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockRepository.getTopRatedMovies() },
      pagingData = fakePagingData,
      interactorCall = { getListMoviesInteractor.getTopRatedMovies() },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getPopularMovies_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockRepository.getPopularMovies() },
      pagingData = fakePagingData,
      interactorCall = { getListMoviesInteractor.getPopularMovies() },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getTrendingThisWeek_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockRepository.getTrendingThisWeek(region) },
      pagingData = fakePagingData,
      interactorCall = { getListMoviesInteractor.getTrendingThisWeek(region) },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getTrendingToday_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockRepository.getTrendingToday(region) },
      pagingData = fakePagingData,
      interactorCall = { getListMoviesInteractor.getTrendingToday(region) },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getUpcomingMovies_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockRepository.getUpcomingMovies(region) },
      pagingData = fakePagingData,
      interactorCall = { getListMoviesInteractor.getUpcomingMovies(region) },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getPlayingNowMovies_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockRepository.getPlayingNowMovies(region) },
      pagingData = fakePagingData,
      interactorCall = { getListMoviesInteractor.getPlayingNowMovies(region) },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }
}
