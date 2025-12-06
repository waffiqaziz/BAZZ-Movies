package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.SearchPagingSource
import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump2
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump3
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump4
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump7
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.personDump1
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump1
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMediaResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMultiSearchResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlow
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlowSearch
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingSearchSource
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingSource
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TrendingDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getTrendingThisWeek_pagingSource_returnsExpectedData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getTrendingThisWeek("id", 1).results }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(movieDump4, movieDump2, movieDump3)),
      mockApiCall = { tmdbApiService.getTrendingThisWeek("id", 1) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(3, page.data.size)
    }
  }

  @Test
  fun getTrendingThisWeek_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(movieDump4, movieDump2, movieDump3)
    coEvery { tmdbApiService.getTrendingThisWeek("id", 1) } returns defaultMediaResponse(expected)
    movieDataSource.getTrendingThisWeek("id").testPagingFlow(this, expected)
    coVerify { tmdbApiService.getTrendingThisWeek("id", 1) }
  }


  @Test
  fun getTrendingToday_pagingSource_returnsExpectedData() = runTest {
    val pagingSource = GenericPagingSource { tmdbApiService.getTrendingToday("ca", 1).results }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(tvShowDump1, movieDump7)),
      mockApiCall = { tmdbApiService.getTrendingToday("ca", 1) },
      loader = { pagingSource.toLoadResult() }
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
  fun getTrendingToday_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(tvShowDump1, movieDump7)
    coEvery { tmdbApiService.getTrendingToday("ca", 1) } returns defaultMediaResponse(expected)
    movieDataSource.getTrendingToday("ca").testPagingFlow(this, expected)
    coVerify { tmdbApiService.getTrendingToday("ca", 1) }
  }

  @Test
  fun search_pagingSource_returnsExpectedData() = runTest {
    val pagingSource = SearchPagingSource(tmdbApiService, "john")
    testPagingSearchSource(
      mockResults = defaultMultiSearchResponse(listOf(personDump1)),
      mockApiCall = { tmdbApiService.search("john", 1) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(1, page.data.size)
    }
  }

  @Test
  fun search_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(personDump1)
    coEvery { tmdbApiService.search("john", 1) } returns defaultMultiSearchResponse(expected)
    movieDataSource.search("john").testPagingFlowSearch(this, expected)
    coVerify { tmdbApiService.search("john", 1) }
  }
}
