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
import com.waffiq.bazz_movies.feature.detail.R.id.rv_cast
import com.waffiq.bazz_movies.feature.detail.R.id.tv_age_rating
import com.waffiq.bazz_movies.feature.detail.R.id.tv_duration
import com.waffiq.bazz_movies.feature.detail.R.id.tv_score_tmdb
import com.waffiq.bazz_movies.feature.detail.R.id.tv_year_released
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaCredits
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaDetail
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestHelper
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestSetup
import com.waffiq.bazz_movies.feature.detail.ui.state.MediaDetailUiState
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.assertTrue
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
  fun metaData_whenInitiateNull_doNothing() {
    // due to base test helper always set initiate to not null,
    // then we set initiate to null for only this case
    every { mockMediaDetailViewModel.uiState } returns MutableStateFlow(MediaDetailUiState())

    context.launchMediaDetailActivity {
      verify(exactly = 0){ mockMediaDetailViewModel.getOMDbDetails(any()) }
    }
  }

  @Test
  fun imdbId_withValidValue_shouldHandleAllPossibility() {
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(imdbId = "tt1234567")) }
      shortDelay()
    }
  }

  @Test
  fun imdbId_withNullValue_shouldHandleAllPossibility() {
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(imdbId = null)) }
      shortDelay()
    }
  }

  @Test
  fun imdbId_withEmptyValue_shouldHandleAllPossibility() {
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(imdbId = "")) }
      shortDelay()
    }
  }

  @Test
  fun imdbId_withBlankValue_shouldHandleAllPossibility() {
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(imdbId = " ")) }
      shortDelay()
    }
  }

  @Test
  fun mediaDetailValue_withMixedValue_showsViewsCorrectly() {
    // genre null
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(genreId = emptyList())) }
    }
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(genreId = null)) }
    }

    // status
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(status = null)) }
    }
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(status = "")) }
    }

    // movie duration null
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(duration = null)) }
      onView(withId(tv_duration)).check(matches(withText(context.getString(not_available))))
        .check(matches(isDisplayed()))
    }

    // tv status null or empty
    context.launchMediaDetailActivity(data = testMediaItem.copy(mediaType = TV_MEDIA_TYPE)) {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(status = null)) }
      onView(withId(tv_duration)).check(matches(withText(context.getString(not_available))))
        .check(matches(isDisplayed()))

      uiState.update { s -> s.copy(detail = testMediaDetail.copy(status = "")) }
      onView(withId(tv_duration)).check(matches(withText(context.getString(not_available))))
        .check(matches(isDisplayed()))
    }

    // tmdb score hidden
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(tmdbScore = null)) }
      onView(withId(tv_score_tmdb)).check(matches(not(isDisplayed())))

      uiState.update { s -> s.copy(detail = testMediaDetail.copy(tmdbScore = "")) }
      onView(withId(tv_score_tmdb)).check(matches(not(isDisplayed())))
    }
  }

  @Test
  fun ageRatingValue_withMixedValue_showsAgeViewsCorrectly() {
    // age rating null
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(ageRating = null)) }
      onView(withId(tv_age_rating)).check(matches(not(isDisplayed())))
    }

    // age rating empty
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(ageRating = "")) }
      onView(withId(tv_age_rating)).check(matches(not(isDisplayed())))
    }
  }

  @Test
  fun releaseDateValue_withEmptyValue_showsReleaseDateCorrectly() {
    // release date empty
    context.launchMediaDetailActivity {
      uiState.update { s ->
        s.copy(
          detail = testMediaDetail.copy(
            releaseDateRegion = ReleaseDateRegion(regionRelease = "", releaseDate = "")
          )
        )
      }
      onView(withId(tv_year_released)).check(matches(not(isDisplayed())))
    }
  }

  @Test
  fun creditsValue_withEmptyValue_showsReleaseDateCorrectly() {
    context.launchMediaDetailActivity {
      uiState.update { s ->
        s.copy(credits = testMediaCredits.copy(crew = emptyList(), cast = emptyList()))
      }
      onView(withId(rv_cast)).check(matches(not(isDisplayed())))
    }
  }
}
