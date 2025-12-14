package com.waffiq.bazz_movies.core.network.data.remote.datasource

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
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TvDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getFavoriteTv_pagingSource_returnsExpectedData() = runTest {
    val pagingSource = GenericPagingSource {
      tmdbApiService.getFavoriteTv("session_id", 1).results
    }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(tvShowDump2)),
      mockApiCall = { tmdbApiService.getFavoriteTv("session_id", 1) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }
  }

  @Test
  fun getFavoriteTv_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(tvShowDump2)
    coEvery { tmdbApiService.getFavoriteTv("session_id", 1) } returns
      defaultMediaResponse(expected)
    movieDataSource.getFavoriteTv("session_id").testPagingFlow(this, expected)
    coVerify { tmdbApiService.getFavoriteTv("session_id", 1) }
  }

  @Test
  fun getWatchlistTv_pagingSource_returnsExpectedData() = runTest {
    val pagingSource = GenericPagingSource {
      tmdbApiService.getWatchlistTv("session_id", 1).results
    }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(tvShowDump2)),
      mockApiCall = { tmdbApiService.getWatchlistTv("session_id", 1) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }
  }

  @Test
  fun getWatchlistTv_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(tvShowDump2)
    coEvery { tmdbApiService.getWatchlistTv("session_id", 1) } returns
      defaultMediaResponse(expected)
    movieDataSource.getWatchlistTv("session_id").testPagingFlow(this, expected)
    coVerify { tmdbApiService.getWatchlistTv("session_id", 1) }
  }

  @Test
  fun getPopularTv_pagingSource_returnsExpectedData() = runTest {
    val airDate = "2012-06-04"
    val pagingSource = GenericPagingSource {
      tmdbApiService.getPopularTv(1, "id", airDate).results
    }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(tvShowDump3)),
      mockApiCall = { tmdbApiService.getPopularTv(1, "id", airDate) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }
  }

  @Test
  fun getPopularTv_pagingFlow_returnsExpectedData() = runTest {
    val airDate = "2012-06-04"
    val expected = listOf(tvShowDump3)
    coEvery { tmdbApiService.getPopularTv(1, "id", airDate) } returns
      defaultMediaResponse(expected)
    movieDataSource.getPopularTv("id", airDate).testPagingFlow(this, expected)
    coVerify { tmdbApiService.getPopularTv(1, "id", airDate) }
  }

  @Test
  fun getAiringTv_pagingSource_returnsExpectedData() = runTest {
    val airDate = "2023-11-14"
    val airDateEnd = "2023-11-06"
    val pagingSource = GenericPagingSource {
      tmdbApiService.getAiringTv("id", airDate, airDateEnd, 1).results
    }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(tvShowDump1, tvShowDump2, tvShowDump3)),
      mockApiCall = { tmdbApiService.getAiringTv("id", airDate, airDateEnd, 1) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }
  }

  @Test
  fun getAiringTv_pagingFlow_returnsExpectedData() = runTest {
    val airDate = "2023-11-14"
    val expected = listOf(tvShowDump1, tvShowDump3)
    coEvery { tmdbApiService.getAiringTv("id", airDate, airDate, 1) } returns
      defaultMediaResponse(expected)
    movieDataSource.getAiringTv("id", airDate, airDate).testPagingFlow(this, expected)
    coVerify { tmdbApiService.getAiringTv("id", airDate, airDate, 1) }
  }

  @Test
  fun getTvRecommendation_pagingSource_returnsExpectedData() = runTest {
    val pagingSource = GenericPagingSource {
      tmdbApiService.getTvRecommendations(98765, 1).results
    }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(tvShowDump1)),
      mockApiCall = { tmdbApiService.getTvRecommendations(98765, 1) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }
  }

  @Test
  fun getTvRecommendation_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(tvShowDump1)
    coEvery { tmdbApiService.getTvRecommendations(98765, 1) } returns
      defaultMediaResponse(expected)
    movieDataSource.getTvRecommendation(98765).testPagingFlow(this, expected)
    coVerify { tmdbApiService.getTvRecommendations(98765, 1) }
  }

  @Test
  fun getTopRatedTv_pagingSource_returnsExpectedData() = runTest {
    val pagingSource = GenericPagingSource {
      tmdbApiService.getTopRatedTv(1).results
    }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(tvShowDump3, tvShowDump2, tvShowDump1)),
      mockApiCall = { tmdbApiService.getTopRatedTv(1) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }
  }

  @Test
  fun getTopRatedTv_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(tvShowDump3, tvShowDump2, tvShowDump1)
    coEvery { tmdbApiService.getTopRatedTv(1) } returns defaultMediaResponse(expected)
    movieDataSource.getTopRatedTv().testPagingFlow(this, expected)
    coVerify { tmdbApiService.getTopRatedTv(1) }
  }
}
