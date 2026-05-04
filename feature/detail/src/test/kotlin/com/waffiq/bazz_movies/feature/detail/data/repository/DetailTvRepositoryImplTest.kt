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
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DetailTvRepositoryImplTest : BaseDetailRepositoryImplTest() {

  @Test
  fun getTvDetail_whenSuccessful_returnsSuccessResult() =
    runTest {
      val mockResponse = mockk<DetailTvResponse>(relaxed = true)

      testSuccessfulCall(
        mockResponse = mockResponse,
        dataSourceCall = { mockTvRemoteDataSource.getTvDetail(id) },
        repositoryCall = { repository.getTvDetail(id) },
        expectedData = mockResponse.toTvDetail(),
        verifyDataSourceCall = { coVerify(atLeast = 1) { mockTvRemoteDataSource.getTvDetail(id) } },
      )
    }

  @Test
  fun getTvDetail_whenUnsuccessful_returnsErrorResult() =
    runTest {
      testUnsuccessfulCall(
        dataSourceCall = { mockTvRemoteDataSource.getTvDetail(id) },
        repositoryCall = { repository.getTvDetail(id) },
        verifyDataSourceCall = { coVerify { mockTvRemoteDataSource.getTvDetail(id) } },
      )
    }

  @Test
  fun getTvDetail_whenLoadingEmitted_returnsLoadingOutcome() =
    runTest {
      testLoadingState(
        dataSourceCall = { mockTvRemoteDataSource.getTvDetail(id) },
        repositoryCall = { repository.getTvDetail(id) },
        verifyDataSourceCall = { coVerify { mockTvRemoteDataSource.getTvDetail(id) } },
      )
    }

  @Test
  fun getTvExternalIds_whenSuccessful_returnsSuccessResult() =
    runTest {
      val mockResponse = mockk<ExternalIdResponse>(relaxed = true)
      testSuccessfulCall(
        mockResponse = mockResponse,
        dataSourceCall = { mockTvRemoteDataSource.getTvExternalIds(id) },
        repositoryCall = { repository.getTvExternalIds(id) },
        expectedData = mockResponse.toExternalTvID(),
        verifyDataSourceCall = {
          coVerify(atLeast = 1) { mockTvRemoteDataSource.getTvExternalIds(id) }
        },
      )
    }

  @Test
  fun getTvExternalIds_whenUnsuccessful_returnsErrorResult() =
    runTest {
      testUnsuccessfulCall(
        dataSourceCall = { mockTvRemoteDataSource.getTvExternalIds(id) },
        repositoryCall = { repository.getTvExternalIds(id) },
        verifyDataSourceCall = { coVerify { mockTvRemoteDataSource.getTvExternalIds(id) } },
      )
    }

  @Test
  fun getTvExternalIds_whenLoadingEmitted_returnsLoadingOutcome() =
    runTest {
      testLoadingState(
        dataSourceCall = { mockTvRemoteDataSource.getTvExternalIds(id) },
        repositoryCall = { repository.getTvExternalIds(id) },
        verifyDataSourceCall = { coVerify { mockTvRemoteDataSource.getTvExternalIds(id) } },
      )
    }

  @Test
  fun getTvTrailerLink_whenSuccessful_returnsSuccessResult() =
    runTest {
      val mockResponse = mockk<VideoResponse>(relaxed = true)
      testSuccessfulCall(
        mockResponse = mockResponse,
        dataSourceCall = { mockTvRemoteDataSource.getTvVideo(id) },
        repositoryCall = { repository.getTvTrailerLink(id) },
        expectedData = mockResponse.toVideo(),
        verifyDataSourceCall = { coVerify(atLeast = 1) { mockTvRemoteDataSource.getTvVideo(id) } },
      )
    }

  @Test
  fun getTvTrailerLink_whenUnsuccessful_returnsErrorResult() =
    runTest {
      testUnsuccessfulCall(
        dataSourceCall = { mockTvRemoteDataSource.getTvVideo(id) },
        repositoryCall = { repository.getTvTrailerLink(id) },
        verifyDataSourceCall = { coVerify { mockTvRemoteDataSource.getTvVideo(id) } },
      )
    }

  @Test
  fun getTvTrailerLink_whenLoadingEmitted_returnsLoadingOutcome() =
    runTest {
      testLoadingState(
        dataSourceCall = { mockTvRemoteDataSource.getTvVideo(id) },
        repositoryCall = { repository.getTvTrailerLink(id) },
        verifyDataSourceCall = { coVerify { mockTvRemoteDataSource.getTvVideo(id) } },
      )
    }

  @Test
  fun getTvCredits_whenSuccessful_returnsSuccessResult() =
    runTest {
      val mockResponse = mockk<MediaCreditsResponse>(relaxed = true)
      testSuccessfulCall(
        mockResponse = mockResponse,
        dataSourceCall = { mockTvRemoteDataSource.getTvCredits(id) },
        repositoryCall = { repository.getTvCredits(id) },
        expectedData = mockResponse.toMediaCredits(),
        verifyDataSourceCall = {
          coVerify(atLeast = 1) { mockTvRemoteDataSource.getTvCredits(id) }
        },
      )
    }

  @Test
  fun getTvCredits_whenUnsuccessful_returnsErrorResult() =
    runTest {
      testUnsuccessfulCall(
        dataSourceCall = { mockTvRemoteDataSource.getTvCredits(id) },
        repositoryCall = { repository.getTvCredits(id) },
        verifyDataSourceCall = { coVerify { mockTvRemoteDataSource.getTvCredits(id) } },
      )
    }

  @Test
  fun getTvCredits_whenLoadingEmitted_returnsLoadingOutcome() =
    runTest {
      testLoadingState(
        dataSourceCall = { mockTvRemoteDataSource.getTvCredits(id) },
        repositoryCall = { repository.getTvCredits(id) },
        verifyDataSourceCall = { coVerify { mockTvRemoteDataSource.getTvCredits(id) } },
      )
    }

  @Test
  fun getTvKeywords_whenSuccessful_returnsSuccessResult() =
    runTest {
      val mockResponse = mockk<TvKeywordsResponse>(relaxed = true)
      testSuccessfulCall(
        mockResponse = mockResponse,
        dataSourceCall = { mockTvRemoteDataSource.getTvKeywords(idString) },
        repositoryCall = { repository.getTvKeywords(idString) },
        expectedData = mockResponse.toMediaKeywords(),
        verifyDataSourceCall = {
          coVerify(atLeast = 1) { mockTvRemoteDataSource.getTvKeywords(idString) }
        },
      )
    }

  @Test
  fun getTvKeywords_whenUnsuccessful_returnsErrorResult() =
    runTest {
      testUnsuccessfulCall(
        dataSourceCall = { mockTvRemoteDataSource.getTvKeywords(idString) },
        repositoryCall = { repository.getTvKeywords(idString) },
        verifyDataSourceCall = { coVerify { mockTvRemoteDataSource.getTvKeywords(idString) } },
      )
    }

  @Test
  fun getTvKeywords_whenLoadingEmitted_returnsLoadingOutcome() =
    runTest {
      testLoadingState(
        dataSourceCall = { mockTvRemoteDataSource.getTvKeywords(idString) },
        repositoryCall = { repository.getTvKeywords(idString) },
        verifyDataSourceCall = { coVerify { mockTvRemoteDataSource.getTvKeywords(idString) } },
      )
    }
}
