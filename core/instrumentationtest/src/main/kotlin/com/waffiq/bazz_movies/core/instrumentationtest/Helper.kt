package com.waffiq.bazz_movies.core.instrumentationtest

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import org.hamcrest.Matcher

object Helper {

  private const val DELAY_TIME = 50L
  private const val DELAY_TIME_UI = 300L

  /**
   * Used to verifying that the activity finishes properly, especially after calling `finish()`
   * or testing navigation behavior that should close the screen.
   *
   * Continuously polls the activity's lifecycle state until it becomes `DESTROYED` or the timeout is reached.
   * If the timeout passes without reaching the state, throws an [AssertionError].
   *
   * @param timeoutMillis Maximum time to wait before failing, in milliseconds (default is 500ms).
   * @throws AssertionError if the activity is not destroyed within [timeoutMillis].
   */
  fun ActivityScenario<*>.waitForActivityToBeDestroyed(timeoutMillis: Long = 500) {
    val startTime = System.currentTimeMillis()
    while (state != Lifecycle.State.DESTROYED) {
      if (System.currentTimeMillis() - startTime > timeoutMillis) {
        throw AssertionError("Activity did not reach DESTROYED state in time")
      }
      Thread.sleep(DELAY_TIME)
    }
  }

  fun waitFor(millis: Long = 1000): ViewAction {
    return object : ViewAction {
      override fun getConstraints(): Matcher<View> = isRoot()
      override fun getDescription(): String = "Wait for $millis milliseconds"
      override fun perform(uiController: UiController, view: View) {
        uiController.loopMainThreadForAtLeast(millis)
      }
    }
  }

  fun waitUntil(
    matcher: Matcher<View>,
    timeout: Long = 5000,
  ): ViewAssertion {
    return object : ViewAssertion {
      override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        val endTime = System.currentTimeMillis() + timeout
        do {
          try {
            if (matcher.matches(view)) return
          } catch (_: Throwable) {
            /* do nothing */
          }
          Thread.sleep(DELAY_TIME)
        } while (System.currentTimeMillis() < endTime)
        throw AssertionError("View did not match $matcher within $timeout ms")
      }
    }
  }

  fun shortDelay() {
    onView(isRoot()).perform(waitFor(DELAY_TIME_UI))
  }

  fun waitUntilVisible(matcher: Matcher<View>, timeoutMs: Long = 5000) {
    val endTime = System.currentTimeMillis() + timeoutMs

    while (System.currentTimeMillis() < endTime) {
      try {
        onView(matcher).check(matches(isDisplayed()))
        return
      } catch (_: Throwable) {
        Thread.sleep(DELAY_TIME)
      }
    }

    onView(matcher).check(matches(isDisplayed()))
  }
}
