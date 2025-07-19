package com.waffiq.bazz_movies.feature.person.testutils

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.test.UnconfinedDispatcherRule
import com.waffiq.bazz_movies.feature.person.data.repository.PersonRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule

abstract class BasePersonRepositoryImplTest {

  protected val id = 1
  protected val errorMessage = "Error occurred"
  protected val networkResultError = NetworkResult.Error(errorMessage)

  protected lateinit var repository: PersonRepositoryImpl
  protected val movieDataSource: MovieDataSource = mockk()

  @get:Rule
  val mainDispatcherRule = UnconfinedDispatcherRule()

  @Before
  fun setUp() {
    repository = PersonRepositoryImpl(movieDataSource)
  }

  /**
   * Generic test for successful network calls with flexible parameters
   */
  protected suspend fun <T, R> testSuccessfulCall(
    mockResponse: T,
    dataSourceCall: suspend () -> Flow<NetworkResult<T>>,
    repositoryCall: suspend () -> Flow<Outcome<R>>,
    expectedData: R,
    verifyDataSourceCall: () -> Unit
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
  protected suspend fun <R> testUnsuccessfulCall(
    dataSourceCall: suspend () -> Flow<NetworkResult<*>>,
    repositoryCall: suspend () -> Flow<Outcome<R>>,
    verifyDataSourceCall: () -> Unit
  ) {
    coEvery { dataSourceCall() } returns flow {
      emit(NetworkResult.Loading)
      emit(networkResultError)
    }

    val result = repositoryCall().toList()
    assert(result[1] is Outcome.Error)

    val errorResult = result[1] as Outcome.Error
    assertEquals(errorMessage, errorResult.message)

    verifyDataSourceCall()
  }

  /**
   * Generic test for loading state with flexible parameters
   */
  protected suspend fun <R> testLoadingState(
    dataSourceCall: suspend () -> Flow<NetworkResult<*>>,
    repositoryCall: suspend () -> Flow<Outcome<R>>,
    verifyDataSourceCall: () -> Unit
  ) {
    coEvery { dataSourceCall() } returns flow {
      emit(NetworkResult.Loading)
    }

    val result = repositoryCall().first()
    assert(result is Outcome.Loading)

    verifyDataSourceCall()
  }
}
