package com.waffiq.bazz_movies.feature.search.testutils

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
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

  fun triggerSwipeRefresh(): ViewAction {
    return object : ViewAction {
      override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isAssignableFrom(SwipeRefreshLayout::class.java)
      }

      override fun getDescription(): String = "Trigger swipe refresh"

      override fun perform(uiController: UiController, view: View) {
        val swipeRefreshLayout = view as SwipeRefreshLayout
        swipeRefreshLayout.post {
          swipeRefreshLayout.isRefreshing = true

          // Use reflection to access the private mListener field
          try {
            val listenerField = SwipeRefreshLayout::class.java.getDeclaredField("mListener")
            listenerField.isAccessible = true
            val listener = listenerField.get(swipeRefreshLayout) as? SwipeRefreshLayout.OnRefreshListener
            listener?.onRefresh()
          } catch (e: Exception) {
            // Fallback - just set isRefreshing which should trigger UI updates
            swipeRefreshLayout.isRefreshing = false
            swipeRefreshLayout.isRefreshing = true
          }
        }
        uiController.loopMainThreadUntilIdle()
      }
    }
  }
}
