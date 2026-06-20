package com.waffiq.bazz_movies.feature.detail.data.repository

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.TvKeywordsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.DetailTvResponse
import com.waffiq.bazz_movies.feature.detail.testutils.BaseDetailRepositoryImplTest
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toMediaCredits
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaKeywordsMapper.toMediaKeywords
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

  @Test
  fun getTvCredits_whenSuccessful_returnsSuccessResult() =
    runSuccessTest(
      mockResponse = mockk<MediaCreditsResponse>(relaxed = true),
      dataSourceCall = { mockTvRemoteDataSource.getTvCredits(id) },
      repositoryCall = { repository.getTvCredits(id) },
      expectedData = { it.toMediaCredits() },
      verifyCall = { coVerify(atLeast = 1) { mockTvRemoteDataSource.getTvCredits(id) } },
    )

  @Test
  fun getTvKeywords_whenSuccessful_returnsSuccessResult() =
    runSuccessTest(
      mockResponse = mockk<TvKeywordsResponse>(relaxed = true),
      dataSourceCall = { mockTvRemoteDataSource.getTvKeywords(id.toString()) },
      repositoryCall = { repository.getTvKeywords(id.toString()) },
      expectedData = { it.toMediaKeywords() },
      verifyCall = {
        coVerify(atLeast = 1) {
          mockTvRemoteDataSource.getTvKeywords(id.toString())
        }
      },
    )
}
