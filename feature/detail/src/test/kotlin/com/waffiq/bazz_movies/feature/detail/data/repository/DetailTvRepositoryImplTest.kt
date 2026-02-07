package com.waffiq.bazz_movies.feature.detail.data.repository

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.TvKeywordsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.DetailTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ExternalIdResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia.VideoResponse
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testLoadingState
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testSuccessfulCall
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testUnsuccessfulCall
import com.waffiq.bazz_movies.feature.detail.testutils.BaseDetailRepositoryImplTest
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toMediaCredits
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toVideo
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaKeywordsMapper.toMediaKeywords
import com.waffiq.bazz_movies.feature.detail.utils.mappers.TvMapper.toExternalTvID
import com.waffiq.bazz_movies.feature.detail.utils.mappers.TvMapper.toTvDetail
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DetailTvRepositoryImplTest : BaseDetailRepositoryImplTest() {

  @Test
  fun getTvDetail_whenSuccessful_returnsSuccessResult() = runTest {
    val mockResponse = mockk<DetailTvResponse>(relaxed = true)

    testSuccessfulCall(
      mockResponse = mockResponse,
      dataSourceCall = { movieDataSource.getTvDetail(id) },
      repositoryCall = { repository.getTvDetail(id) },
      expectedData = mockResponse.toTvDetail(),
      verifyDataSourceCall = { coVerify(atLeast = 1) { movieDataSource.getTvDetail(id) } }
    )
  }

  @Test
  fun getTvDetail_whenUnsuccessful_returnsErrorResult() = runTest {
    testUnsuccessfulCall(
      dataSourceCall = { movieDataSource.getTvDetail(id) },
      repositoryCall = { repository.getTvDetail(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getTvDetail(id) } }
    )
  }

  @Test
  fun getTvDetail_whenLoadingEmitted_returnsLoadingOutcome() = runTest {
    testLoadingState(
      dataSourceCall = { movieDataSource.getTvDetail(id) },
      repositoryCall = { repository.getTvDetail(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getTvDetail(id) } }
    )
  }

  @Test
  fun getTvExternalIds_whenSuccessful_returnsSuccessResult() = runTest {
    val mockResponse = mockk<ExternalIdResponse>(relaxed = true)
    testSuccessfulCall(
      mockResponse = mockResponse,
      dataSourceCall = { movieDataSource.getTvExternalIds(id) },
      repositoryCall = { repository.getTvExternalIds(id) },
      expectedData = mockResponse.toExternalTvID(),
      verifyDataSourceCall = { coVerify(atLeast = 1) { movieDataSource.getTvExternalIds(id) } }
    )
  }

  @Test
  fun getTvExternalIds_whenUnsuccessful_returnsErrorResult() = runTest {
    testUnsuccessfulCall(
      dataSourceCall = { movieDataSource.getTvExternalIds(id) },
      repositoryCall = { repository.getTvExternalIds(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getTvExternalIds(id) } }
    )
  }

  @Test
  fun getTvExternalIds_whenLoadingEmitted_returnsLoadingOutcome() = runTest {
    testLoadingState(
      dataSourceCall = { movieDataSource.getTvExternalIds(id) },
      repositoryCall = { repository.getTvExternalIds(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getTvExternalIds(id) } }
    )
  }

  @Test
  fun getTvTrailerLink_whenSuccessful_returnsSuccessResult() = runTest {
    val mockResponse = mockk<VideoResponse>(relaxed = true)
    testSuccessfulCall(
      mockResponse = mockResponse,
      dataSourceCall = { movieDataSource.getTvVideo(id) },
      repositoryCall = { repository.getTvTrailerLink(id) },
      expectedData = mockResponse.toVideo(),
      verifyDataSourceCall = { coVerify(atLeast = 1) { movieDataSource.getTvVideo(id) } }
    )
  }

  @Test
  fun getTvTrailerLink_whenUnsuccessful_returnsErrorResult() = runTest {
    testUnsuccessfulCall(
      dataSourceCall = { movieDataSource.getTvVideo(id) },
      repositoryCall = { repository.getTvTrailerLink(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getTvVideo(id) } }
    )
  }

  @Test
  fun getTvTrailerLink_whenLoadingEmitted_returnsLoadingOutcome() = runTest {
    testLoadingState(
      dataSourceCall = { movieDataSource.getTvVideo(id) },
      repositoryCall = { repository.getTvTrailerLink(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getTvVideo(id) } }
    )
  }

  @Test
  fun getTvCredits_whenSuccessful_returnsSuccessResult() = runTest {
    val mockResponse = mockk<MediaCreditsResponse>(relaxed = true)
    testSuccessfulCall(
      mockResponse = mockResponse,
      dataSourceCall = { movieDataSource.getTvCredits(id) },
      repositoryCall = { repository.getTvCredits(id) },
      expectedData = mockResponse.toMediaCredits(),
      verifyDataSourceCall = { coVerify(atLeast = 1) { movieDataSource.getTvCredits(id) } }
    )
  }

  @Test
  fun getTvCredits_whenUnsuccessful_returnsErrorResult() = runTest {
    testUnsuccessfulCall(
      dataSourceCall = { movieDataSource.getTvCredits(id) },
      repositoryCall = { repository.getTvCredits(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getTvCredits(id) } }
    )
  }

  @Test
  fun getTvCredits_whenLoadingEmitted_returnsLoadingOutcome() = runTest {
    testLoadingState(
      dataSourceCall = { movieDataSource.getTvCredits(id) },
      repositoryCall = { repository.getTvCredits(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getTvCredits(id) } }
    )
  }

  @Test
  fun getTvKeywords_whenSuccessful_returnsSuccessResult() = runTest {
    val mockResponse = mockk<TvKeywordsResponse>(relaxed = true)
    testSuccessfulCall(
      mockResponse = mockResponse,
      dataSourceCall = { movieDataSource.getTvKeywords(idString) },
      repositoryCall = { repository.getTvKeywords(idString) },
      expectedData = mockResponse.toMediaKeywords(),
      verifyDataSourceCall = { coVerify(atLeast = 1) { movieDataSource.getTvKeywords(idString) } }
    )
  }

  @Test
  fun getTvKeywords_whenUnsuccessful_returnsErrorResult() = runTest {
    testUnsuccessfulCall(
      dataSourceCall = { movieDataSource.getTvKeywords(idString) },
      repositoryCall = { repository.getTvKeywords(idString) },
      verifyDataSourceCall = { coVerify { movieDataSource.getTvKeywords(idString) } }
    )
  }

  @Test
  fun getTvKeywords_whenLoadingEmitted_returnsLoadingOutcome() = runTest {
    testLoadingState(
      dataSourceCall = { movieDataSource.getTvKeywords(idString) },
      repositoryCall = { repository.getTvKeywords(idString) },
      verifyDataSourceCall = { coVerify { movieDataSource.getTvKeywords(idString) } }
    )
  }

  @Test
  fun getTvRecommendation_whenSuccessful_returnsDataCorrectly() {
    val response = createSampleMediaItemResponse()
    val fakePagingData = createSamplePagingData(response, response)

    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { movieDataSource.getTvRecommendation(id) },
      repositoryCall = { repository.getTvRecommendationPagingData(id) },
      verifyDataSourceCall = { verify { movieDataSource.getTvRecommendation(id) } }
    )
  }

  @Test
  fun getTvRecommendation_whenSuccessful_returnsPagedData() {
    testEmptyPagingData(
      dataSourceCall = { movieDataSource.getTvRecommendation(id) },
      repositoryCall = { repository.getTvRecommendationPagingData(id) },
      verifyDataSourceCall = { verify { movieDataSource.getTvRecommendation(id) } }
    )
  }

  @Test
  fun getTvRecommendation_whenSearchItemIsNull_returnsNonEmptyPagingData() {
    testPagingDataWithMockItems(
      dataSourceCall = { movieDataSource.getTvRecommendation(id) },
      repositoryCall = { repository.getTvRecommendationPagingData(id) },
      verifyDataSourceCall = { verify { movieDataSource.getTvRecommendation(id) } }
    )
  }
}
