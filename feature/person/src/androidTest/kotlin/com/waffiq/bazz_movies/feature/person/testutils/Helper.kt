package com.waffiq.bazz_movies.feature.person.testutils

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.matcher.BoundedMatcher
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.waffiq.bazz_movies.core.domain.MediaCastItem
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ProfilesItem
import com.waffiq.bazz_movies.feature.person.ui.PersonActivity
import org.hamcrest.Description
import org.hamcrest.Matcher

object Helper {

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

      override fun matchesSafely(view: SwipeRefreshLayout?): Boolean {
        return view?.isRefreshing == true
      }
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
        description.appendText("with CollapsingToolbarLayout title: $expectedTitle")
      }

      override fun matchesSafely(view: CollapsingToolbarLayout): Boolean {
        return view.title?.toString() == expectedTitle
      }
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
    person: MediaCastItem = testMediaCastItem,
    block: (ActivityScenario<PersonActivity>) -> Unit
  ) {
    val intent = Intent(this, PersonActivity::class.java).apply {
      putExtra(PersonActivity.EXTRA_PERSON, person)
    }

    ActivityScenario.launch<PersonActivity>(intent).use { scenario ->
      block(scenario)
    }
  }

  // helper methods to create test data
  val testMediaCastItem =
    MediaCastItem(
      id = 123,
      name = "Test Actor",
      originalName = "Test Actor Original",
      profilePath = "/test_profile.jpg",
    )


  val testDetailPerson =
    DetailPerson(
      id = 123,
      name = "Test Actor",
      biography = "Test biography for the actor",
      birthday = "1990-01-01",
      placeOfBirth = "Test City",
      homepage = "https://example.com"
    )


  val testCastItem =
    CastItem(
      id = 456,
      title = "Test Movie",
      character = "Test Character",
      posterPath = "/test_poster.jpg",
      releaseDate = "2023-01-01"
    )


  val testProfileItem =
    ProfilesItem(
      filePath = "/test_image.jpg",
      width = 500,
      height = 750
    )


  val testExternalIDPerson =
    ExternalIDPerson(
      id = 123,
      imdbId = "nm1234567",
      instagramId = "test_instagram",
      twitterId = "test_twitter",
      facebookId = "test_facebook",
      tiktokId = null,
      youtubeId = null,
      wikidataId = "Q123456"
    )


  val testKnownForList =
    listOf(
      CastItem(
        id = 1,
        title = "Test Movie",
        character = "Test Character",
        posterPath = "/test.jpg"
      )
    )


  val testImagesList =
    listOf(
      ProfilesItem(
        filePath = "/test1.jpg",
        width = 300,
        height = 450
      )
    )
}
