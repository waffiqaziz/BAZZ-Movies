package com.waffiq.bazz_movies.feature.favorite.testutils

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher

object Helper {
  fun withCustomConstraints(
    action: ViewAction,
    constraints: Matcher<View>,
  ): ViewAction {
    return object : ViewAction {
      override fun getConstraints(): Matcher<View> = constraints
      override fun getDescription(): String = action.description
      override fun perform(uiController: UiController, view: View) {
        action.perform(uiController, view)
      }
    }
  }

  // created helper instance each test class
  fun favoriteHelperFactory(): FavoriteFragmentTestHelper =
    DefaultFavoriteFragmentTestHelper()
}
