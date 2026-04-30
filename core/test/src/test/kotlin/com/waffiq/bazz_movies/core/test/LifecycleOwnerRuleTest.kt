package com.waffiq.bazz_movies.core.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.Description
import org.junit.runners.model.Statement

class LifecycleOwnerRuleTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  private val description = Description.createTestDescription(
    LifecycleOwnerRuleTest::class.java,
    "test",
  )

  private fun LifecycleOwnerRule.runWithRule(block: LifecycleOwnerRule.() -> Unit) {
    apply(
      object : Statement() {
        override fun evaluate() = block()
      },
      description,
    ).evaluate()
  }

  @Test
  fun lifecycle_whenRuleIsActive_shouldBeInStartedState() {
    val rule = LifecycleOwnerRule()

    rule.runWithRule {
      assertEquals(Lifecycle.State.STARTED, lifecycleRegistry.currentState)
    }
  }

  @Test
  fun lifecycle_whenRuleFinishes_shouldBeInDestroyedState() {
    val rule = LifecycleOwnerRule()

    rule.runWithRule { }

    assertEquals(Lifecycle.State.DESTROYED, rule.lifecycleRegistry.currentState)
  }

  @Test
  fun lifecycleOwner_shouldReturnSameLifecycleAsRegistry() {
    val rule = LifecycleOwnerRule()

    rule.runWithRule {
      assertSame(lifecycleRegistry, lifecycleOwner.lifecycle)
    }
  }

  @Test
  fun lifecycleObserver_whenRuleStarts_shouldReceiveOnStartEvent() {
    val rule = LifecycleOwnerRule()
    val events = mutableListOf<Lifecycle.Event>()

    rule.runWithRule {
      lifecycleOwner.lifecycle.addObserver(
        LifecycleEventObserver { _, event -> events.add(event) },
      )
    }

    assertTrue(events.contains(Lifecycle.Event.ON_START))
  }

  @Test
  fun lifecycleObserver_whenRuleFinishes_shouldReceiveOnDestroyEvent() {
    val rule = LifecycleOwnerRule()
    val events = mutableListOf<Lifecycle.Event>()

    // Add observer before rule runs so it captures all events
    rule.lifecycleRegistry.addObserver(
      LifecycleEventObserver { _, event -> events.add(event) },
    )

    rule.runWithRule { }

    assertTrue(events.contains(Lifecycle.Event.ON_DESTROY))
  }
}
