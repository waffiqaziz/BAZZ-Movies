package com.waffiq.bazz_movies.feature.detail.data.repository

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testLoadingState
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testSuccessfulCall
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testUnsuccessfulCall
import com.waffiq.bazz_movies.feature.detail.testutils.BaseDetailRepositoryImplTest
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
}
