package com.waffiq.bazz_movies.core.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.Description
import org.junit.runners.model.Statement
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.assertFailsWith

class MainCoroutineRuleTest {

  private val description = Description.createTestDescription(
    MainCoroutineRuleTest::class.java,
    "test",
  )

  private fun MainCoroutineRule.runWithRule(block: () -> Unit) {
    apply(
      object : Statement() {
        override fun evaluate() = block()
      },
      description,
    ).evaluate()
  }

  @Test
  fun runTest_whenUsingMainCoroutineRule_shouldSetMainDispatcherDuringExecution() {
    val rule = MainCoroutineRule()

    rule.runWithRule {
      runTest(rule.dispatcher) {
        var executed = false
        withContext(Dispatchers.Main) { executed = true }
        advanceUntilIdle()
        assertTrue(executed)
      }
    }
  }

  @Test
  fun mainDispatcher_afterRuleFinished_shouldThrowIllegalStateException() {
    val rule = MainCoroutineRule()

    rule.runWithRule { }

    assertFailsWith<IllegalStateException> {
      Dispatchers.Main.isDispatchNeeded(EmptyCoroutineContext)
    }
  }

  @Test
  fun mainDispatcher_whenRuleThrows_shouldStillReset() {
    val rule = MainCoroutineRule()

    runCatching {
      rule.runWithRule { throw RuntimeException("simulated test failure") }
    }

    // Despite the exception, Main must still be reset
    assertFailsWith<IllegalStateException> {
      Dispatchers.Main.isDispatchNeeded(EmptyCoroutineContext)
    }
  }

  @Test
  fun scope_whenInitialized_shouldUseSameDispatcherAsRule() {
    val testDispatcher = StandardTestDispatcher()
    val rule = MainCoroutineRule(dispatcher = testDispatcher)

    assertSame(testDispatcher, rule.dispatcher)
    assertEquals(testDispatcher, rule.scope.coroutineContext[ContinuationInterceptor])
  }
}
