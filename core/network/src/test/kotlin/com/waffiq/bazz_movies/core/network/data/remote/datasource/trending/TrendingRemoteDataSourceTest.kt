package com.waffiq.bazz_movies.core.network.data.remote.datasource.trending

import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump2
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump3
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump4
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump7
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump1
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMediaResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlow
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingSource
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TrendingRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getTrendingThisWeek_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource =
        GenericPagingSource { mockTrendingApiService.getTrendingThisWeek("id", 1).results }

      testPagingSource(
        mockResults = defaultMediaResponse(listOf(movieDump4, movieDump2, movieDump3)),
        mockApiCall = { mockTrendingApiService.getTrendingThisWeek("id", 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        assertEquals(3, page.data.size)
      }
    }

  @Test
  fun getTrendingThisWeek_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(movieDump4, movieDump2, movieDump3)
      coEvery { mockTrendingApiService.getTrendingThisWeek("id", 1) } returns
        defaultMediaResponse(expected)

      trendingRemoteDataSource.getTrendingThisWeek("id").testPagingFlow(this, expected)

      coVerify { mockTrendingApiService.getTrendingThisWeek("id", 1) }
    }

  @Test
  fun getTrendingToday_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource =
        GenericPagingSource { mockTrendingApiService.getTrendingToday("ca", 1).results }
      testPagingSource(
        mockResults = defaultMediaResponse(listOf(tvShowDump1, movieDump7)),
        mockApiCall = { mockTrendingApiService.getTrendingToday("ca", 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        assertEquals(2, page.data.size)
        assertEquals("Squid Game", page.data[0].name)
        assertEquals("Wicked", page.data[1].title)
        assertEquals(tvShowDump1, page.data[0])
        assertEquals(movieDump7, page.data[1])
        assertEquals(null, page.prevKey)
        assertEquals(2, page.nextKey)
      }
    }

  @Test
  fun getTrendingToday_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(tvShowDump1, movieDump7)
      coEvery { mockTrendingApiService.getTrendingToday("ca", 1) } returns
        defaultMediaResponse(expected)

      trendingRemoteDataSource.getTrendingToday("ca").testPagingFlow(this, expected)

      coVerify { mockTrendingApiService.getTrendingToday("ca", 1) }
    }
}
