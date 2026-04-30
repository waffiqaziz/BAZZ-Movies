package com.waffiq.bazz_movies.core.network.data.remote.datasource.account

import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump2
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
  fun getFavoriteMovies_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(DataDumpManager.movieDump6)
      coEvery { mockAccountApiService.getFavoriteMovies(userId, sessionId, 1) } returns
        defaultMediaResponse(expected)
      accountRemoteDataSource.getFavoriteMovies(userId, sessionId).testPagingFlow(this, expected)
      coVerify { mockAccountApiService.getFavoriteMovies(userId, sessionId, 1) }
    }

  @Test
  fun getWatchlistMovies_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(DataDumpManager.movieDump2)
      coEvery { mockAccountApiService.getWatchlistMovies(userId, sessionId, 1) } returns
        defaultMediaResponse(expected)
      accountRemoteDataSource.getWatchlistMovies(userId, sessionId).testPagingFlow(this, expected)
      coVerify { mockAccountApiService.getWatchlistMovies(userId, sessionId, 1) }
    }

  @Test
  fun getFavoriteTv_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource = GenericPagingSource {
        mockAccountApiService.getFavoriteTv(userId, sessionId, 1).results
      }
      testPagingSource(
        mockResults = defaultMediaResponse(listOf(tvShowDump2)),
        mockApiCall = { mockAccountApiService.getFavoriteTv(userId, sessionId, 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        assertEquals(null, page.prevKey)
        assertEquals(2, page.nextKey)
      }
    }

  @Test
  fun getFavoriteTv_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(tvShowDump2)
      coEvery { mockAccountApiService.getFavoriteTv(userId, sessionId, 1) } returns
        defaultMediaResponse(expected)
      accountRemoteDataSource.getFavoriteTv(userId, sessionId).testPagingFlow(this, expected)
      coVerify { mockAccountApiService.getFavoriteTv(userId, sessionId, 1) }
    }

  @Test
  fun getWatchlistTv_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource = GenericPagingSource {
        mockAccountApiService.getWatchlistTv(userId, sessionId, 1).results
      }
      testPagingSource(
        mockResults = defaultMediaResponse(listOf(tvShowDump2)),
        mockApiCall = { mockAccountApiService.getWatchlistTv(userId, sessionId, 1) },
        loader = { pagingSource.toLoadResult() },
      ) { page ->
        assertEquals(null, page.prevKey)
        assertEquals(2, page.nextKey)
      }
    }

  @Test
  fun getWatchlistTv_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(tvShowDump2)
      coEvery { mockAccountApiService.getWatchlistTv(userId, sessionId, 1) } returns
        defaultMediaResponse(expected)
      accountRemoteDataSource.getWatchlistTv(userId, sessionId).testPagingFlow(this, expected)
      coVerify { mockAccountApiService.getWatchlistTv(userId, sessionId, 1) }
    }
}
