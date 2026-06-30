package com.waffiq.bazz_movies.core.uihelper.state

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UIStateTest {

  @Test
  fun isLoading_whenStateIsLoading_expectedTrue() {
    val state: UIState<Unit> = UIState.Loading
    assertTrue(state.isLoading)
  }

  @Test
  fun isSuccess_whenDataProvided_returnsDataCorrectly() {
    val tempNumber = 1234

    data class TestClass(val testNumber: Int)

    val data = TestClass(tempNumber)
    val state: UIState<TestClass> = UIState.Success(data)
    val expected = (state as UIState.Success).data.testNumber
    assertEquals(expected, tempNumber)

    val result = state.dataOrNull
    assertEquals(tempNumber, result?.testNumber)
  }

  @Test
  fun isError_whenMessageProvided_returnsMessageCorrectly() {
    val errorMessage = "Something went wrong"

    val state: UIState<Unit> = UIState.Error(message = errorMessage)
    val expectedMessage = (state as UIState.Error).message

    assertEquals(expectedMessage, errorMessage)
    assertNull(state.dataOrNull)
  }
}
