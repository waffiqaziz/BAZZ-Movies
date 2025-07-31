package com.waffiq.bazz_movies.feature.detail.testutils

import android.view.View
import android.widget.RatingBar
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

/**
 * Custom ViewAction to set the rating on a [RatingBar].
 *
 * @param rating The rating value to set on the RatingBar.
 */
class SetRatingAction(private val rating: Float) : ViewAction {
  override fun getConstraints(): Matcher<View?>? {
    return ViewMatchers.isAssignableFrom(RatingBar::class.java)
  }

  override fun getDescription(): String {
    return "Set rating on RatingBar to $rating"
  }

  override fun perform(uiController: UiController?, view: View?) {
    val ratingBar = view as RatingBar
    ratingBar.rating = rating
  }
}