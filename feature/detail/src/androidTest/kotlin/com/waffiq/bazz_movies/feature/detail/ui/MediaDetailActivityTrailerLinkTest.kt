package com.waffiq.bazz_movies.feature.detail.ui

import android.content.ActivityNotFoundException
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import com.waffiq.bazz_movies.core.common.utils.Constants.YOUTUBE_LINK_VIDEO
import com.waffiq.bazz_movies.core.designsystem.R.string.yt_not_installed
import com.waffiq.bazz_movies.core.instrumentationtest.ViewMatcher.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.ViewMatcher.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.ViewMatcher.textIsDisplayed
import com.waffiq.bazz_movies.feature.detail.R.id.ib_play
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestHelper
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestSetup
import com.waffiq.bazz_movies.feature.detail.ui.launcher.DefaultTrailerLauncher
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.update
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test for [MediaDetailActivity] trailer link functionality.
 * This test checks the behavior of the trailer link when different values are posted to it.
 */
@HiltAndroidTest
class MediaDetailActivityTrailerLinkTest :
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
  fun trailerLink_withMixedValue_showsTrailerCorrectly() {
    context.launchMediaDetailActivity {
      uiState.update { s -> s.copy(videoLink = null) }
      ib_play.isNotDisplayed()
      uiState.update { s -> s.copy(videoLink = "") }
      ib_play.isNotDisplayed()
      uiState.update { s -> s.copy(videoLink = " ") }
      ib_play.isNotDisplayed()
      uiState.update { s -> s.copy(videoLink = "OIuG1bBkfs0") }
      ib_play.performClick()
      checkIntentData("${YOUTUBE_LINK_VIDEO}OIuG1bBkfs0")
    }
  }

  @Test
  fun playTrailer_whenActivityNotFoundException_showsSnackbar() {
    context.launchMediaDetailActivity { scenario ->
      val mockTrailerLauncher = mockk<DefaultTrailerLauncher>()
      every {
        mockTrailerLauncher.launch(any(), any())
      } throws ActivityNotFoundException("YouTube app not found")

      scenario.onActivity { activity ->
        activity.uiManager.trailerLauncher = mockTrailerLauncher
      }

      uiState.update { s -> s.copy(videoLink = "test_video_id") }
      ib_play.performClick()
      yt_not_installed.textIsDisplayed()

      verify { mockTrailerLauncher.launch(any(), "test_video_id") }
    }
  }
}
