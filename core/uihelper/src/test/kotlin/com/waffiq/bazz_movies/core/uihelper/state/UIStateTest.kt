package com.waffiq.bazz_movies.core.uihelper.state

import org.junit.Assert
import org.junit.Test
import kotlin.test.assertEquals

class UIStateTest {

  @Test
  fun isLoading_whenStateIsLoading_expectedTrue() {
    val state: UIState<Unit> = UIState.Loading
    val result = state.isLoading
    Assert.assertTrue(result)
  }

  @Test
  fun isSuccess_whenDataProvided_returnsDataCorrectly() {
    val tempNumber = 1234

    data class TestClass(val testNumber: Int)

    val data = TestClass(tempNumber)
    val state: UIState<TestClass> = UIState.Success(data)
    val expected = (state as UIState.Success).data.testNumber
    assertEquals(expected = expected, actual = tempNumber)
  }


  @Test
  fun isError_whenMessageProvided_returnsMessageCorrectly() {
    val errorMessage = "Something went wrong"
    val state: UIState<Unit> = UIState.Error(message = errorMessage)
    val expectedMessage = (state as UIState.Error).message
    assertEquals(expectedMessage, errorMessage)
  }
}
