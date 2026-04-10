package com.waffiq.bazz_movies.core.instrumentationtest

import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist as notExist
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

  fun Int.isNotDisplayed() {
    onView(withId(this)).check(matches(not(isViewDisplayed())))
  }

  fun Int.isDisplayed() {
    onView(withId(this)).check(matches(isViewDisplayed()))
  }

  fun Int.view(): ViewInteraction = onView(withId(this))

  fun Int.hasText(text: String) {
    view().check(matches(withText(text))).check(matches(isViewDisplayed()))
  }
}
