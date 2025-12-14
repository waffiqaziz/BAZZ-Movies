package com.waffiq.bazz_movies.core.uihelper.state

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UIStateTest {

  @Test
  fun isLoading_whenStateIsLoading_expectedTrue() {
    val state: UIState = UIState.Loading
    val result = state.isLoading
    assertTrue(result)
  }

  @Test
  fun isLoading_whenStateIsIdle_expectedFalse() {
    val state: UIState = UIState.Idle
    val result = state.isLoading
    assertFalse(result)
  }

  @Test
  fun isLoading_whenStateIsSuccess_expectedFalse() {
    val state: UIState = UIState.Success
    val result = state.isLoading
    assertFalse(result)
  }

  @Test
  fun isLoading_whenStateIsError_expectedFalse() {
    val state: UIState = UIState.Error(message = "Something went wrong")
    val result = state.isLoading
    assertFalse(result)
  }
}
