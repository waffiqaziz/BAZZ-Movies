package com.waffiq.bazz_movies.core.network.data.remote.datasource.discover

import com.waffiq.bazz_movies.core.network.data.remote.query.DiscoverMovieParams
import com.waffiq.bazz_movies.core.network.data.remote.query.DiscoverTvParams
import com.waffiq.bazz_movies.core.network.data.remote.query.toQueryMap
import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMediaResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlow
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DiscoverRemoteDataSourceTest : BaseMediaDataSourceTest() {

  private val expectedMovie = listOf(DataDumpManager.movieDump1)
  private val expectedTv = listOf(DataDumpManager.tvShowDump1)

  @Test
  fun getMovieByGenres_pagingFlow_returnsExpectedData() =
    runTest {
      coEvery {
        mockDiscoverApiService.discoverMovie(
          DiscoverMovieParams(genre = "1", watchRegion = "id", page = 1).toQueryMap(),
        )
      } returns defaultMediaResponse(expectedMovie)

      discoverRemoteDataSource.getMovieByGenres("1", "id").testPagingFlow(this, expectedMovie)
      coVerify {
        mockDiscoverApiService.discoverMovie(
          DiscoverMovieParams(genre = "1", watchRegion = "id", page = 1).toQueryMap(),
        )
      }
    }

  @Test
  fun getMovieByKeywords_pagingFlow_returnsExpectedData() =
    runTest {
      val query = DiscoverMovieParams(keyword = "1", page = 1).toQueryMap()

      coEvery { mockDiscoverApiService.discoverMovie(query) } returns
        defaultMediaResponse(expectedMovie)

      discoverRemoteDataSource.getMovieByKeywords("1").testPagingFlow(this, expectedMovie)
      coVerify { mockDiscoverApiService.discoverMovie(query) }
    }

  @Test
  fun getTvByGenre_pagingFlow_returnsExpectedData() =
    runTest {
      val query = DiscoverTvParams(genre = "1", watchRegion = "id", page = 1).toQueryMap()

      coEvery { mockDiscoverApiService.discoverTv(query) } returns defaultMediaResponse(expectedTv)

      discoverRemoteDataSource.getTvByGenres("1", "id").testPagingFlow(this, expectedTv)
      coVerify { mockDiscoverApiService.discoverTv(query) }
    }

  @Test
  fun getTvByKeywords_pagingFlow_returnsExpectedData() =
    runTest {
      val query = DiscoverTvParams(keyword = "1", page = 1).toQueryMap()

      coEvery { mockDiscoverApiService.discoverTv(query) } returns
        defaultMediaResponse(expectedTv)

      discoverRemoteDataSource.getTvByKeywords("1").testPagingFlow(this, expectedTv)
      coVerify { mockDiscoverApiService.discoverTv(query) }
    }
}
