package com.waffiq.bazz_movies.core.instrumentationtest

import android.view.View
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import org.hamcrest.Matcher

object Helper {

  fun waitFor(millis: Long): ViewAction {
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
    timeout: Long = 2000
  ): ViewAssertion {
    return object : ViewAssertion {
      override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        val endTime = System.currentTimeMillis() + timeout
        do {
          try {
            if (matcher.matches(view)) return
          } catch (_: Throwable) {}
          Thread.sleep(50)
        } while (System.currentTimeMillis() < endTime)
        throw AssertionError("View did not match $matcher within $timeout ms")
      }
    }
  }
}
