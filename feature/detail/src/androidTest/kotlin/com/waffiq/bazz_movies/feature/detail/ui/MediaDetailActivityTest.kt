package com.waffiq.bazz_movies.feature.detail.ui

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.NOT_AVAILABLE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.waitFor
import com.waffiq.bazz_movies.feature.detail.R.id.btn_back
import com.waffiq.bazz_movies.feature.detail.R.id.btn_favorite
import com.waffiq.bazz_movies.feature.detail.R.id.btn_watchlist
import com.waffiq.bazz_movies.feature.detail.R.id.iv_picture_backdrop
import com.waffiq.bazz_movies.feature.detail.R.id.rv_cast
import com.waffiq.bazz_movies.feature.detail.R.id.tv_age_rating
import com.waffiq.bazz_movies.feature.detail.R.id.tv_duration
import com.waffiq.bazz_movies.feature.detail.R.id.tv_genre
import com.waffiq.bazz_movies.feature.detail.R.id.tv_score_imdb
import com.waffiq.bazz_movies.feature.detail.R.id.tv_score_metascore
import com.waffiq.bazz_movies.feature.detail.R.id.tv_score_rotten_tomatoes
import com.waffiq.bazz_movies.feature.detail.R.id.tv_score_tmdb
import com.waffiq.bazz_movies.feature.detail.R.id.tv_year_released
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.RatingsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaCredits
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaDetail
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testOMDbDetails
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestHelper
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestSetup
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test for [MediaDetailActivity] that checks the visibility of UI elements
 * and the correct display of media details based on various conditions.
 */
