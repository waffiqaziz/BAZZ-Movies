package com.waffiq.bazz_movies.feature.detail.data.repository

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.DetailTvResponse
import com.waffiq.bazz_movies.feature.detail.testutils.BaseDetailRepositoryImplTest
import com.waffiq.bazz_movies.feature.detail.utils.mappers.TvMapper.toTvDetail
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Test

class DetailTvRepositoryImplTest : BaseDetailRepositoryImplTest() {

  @Test
  fun getTvDetail_whenSuccessful_returnsSuccessResult() =
    runSuccessTest(
      mockResponse = mockk<DetailTvResponse>(relaxed = true),
      dataSourceCall = { mockTvRemoteDataSource.getTvDetail(id) },
      repositoryCall = { repository.getTvDetail(id) },
      expectedData = { it.toTvDetail() },
      verifyCall = { coVerify(atLeast = 1) { mockTvRemoteDataSource.getTvDetail(id) } },
    )

  @Test
  fun getTvDetail_whenUnsuccessful_returnsErrorResult() =
    runErrorTest(
      dataSourceCall = { mockTvRemoteDataSource.getTvDetail(id) },
      repositoryCall = { repository.getTvDetail(id) },
      verifyCall = { coVerify { mockTvRemoteDataSource.getTvDetail(id) } },
    )

  @Test
  fun getTvDetail_whenLoadingEmitted_returnsLoadingOutcome() =
    runLoadingTest(
      dataSourceCall = { mockTvRemoteDataSource.getTvDetail(id) },
      repositoryCall = { repository.getTvDetail(id) },
      verifyCall = { coVerify { mockTvRemoteDataSource.getTvDetail(id) } },
    )
}
