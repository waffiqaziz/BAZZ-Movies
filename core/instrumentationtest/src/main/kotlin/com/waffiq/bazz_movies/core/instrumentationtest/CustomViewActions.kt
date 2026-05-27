package com.waffiq.bazz_movies.core.instrumentationtest

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.MotionEvents
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher

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
    onView(withId(this)).perform(
      object : ViewAction {
        override fun getConstraints(): Matcher<View> = isDisplayingAtLeast(1)

        override fun getDescription(): String = "swipe down ignoring visibility constraints"

        override fun perform(uiController: UiController, view: View) {
          val coords = GeneralLocation.TOP_CENTER.calculateCoordinates(view)
          val endCoords = GeneralLocation.BOTTOM_CENTER.calculateCoordinates(view)

          val downEvent = MotionEvents.sendDown(uiController, coords, floatArrayOf(1f, 1f))
          try {
            MotionEvents.sendMovement(uiController, downEvent.down, endCoords)
            MotionEvents.sendUp(uiController, downEvent.down, endCoords)
          } finally {
            downEvent.down.recycle()
          }
        }
      }
    )
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
