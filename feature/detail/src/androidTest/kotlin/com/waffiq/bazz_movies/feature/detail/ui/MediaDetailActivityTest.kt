package com.waffiq.bazz_movies.feature.detail.ui

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.feature.detail.R.id.btn_back
import com.waffiq.bazz_movies.feature.detail.R.id.btn_favorite
import com.waffiq.bazz_movies.feature.detail.R.id.btn_watchlist
import com.waffiq.bazz_movies.feature.detail.R.id.iv_poster
import com.waffiq.bazz_movies.feature.detail.R.id.rv_cast
import com.waffiq.bazz_movies.feature.detail.R.id.tv_age_rating
import com.waffiq.bazz_movies.feature.detail.R.id.tv_duration
import com.waffiq.bazz_movies.feature.detail.R.id.tv_genre
import com.waffiq.bazz_movies.feature.detail.R.id.tv_mediaType
import com.waffiq.bazz_movies.feature.detail.R.id.tv_score_tmdb
import com.waffiq.bazz_movies.feature.detail.R.id.tv_title
import com.waffiq.bazz_movies.feature.detail.R.id.tv_year_released
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaCredits
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaDetail
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestHelper
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestSetup
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.abs

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
  fun detailScreen_whenAllDataProvided_showsAllViews() {
    context.launchMediaDetailActivity {
      onView(withId(btn_back)).check(matches(isDisplayed()))
      onView(withId(btn_watchlist)).check(matches(isDisplayed()))
      onView(withId(btn_favorite)).check(matches(isDisplayed()))
    }
  }

  @Test
  fun extractDataFromIntent_returnsFalse_whenExtraMissing() {
    context.launchNullMediaDetailActivity(data = null) { scenario ->
      assertTrue(scenario.state == Lifecycle.State.DESTROYED)
    }
  }

  @Test
  fun imdbId_withValidValue_shouldHandleAllPossibility() {
    context.launchMediaDetailActivity {
      detailMedia.postValue(testMediaDetail.copy(imdbId = "tt1234567"))
      shortDelay()
    }
  }

  @Test
  fun imdbId_withNullValue_shouldHandleAllPossibility() {
    context.launchMediaDetailActivity {
      detailMedia.postValue(testMediaDetail.copy(imdbId = null))
      shortDelay()
    }
  }

  @Test
  fun imdbId_withEmptyValue_shouldHandleAllPossibility() {
    context.launchMediaDetailActivity {
      detailMedia.postValue(testMediaDetail.copy(imdbId = ""))
      shortDelay()
    }
  }

  @Test
  fun imdbId_withBlankValue_shouldHandleAllPossibility() {
    context.launchMediaDetailActivity {
      detailMedia.postValue(testMediaDetail.copy(imdbId = " "))
      shortDelay()
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
  fun mediaItem_withShortTitle_shouldKeepMetadataNextToPoster() {
    val shortTitle = "Short"

    context.launchMediaDetailActivity(
      data = testMediaItem.copy(name = shortTitle)
    ) {
      val posterBottom = getViewBottomPosition(iv_poster)
      val mediaTypeTop = getViewTopPosition(tv_mediaType)

      // MediaType should still be beside/aligned with poster
      assert(mediaTypeTop < posterBottom) {
        "MediaType should be beside poster for short titles"
      }
    }
  }

  @Test
  fun mediaItem_withLongTitle_shouldMoveMetadataUnderPoster() {
    val longTitle =
      "This title should be long enough to trigger the metadata of media item under the media poster"

    context.launchMediaDetailActivity(
      data = testMediaItem.copy(name = longTitle)
    ) {
      onView(withId(tv_title)).check(matches(isDisplayed()))

      // verify title displays the long text
      onView(withId(tv_title)).check(matches(withText(longTitle)))

      // Verify tv_mediaType is positioned BELOW the poster
      val posterBottom = getViewBottomPosition(iv_poster)
      val mediaTypeTop = getViewTopPosition(tv_mediaType)
      assert(mediaTypeTop > posterBottom) {
        "MediaType should be below poster. MediaType top: $mediaTypeTop, Poster bottom: $posterBottom"
      }

      // verify the constraint chain: mediaType -> genre -> duration
      val mediaTypeBottom = getViewBottomPosition(tv_mediaType)
      val genreTop = getViewTopPosition(tv_genre)
      assert(genreTop > mediaTypeBottom) {
        "Genre should be below mediaType"
      }

      val genreBottom = getViewBottomPosition(tv_genre)
      val durationTop = getViewTopPosition(tv_duration)
      assert(durationTop > genreBottom) {
        "Duration should be below genre"
      }

      // verify title is aligned to poster bottom
      val titleBottom = getViewBottomPosition(tv_title)
      assert(abs(titleBottom - posterBottom) < 5) { // 5px tolerance
        "Title bottom should align with poster bottom"
      }
    }
  }

  private fun getViewTopPosition(viewId: Int): Int {
    var top = 0
    onView(withId(viewId)).check { view, _ ->
      val location = IntArray(2)
      view.getLocationOnScreen(location)
      top = location[1]
    }
    return top
  }

  private fun getViewBottomPosition(viewId: Int): Int {
    var bottom = 0
    onView(withId(viewId)).check { view, _ ->
      val location = IntArray(2)
      view.getLocationOnScreen(location)
      bottom = location[1] + view.height
    }
    return bottom
  }
}
