package com.waffiq.bazz_movies.core.network.data.remote.datasource.discover

import com.waffiq.bazz_movies.core.network.data.remote.query.DiscoverMovieParams
import com.waffiq.bazz_movies.core.network.data.remote.query.DiscoverTvParams
import com.waffiq.bazz_movies.core.network.data.remote.query.toQueryMap
import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DiscoverRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getMovieByGenres_pagingFlow_returnsExpectedData() =
    runTest {
      val query = DiscoverMovieParams(genre = "1", watchRegion = "id", page = 1).toQueryMap()

      verifyMovieDiscovery(discoverRemoteDataSource.getMovieByGenres("1", "id"), query)
    }

  @Test
  fun getMovieByKeywords_pagingFlow_returnsExpectedData() =
    runTest {
      val query = DiscoverMovieParams(keyword = "1", page = 1).toQueryMap()

      verifyMovieDiscovery(discoverRemoteDataSource.getMovieByKeywords("1"), query)
    }

  @Test
  fun getTvByGenre_pagingFlow_returnsExpectedData() =
    runTest {
      val query = DiscoverTvParams(genre = "1", watchRegion = "id", page = 1).toQueryMap()

      verifyTvDiscovery(discoverRemoteDataSource.getTvByGenres("1", "id"), query)
    }

  @Test
  fun getTvByKeywords_pagingFlow_returnsExpectedData() =
    runTest {
      val query = DiscoverTvParams(keyword = "1", page = 1).toQueryMap()

      verifyTvDiscovery(discoverRemoteDataSource.getTvByKeywords("1"), query)
    }
}
