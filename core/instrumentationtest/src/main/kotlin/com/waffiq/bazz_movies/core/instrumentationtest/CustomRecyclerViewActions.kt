package com.waffiq.bazz_movies.core.instrumentationtest

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId

object CustomRecyclerViewActions {

  fun Int.scrollToPosition(position: Int) {
    onView(withId(this)).perform(
      RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position),
    )
  }

  fun Int.clickItemAt(position: Int) {
    onView(withId(this)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()),
    )
  }

  fun Int.actionOnItemAt(position: Int, action: ViewAction) {
    onView(withId(this)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, action),
    )
  }
}