@HiltAndroidTest
class MediaDetailActivityTest :
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
  fun detailScreen_whenAllDataProvided_showsAllViews() = runTest {
    context.launchMediaDetailActivity {
      onView(withId(btn_back)).check(matches(isDisplayed()))
      onView(withId(btn_watchlist)).check(matches(isDisplayed()))
      onView(withId(btn_favorite)).check(matches(isDisplayed()))
      advanceUntilIdle()
    }
  }

  @Test
  fun imdbId_withValidValue_shouldHandleAllPossibility() {
    context.launchMediaDetailActivity {
      detailMedia.postValue(testMediaDetail.copy(imdbId = "tt1234567"))
      waitFor(500)
    }
  }

  @Test
  fun imdbId_withNullValue_shouldHandleAllPossibility() {
    context.launchMediaDetailActivity {
      detailMedia.postValue(testMediaDetail.copy(imdbId = null))
      waitFor(500)
    }
  }

  @Test
  fun imdbId_withEmptyValue_shouldHandleAllPossibility() {
    context.launchMediaDetailActivity {
      detailMedia.postValue(testMediaDetail.copy(imdbId = ""))
      waitFor(500)
    }
  }

  @Test
  fun imdbId_withBlankValue_shouldHandleAllPossibility() {
    context.launchMediaDetailActivity {
      detailMedia.postValue(testMediaDetail.copy(imdbId = " "))
      waitFor(500)
    }
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
  fun mediaDetailValue_withMixedValue_showsViewsCorrectly() {
    // genre null
    context.launchMediaDetailActivity {
      detailMedia.postValue(testMediaDetail.copy(genre = null))
      onView(withId(tv_genre)).check(matches(withText(context.getString(not_available))))
        .check(matches(isDisplayed()))
    }

    // movie duration null
    context.launchMediaDetailActivity {
      detailMedia.postValue(testMediaDetail.copy(duration = null))
      onView(withId(tv_duration)).check(matches(withText(context.getString(not_available))))
        .check(matches(isDisplayed()))
    }

    // tv duration null or empty
    context.launchMediaDetailActivity(data = testMediaItem.copy(mediaType = TV_MEDIA_TYPE)) {
      detailMedia.postValue(testMediaDetail.copy(duration = null))
      onView(withId(tv_duration)).check(matches(withText(context.getString(not_available))))
        .check(matches(isDisplayed()))

      detailMedia.postValue(testMediaDetail.copy(duration = ""))
      onView(withId(tv_duration)).check(matches(withText(context.getString(not_available))))
        .check(matches(isDisplayed()))
    }

    // tmdb score null or empty
    context.launchMediaDetailActivity {
      detailMedia.postValue(testMediaDetail.copy(tmdbScore = null))
      onView(withId(tv_score_tmdb)).check(matches(withText(context.getString(not_available))))
        .check(matches(isDisplayed()))

      detailMedia.postValue(testMediaDetail.copy(tmdbScore = ""))
      onView(withId(tv_score_tmdb)).check(matches(withText("")))
        .check(matches(not(isDisplayed())))
    }
  }

  @Test
  fun ageRatingValue_withMixedValue_showsAgeViewsCorrectly() {
    // age rating null
    context.launchMediaDetailActivity {
      detailMedia.postValue(testMediaDetail.copy(ageRating = null))
      onView(withId(tv_age_rating)).check(matches(not(isDisplayed())))
    }

    // age rating empty
    context.launchMediaDetailActivity {
      detailMedia.postValue(testMediaDetail.copy(ageRating = ""))
      onView(withId(tv_age_rating)).check(matches(not(isDisplayed())))
    }
  }

  @Test
  fun releaseDateValue_withEmptyValue_showsReleaseDateCorrectly() {
    // release date empty
    context.launchMediaDetailActivity {
      detailMedia.postValue(
        testMediaDetail.copy(
          releaseDateRegion = ReleaseDateRegion(regionRelease = "", releaseDate = "")
        )
      )
      onView(withId(tv_year_released)).check(matches(not(isDisplayed())))
    }
  }

  @Test
  fun creditsValue_withEmptyValue_showsReleaseDateCorrectly() {
    // release date empty
    context.launchMediaDetailActivity {
      mediaCredits.postValue(
        testMediaCredits.copy(crew = emptyList(), cast = emptyList())
      )
      onView(withId(rv_cast)).check(matches(not(isDisplayed())))
    }
  }

  @Test
  fun omdbScoreValue_withEmptyValue_showsOMDbScoreCorrectly() {
    // omdb score empty
    context.launchMediaDetailActivity {
      omdbResult.postValue(
        testOMDbDetails.copy(
          imdbRating = "",
          metascore = "",
          ratings = null
        )
      )
      onView(withId(tv_score_imdb))
        .check(matches(withText(context.getString(not_available))))
      onView(withId(tv_score_metascore))
        .check(matches(withText(context.getString(not_available))))
      onView(withId(tv_score_rotten_tomatoes))
        .check(matches(withText(context.getString(not_available))))
    }
  }

  @Test
  fun omdbScoreValue_withNullValue_showsOMDbScoreCorrectly() {
    context.launchMediaDetailActivity {
      omdbResult.postValue(
        testOMDbDetails.copy(
          imdbRating = null,
          metascore = null,
          ratings = null
        )
      )
      onView(withId(tv_score_imdb))
        .check(matches(withText(context.getString(not_available))))
      onView(withId(tv_score_metascore))
        .check(matches(withText(context.getString(not_available))))
      onView(withId(tv_score_rotten_tomatoes))
        .check(matches(withText(context.getString(not_available))))
    }
  }

  @Test
  fun omdbScoreValue_withEmptyRatings_showsOMDbScoreCorrectly() {
    context.launchMediaDetailActivity {
      omdbResult.postValue(
        testOMDbDetails.copy(
          imdbRating = null,
          metascore = null,
          ratings = emptyList()
        )
      )
      onView(withId(tv_score_imdb))
        .check(matches(withText(context.getString(not_available))))
      onView(withId(tv_score_metascore))
        .check(matches(withText(context.getString(not_available))))
      onView(withId(tv_score_rotten_tomatoes))
        .check(matches(withText(context.getString(not_available))))
    }
  }

  @Test
  fun omdbScoreValue_withValidRottenTomatoes_showsOMDbScoreCorrectly() {
    context.launchMediaDetailActivity {
      omdbResult.postValue(
        testOMDbDetails.copy(
          imdbRating = "",
          metascore = "",
          ratings = listOf(RatingsItem(source = "Rotten Tomatoes", value = "90%"))
        )
      )
      onView(withId(tv_score_rotten_tomatoes)).check(matches((isDisplayed())))
        .check(matches(withText("90%")))
    }
  }

  @Test
  fun omdbScoreValue_withRottenTomatoesNullValue_showsOMDbScoreCorrectly() {
    context.launchMediaDetailActivity {
      omdbResult.postValue(
        OMDbDetails(
          ratings = listOf(
            RatingsItem(source = "Rotten Tomatoes", value = null), // null value on rotten tomatoes
          )
        )
      )
      onView(withId(tv_score_rotten_tomatoes))
        .check(matches(withText(context.getString(not_available))))
    }
  }
}
