package com.waffiq.bazz_movies.feature.detail.data.repository

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.MovieKeywordsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia.VideoResponse
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testLoadingState
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testSuccessfulCall
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testUnsuccessfulCall
import com.waffiq.bazz_movies.feature.detail.testutils.BaseDetailRepositoryImplTest
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toMediaCredits
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toVideo
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaKeywordsMapper.toMediaKeywords
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MovieMapper.toDetailMovie
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DetailMovieRepositoryImplTest : BaseDetailRepositoryImplTest() {

  @Test
  fun getMovieDetail_whenSuccessful_returnsSuccessResult() =
    runTest {
      val mockResponse = mockk<DetailMovieResponse>(relaxed = true)
      testSuccessfulCall(
        mockResponse = mockResponse,
        dataSourceCall = { mockMovieDataSource.getMovieDetail(id) },
        repositoryCall = { repository.getMovieDetail(id) },
        expectedData = mockResponse.toDetailMovie(),
        verifyDataSourceCall = { coVerify(atLeast = 1) { mockMovieDataSource.getMovieDetail(id) } },
      )
    }

  @Test
  fun getMovieDetail_whenUnsuccessful_returnsErrorResult() =
    runTest {
      testUnsuccessfulCall(
        dataSourceCall = { mockMovieDataSource.getMovieDetail(id) },
        repositoryCall = { repository.getMovieDetail(id) },
        verifyDataSourceCall = { coVerify { mockMovieDataSource.getMovieDetail(id) } },
      )
    }

  @Test
  fun getMovieDetail_whenLoadingEmitted_returnsLoadingOutcome() =
    runTest {
      testLoadingState(
        dataSourceCall = { mockMovieDataSource.getMovieDetail(id) },
        repositoryCall = { repository.getMovieDetail(id) },
        verifyDataSourceCall = { coVerify { mockMovieDataSource.getMovieDetail(id) } },
      )
    }

  @Test
  fun getMovieTrailerLink_whenSuccessful_returnsSuccessResult() =
    runTest {
      val mockResponse = mockk<VideoResponse>(relaxed = true)
      testSuccessfulCall(
        mockResponse = mockResponse,
        dataSourceCall = { mockMovieDataSource.getMovieVideo(id) },
        repositoryCall = { repository.getMovieTrailerLink(id) },
        expectedData = mockResponse.toVideo(),
        verifyDataSourceCall = { coVerify(atLeast = 1) { mockMovieDataSource.getMovieVideo(id) } },
      )
    }

  @Test
  fun getMovieTrailerLink_whenUnsuccessful_returnsErrorResult() =
    runTest {
      testUnsuccessfulCall(
        dataSourceCall = { mockMovieDataSource.getMovieVideo(id) },
        repositoryCall = { repository.getMovieTrailerLink(id) },
        verifyDataSourceCall = { coVerify { mockMovieDataSource.getMovieVideo(id) } },
      )
    }

  @Test
  fun getMovieTrailerLink_whenLoadingEmitted_returnsLoadingOutcome() =
    runTest {
      testLoadingState(
        dataSourceCall = { mockMovieDataSource.getMovieVideo(id) },
        repositoryCall = { repository.getMovieTrailerLink(id) },
        verifyDataSourceCall = { coVerify { mockMovieDataSource.getMovieVideo(id) } },
      )
    }

  @Test
  fun getMovieCredits_whenSuccessful_returnsSuccessResult() =
    runTest {
      val mockResponse = mockk<MediaCreditsResponse>(relaxed = true)
      testSuccessfulCall(
        mockResponse = mockResponse,
        dataSourceCall = { mockMovieDataSource.getMovieCredits(id) },
        repositoryCall = { repository.getMovieCredits(id) },
        expectedData = mockResponse.toMediaCredits(),
        verifyDataSourceCall = {
          coVerify(atLeast = 1) { mockMovieDataSource.getMovieCredits(id) }
        },
      )
    }

  @Test
  fun getMovieCredits_whenUnsuccessful_returnsErrorResult() =
    runTest {
      testUnsuccessfulCall(
        dataSourceCall = { mockMovieDataSource.getMovieCredits(id) },
        repositoryCall = { repository.getMovieCredits(id) },
        verifyDataSourceCall = { coVerify { mockMovieDataSource.getMovieCredits(id) } },
      )
    }

  @Test
  fun getMovieCredits_whenLoadingEmitted_returnsLoadingOutcome() =
    runTest {
      testLoadingState(
        dataSourceCall = { mockMovieDataSource.getMovieCredits(id) },
        repositoryCall = { repository.getMovieCredits(id) },
        verifyDataSourceCall = { coVerify { mockMovieDataSource.getMovieCredits(id) } },
      )
    }

  @Test
  fun getMovieKeywords_whenSuccessful_returnsSuccessResult() =
    runTest {
      val mockResponse = mockk<MovieKeywordsResponse>(relaxed = true)
      testSuccessfulCall(
        mockResponse = mockResponse,
        dataSourceCall = { mockMovieDataSource.getMovieKeywords(idString) },
        repositoryCall = { repository.getMovieKeywords(idString) },
        expectedData = mockResponse.toMediaKeywords(),
        verifyDataSourceCall = {
          coVerify(atLeast = 1) { mockMovieDataSource.getMovieKeywords(idString) }
        },
      )
    }

  @Test
  fun getMovieKeywords_whenUnsuccessful_returnsErrorResult() =
    runTest {
      testUnsuccessfulCall(
        dataSourceCall = { mockMovieDataSource.getMovieKeywords(idString) },
        repositoryCall = { repository.getMovieKeywords(idString) },
        verifyDataSourceCall = { coVerify { mockMovieDataSource.getMovieKeywords(idString) } },
      )
    }

  @Test
  fun getMovieKeywords_whenLoadingEmitted_returnsLoadingOutcome() =
    runTest {
      testLoadingState(
        dataSourceCall = { mockMovieDataSource.getMovieKeywords(idString) },
        repositoryCall = { repository.getMovieKeywords(idString) },
        verifyDataSourceCall = { coVerify { mockMovieDataSource.getMovieKeywords(idString) } },
      )
    }
}
