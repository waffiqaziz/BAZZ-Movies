package com.waffiq.bazz_movies.feature.detail.data.repository

import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testLoadingState
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testSuccessfulCall
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testUnsuccessfulCall
import com.waffiq.bazz_movies.feature.detail.testutils.BaseDetailRepositoryImplTest
import com.waffiq.bazz_movies.feature.detail.utils.mappers.OMDbMapper.toOMDbDetails
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DetailOMDbRepositoryImplTest : BaseDetailRepositoryImplTest() {

  private val imdb = "IMDB ID"

  @Test
  fun getOMDbDetails_whenSuccessful_returnsSuccessResult() = runTest {
    val mockResponse = mockk<OMDbDetailsResponse>(relaxed = true)
    testSuccessfulCall(
      mockResponse = mockResponse,
      dataSourceCall = { movieDataSource.getOMDbDetails(imdb) },
      repositoryCall = { repository.getOMDbDetails(imdb) },
      expectedData = mockResponse.toOMDbDetails(),
      verifyDataSourceCall = { coVerify(atLeast = 1) { movieDataSource.getOMDbDetails(imdb) } }
    )
  }

  @Test
  fun getOMDbDetails_whenUnsuccessful_returnsErrorResult() = runTest {
    testUnsuccessfulCall(
      dataSourceCall = { movieDataSource.getOMDbDetails(imdb) },
      repositoryCall = { repository.getOMDbDetails(imdb) },
      verifyDataSourceCall = { coVerify { movieDataSource.getOMDbDetails(imdb) } }
    )
  }

  @Test
  fun getOMDbDetails_whenLoadingEmitted_returnsLoadingOutcome() = runTest {
    testLoadingState(
      dataSourceCall = { movieDataSource.getOMDbDetails(imdb) },
      repositoryCall = { repository.getOMDbDetails(imdb) },
      verifyDataSourceCall = { coVerify { movieDataSource.getOMDbDetails(imdb) } }
    )
  }
}
