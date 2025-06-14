package com.waffiq.bazz_movies.core.test

import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

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
}
