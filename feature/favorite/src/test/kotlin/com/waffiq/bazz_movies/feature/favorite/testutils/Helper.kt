package com.waffiq.bazz_movies.feature.favorite.testutils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.differ
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Helper object for testing PagingData flows and ViewModel LiveData events.
 * Provides utility functions to test PagingData and LiveData events in a structured way.
 */
object Helper {

  /**
   * Test a PagingData flow and assert the expected items.
   *
   * @param flow The PagingData flow to test.
   * @param expectedAssertions A lambda that takes the list of items and performs assertions.
   */
  suspend fun <T : Any> testPagingFlow(
    flow: Flow<PagingData<T>>,
    differ: AsyncPagingDataDiffer<T> = differ(),
    expectedAssertions: (List<T>) -> Unit,
  ) {

    flow.test {
      val actualPagingData = awaitItem()
      val job = launch { differ.submitData(actualPagingData) }
      job.join()
      yield()

      val pagingList = differ.snapshot().items
      expectedAssertions(pagingList)

      job.cancel()
      awaitComplete()
    }
  }

  /**
   * Test a PagingData flow with a custom dispatcher and assert the expected items.
   *
   * @param flow The PagingData flow to test.
   * @param expectedAssertions A lambda that takes the list of items and performs assertions.
   */
  suspend fun testPagingFlowCustomDispatcher(
    flow: Flow<PagingData<MediaItem>>,
    differ: AsyncPagingDataDiffer<MediaItem> = differ(),
    expectedAssertions: (List<MediaItem>) -> Unit,
  ) {

    flow.test {
      val actualPagingData = awaitItem()
      val job = launch { differ.submitData(actualPagingData) }
      job.join()
      yield()

      val pagingList = differ.snapshot().items
      expectedAssertions(pagingList)

      job.cancel()
      cancelAndIgnoreRemainingEvents()
    }
  }

  /**
   * Helper function to test ViewModel LiveData events.
   * It runs a block of code, collects the LiveData events, and verifies the expected outcome.
   *
   * @param runBlock The block of code to execute that triggers the LiveData event.
   * @param liveData The LiveData to observe for events.
   * @param expected The expected event data to verify against collected data.
   * @param verifyBlock Additional verification logic after the event is collected.
   */
  fun <T : Any> testViewModelFlowEvent(
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

  fun <T> checkEventSuccess(
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
