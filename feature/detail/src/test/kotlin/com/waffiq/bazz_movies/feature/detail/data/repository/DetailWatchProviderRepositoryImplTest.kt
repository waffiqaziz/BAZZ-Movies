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

  private val params = "params"

  @Test
  fun getWatchProviders_whenSuccessful_returnsSuccessResult() = runTest {
    val mockResponse = mockk<WatchProvidersResponse>(relaxed = true)
    testSuccessfulCall(
      mockResponse = mockResponse,
      dataSourceCall = { movieDataSource.getWatchProviders(params, id) },
      repositoryCall = { repository.getWatchProviders(params, id) },
      expectedData = mockResponse.toWatchProviders(),
      verifyDataSourceCall = {
        coVerify(atLeast = 1) { movieDataSource.getWatchProviders(params, id) }
      }
    )
  }

  @Test
  fun getWatchProviders_whenUnsuccessful_returnsErrorResult() = runTest {
    testUnsuccessfulCall(
      dataSourceCall = { movieDataSource.getWatchProviders(params, id) },
      repositoryCall = { repository.getWatchProviders(params, id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getWatchProviders(params, id) } }
    )
  }

  @Test
  fun getWatchProviders_whenLoadingEmitted_returnsLoadingOutcome() = runTest {
    testLoadingState(
      dataSourceCall = { movieDataSource.getWatchProviders(params, id) },
      repositoryCall = { repository.getWatchProviders(params, id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getWatchProviders(params, id) } }
    )
  }
}
