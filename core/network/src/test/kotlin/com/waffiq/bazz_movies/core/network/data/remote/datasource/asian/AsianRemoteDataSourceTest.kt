package com.waffiq.bazz_movies.core.network.data.remote.datasource.asian

import com.waffiq.bazz_movies.core.network.data.remote.constants.Genre.ANIMATION
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.COSTUME_DRAMA
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.DONGHUA
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.ROMANCE
import com.waffiq.bazz_movies.core.network.data.remote.constants.Region.JAPAN
import com.waffiq.bazz_movies.core.network.data.remote.datasource.asian.AsianRemoteDataSource.Companion.ANIME_WITHOUT_KEYWORDS
import com.waffiq.bazz_movies.core.network.data.remote.datasource.asian.AsianRemoteDataSource.Companion.ASIAN_REGION
import com.waffiq.bazz_movies.core.network.data.remote.datasource.asian.AsianRemoteDataSource.Companion.ONE_MONTH
import com.waffiq.bazz_movies.core.network.data.remote.datasource.asian.AsianRemoteDataSource.Companion.ROMANCE_DRAMA_WITHOUT_GENRES
import com.waffiq.bazz_movies.core.network.data.remote.datasource.asian.AsianRemoteDataSource.Companion.ROMANCE_DRAMA_WITHOUT_KEYWORDS
import com.waffiq.bazz_movies.core.network.data.remote.datasource.asian.AsianRemoteDataSource.Companion.THREE_MONTHS
import com.waffiq.bazz_movies.core.network.data.remote.query.DiscoverTvParams
import com.waffiq.bazz_movies.core.network.data.remote.query.toQueryMap
import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.defaultMediaResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testPagingFlow
import com.waffiq.bazz_movies.core.network.utils.helpers.DateHelper.monthsAgo
import com.waffiq.bazz_movies.core.network.utils.helpers.DateHelper.monthsLater
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AsianRemoteDataSourceTest : BaseMediaDataSourceTest() {

  private val expectedTv = listOf(DataDumpManager.tvShowDump1)

  @Test
  fun getAnimeThisSeason_pagingFlow_returnsExpectedData() =
    runTest {
      val query = DiscoverTvParams(
        genres = listOf(ANIMATION),
        originCountry = listOf(JAPAN),
        page = 1,
        firstAirDateGte = THREE_MONTHS.monthsAgo,
        firstAirDateLte = ONE_MONTH.monthsLater,
        withoutKeywords = ANIME_WITHOUT_KEYWORDS,
      ).toQueryMap()

      coEvery { mockDiscoverApiService.discoverTv(query) } returns
        defaultMediaResponse(expectedTv)

      asianRemoteDataSource.getAnimeThisSeason().testPagingFlow(this, expectedTv)
      coVerify { mockDiscoverApiService.discoverTv(query) }
    }

  @Test
  fun getAnimeAllTime_pagingFlow_returnsExpectedData() =
    runTest {
      val query = DiscoverTvParams(
        genres = listOf(ANIMATION),
        originCountry = listOf(JAPAN),
        page = 1,
        withoutKeywords = ANIME_WITHOUT_KEYWORDS,
      ).toQueryMap()

      coEvery { mockDiscoverApiService.discoverTv(query) } returns
        defaultMediaResponse(expectedTv)

      asianRemoteDataSource.getAnimeAllTime().testPagingFlow(this, expectedTv)
      coVerify { mockDiscoverApiService.discoverTv(query) }
    }

  @Test
  fun getDonghua_pagingFlow_returnsExpectedData() =
    runTest {
      val query = DiscoverTvParams(keywords = listOf(DONGHUA), page = 1).toQueryMap()

      coEvery { mockDiscoverApiService.discoverTv(query) } returns
        defaultMediaResponse(expectedTv)

      asianRemoteDataSource.getDonghua().testPagingFlow(this, expectedTv)
      coVerify { mockDiscoverApiService.discoverTv(query) }
    }

  @Test
  fun getAsianRomance_pagingFlow_returnsExpectedData() =
    runTest {
      val query = DiscoverTvParams(
        keywords = listOf(ROMANCE),
        originCountry = ASIAN_REGION,
        page = 1,
        withoutGenres = ROMANCE_DRAMA_WITHOUT_GENRES,
        withoutKeywords = ROMANCE_DRAMA_WITHOUT_KEYWORDS,
      ).toQueryMap()

      coEvery { mockDiscoverApiService.discoverTv(query) } returns defaultMediaResponse(expectedTv)

      asianRemoteDataSource.getAsianRomance().testPagingFlow(this, expectedTv)
      coVerify { mockDiscoverApiService.discoverTv(query) }
    }

  @Test
  fun getCostumeDrama_pagingFlow_returnsExpectedData() =
    runTest {
      val query = DiscoverTvParams(
        keywords = listOf(COSTUME_DRAMA),
        originCountry = ASIAN_REGION,
        page = 1,
      ).toQueryMap()

      coEvery { mockDiscoverApiService.discoverTv(query) } returns
        defaultMediaResponse(expectedTv)

      asianRemoteDataSource.getCostumeDrama().testPagingFlow(this, expectedTv)
      coVerify { mockDiscoverApiService.discoverTv(query) }
    }
}
