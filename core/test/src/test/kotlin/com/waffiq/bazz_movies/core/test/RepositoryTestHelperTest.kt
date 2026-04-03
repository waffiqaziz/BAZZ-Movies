package com.waffiq.bazz_movies.core.test

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testLoadingState
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testSuccessfulCall
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testUnsuccessfulCall
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFailsWith

class RepositoryTestHelperTest {

  @get:Rule
  val dispatcherRule = UnconfinedDispatcherRule()

  private val fakeString = "fake_response"
  private val fakeMappedInt = 42

  private val mockDataSourceCall: suspend () -> Flow<NetworkResult<String>> = mockk()
  private val mockRepositoryCall: suspend () -> Flow<Outcome<Int>> = mockk()
  private val mockVerify: () -> Unit = mockk(relaxed = true)

  @Test
  fun errorMessageConstant_whenAccessed_shouldReturnExpectedValue() {
    assertEquals("Error occurred", RepositoryTestHelper.ERROR_MESSAGE)
  }

  @Test
  fun networkResultError_whenAccessed_shouldContainCorrectMessage() {
    val error = RepositoryTestHelper.networkResultError
    assertEquals(error.message, "Error occurred")
  }

  @Test
  fun testSuccessfulCall_whenRepositoryEmitsSuccess_shouldEmitLoadingThenExpectedData() = runTest {
    dataSourceEmitSuccess()
    coEvery { mockRepositoryCall() } returns flow {
      emit(Outcome.Loading)
      emit(Outcome.Success(fakeMappedInt))
    }

    assertTestSuccessfulCall()

    verify { mockVerify() }
  }

  @Test
  fun testSuccessfulCall_whenRepositoryEmitsError_shouldFail() = runTest {
    dataSourceEmitSuccess()
    coEvery { mockRepositoryCall() } returns flow {
      emit(Outcome.Loading)
      emit(Outcome.Error("unexpected error"))
    }

    assertFailsWith<AssertionError> {
      assertTestSuccessfulCall()
    }
  }

  @Test
  fun testSuccessfulCall_whenRepositoryEmitsIncorrectData_shouldFail() = runTest {
    dataSourceEmitSuccess()
    coEvery { mockRepositoryCall() } returns flow {
      emit(Outcome.Loading)
      emit(Outcome.Success(999)) // wrong data
    }

    assertFailsWith<AssertionError> {
      // expects 42, gets 999
      assertTestSuccessfulCall()
    }
  }

  @Test
  fun testUnsuccessfulCall_whenRepositoryEmitsError_shouldEmitLoadingThenErrorMessage() = runTest {
    dataSourceEmitError()

    coEvery { mockRepositoryCall() } returns flow {
      emit(Outcome.Loading)
      emit(Outcome.Error(RepositoryTestHelper.ERROR_MESSAGE))
    }

    assertTestUnsuccessfulCall()

    verify { mockVerify() }
  }

  @Test
  fun testUnsuccessfulCall_whenRepositoryEmitsSuccess_shouldFail() = runTest {
    dataSourceEmitError()
    coEvery { mockRepositoryCall() } returns flow {
      emit(Outcome.Loading)
      emit(Outcome.Success(fakeMappedInt)) // wrong, should be Error
    }

    assertFailsWith<AssertionError> {
      assertTestUnsuccessfulCall()
    }
  }

  @Test
  fun testUnsuccessfulCall_whenErrorMessageDoesNotMatch_shouldFail() = runTest {
    dataSourceEmitError()

    coEvery { mockRepositoryCall() } returns flow {
      emit(Outcome.Loading)
      emit(Outcome.Error("some other error")) // wrong message
    }

    assertFailsWith<AssertionError> {
      assertTestUnsuccessfulCall()
    }
  }

  @Test
  fun testLoadingState_whenRepositoryEmitsLoading_shouldPass() = runTest {
    dataSourceEmitLoading()
    coEvery { mockRepositoryCall() } returns flow {
      emit(Outcome.Loading)
    }

    assertTestLoadingState()

    verify { mockVerify() }
  }

  @Test
  fun testLoadingState_whenRepositoryEmitsNonLoading_shouldFail() = runTest {
    dataSourceEmitLoading()
    coEvery { mockRepositoryCall() } returns flow {
      emit(Outcome.Success(fakeMappedInt)) // wrong, should be Loading
    }

    assertFailsWith<AssertionError> {
      assertTestLoadingState()
    }
  }

  private fun dataSourceEmitSuccess() {
    coEvery { mockDataSourceCall() } returns flow {
      emit(NetworkResult.Loading)
      emit(NetworkResult.Success(fakeString))
    }
  }

  private fun dataSourceEmitError() {
    coEvery { mockDataSourceCall() } returns flow {
      emit(NetworkResult.Loading)
      emit(RepositoryTestHelper.networkResultError)
    }
  }

  private fun dataSourceEmitLoading() {
    coEvery { mockDataSourceCall() } returns flow {
      emit(NetworkResult.Loading)
    }
  }

  private suspend fun assertTestSuccessfulCall() {
    testSuccessfulCall(
      mockResponse = fakeString,
      dataSourceCall = mockDataSourceCall,
      repositoryCall = mockRepositoryCall,
      expectedData = fakeMappedInt,
      verifyDataSourceCall = mockVerify,
    )
  }

  private suspend fun assertTestUnsuccessfulCall() {
    testUnsuccessfulCall(
      dataSourceCall = mockDataSourceCall,
      repositoryCall = mockRepositoryCall,
      verifyDataSourceCall = mockVerify,
    )
  }

  private suspend fun assertTestLoadingState() {
    testLoadingState(
      dataSourceCall = mockDataSourceCall,
      repositoryCall = mockRepositoryCall,
      verifyDataSourceCall = mockVerify,
    )
  }
}
