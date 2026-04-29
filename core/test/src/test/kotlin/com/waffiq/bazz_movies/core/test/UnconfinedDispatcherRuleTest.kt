package com.waffiq.bazz_movies.core.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.Description
import org.junit.runners.model.Statement
import kotlin.test.assertFailsWith

class UnconfinedDispatcherRuleTest {

  private val description: Description = Description.createTestDescription(
    UnconfinedDispatcherRuleTest::class.java,
    "test",
  )

  private fun UnconfinedDispatcherRule.runWithRule(block: () -> Unit) {
    apply(
      object : Statement() {
        override fun evaluate() = block()
      },
      description,
    ).evaluate()
  }

  @Test
  fun mainDispatcher_withoutAdvanceUntilIdle_shouldExecuteCoroutineImmediately() {
    val rule = UnconfinedDispatcherRule()

    rule.runWithRule {
      var executed = false

      CoroutineScope(Dispatchers.Main).launch {
        executed = true
      }

      // With UnconfinedTestDispatcher, no need to advance until idle
      assertTrue("Expected coroutine to run eagerly on Main", executed)
    }
  }

  @Test
  fun mainDispatcher_afterRuleReset_shouldThrowIllegalStateException() {
    val rule = UnconfinedDispatcherRule()

    rule.runWithRule { /* test runs and finishes */ }

    // After reset, launching on Main with no UI context should throw
    assertFailsWith<IllegalStateException> {
      CoroutineScope(Dispatchers.Main).launch { }
    }
  }
}
