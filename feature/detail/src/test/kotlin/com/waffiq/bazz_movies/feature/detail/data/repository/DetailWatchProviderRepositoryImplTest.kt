package com.waffiq.bazz_movies.feature.detail.data.repository

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders.WatchProvidersResponse
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testLoadingState
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testSuccessfulCall
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testUnsuccessfulCall
import com.waffiq.bazz_movies.feature.detail.testutils.BaseDetailRepositoryImplTest
import com.waffiq.bazz_movies.feature.detail.utils.mappers.WatchProvidersMapper.toWatchProviders
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DetailWatchProviderRepositoryImplTest : BaseDetailRepositoryImplTest() {

  @Test
  fun getMovieWatchProviders_whenSuccessful_returnsSuccessResult() = runTest {
    val mockResponse = mockk<WatchProvidersResponse>(relaxed = true)
    testSuccessfulCall(
      mockResponse = mockResponse,
      dataSourceCall = { mockMovieDataSource.getMovieWatchProviders(id) },
      repositoryCall = { repository.getMovieWatchProviders(id) },
      expectedData = mockResponse.toWatchProviders(),
      verifyDataSourceCall = {
        coVerify(atLeast = 1) { mockMovieDataSource.getMovieWatchProviders(id) }
      }
    )
  }

  @Test
  fun getMovieWatchProviders_whenUnsuccessful_returnsErrorResult() = runTest {
    testUnsuccessfulCall(
      dataSourceCall = { mockMovieDataSource.getMovieWatchProviders(id) },
      repositoryCall = { repository.getMovieWatchProviders(id) },
      verifyDataSourceCall = { coVerify { mockMovieDataSource.getMovieWatchProviders(id) } }
    )
  }

  @Test
  fun getMovieWatchProviders_whenLoadingEmitted_returnsLoadingOutcome() = runTest {
    testLoadingState(
      dataSourceCall = { mockMovieDataSource.getMovieWatchProviders(id) },
      repositoryCall = { repository.getMovieWatchProviders(id) },
      verifyDataSourceCall = { coVerify { mockMovieDataSource.getMovieWatchProviders(id) } }
    )
  }

  @Test
  fun getTvWatchProviders_whenSuccessful_returnsSuccessResult() = runTest {
    val mockResponse = mockk<WatchProvidersResponse>(relaxed = true)
    testSuccessfulCall(
      mockResponse = mockResponse,
      dataSourceCall = { mockTvRemoteDataSource.getTvWatchProviders(id) },
      repositoryCall = { repository.getTvWatchProviders(id) },
      expectedData = mockResponse.toWatchProviders(),
      verifyDataSourceCall = {
        coVerify(atLeast = 1) { mockTvRemoteDataSource.getTvWatchProviders(id) }
      }
    )
  }

  @Test
  fun getTvWatchProviders_whenUnsuccessful_returnsErrorResult() = runTest {
    testUnsuccessfulCall(
      dataSourceCall = { mockTvRemoteDataSource.getTvWatchProviders(id) },
      repositoryCall = { repository.getTvWatchProviders(id) },
      verifyDataSourceCall = { coVerify { mockTvRemoteDataSource.getTvWatchProviders(id) } }
    )
  }

  @Test
  fun getTvWatchProviders_whenLoadingEmitted_returnsLoadingOutcome() = runTest {
    testLoadingState(
      dataSourceCall = { mockTvRemoteDataSource.getTvWatchProviders(id) },
      repositoryCall = { repository.getTvWatchProviders(id) },
      verifyDataSourceCall = { coVerify { mockTvRemoteDataSource.getTvWatchProviders(id) } }
    )
  }
}
