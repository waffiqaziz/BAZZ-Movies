package com.waffiq.bazz_movies.core.network.data.remote.datasource.discover

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump1
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMediaResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlow
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DiscoverRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getMovieByGenres_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(DataDumpManager.movieDump5)

    coEvery { mockDiscoverApiService.getMovieByGenres("1", "id", 1) } returns
      defaultMediaResponse(expected)

    discoverRemoteDataSource.getMovieByGenres("1", "id").testPagingFlow(this, expected)
    coVerify { mockDiscoverApiService.getMovieByGenres("1", "id", 1) }
  }

  @Test
  fun getMovieByKeywords_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(DataDumpManager.movieDump2)

    coEvery { mockDiscoverApiService.getMovieByKeywords("1", 1) } returns
      defaultMediaResponse(expected)

    discoverRemoteDataSource.getMovieByKeywords("1").testPagingFlow(this, expected)
    coVerify { mockDiscoverApiService.getMovieByKeywords("1", 1) }
  }

  @Test
  fun getTvByGenre_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(tvShowDump1)

    coEvery { mockDiscoverApiService.getTvByGenres("1", "id", 1) } returns
      defaultMediaResponse(expected)

    discoverRemoteDataSource.getTvByGenres("1", "id").testPagingFlow(this, expected)
    coVerify { mockDiscoverApiService.getTvByGenres("1", "id", 1) }
  }

  @Test
  fun getTvByKeywords_pagingFlow_returnsExpectedData() = runTest {
    val expected = listOf(tvShowDump1)

    coEvery { mockDiscoverApiService.getTvByKeywords("1", 1) } returns
      defaultMediaResponse(expected)

    discoverRemoteDataSource.getTvByKeywords("1").testPagingFlow(this, expected)
    coVerify { mockDiscoverApiService.getTvByKeywords("1", 1) }
  }
}
