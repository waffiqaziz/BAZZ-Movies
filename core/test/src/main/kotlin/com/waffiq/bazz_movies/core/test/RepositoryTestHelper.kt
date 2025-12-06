package com.waffiq.bazz_movies.core.test

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import io.mockk.coEvery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import org.junit.Assert.assertEquals

object RepositoryTestHelper {

  const val ERROR_MESSAGE = "Error occurred"
  val networkResultError = NetworkResult.Error(ERROR_MESSAGE)

  /**
   * Generic test for successful network calls with flexible parameters
   */
  suspend fun <T, R> testSuccessfulCall(
    mockResponse: T,
    dataSourceCall: suspend () -> Flow<NetworkResult<T>>,
    repositoryCall: suspend () -> Flow<Outcome<R>>,
    expectedData: R,
    verifyDataSourceCall: () -> Unit,
  ) {
    val networkResult = NetworkResult.Success(mockResponse)

    coEvery { dataSourceCall() } returns flow {
      emit(NetworkResult.Loading)
      emit(networkResult)
    }

    val result = repositoryCall().toList()
    assert(result[1] is Outcome.Success)

    val successResult = result[1] as Outcome.Success
    assertEquals(expectedData, successResult.data)

    verifyDataSourceCall()
  }

  /**
   * Generic test for unsuccessful network calls with flexible parameters
   */
  suspend fun <R> testUnsuccessfulCall(
    dataSourceCall: suspend () -> Flow<NetworkResult<*>>,
    repositoryCall: suspend () -> Flow<Outcome<R>>,
    verifyDataSourceCall: () -> Unit,
  ) {
    coEvery { dataSourceCall() } returns flow {
      emit(NetworkResult.Loading)
      emit(networkResultError)
    }

    val result = repositoryCall().toList()
    assert(result[1] is Outcome.Error)

    val errorResult = result[1] as Outcome.Error
    assertEquals(ERROR_MESSAGE, errorResult.message)

    verifyDataSourceCall()
  }

  /**
   * Generic test for loading state with flexible parameters
   */
  suspend fun <R> testLoadingState(
    dataSourceCall: suspend () -> Flow<NetworkResult<*>>,
    repositoryCall: suspend () -> Flow<Outcome<R>>,
    verifyDataSourceCall: () -> Unit,
  ) {
    coEvery { dataSourceCall() } returns flow {
      emit(NetworkResult.Loading)
    }

    val result = repositoryCall().first()
    assert(result is Outcome.Loading)

    verifyDataSourceCall()
  }
}
