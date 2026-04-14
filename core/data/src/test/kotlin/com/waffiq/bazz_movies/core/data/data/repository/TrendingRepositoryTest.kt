package com.waffiq.bazz_movies.core.data.data.repository

import com.waffiq.bazz_movies.core.data.testutils.BaseRepositoryTest
import com.waffiq.bazz_movies.core.network.data.remote.datasource.trending.TrendingRemoteDataSource
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.testSuccessfulPagingData
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class TrendingRepositoryTest : BaseRepositoryTest() {

  private lateinit var trendingRepository: TrendingRepositoryImpl
  private val mockTrendingRemoteDataSource: TrendingRemoteDataSource = mockk()

  @Before
  fun setup() {
    trendingRepository = TrendingRepositoryImpl(mockTrendingRemoteDataSource)
  }

  @Test
  fun getTrendingToday_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockTrendingRemoteDataSource.getTrendingToday(region) },
      repositoryCall = { trendingRepository.getTrendingToday(region) },
      verifyDataSourceCall = { verify { mockTrendingRemoteDataSource.getTrendingToday(region) } }
    )
  }

  @Test
  fun getTrendingThisWeek_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockTrendingRemoteDataSource.getTrendingThisWeek(region) },
      repositoryCall = { trendingRepository.getTrendingThisWeek(region) },
      verifyDataSourceCall = { verify { mockTrendingRemoteDataSource.getTrendingThisWeek(region) } }
    )
  }
}
