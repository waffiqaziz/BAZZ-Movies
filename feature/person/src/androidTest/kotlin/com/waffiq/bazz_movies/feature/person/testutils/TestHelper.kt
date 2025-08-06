package com.waffiq.bazz_movies.feature.person.testutils

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.matcher.BoundedMatcher
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.waffiq.bazz_movies.core.domain.MediaCastItem
import com.waffiq.bazz_movies.feature.person.testutils.DataDumpTest.testMediaCastItem
import com.waffiq.bazz_movies.feature.person.ui.PersonActivity
import org.hamcrest.Description
import org.hamcrest.Matcher

/** * Helper functions and matchers for testing [PersonActivity].
 *
 * Provides utility methods to simplify UI tests, such as checking if a
 * [SwipeRefreshLayout] is refreshing and verifying the title of a
 * [CollapsingToolbarLayout].
 */
object TestHelper {

  /**
   * Returns a matcher that checks whether a [SwipeRefreshLayout] is currently refreshing.
   *
   * Use this to verify that the loading spinner is visible during swipe-to-refresh.
   */
  fun isRefreshing(): Matcher<View> {
    return object : BoundedMatcher<View, SwipeRefreshLayout>(SwipeRefreshLayout::class.java) {
      override fun describeTo(description: Description?) {
        description?.appendText("is refreshing")
      }

      override fun matchesSafely(view: SwipeRefreshLayout?): Boolean =
        view?.isRefreshing == true
    }
  }

  /**
   * Returns a matcher that checks if a [CollapsingToolbarLayout] has the given [expectedTitle].
   *
   * Used for asserting toolbar titles in Espresso UI tests.
   *
   * @param expectedTitle The text  expect to see.
   * @return A [Matcher] that matches the [CollapsingToolbarLayout] with that title.
   */
  fun withCollapsingToolbarTitle(expectedTitle: String?): Matcher<View> {
    return object :
      BoundedMatcher<View, CollapsingToolbarLayout>(CollapsingToolbarLayout::class.java) {
      override fun describeTo(description: Description) {
        description.appendText("with CollapsingToolbarLayout title: ${expectedTitle ?: "null title"}")
      }

      override fun matchesSafely(view: CollapsingToolbarLayout): Boolean =
        view.title?.toString() == expectedTitle
    }
  }

  /**
   * Starts [PersonActivity] with a test [person] and runs the given [block] while it's active.
   *
   * Used to launch [PersonActivity] for UI tests.
   *
   * @param person The person to show in the activity (default is [testMediaCastItem]).
   * @param block What to do with the launched [PersonActivity].
   */
  inline fun Context.launchPersonActivity(
    person: MediaCastItem? = testMediaCastItem,
    block: (ActivityScenario<PersonActivity>) -> Unit,
  ) {
    val intent = Intent(this, PersonActivity::class.java).apply {
      if (person != null) {
        putExtra(PersonActivity.EXTRA_PERSON, person)
      }
    }

    ActivityScenario.launch<PersonActivity>(intent).use { scenario ->
      block(scenario)
    }
  }
}
