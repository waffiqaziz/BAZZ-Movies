package com.waffiq.bazz_movies.core.instrumentationtest

import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.waffiq.bazz_movies.core.instrumentationtest.CustomAssertions.withErrorText
import com.waffiq.bazz_movies.core.instrumentationtest.CustomAssertions.withoutError
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist as notExist
import androidx.test.espresso.matcher.ViewMatchers.isClickable as clickable
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed as isViewDisplayed

@Suppress("TooManyFunctions")
object CustomViewMatchers {
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

  fun ViewInteraction.isDisplayed(): ViewInteraction = check(matches(isViewDisplayed()))

  fun Int.isClickable() {
    onView(withId(this)).check(matches(allOf(isViewDisplayed(), clickable())))
  }

  fun Int.isNotEnable() {
    onView(withId(this)).check(matches(not(isEnabled())))
  }

  fun Int.isEnable() {
    onView(withId(this)).check(matches(isEnabled()))
  }

  fun Int.doesNotHaveText(text: String) {
    onView(withId(this)).check(matches(not(withText(text))))
  }

  fun Int.doesHaveText(text: String) {
    onView(withId(this)).check(matches((isViewDisplayed()))).check(matches(withText(text)))
  }

  fun Int.hasErrorText(text: String) {
    onView(withId(this))
      .check(matches(withErrorText(text)))
  }

  fun Int.hasNoError() {
    onView(withId(this))
      .check(matches(withoutError()))
  }

  fun Int.hasContentDescription(expected: String) {
    onView(withId(this)).check(matches(withContentDescription(expected)))
  }

  fun Int.view(): ViewInteraction = onView(withId(this))

  fun String.view(): ViewInteraction = onView(withText(this))

  fun clickChildViewWithId(id: Int): ViewAction =
    object : ViewAction {
      override fun getConstraints() = null
      override fun getDescription() = "Click on child view with id $id"
      override fun perform(uiController: UiController, view: View) {
        view.findViewById<View>(id).performClick()
      }
    }
}
