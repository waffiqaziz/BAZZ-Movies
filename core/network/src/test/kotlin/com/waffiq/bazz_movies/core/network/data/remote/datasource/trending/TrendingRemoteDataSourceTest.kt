package com.waffiq.bazz_movies.core.network.data.remote.datasource.trending

import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager
import com.waffiq.bazz_movies.core.network.testutils.TestHelper
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlow
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TrendingRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getTrendingThisWeek_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource =
        GenericPagingSource { mockTrendingApiService.getTrendingThisWeek("id", 1).results }
      TestHelper.testPagingSource(
        mockResults = TestHelper.defaultMediaResponse(
          listOf(
            DataDumpManager.movieDump4,
            DataDumpManager.movieDump2,
            DataDumpManager.movieDump3,
          ),
        ),
        mockApiCall = { mockTrendingApiService.getTrendingThisWeek("id", 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        TestCase.assertEquals(3, page.data.size)
      }
    }

  @Test
  fun getTrendingThisWeek_pagingFlow_returnsExpectedData() =
    runTest {
      val expected =
        listOf(DataDumpManager.movieDump4, DataDumpManager.movieDump2, DataDumpManager.movieDump3)
      coEvery {
        mockTrendingApiService.getTrendingThisWeek(
          "id",
          1,
        )
      } returns TestHelper.defaultMediaResponse(expected)
      trendingRemoteDataSource.getTrendingThisWeek("id").testPagingFlow(this, expected)
      coVerify { mockTrendingApiService.getTrendingThisWeek("id", 1) }
    }

  @Test
  fun getTrendingToday_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource =
        GenericPagingSource { mockTrendingApiService.getTrendingToday("ca", 1).results }
      TestHelper.testPagingSource(
        mockResults = TestHelper.defaultMediaResponse(
          listOf(
            DataDumpManager.tvShowDump1,
            DataDumpManager.movieDump7,
          ),
        ),
        mockApiCall = { mockTrendingApiService.getTrendingToday("ca", 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        TestCase.assertEquals(2, page.data.size)
        TestCase.assertEquals("Squid Game", page.data[0].name)
        TestCase.assertEquals("Wicked", page.data[1].title)
        TestCase.assertEquals(DataDumpManager.tvShowDump1, page.data[0])
        TestCase.assertEquals(DataDumpManager.movieDump7, page.data[1])
        TestCase.assertEquals(null, page.prevKey)
        TestCase.assertEquals(2, page.nextKey)
      }
    }

  @Test
  fun getTrendingToday_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(DataDumpManager.tvShowDump1, DataDumpManager.movieDump7)
      coEvery {
        mockTrendingApiService.getTrendingToday(
          "ca",
          1,
        )
      } returns TestHelper.defaultMediaResponse(expected)
      trendingRemoteDataSource.getTrendingToday("ca").testPagingFlow(this, expected)
      coVerify { mockTrendingApiService.getTrendingToday("ca", 1) }
    }
}
