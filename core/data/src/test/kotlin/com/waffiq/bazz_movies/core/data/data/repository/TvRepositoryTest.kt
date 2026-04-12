package com.waffiq.bazz_movies.core.data.data.repository

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.data.testutils.BaseRepositoryTest
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.mediaStateResponse
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.postTvRateResponseSuccess
import com.waffiq.bazz_movies.core.data.utils.Helper.getDateToday
import com.waffiq.bazz_movies.core.data.utils.Helper.getDateTwoWeeksFromToday
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.mappers.MediaStateMapper.toMediaState
import com.waffiq.bazz_movies.core.network.data.remote.datasource.tv.TvRemoteDataSource
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

class TvRepositoryTest : BaseRepositoryTest() {

  private lateinit var tvRepositoryImpl: TvRepositoryImpl
  private val mockTvRemoteDataSource: TvRemoteDataSource = mockk()

  @Before
  fun setup() {
    tvRepositoryImpl = TvRepositoryImpl(mockTvRemoteDataSource)
  }

  @Test
  fun getPopularTv_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockTvRemoteDataSource.getPopularTv(region, getDateTwoWeeksFromToday()) },
      repositoryCall = { tvRepositoryImpl.getPopularTv(region) },
      verifyDataSourceCall = {
        verify { mockTvRemoteDataSource.getPopularTv(region, getDateTwoWeeksFromToday()) }
      }
    )
  }

  @Test
  fun getAiringThisWeekTv_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = {
        mockTvRemoteDataSource.getAiringTv(region, getDateTwoWeeksFromToday(), getDateToday())
      },
      repositoryCall = { tvRepositoryImpl.getAiringThisWeekTv(region) },
      verifyDataSourceCall = {
        verify {
          mockTvRemoteDataSource.getAiringTv(region, getDateTwoWeeksFromToday(), getDateToday())
        }
      }
    )
  }

  @Test
  fun getAiringTodayTv_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = {
        mockTvRemoteDataSource.getAiringTv(
          region,
          getDateToday(),
          getDateToday()
        )
      },
      repositoryCall = { tvRepositoryImpl.getAiringTodayTv(region) },
      verifyDataSourceCall = {
        verify { mockTvRemoteDataSource.getAiringTv(region, getDateToday(), getDateToday()) }
      }
    )
  }

  @Test
  fun getTopRatedTv_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockTvRemoteDataSource.getTopRatedTv() },
      repositoryCall = { tvRepositoryImpl.getTopRatedTv() },
      verifyDataSourceCall = { verify { mockTvRemoteDataSource.getTopRatedTv() } }
    )
  }

  @Test
  fun getTvRecommendation_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockTvRemoteDataSource.getTvRecommendation(id) },
      repositoryCall = { tvRepositoryImpl.getTvRecommendation(id) },
      verifyDataSourceCall = { verify { mockTvRemoteDataSource.getTvRecommendation(any()) } }
    )
  }

  @Test
  fun getStatedTv_whenSuccessful_returnsMappedStatedTv() = runTest {
    coEvery { mockTvRemoteDataSource.getTvState("sessionId", 8888) } returns
      flowOf(NetworkResult.Success(mediaStateResponse))

    tvRepositoryImpl.getTvState("sessionId", 8888).test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      assertEquals(mediaStateResponse.toMediaState(), result.data)
      awaitComplete()
    }
  }

  @Test
  fun postTvRate_whenSuccessful_returnsCorrectResponse() = runTest {
    coEvery {
      mockTvRemoteDataSource.postTvRate("sessionId", 9.0f, 7777)
    } returns flowOf(NetworkResult.Success(postTvRateResponseSuccess))

    tvRepositoryImpl.postTvRate("sessionId", 9.0f, 7777).test {
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