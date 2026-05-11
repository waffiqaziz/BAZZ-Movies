package com.waffiq.bazz_movies.core.data.data.repository

import com.waffiq.bazz_movies.core.data.testutils.BaseRepositoryTest
import com.waffiq.bazz_movies.core.network.data.remote.datasource.asian.AsianRemoteDataSource
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.testSuccessfulPagingData
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class AsianRepositoryTest : BaseRepositoryTest() {

  private lateinit var asianRepository: AsianRepositoryImpl
  private val mockAsianRemoteDataSource: AsianRemoteDataSource = mockk()

  @Before
  fun setup() {
    asianRepository = AsianRepositoryImpl(mockAsianRemoteDataSource)
  }

  @Test
  fun getAnimeAllTime_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockAsianRemoteDataSource.getAnimeAllTime() },
      repositoryCall = { asianRepository.getAnimeAllTime() },
      verifyDataSourceCall = { verify { mockAsianRemoteDataSource.getAnimeAllTime() } },
    )
  }

  @Test
  fun getAnimeThisSeason_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockAsianRemoteDataSource.getAnimeThisSeason() },
      repositoryCall = { asianRepository.getAnimeThisSeason() },
      verifyDataSourceCall = { verify { mockAsianRemoteDataSource.getAnimeThisSeason() } },
    )
  }

  @Test
  fun getDonghua_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockAsianRemoteDataSource.getDonghua() },
      repositoryCall = { asianRepository.getDonghua() },
      verifyDataSourceCall = { verify { mockAsianRemoteDataSource.getDonghua() } },
    )
  }

  @Test
  fun getAsianRomance_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockAsianRemoteDataSource.getAsianRomance() },
      repositoryCall = { asianRepository.getAsianRomance() },
      verifyDataSourceCall = { verify { mockAsianRemoteDataSource.getAsianRomance() } },
    )
  }

  @Test
  fun getCostumeDrama_whenSuccessful_returnsDataCorrectly() {
    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { mockAsianRemoteDataSource.getCostumeDrama() },
      repositoryCall = { asianRepository.getCostumeDrama() },
      verifyDataSourceCall = { verify { mockAsianRemoteDataSource.getCostumeDrama() } },
    )
  }
}
