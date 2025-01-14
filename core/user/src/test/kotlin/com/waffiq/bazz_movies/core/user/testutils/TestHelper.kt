package com.waffiq.bazz_movies.core.user.testutils

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.Outcome
import kotlinx.coroutines.flow.Flow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

object TestHelper {
  suspend inline fun <reified D> Flow<Outcome<*>>.testOutcome(
    expectedData: D,
    crossinline transform: (Any?) -> D = { it as D }
  ) {
    test {
      val result = awaitItem()
      assertTrue(result is Outcome.Success)
      result as Outcome.Success
      val data = transform(result.data)
      assertEquals(expectedData, data)
      cancelAndIgnoreRemainingEvents()
    }
  }

  suspend inline fun <reified D> Flow<*>.testResult(
    expectedData: D
  ) {
    test {
      val result = awaitItem()
      assertEquals(expectedData, result)
      cancelAndIgnoreRemainingEvents()
    }
  }
}