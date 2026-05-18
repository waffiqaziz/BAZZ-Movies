package com.waffiq.bazz_movies.feature.detail.data.repository

import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.DetailTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ExternalIdResponse
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testLoadingState
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testSuccessfulCall
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testUnsuccessfulCall
import com.waffiq.bazz_movies.feature.detail.testutils.BaseDetailRepositoryImplTest
import com.waffiq.bazz_movies.feature.detail.utils.mappers.TvMapper.toExternalTvID
import com.waffiq.bazz_movies.feature.detail.utils.mappers.TvMapper.toTvDetail
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
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
  fun getTvExternalIds_whenSuccessful_returnsSuccessResult() =
    runSuccessTest(
      mockResponse = mockk<ExternalIdResponse>(relaxed = true),
      dataSourceCall = { mockTvRemoteDataSource.getTvExternalIds(id) },
      repositoryCall = { repository.getTvExternalIds(id) },
      expectedData = { it.toExternalTvID() },
      verifyCall = {
        coVerify(atLeast = 1) {
          mockTvRemoteDataSource.getTvExternalIds(id)
        }
      },
    )

  @Test
  fun getTvExternalIds_whenUnsuccessful_returnsErrorResult() =
    runErrorTest(
      dataSourceCall = { mockTvRemoteDataSource.getTvExternalIds(id) },
      repositoryCall = { repository.getTvExternalIds(id) },
      verifyCall = {
        coVerify {
          mockTvRemoteDataSource.getTvExternalIds(id)
        }
      },
    )

  @Test
  fun getTvExternalIds_whenLoadingEmitted_returnsLoadingOutcome() =
    runLoadingTest(
      dataSourceCall = { mockTvRemoteDataSource.getTvExternalIds(id) },
      repositoryCall = { repository.getTvExternalIds(id) },
      verifyCall = {
        coVerify {
          mockTvRemoteDataSource.getTvExternalIds(id)
        }
      },
    )

  private fun <R> runErrorTest(
    dataSourceCall: suspend () -> Flow<NetworkResult<*>>,
    repositoryCall: suspend () -> Flow<Outcome<R>>,
    verifyCall: () -> Unit,
  ) = runTest {
    testUnsuccessfulCall(
      dataSourceCall = dataSourceCall,
      repositoryCall = repositoryCall,
      verifyDataSourceCall = verifyCall,
    )
  }

  private fun <R> runLoadingTest(
    dataSourceCall: suspend () -> Flow<NetworkResult<*>>,
    repositoryCall: suspend () -> Flow<Outcome<R>>,
    verifyCall: () -> Unit,
  ) = runTest {
    testLoadingState(
      dataSourceCall = dataSourceCall,
      repositoryCall = repositoryCall,
      verifyDataSourceCall = verifyCall,
    )
  }

  private inline fun <Response, Expected> runSuccessTest(
    mockResponse: Response,
    noinline dataSourceCall: suspend () -> Flow<NetworkResult<Response>>,
    noinline repositoryCall: suspend () -> Flow<Outcome<Expected>>,
    crossinline expectedData: (Response) -> Expected,
    noinline verifyCall: () -> Unit,
  ) = runTest {
    testSuccessfulCall(
      mockResponse = mockResponse,
      dataSourceCall = dataSourceCall,
      repositoryCall = repositoryCall,
      expectedData = expectedData(mockResponse),
      verifyDataSourceCall = verifyCall,
    )
  }
}
