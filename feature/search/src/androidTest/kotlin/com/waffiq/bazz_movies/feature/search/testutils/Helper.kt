package com.waffiq.bazz_movies.feature.search.testutils

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
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
}
