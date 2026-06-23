package com.waffiq.bazz_movies.core.network.data.remote.datasource.account

import com.waffiq.bazz_movies.core.network.data.remote.constants.AccountMediaCategory
import com.waffiq.bazz_movies.core.network.data.remote.constants.MediaType
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump2
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMediaResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlow
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingSource
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AccountRemoteDataSourceTest : BaseMediaDataSourceTest() {

  private suspend fun stubGeneral(category: AccountMediaCategory, mediaType: MediaType) =
    mockAccountApiService.getMediaList(
      accountId = userId,
      sessionId = sessionId,
      category = category.asApiValue(),
      mediaType = mediaType.asApiValue(),
      page = 1,
    )

  private suspend fun stubFavoriteMovies() =
    stubGeneral(AccountMediaCategory.FAVORITE, MediaType.MOVIES)

  private suspend fun stubFavoriteTv() = stubGeneral(AccountMediaCategory.FAVORITE, MediaType.TV)

  private suspend fun stubWatchlistMovies() =
    stubGeneral(AccountMediaCategory.WATCHLIST, MediaType.MOVIES)

  private suspend fun stubWatchlistTv() = stubGeneral(AccountMediaCategory.WATCHLIST, MediaType.TV)

  @Test
  fun getFavoriteMovies_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(DataDumpManager.movieDump6)
      coEvery { stubFavoriteMovies() } returns defaultMediaResponse(expected)
      accountRemoteDataSource.getFavoriteMovies(userId, sessionId).testPagingFlow(this, expected)
      coVerify { stubFavoriteMovies() }
    }

  @Test
  fun getWatchlistMovies_pagingFlow_returnsExpectedData() =
    runTest {
      val expected = listOf(DataDumpManager.movieDump2)
      coEvery { stubWatchlistMovies() } returns defaultMediaResponse(expected)
      accountRemoteDataSource.getWatchlistMovies(userId, sessionId).testPagingFlow(this, expected)
      coVerify { stubWatchlistMovies() }
    }

  @Test
  fun getFavoriteTv_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource = GenericPagingSource { stubFavoriteTv().results }
      testPagingSource(
        mockResults = defaultMediaResponse(listOf(tvShowDump2)),
        mockApiCall = { stubFavoriteTv() },
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
      coEvery { stubFavoriteTv() } returns defaultMediaResponse(expected)
      accountRemoteDataSource.getFavoriteTv(userId, sessionId).testPagingFlow(this, expected)
      coVerify { stubFavoriteTv() }
    }

  @Test
  fun getWatchlistTv_pagingSource_returnsExpectedData() =
    runTest {
      val pagingSource = GenericPagingSource { stubWatchlistTv().results }
      testPagingSource(
        mockResults = defaultMediaResponse(listOf(tvShowDump2)),
        mockApiCall = { stubWatchlistTv() },
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
      coEvery { stubWatchlistTv() } returns defaultMediaResponse(expected)
      accountRemoteDataSource.getWatchlistTv(userId, sessionId).testPagingFlow(this, expected)
      coVerify { stubWatchlistTv() }
    }
}
