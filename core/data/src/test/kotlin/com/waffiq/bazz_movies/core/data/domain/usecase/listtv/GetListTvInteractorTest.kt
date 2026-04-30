package com.waffiq.bazz_movies.core.data.domain.usecase.listtv

import com.waffiq.bazz_movies.core.data.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.TV_ID
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.USER_REGION
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.tvMediaItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetListTvInteractorTest : BaseInteractorTest() {

  private lateinit var getListTvInteractor: GetListTvInteractor

  @Before
  override fun setup() {
    super.setup()
    getListTvInteractor = GetListTvInteractor(mockTvRepository, mockUserRepository)
  }

  @Test
  fun getPopularTv_whenValueIsValid_returnsDataCorrectly() =
    runTest {
      testPagingData(
        mockCall = { mockTvRepository.getPopularTv(USER_REGION) },
        pagingData = fakeTvPagingData,
        interactorCall = { getListTvInteractor.getPopularTv() },
      ) { pagingList ->
        assertEquals(tvMediaItem, pagingList[0])
      }
    }

  @Test
  fun getAiringThisWeekTv_whenValueIsValid_returnsDataCorrectly() =
    runTest {
      testPagingData(
        mockCall = { mockTvRepository.getAiringThisWeekTv(USER_REGION) },
        pagingData = fakeTvPagingData,
        interactorCall = { getListTvInteractor.getAiringThisWeekTv() },
      ) { pagingList ->
        assertEquals(tvMediaItem, pagingList[0])
      }
    }

  @Test
  fun getAiringTodayTv_whenValueIsValid_returnsDataCorrectly() =
    runTest {
      testPagingData(
        mockCall = { mockTvRepository.getAiringTodayTv(USER_REGION) },
        pagingData = fakeTvPagingData,
        interactorCall = { getListTvInteractor.getAiringTodayTv() },
      ) { pagingList ->
        assertEquals(tvMediaItem, pagingList[0])
      }
    }

  @Test
  fun getTopRatedTv_whenValueIsValid_returnsDataCorrectly() =
    runTest {
      testPagingData(
        mockCall = { mockTvRepository.getTopRatedTv() },
        pagingData = fakeTvPagingData,
        interactorCall = { getListTvInteractor.getTopRatedTv() },
      ) { pagingList ->
        assertEquals(tvMediaItem, pagingList[0])
      }
    }

  @Test
  fun getTvRecommendation_whenValueIsValid_returnsDataCorrectly() =
    runTest {
      testPagingData(
        mockCall = { mockTvRepository.getTvRecommendation(TV_ID) },
        pagingData = fakeTvPagingData,
        interactorCall = { getListTvInteractor.getTvRecommendation(TV_ID) },
        assertions = { pagingList ->
          assertEquals(tvMediaItem, pagingList[0])
        },
      )
    }
}
