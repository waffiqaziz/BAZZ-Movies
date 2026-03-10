package com.waffiq.bazz_movies.core.movie.domain.usecase.listtv

import com.waffiq.bazz_movies.core.movie.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.movieMediaItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetListTvInteractorTest : BaseInteractorTest() {

  private lateinit var getListTvInteractor: GetListTvInteractor

  @Before
  fun setup() {
    getListTvInteractor = GetListTvInteractor(mockRepository)
  }

  @Test
  fun getPopularTv_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockRepository.getPopularTv(region) },
      pagingData = fakePagingData,
      interactorCall = { getListTvInteractor.getPopularTv(region) },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getAiringThisWeekTv_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockRepository.getAiringThisWeekTv(region) },
      pagingData = fakePagingData,
      interactorCall = { getListTvInteractor.getAiringThisWeekTv(region) },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getAiringTodayTv_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockRepository.getAiringTodayTv(region) },
      pagingData = fakePagingData,
      interactorCall = { getListTvInteractor.getAiringTodayTv(region) },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }

  @Test
  fun getTopRatedTv_whenValueIsValid_returnsDataCorrectly() = runTest {
    testPagingData(
      mockCall = { mockRepository.getTopRatedTv() },
      pagingData = fakePagingData,
      interactorCall = { getListTvInteractor.getTopRatedTv() },
    ) { pagingList ->
      assertEquals(movieMediaItem, pagingList[0])
    }
  }
}
