package com.waffiq.bazz_movies.feature.favorite.testutils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.common.utils.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
        assertThat(item).isEqualTo(expected)
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
  fun <T : Any> testViewModelLiveDataEvent(
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

    checkEventSuccess(liveData, expected, collectedData)
    verifyBlock()
  }

  private fun <T> checkEventSuccess(
    liveData: LiveData<T>,
    expectedSuccess: T? = null,
    collectedData: MutableList<T>,
  ) {
    expectedSuccess?.let { expected ->
      assertFalse(collectedData.isEmpty())
      val actual = collectedData.last()

      // special handling for Event objects
      if (expected is Event<*> && actual is Event<*>) {
        assertEquals(actual.getContentIfNotHandled(), expected.getContentIfNotHandled())
        val currentValue = liveData.value as Event<*>
        assertEquals(currentValue.peekContent(), expected.peekContent())
      } else if (actual is Event<*>) {
        assertEquals(actual.getContentIfNotHandled(), expected)
        val currentValue = liveData.value as Event<*>
        assertEquals(currentValue.peekContent(), expected)
      } else {
        assertThat(collectedData).containsExactly(expected)
        assertEquals(liveData.value, expected)
      }
    } ?: assertTrue(collectedData.isEmpty())
  }
}
