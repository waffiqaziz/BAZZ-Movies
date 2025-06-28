package com.waffiq.bazz_movies.core.domain

import junit.framework.TestCase.assertEquals
import org.junit.Test

@Suppress("SENSELESS_COMPARISON")
class OutcomeTest {

  @Test
  fun outcomeSuccess_withValidValue_returnsCorrectData() {
    val result = Outcome.Success(data = "Success Result")
    assertEquals("Success Result", result.data)
  }

  @Test
  fun outcomeError_withValidValue_returnsCorrectData() {
    val result = Outcome.Error(message = "Something went wrong")
    assertEquals("Something went wrong", result.message)
  }

  @Test
  fun outcomeLoading_withValidValue_returnsCorrectData() {
    val result = Outcome.Loading
    assertEquals(Outcome.Loading, result)
  }
}
