package com.waffiq.bazz_movies.feature.detail.ui

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.NOT_AVAILABLE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.feature.detail.R.id.iv_picture_backdrop
import com.waffiq.bazz_movies.feature.detail.testutils.basetest.BaseMediaDetailActivityTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

/**
 * Instrumented test for [MediaDetailActivity] that checks the visibility of UI elements
 * and the correct display of media details based on various conditions.
 */
@HiltAndroidTest
class MediaDetailActivityBackdropTest : BaseMediaDetailActivityTest() {

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
      ),
    ) {
      backdropIsDisplayed()
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
      ),
    ) {
      backdropIsDisplayed()
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
      ),
    ) {
      backdropIsDisplayed()
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
      ),
    ) {
      backdropIsDisplayed()
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
      ),
    ) {
      backdropIsDisplayed()
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
      ),
    ) {
      backdropIsDisplayed()
    }

    context.launchMediaDetailActivity(
      data = MediaItem(
        originalTitle = "original title",
        mediaType = TV_MEDIA_TYPE,
        backdropPath = "",
        posterPath = null,
      ),
    ) {
      backdropIsDisplayed()
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
      ),
    ) {
      backdropIsDisplayed()
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
      ),
    ) {
      backdropIsDisplayed()
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
      ),
    ) {
      backdropIsDisplayed()
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
      ),
    ) {
      backdropIsDisplayed()
    }
  }

  private fun backdropIsDisplayed() {
    iv_picture_backdrop.isDisplayed()
  }
}
