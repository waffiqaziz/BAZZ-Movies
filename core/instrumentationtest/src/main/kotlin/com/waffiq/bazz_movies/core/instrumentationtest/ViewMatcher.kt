package com.waffiq.bazz_movies.core.instrumentationtest

import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist as notExist
import androidx.test.espresso.matcher.ViewMatchers.isClickable as clickable
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed as isViewDisplayed

object ViewMatcher {
  fun withDrawable(resourceId: Int): Matcher<View> {
    return object : BoundedMatcher<View, ImageView>(ImageView::class.java) {

      override fun describeTo(description: Description) {
        description.appendText("with drawable resource id: $resourceId")
      }

      override fun matchesSafely(imageView: ImageView): Boolean {
        val context = imageView.context
        val expectedDrawable = AppCompatResources.getDrawable(context, resourceId)
        val actualDrawable = imageView.drawable

        if (expectedDrawable == null || actualDrawable == null) return false

        val expectedBitmap = expectedDrawable.toBitmap()
        val actualBitmap = actualDrawable.toBitmap()

        return expectedBitmap.sameAs(actualBitmap)
      }
    }
  }

  fun Int.doesNotExist() {
    onView(withId(this)).check(notExist())
  }

  fun String.doesNotExist() {
    onView(withText(this)).check(notExist())
  }

  fun Int.isNotDisplayed() {
    onView(withId(this)).check(matches(not(isViewDisplayed())))
  }

  fun String.isNotDisplayed() {
    onView(withText(this)).check(matches(not(isViewDisplayed())))
  }

  // displayed should be on top of the view, should be not covered / clipped / off-screen
  fun Int.isDisplayed() {
    onView(withId(this)).check(matches(isViewDisplayed()))
  }

  fun Int.textIsDisplayed() {
    onView(withText(this)).check(matches(isViewDisplayed()))
  }

  fun String.isDisplayed() {
    onView(withText(this)).check(matches(isViewDisplayed()))
  }

  // visibility can handle behind view, like on collapse toolbar
  fun Int.isVisible() {
    onView(withId(this))
      .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
  }

  fun Int.isTextVisible() {
    onView(withText(this))
      .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
  }

  fun String.isVisible() {
    onView(withText(this))
      .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
  }

  fun Int.isGone() {
    onView(withId(this))
      .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
  }

  fun ViewInteraction.isDisplayed(): ViewInteraction =
    check(matches(isViewDisplayed()))

  fun Int.isClickable() {
    onView(withId(this)).check(matches(allOf(isViewDisplayed(), clickable())))
  }

  fun Int.isNotEnable() {
    onView(withId(this)).check(matches(not(isEnabled())))
  }

  fun Int.isEnable() {
    onView(withId(this)).check(matches(isEnabled()))
  }

  fun Int.performClick() {
    onView(withId(this)).perform(click())
  }

  fun String.performClick() {
    onView(withText(this)).perform(click())
  }

  fun Int.performTextClick() {
    onView(withText(this)).perform(click())
  }

  fun Int.performType(text: String) {
    onView(withId(this)).perform(typeText(text))
  }

  fun Int.clickItemAt(position: Int) {
    onView(withId(this)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click())
    )
  }

  fun Int.actionOnItemAt(position: Int, action: ViewAction) {
    onView(withId(this)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, action)
    )
  }

  fun Int.performSwipeUp() {
    onView(withId(this)).perform(swipeUp())
  }

  fun Int.performSwipeDown() {
    onView(withId(this)).perform(swipeDown())
  }

  fun Int.performSwipeLeft() {
    onView(withId(this)).perform(swipeLeft())
  }

  fun Int.performSwipeRight() {
    onView(withId(this)).perform(swipeRight())
  }

  fun Int.performScrollTo() {
    onView(withId(this)).perform(scrollTo())
  }

  fun Int.doesNotHaveText(text: String) {
    onView(withId(this)).check(matches(not(withText(text))))
  }

  fun Int.doesHaveText(text: String) {
    onView(withId(this)).check(matches((isViewDisplayed()))).check(matches(withText(text)))
  }

  fun Int.hasContentDescription(expected: String) {
    onView(withId(this))
      .check(matches(withContentDescription(expected)))
  }

  fun Int.performAction(action: ViewAction) {
    onView(withId(this)).perform(action)
  }

  fun Int.view(): ViewInteraction = onView(withId(this))

  fun String.view(): ViewInteraction = onView(withText(this))

  fun clickChildViewWithId(id: Int): ViewAction {
    return object : ViewAction {
      override fun getConstraints() = null
      override fun getDescription() = "Click on child view with id $id"
      override fun perform(uiController: UiController, view: View) {
        view.findViewById<View>(id).performClick()
      }
    }
  }
}
