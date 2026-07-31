package com.waffiq.bazz_movies.feature.favorite.testutils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import app.cash.turbine.test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

/**
 * Helper object for testing PagingData flows and ViewModel LiveData events.
 * Provides utility functions to test PagingData and LiveData events in a structured way.
 */
object Helper {

  /**
   * Helper function to test ViewModel Flow events.
   */
  fun <T : Any> testViewModelFlow(
    runBlock: () -> Unit,
    flow: Flow<T>,
    expected: T? = null,
    verifyBlock: () -> Unit,
  ) = runTest {
    flow.test {
      runBlock()

      if (expected != null) {
        val item = awaitItem()
        assertEquals(expected, item)
      } else {
        // Expect no emissions
        expectNoEvents()
      }

      verifyBlock()
      cancelAndIgnoreRemainingEvents()
    }
  }

  /**
   * Helper function to test ViewModel LiveData events.
   * It runs a block of code, collects the LiveData events, and verifies the expected outcome.
   */
  fun <T : Any> testViewModelLiveData(
    runBlock: () -> Unit,
    liveData: LiveData<T>,
    expected: T? = null,
    verifyBlock: () -> Unit,
  ) = runTest {
    val collectedData = mutableListOf<T>()
    val observer = Observer<T> { collectedData.add(it) }
    liveData.observeForever(observer)

    // trigger the test block
    runBlock()
    advanceUntilIdle()

    // stop observing
    liveData.removeObserver(observer)

    assertFalse(collectedData.isEmpty())
    assertTrue(collectedData.contains(expected))
    assertEquals(liveData.value, expected)

    verifyBlock()
  }
}
