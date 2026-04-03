package com.waffiq.bazz_movies.core.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFailsWith

class MainDispatcherRuleTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

  @Test
  fun runTest_whenUsingMainDispatcher_shouldExecuteCoroutineSuccessfully() = runTest {
    var executed = false
    withContext(Dispatchers.Main) { executed = true }
    assertTrue(executed)
  }

  @Test
  fun mainDispatcher_afterRuleFinished_shouldThrowIllegalStateException() {
    Dispatchers.resetMain()

    assertFailsWith<IllegalStateException> {
      runBlocking(Dispatchers.Main) { }
    }
  }

  @Test
  fun runTest_whenUsingStandardTestDispatcher_shouldNotExecuteUntilAdvanced() = runTest {
    var executed = false

    CoroutineScope(Dispatchers.Main).launch {
      executed = true
    }

    // should false, due to StandardTestDispatcher
    assertFalse(executed)

    advanceUntilIdle()
    assertTrue(executed)
  }
}
