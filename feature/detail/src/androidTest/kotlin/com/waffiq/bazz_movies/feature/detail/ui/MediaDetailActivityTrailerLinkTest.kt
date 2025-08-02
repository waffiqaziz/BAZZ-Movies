package com.waffiq.bazz_movies.feature.detail.ui

import android.content.ActivityNotFoundException
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.waffiq.bazz_movies.core.common.utils.Constants.YOUTUBE_LINK_VIDEO
import com.waffiq.bazz_movies.core.designsystem.R.string.yt_not_installed
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
import org.hamcrest.Matchers.not
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
      linkVideo.postValue(null)
      onView(withId(ib_play)).check(matches(not(isDisplayed())))
      linkVideo.postValue("")
      onView(withId(ib_play)).check(matches(not(isDisplayed())))
      linkVideo.postValue(" ")
      onView(withId(ib_play)).check(matches(not(isDisplayed())))
      linkVideo.postValue("OIuG1bBkfs0")
      onView(withId(ib_play)).check(matches(isDisplayed())).perform(click())
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

      context.launchMediaDetailActivity { scenario ->
        scenario.onActivity { activity ->
          activity.uiManager.trailerLauncher = mockTrailerLauncher
        }

        linkVideo.postValue("test_video_id")
        onView(withId(ib_play)).perform(click())
        onView(withText(yt_not_installed)).check(matches(isDisplayed()))

        verify { mockTrailerLauncher.launch(any(), "test_video_id") }
      }
    }
  }
}
