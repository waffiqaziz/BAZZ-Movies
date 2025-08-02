package com.waffiq.bazz_movies.feature.detail.data.repository

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia.VideoResponse
import com.waffiq.bazz_movies.feature.detail.testutils.BaseDetailRepositoryImplTest
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toMediaCredits
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toVideo
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MovieMapper.toDetailMovie
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DetailMovieRepositoryImplTest : BaseDetailRepositoryImplTest() {

  @Test
  fun getMovieDetail_whenSuccessful_returnsSuccessResult() = runTest {
    val mockResponse = mockk<DetailMovieResponse>(relaxed = true)
    testSuccessfulCall(
      mockResponse = mockResponse,
      dataSourceCall = { movieDataSource.getMovieDetail(id) },
      repositoryCall = { repository.getMovieDetail(id) },
      expectedData = mockResponse.toDetailMovie(),
      verifyDataSourceCall = { coVerify(atLeast = 1) { movieDataSource.getMovieDetail(id) } }
    )
  }

  @Test
  fun getMovieDetail_whenUnsuccessful_returnsErrorResult() = runTest {
    testUnsuccessfulCall(
      dataSourceCall = { movieDataSource.getMovieDetail(id) },
      repositoryCall = { repository.getMovieDetail(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getMovieDetail(id) } }
    )
  }

  @Test
  fun getMovieDetail_whenLoadingEmitted_returnsLoadingOutcome() = runTest {
    testLoadingState(
      dataSourceCall = { movieDataSource.getMovieDetail(id) },
      repositoryCall = { repository.getMovieDetail(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getMovieDetail(id) } }
    )
  }

  @Test
  fun getTrailerLinkMovie_whenSuccessful_returnsSuccessResult() = runTest {
    val mockResponse = mockk<VideoResponse>(relaxed = true)
    testSuccessfulCall(
      mockResponse = mockResponse,
      dataSourceCall = { movieDataSource.getMovieVideo(id) },
      repositoryCall = { repository.getMovieTrailerLink(id) },
      expectedData = mockResponse.toVideo(),
      verifyDataSourceCall = { coVerify(atLeast = 1) { movieDataSource.getMovieVideo(id) } }
    )
  }

  @Test
  fun getTrailerLinkMovie_whenUnsuccessful_returnsErrorResult() = runTest {
    testUnsuccessfulCall(
      dataSourceCall = { movieDataSource.getMovieVideo(id) },
      repositoryCall = { repository.getMovieTrailerLink(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getMovieVideo(id) } }
    )
  }

  @Test
  fun getTrailerLinkMovie_whenLoadingEmitted_returnsLoadingOutcome() = runTest {
    testLoadingState(
      dataSourceCall = { movieDataSource.getMovieVideo(id) },
      repositoryCall = { repository.getMovieTrailerLink(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getMovieVideo(id) } }
    )
  }

  @Test
  fun getCreditMovies_whenSuccessful_returnsSuccessResult() = runTest {
    val mockResponse = mockk<MediaCreditsResponse>(relaxed = true)
    testSuccessfulCall(
      mockResponse = mockResponse,
      dataSourceCall = { movieDataSource.getMovieCredits(id) },
      repositoryCall = { repository.getMovieCredits(id) },
      expectedData = mockResponse.toMediaCredits(),
      verifyDataSourceCall = { coVerify(atLeast = 1) { movieDataSource.getMovieCredits(id) } }
    )
  }

  @Test
  fun getCreditMovies_whenUnsuccessful_returnsErrorResult() = runTest {
    testUnsuccessfulCall(
      dataSourceCall = { movieDataSource.getMovieCredits(id) },
      repositoryCall = { repository.getMovieCredits(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getMovieCredits(id) } }
    )
  }

  @Test
  fun getCreditMovies_whenLoadingEmitted_returnsLoadingOutcome() = runTest {
    testLoadingState(
      dataSourceCall = { movieDataSource.getMovieCredits(id) },
      repositoryCall = { repository.getMovieCredits(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getMovieCredits(id) } }
    )
  }

  @Test
  fun getMovieRecommendation_whenSuccessful_returnsDataCorrectly() {
    val response = createSampleMediaItemResponse()
    val fakePagingData = createSamplePagingData(response, response)

    testSuccessfulPagingData(
      mockPagingData = fakePagingData,
      dataSourceCall = { movieDataSource.getMovieRecommendation(id) },
      repositoryCall = { repository.getMovieRecommendationPagingData(id) },
      verifyDataSourceCall = { verify { movieDataSource.getMovieRecommendation(id) } }
    )
  }

  @Test
  fun getMovieRecommendation_whenSuccessful_returnsPagedData() {
    testEmptyPagingData(
      dataSourceCall = { movieDataSource.getMovieRecommendation(id) },
      repositoryCall = { repository.getMovieRecommendationPagingData(id) },
      verifyDataSourceCall = { verify { movieDataSource.getMovieRecommendation(id) } }
    )
  }

  @Test
  fun getMovieRecommendation_whenSearchItemIsNull_returnsNonEmptyPagingData() {
    testPagingDataWithMockItems(
      dataSourceCall = { movieDataSource.getMovieRecommendation(id) },
      repositoryCall = { repository.getMovieRecommendationPagingData(id) },
      verifyDataSourceCall = { verify { movieDataSource.getMovieRecommendation(id) } }
    )
  }
}
