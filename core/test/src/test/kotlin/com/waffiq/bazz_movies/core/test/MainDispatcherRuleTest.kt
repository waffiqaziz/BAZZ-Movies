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
import org.junit.runner.Description
import org.junit.runners.model.Statement
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

  @Test
  fun mainDispatcherManual_afterRuleFinished_shouldThrowIllegalStateException() {
    val isolatedRule = MainDispatcherRule(StandardTestDispatcher())

    // manual use rule full lifecycle via JUnit Statement API
    isolatedRule.apply(
      object : Statement() {
        override fun evaluate() { /* just test  */
        }
      },
      Description.EMPTY
    ).evaluate()

    // Dispatchers.Main it should reset by the rule finished()
    assertFailsWith<IllegalStateException> {
      runBlocking(Dispatchers.Main) { }
    }
  }

  @Test
  fun mainDispatcher_shouldUseTestDispatcher_whenRuleActive() {
    val testDispatcher = StandardTestDispatcher()
    val rule = MainDispatcherRule(testDispatcher)

    var executed = false

    rule.apply(
      object : Statement() {
        override fun evaluate() {
          CoroutineScope(Dispatchers.Main).launch {
            executed = true
          }

          testDispatcher.scheduler.advanceUntilIdle()
          assertTrue(executed)
        }
      },
      Description.EMPTY
    ).evaluate()
  }

  @Test
  fun mainDispatcher_afterRuleFinished_shouldThrow() {
    val testDispatcher = StandardTestDispatcher()
    val isolatedRule = MainDispatcherRule(testDispatcher)

    isolatedRule.apply(
      object : Statement() {
        override fun evaluate() { /* no-op */
        }
      },
      Description.EMPTY
    ).evaluate()

    assertFailsWith<IllegalStateException> {
      Dispatchers.Main.immediate
    }
  }
}
