package com.waffiq.bazz_movies.feature.detail.ui

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.NOT_AVAILABLE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.detail.R.id.iv_picture_backdrop
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestHelper
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestSetup
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test for [MediaDetailActivity] that checks the visibility of UI elements
 * and the correct display of media details based on various conditions.
 */
@HiltAndroidTest
class MediaDetailActivityBackdropTest :
  MediaDetailActivityTestSetup by MediaDetailActivityTestHelper() {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @BindValue
  @JvmField
  val mockNavigator: INavigator = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockMediaDetailViewModel: MediaDetailViewModel = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockPrefViewModel: DetailUserPrefViewModel = mockk(relaxed = true)

  @Before
  fun setup() {
    hiltRule.inject()
    setupMocks()
    Intents.init()
    initializeTest(ApplicationProvider.getApplicationContext())
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  private fun setupMocks() {
    setupBaseMocks()
    setupObservables(mockMediaDetailViewModel)
    setupPreferencesViewModelMocks(mockPrefViewModel)
    setupMediaDetailViewModelMocks(mockMediaDetailViewModel)
    setupNavigatorMocks(mockNavigator)
  }

  @Test
  fun mediaItemValue_withNullValue_showsViewsCorrectly() {
    context.launchMediaDetailActivity(
      data = MediaItem(
        originalName = "original name",
        mediaType = MOVIE_MEDIA_TYPE,
        backdropPath = null,
        posterPath = null,
        releaseDate = null,
        firstAirDate = null,
        overview = null,
      )
    ) {
      onView(withId(iv_picture_backdrop)).check(matches(isDisplayed()))
    }
  }

  @Test
  fun mediaItemValue_withEmptyValue_showsViewsCorrectly() {
    context.launchMediaDetailActivity(
      data = MediaItem(
        title = "title",
        mediaType = TV_MEDIA_TYPE,
        backdropPath = "",
        posterPath = "",
        releaseDate = "",
        firstAirDate = "",
        overview = "",
      )
    ) {
      onView(withId(iv_picture_backdrop)).check(matches(isDisplayed()))
    }
  }

  @Test
  fun mediaItemValue_withNotAvailableValue_showsViewsCorrectly() {
    context.launchMediaDetailActivity(
      data = MediaItem(
        originalTitle = "original title",
        mediaType = TV_MEDIA_TYPE,
        backdropPath = NOT_AVAILABLE,
        posterPath = NOT_AVAILABLE,
        releaseDate = null,
        firstAirDate = "2025-07-09",
        overview = "overview",
      )
    ) {
      onView(withId(iv_picture_backdrop)).check(matches(isDisplayed()))
    }
  }

  @Test
  fun mediaItemValue_withOneNotAvailableValue_showsViewsCorrectly() {
    context.launchMediaDetailActivity(
      data = MediaItem(
        originalTitle = "original title",
        mediaType = MOVIE_MEDIA_TYPE,
        backdropPath = NOT_AVAILABLE,
        posterPath = null,
        releaseDate = "2025-07-09",
        overview = "overview",
      )
    ) {
      onView(withId(iv_picture_backdrop)).check(matches(isDisplayed()))
    }
  }

  @Test
  fun mediaItemValue_withOneNullAvailableValue_showsViewsCorrectly() {
    context.launchMediaDetailActivity(
      data = MediaItem(
        originalTitle = "original title",
        mediaType = MOVIE_MEDIA_TYPE,
        backdropPath = null,
        posterPath = NOT_AVAILABLE,
        releaseDate = "2025-07-09",
        overview = "overview",
      )
    ) {
      onView(withId(iv_picture_backdrop)).check(matches(isDisplayed()))
    }
  }

  @Test
  fun mediaItemValue_withMixedValue_showsViewsCorrectly() {
    context.launchMediaDetailActivity(
      data = MediaItem(
        originalTitle = "original title",
        mediaType = TV_MEDIA_TYPE,
        backdropPath = null,
        posterPath = "",
      )
    ) {
      onView(withId(iv_picture_backdrop)).check(matches(isDisplayed()))
    }

    context.launchMediaDetailActivity(
      data = MediaItem(
        originalTitle = "original title",
        mediaType = TV_MEDIA_TYPE,
        backdropPath = "",
        posterPath = null,
      )
    ) {
      onView(withId(iv_picture_backdrop)).check(matches(isDisplayed()))
    }
  }

  @Test
  fun mediaItemValue_withValidBackdropPath_showsViewsCorrectly() {
    context.launchMediaDetailActivity(
      data = MediaItem(
        originalTitle = "original title",
        mediaType = MOVIE_MEDIA_TYPE,
        backdropPath = "/valid_backdrop_path.jpg",
        posterPath = NOT_AVAILABLE,
        releaseDate = "2025-07-09",
        overview = "overview",
      )
    ) {
      onView(withId(iv_picture_backdrop)).check(matches(isDisplayed()))
    }
  }

  @Test
  fun mediaItemValue_withValidPosterPathOnly_showsViewsCorrectly() {
    context.launchMediaDetailActivity(
      data = MediaItem(
        originalTitle = "original title",
        mediaType = MOVIE_MEDIA_TYPE,
        backdropPath = null,
        posterPath = "/valid_poster_path.jpg",
        releaseDate = "2025-07-09",
        overview = "overview",
      )
    ) {
      onView(withId(iv_picture_backdrop)).check(matches(isDisplayed()))
    }
  }

  @Test
  fun mediaItemValue_withInvalidPosterPath_showsViewsCorrectly() {
    context.launchMediaDetailActivity(
      data = MediaItem(
        originalTitle = "original title",
        mediaType = MOVIE_MEDIA_TYPE,
        backdropPath = "",
        posterPath = null,
        releaseDate = "2025-07-09",
        overview = "overview",
      )
    ) {
      onView(withId(iv_picture_backdrop)).check(matches(isDisplayed()))
    }
  }

  @Test
  fun mediaItemValue_withEmptyPosterPath_showsViewsCorrectly() {
    context.launchMediaDetailActivity(
      data = MediaItem(
        originalTitle = "original title",
        mediaType = MOVIE_MEDIA_TYPE,
        backdropPath = null,
        posterPath = "",
        releaseDate = "2025-07-09",
        overview = "overview",
      )
    ) {
      onView(withId(iv_picture_backdrop)).check(matches(isDisplayed()))
    }
  }
}
