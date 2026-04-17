package com.waffiq.bazz_movies.core.instrumentationtest

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

object CustomVisibilityMatchers {

  // visibility can handle behind view, like on collapse toolbar
  fun Int.isVisible() {
    onView(withId(this))
      .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
  }

  fun String.isVisible() {
    onView(withText(this))
      .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
  }

  fun Int.isTextVisible() {
    onView(withText(this))
      .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
  }

  fun Int.isGone() {
    onView(withId(this))
      .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
  }
}
