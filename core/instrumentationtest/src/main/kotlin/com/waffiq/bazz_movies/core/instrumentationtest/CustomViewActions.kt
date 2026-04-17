package com.waffiq.bazz_movies.core.instrumentationtest

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

object CustomViewActions {

  fun Int.performClick() {
    onView(withId(this)).perform(click())
  }

  fun String.performClick() {
    onView(withText(this)).perform(click())
  }

  fun Int.performTextClick() {
    onView(withText(this)).perform(click())
  }

  fun Int.performScrollTo() {
    onView(withId(this)).perform(scrollTo())
  }

  fun Int.performSwipeRight() {
    onView(withId(this)).perform(swipeRight())
  }

  fun Int.performSwipeLeft() {
    onView(withId(this)).perform(swipeLeft())
  }

  fun Int.performSwipeUp() {
    onView(withId(this)).perform(swipeUp())
  }

  fun Int.performSwipeDown() {
    onView(withId(this)).perform(swipeDown())
  }

  fun Int.performType(text: String) {
    onView(withId(this)).perform(typeText(text))
  }

  fun Int.replaceWithText(text: String) {
    onView(withId(this)).perform(replaceText(text))
  }

  fun Int.performAction(action: ViewAction) {
    onView(withId(this)).perform(action)
  }
}
