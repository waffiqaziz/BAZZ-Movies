package com.waffiq.bazz_movies.feature.detail.testutils

import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.core.network.data.remote.datasource.movie.MovieRemoteDataSource
import com.waffiq.bazz_movies.core.network.data.remote.datasource.omdb.OmdbRemoteDataSource
import com.waffiq.bazz_movies.core.network.data.remote.datasource.tv.TvRemoteDataSource
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testLoadingState
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testSuccessfulCall
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testUnsuccessfulCall
import com.waffiq.bazz_movies.core.test.UnconfinedDispatcherRule
import com.waffiq.bazz_movies.feature.detail.data.repository.DetailRepositoryImpl
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest

abstract class BaseDetailRepositoryImplTest {

  protected lateinit var repository: DetailRepositoryImpl
  protected val mockMovieDataSource: MovieRemoteDataSource = mockk()
  protected val mockTvRemoteDataSource: TvRemoteDataSource = mockk()
  protected val mockOmdbRemoteDataSource: OmdbRemoteDataSource = mockk()

  protected val id = 1
  protected val idString = "tt12345"

  @get:Rule
  val mainDispatcherRule = UnconfinedDispatcherRule()

  @BeforeTest
  fun setUp() {
    repository = DetailRepositoryImpl(
      mockMovieDataSource,
      mockTvRemoteDataSource,
      mockOmdbRemoteDataSource,
    )
  }

  protected fun <R> runErrorTest(
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

  protected fun <R> runLoadingTest(
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

  protected inline fun <Response, Expected> runSuccessTest(
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
