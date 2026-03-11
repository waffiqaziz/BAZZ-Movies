package com.waffiq.bazz_movies.core.movie.domain.usecase.listtv

import com.waffiq.bazz_movies.core.movie.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.tvMediaItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetListTvInteractorTest : BaseInteractorTest() {

  private lateinit var getListTvInteractor: GetListTvInteractor

  @Before
  fun setup() {
    getListTvInteractor = GetListTvInteractor(mockMovieRepository)
  }

  @Test
  fun getPopularTv_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockMovieRepository.getPopularTv(region) },
      pagingData = fakeTvPagingData,
      interactorCall = { getListTvInteractor.getPopularTv(region) },
    ) { pagingList ->
      assertEquals(tvMediaItem, pagingList[0])
    }
  }

  @Test
  fun getAiringThisWeekTv_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockMovieRepository.getAiringThisWeekTv(region) },
      pagingData = fakeTvPagingData,
      interactorCall = { getListTvInteractor.getAiringThisWeekTv(region) },
    ) { pagingList ->
      assertEquals(tvMediaItem, pagingList[0])
    }
  }

  @Test
  fun getAiringTodayTv_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockMovieRepository.getAiringTodayTv(region) },
      pagingData = fakeTvPagingData,
      interactorCall = { getListTvInteractor.getAiringTodayTv(region) },
    ) { pagingList ->
      assertEquals(tvMediaItem, pagingList[0])
    }
  }

  @Test
  fun getTopRatedTv_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockMovieRepository.getTopRatedTv() },
      pagingData = fakeTvPagingData,
      interactorCall = { getListTvInteractor.getTopRatedTv() },
    ) { pagingList ->
      assertEquals(tvMediaItem, pagingList[0])
    }
  }
}
