package com.waffiq.bazz_movies.core.network.data.remote.datasource.tv

import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump1
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump2
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump3
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMediaResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlow
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingSource
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TvPagingRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getPopularTv_pagingSource_returnsExpectedData() =
    runTest {
      val airDate = "2012-06-04"
      val pagingSource = GenericPagingSource {
        mockTvApiService.getPopularTv("id", airDate, 1).results
      }

      testPagingSource(
        mockResults = defaultMediaResponse(listOf(tvShowDump3)),
        mockApiCall = { mockTvApiService.getPopularTv("id", airDate, 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        assertEquals(null, page.prevKey)
        assertEquals(2, page.nextKey)
      }
    }

  @Test
  fun getPopularTv_pagingFlow_returnsExpectedData() =
    runTest {
      val airDate = "2012-06-04"
      val expected = listOf(tvShowDump3)
      coEvery { mockTvApiService.getPopularTv("id", airDate, 1) } returns
        defaultMediaResponse(expected)

      tvRemoteDataSource.getPopularTv("id", airDate).testPagingFlow(this, expected)

      coVerify { mockTvApiService.getPopularTv("id", airDate, 1) }
    }

  @Test
  fun getAiringTv_pagingSource_returnsExpectedData() =
    runTest {
      val airDate = "2023-11-14"
      val airDateEnd = "2023-11-06"
      val pagingSource = GenericPagingSource {
        mockTvApiService.getAiringTv("id", airDate, airDateEnd, 1).results
      }

      testPagingSource(
        mockResults = defaultMediaResponse(listOf(tvShowDump1, tvShowDump2, tvShowDump3)),
        mockApiCall = { mockTvApiService.getAiringTv("id", airDate, airDateEnd, 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        assertEquals(null, page.prevKey)
        assertEquals(2, page.nextKey)
      }
    }

  @Test
  fun getAiringTv_pagingFlow_returnsExpectedData() =
    runTest {
      val airDate = "2023-11-14"
      val expected = listOf(tvShowDump1, tvShowDump3)
      coEvery { mockTvApiService.getAiringTv("id", airDate, airDate, 1) } returns
        defaultMediaResponse(expected)

      tvRemoteDataSource.getAiringTv("id", airDate, airDate).testPagingFlow(this, expected)

      coVerify { mockTvApiService.getAiringTv("id", airDate, airDate, 1) }
    }

  @Test
  fun getTvRecommendations_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource = GenericPagingSource {
        mockTvApiService.getTvRecommendations(98765, 1).results
      }

      testPagingSource(
        mockResults = defaultMediaResponse(listOf(tvShowDump1)),
        mockApiCall = { mockTvApiService.getTvRecommendations(98765, 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        assertEquals(null, page.prevKey)
        assertEquals(2, page.nextKey)
      }
    }

  @Test
  fun getTvRecommendations_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(tvShowDump1)

      coEvery { mockTvApiService.getTvRecommendations(98765, 1) } returns
        defaultMediaResponse(expected)

      tvRemoteDataSource.getTvRecommendation(98765).testPagingFlow(this, expected)
      coVerify { mockTvApiService.getTvRecommendations(98765, 1) }
    }

  @Test
  fun getTopRatedTv_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource = GenericPagingSource {
        mockTvApiService.getTopRatedTv(1).results
      }

      testPagingSource(
        mockResults = defaultMediaResponse(listOf(tvShowDump3, tvShowDump2, tvShowDump1)),
        mockApiCall = { mockTvApiService.getTopRatedTv(1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        assertEquals(null, page.prevKey)
        assertEquals(2, page.nextKey)
      }
    }

  @Test
  fun getTopRatedTv_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(tvShowDump3, tvShowDump2, tvShowDump1)
      coEvery { mockTvApiService.getTopRatedTv(1) } returns
        defaultMediaResponse(expected)

      tvRemoteDataSource.getTopRatedTv().testPagingFlow(this, expected)

      coVerify { mockTvApiService.getTopRatedTv(1) }
    }
}
