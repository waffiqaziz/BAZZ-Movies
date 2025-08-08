package com.waffiq.bazz_movies.feature.search.testutils

import android.util.Log
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

object Helper {

  fun triggerSwipeRefresh(): ViewAction {
    return object : ViewAction {
      override fun getConstraints(): Matcher<View> =
        ViewMatchers.isAssignableFrom(SwipeRefreshLayout::class.java)

      override fun getDescription(): String = "Trigger swipe refresh"

      override fun perform(uiController: UiController, view: View) {
        val swipeRefreshLayout = view as SwipeRefreshLayout
        swipeRefreshLayout.post {
          swipeRefreshLayout.isRefreshing = true

          // use reflection to access the private mListener field
          try {
            val listenerField = SwipeRefreshLayout::class.java.getDeclaredField("mListener")
            listenerField.isAccessible = true
            val listener = listenerField.get(swipeRefreshLayout) as? SwipeRefreshLayout.OnRefreshListener
            listener?.onRefresh()
          } catch (e: Exception) {
            // set isRefreshing, trigger UI updates
            Log.e("triggerSwipeRefresh", e.toString())
            swipeRefreshLayout.isRefreshing = false
            swipeRefreshLayout.isRefreshing = true
          }
        }
        uiController.loopMainThreadUntilIdle()
      }
    }
  }
}
