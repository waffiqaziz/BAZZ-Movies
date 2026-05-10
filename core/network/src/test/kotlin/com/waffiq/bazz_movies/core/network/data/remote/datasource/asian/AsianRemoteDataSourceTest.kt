package com.waffiq.bazz_movies.core.network.data.remote.datasource.asian

import com.waffiq.bazz_movies.core.network.data.remote.datasource.asian.AsianRemoteDataSource.Companion.ANIME_WITHOUT_KEYWORDS
import com.waffiq.bazz_movies.core.network.data.remote.datasource.asian.AsianRemoteDataSource.Companion.ASIAN_REGION
import com.waffiq.bazz_movies.core.network.data.remote.datasource.asian.AsianRemoteDataSource.Companion.ONE_MONTH
import com.waffiq.bazz_movies.core.network.data.remote.datasource.asian.AsianRemoteDataSource.Companion.THREE_MONTHS
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
      coEvery {
        mockDiscoverApiService.discoverTv(
          genres = "16",
          country = "JP",
          page = 1,
          firstAirDateGte = THREE_MONTHS.monthsAgo,
          firstAirDateLte = ONE_MONTH.monthsLater,
          withoutKeywords = ANIME_WITHOUT_KEYWORDS,
        )
      } returns defaultMediaResponse(expectedTv)

      asianRemoteDataSource.getAnimeThisSeason().testPagingFlow(this, expectedTv)
      coVerify {
        mockDiscoverApiService.discoverTv(
          genres = "16",
          country = "JP",
          page = 1,
          firstAirDateGte = THREE_MONTHS.monthsAgo,
          firstAirDateLte = ONE_MONTH.monthsLater,
          withoutKeywords = ANIME_WITHOUT_KEYWORDS,
        )
      }
    }

  @Test
  fun getAnimeAllTime_pagingFlow_returnsExpectedData() =
    runTest {
      coEvery {
        mockDiscoverApiService.discoverTv(
          genres = "16",
          country = "JP",
          page = 1,
          withoutKeywords = ANIME_WITHOUT_KEYWORDS,
        )
      } returns defaultMediaResponse(expectedTv)

      asianRemoteDataSource.getAnimeAllTime().testPagingFlow(this, expectedTv)
      coVerify {
        mockDiscoverApiService.discoverTv(
          genres = "16",
          country = "JP",
          page = 1,
          withoutKeywords = ANIME_WITHOUT_KEYWORDS,
        )
      }
    }

  @Test
  fun getDonghua_pagingFlow_returnsExpectedData() =
    runTest {
      coEvery { mockDiscoverApiService.discoverTv(keywords = "315535", page = 1) } returns
        defaultMediaResponse(expectedTv)

      asianRemoteDataSource.getDonghua().testPagingFlow(this, expectedTv)
      coVerify { mockDiscoverApiService.discoverTv(keywords = "315535", page = 1) }
    }

  @Test
  fun getAsianRomance_pagingFlow_returnsExpectedData() =
    runTest {
      coEvery {
        mockDiscoverApiService.discoverTv(
          keywords = "9840",
          country = ASIAN_REGION,
          page = 1,
          withoutGenres = "16|10764",
          withoutKeywords = "168812|240305|265777|258533|289844",
        )
      } returns defaultMediaResponse(expectedTv)

      asianRemoteDataSource.getAsianRomance().testPagingFlow(this, expectedTv)
      coVerify {
        mockDiscoverApiService.discoverTv(
          keywords = "9840",
          country = ASIAN_REGION,
          page = 1,
          withoutGenres = "16|10764",
          withoutKeywords = "168812|240305|265777|258533|289844",
        )
      }
    }

  @Test
  fun getCostumeDrama_pagingFlow_returnsExpectedData() =
    runTest {
      coEvery {
        mockDiscoverApiService.discoverTv(
          keywords = "195013",
          page = 1,
          country = ASIAN_REGION,
        )
      } returns defaultMediaResponse(expectedTv)

      asianRemoteDataSource.getCostumeDrama().testPagingFlow(this, expectedTv)
      coVerify {
        mockDiscoverApiService.discoverTv(
          keywords = "195013",
          page = 1,
          country = ASIAN_REGION,
        )
      }
    }
}
