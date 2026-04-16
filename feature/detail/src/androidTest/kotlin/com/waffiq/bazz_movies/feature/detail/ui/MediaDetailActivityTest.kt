package com.waffiq.bazz_movies.feature.detail.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.string.add_to_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesHaveText
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesNotExist
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.feature.detail.R.id.btn_back
import com.waffiq.bazz_movies.feature.detail.R.id.btn_favorite
import com.waffiq.bazz_movies.feature.detail.R.id.btn_watchlist
import com.waffiq.bazz_movies.feature.detail.R.id.chip
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
import kotlinx.coroutines.launch
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
      btn_back.isDisplayed()
      btn_watchlist.isDisplayed()
      btn_favorite.isDisplayed()
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
      verify(exactly = 0) { mockMediaDetailViewModel.getOMDbDetails(any()) }
    }
  }

  @Test
  fun toastEvent_whenErrorIsOccurs_callsToastCreation() {
    context.launchMediaDetailActivity {
      it.onActivity { activity ->
        activity.lifecycleScope.launch {
          toastEvent.emit(add_to_favorite)
        }
      }
      // manual verify
    }
  }

  @Test
  fun mediaDetailValue_withMixedValue_showsViewsCorrectly() {
    // genre empty or null, then chip is not exist at all
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(genreId = emptyList())) }
      chip.doesNotExist()
    }
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(genreId = null)) }
      chip.doesNotExist()
    }

    // status
    context.launchMediaDetailActivity(testMediaItem.copy(mediaType = TV_MEDIA_TYPE)) {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(status = null)) }
      tv_duration.doesHaveText(context.getString(not_available))
    }

    context.launchMediaDetailActivity(testMediaItem.copy(mediaType = TV_MEDIA_TYPE)) {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(status = "")) }
      tv_duration.doesHaveText(context.getString(not_available))
    }

    // movie duration null
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(duration = null)) }
      tv_duration.doesHaveText(context.getString(not_available))
    }

    // tv status null or empty
    context.launchMediaDetailActivity(data = testMediaItem.copy(mediaType = TV_MEDIA_TYPE)) {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(status = null)) }
      tv_duration.doesHaveText(context.getString(not_available))

      uiState.update { s -> s.copy(detail = testMediaDetail.copy(status = "")) }
      tv_duration.doesHaveText(context.getString(not_available))
    }

    // tmdb score hidden
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(tmdbScore = null)) }
      tv_score_tmdb.isNotDisplayed()

      uiState.update { s -> s.copy(detail = testMediaDetail.copy(tmdbScore = "")) }
      tv_score_tmdb.isNotDisplayed()
    }
  }

  @Test
  fun ageRatingValue_withMixedValue_showsAgeViewsCorrectly() {
    // age rating null
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(ageRating = null)) }
      tv_age_rating.isNotDisplayed()
    }

    // age rating empty
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(detail = testMediaDetail.copy(ageRating = "")) }
      tv_age_rating.isNotDisplayed()
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
      tv_year_released.isNotDisplayed()
    }
  }

  @Test
  fun creditsValue_withEmptyValue_showsReleaseDateCorrectly() {
    context.launchMediaDetailActivity {
      uiState.update { s ->
        s.copy(credits = testMediaCredits.copy(crew = emptyList(), cast = emptyList()))
      }
      rv_cast.isNotDisplayed()
    }
  }
}
