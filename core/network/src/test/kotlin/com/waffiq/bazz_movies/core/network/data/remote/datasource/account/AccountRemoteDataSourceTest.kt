package com.waffiq.bazz_movies.core.network.data.remote.datasource.account

import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump2
import com.waffiq.bazz_movies.core.network.testutils.TestHelper
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMediaResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlow
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingSource
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AccountRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getFavoriteMovies_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(DataDumpManager.movieDump6)
    coEvery { mockAccountApiService.getFavoriteMovies("session_id", 1) } returns
      TestHelper.defaultMediaResponse(expected)
    accountRemoteDataSource.getFavoriteMovies("session_id").testPagingFlow(this, expected)
    coVerify { mockAccountApiService.getFavoriteMovies("session_id", 1) }
  }

  @Test
  fun getWatchlistMovies_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(DataDumpManager.movieDump2)
    coEvery { mockAccountApiService.getWatchlistMovies("session_id", 1) } returns
      TestHelper.defaultMediaResponse(expected)
    accountRemoteDataSource.getWatchlistMovies("session_id").testPagingFlow(this, expected)
    coVerify { mockAccountApiService.getWatchlistMovies("session_id", 1) }
  }


  @Test
  fun getFavoriteTv_pagingSource_returnsExpectedData() = runTest {
    val pagingSource = GenericPagingSource {
      mockAccountApiService.getFavoriteTv("session_id", 1).results
    }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(tvShowDump2)),
      mockApiCall = { mockAccountApiService.getFavoriteTv("session_id", 1) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }
  }

  @Test
  fun getFavoriteTv_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(tvShowDump2)
    coEvery { mockAccountApiService.getFavoriteTv("session_id", 1) } returns
      defaultMediaResponse(expected)
    accountRemoteDataSource.getFavoriteTv("session_id").testPagingFlow(this, expected)
    coVerify { mockAccountApiService.getFavoriteTv("session_id", 1) }
  }

  @Test
  fun getWatchlistTv_pagingSource_returnsExpectedData() = runTest {
    val pagingSource = GenericPagingSource {
      mockAccountApiService.getWatchlistTv("session_id", 1).results
    }
    testPagingSource(
      mockResults = defaultMediaResponse(listOf(tvShowDump2)),
      mockApiCall = { mockAccountApiService.getWatchlistTv("session_id", 1) },
      loader = { pagingSource.toLoadResult() }
    ) { page ->
      assertEquals(null, page.prevKey)
      assertEquals(2, page.nextKey)
    }
  }

  @Test
  fun getWatchlistTv_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(tvShowDump2)
    coEvery { mockAccountApiService.getWatchlistTv("session_id", 1) } returns
      defaultMediaResponse(expected)
    accountRemoteDataSource.getWatchlistTv("session_id").testPagingFlow(this, expected)
    coVerify { mockAccountApiService.getWatchlistTv("session_id", 1) }
  }
}
