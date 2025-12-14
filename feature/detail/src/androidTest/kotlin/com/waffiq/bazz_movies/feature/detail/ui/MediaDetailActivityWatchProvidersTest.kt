package com.waffiq.bazz_movies.feature.detail.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.common.utils.Constants.JUSTWATCH_LINK_MAIN
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.feature.detail.R.id.btn_justwatch
import com.waffiq.bazz_movies.feature.detail.R.id.layout_ads
import com.waffiq.bazz_movies.feature.detail.R.id.layout_buy
import com.waffiq.bazz_movies.feature.detail.R.id.layout_free
import com.waffiq.bazz_movies.feature.detail.R.id.layout_justwatch
import com.waffiq.bazz_movies.feature.detail.R.id.layout_rent
import com.waffiq.bazz_movies.feature.detail.R.id.layout_streaming
import com.waffiq.bazz_movies.feature.detail.R.id.progress_bar
import com.waffiq.bazz_movies.feature.detail.R.id.rv_streaming
import com.waffiq.bazz_movies.feature.detail.R.id.tv_toggle_watch_providers
import com.waffiq.bazz_movies.feature.detail.R.id.tv_watch_providers_message
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestHelper
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestSetup
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MediaDetailActivityWatchProvidersTest :
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
  fun expandWatchProvider_whenClicked_opensWatchProvider() {
    context.launchMediaDetailActivity {
      onView(withId(tv_toggle_watch_providers)).perform(scrollTo())
      performClickWatchProvidersButton()

      // check that all watch provider layouts are displayed
      onView(withId(layout_ads)).check(matches(isDisplayed()))
      onView(withId(layout_free)).perform(scrollTo())
      onView(withId(layout_free)).check(matches(isDisplayed()))
      onView(withId(layout_rent)).check(matches(isDisplayed()))
      onView(withId(layout_streaming)).check(matches(isDisplayed()))
      onView(withId(layout_buy)).perform(scrollTo())
      onView(withId(layout_buy)).check(matches(isDisplayed()))
    }
  }

  @Test
  fun expandWatchProvider_doubleClickOnToggleButton_closesWatchProvider() {
    context.launchMediaDetailActivity {
      performClickWatchProvidersButton()
      shortDelay()
      performClickWatchProvidersButton()

      // check that all watch provider layouts are not displayed
      onView(withId(layout_ads)).check(matches(not(isDisplayed())))
      onView(withId(layout_buy)).check(matches(not(isDisplayed())))
      onView(withId(layout_free)).check(matches(not(isDisplayed())))
      onView(withId(layout_rent)).check(matches(not(isDisplayed())))
      onView(withId(layout_streaming)).check(matches(not(isDisplayed())))
    }
  }

  @Test
  fun buttonJustWatch_whenClicked_opensJustWatch() {
    context.launchMediaDetailActivity {
      performClickWatchProvidersButton()
      onView(withId(btn_justwatch)).perform(scrollTo(), click())
      checkIntentData(JUSTWATCH_LINK_MAIN)
    }
  }

  @Test
  fun watchProviderItem_whenClicked_opensTMDB() {
    context.launchMediaDetailActivity {
      performClickWatchProvidersButton()
      onView(withId(layout_streaming)).perform(scrollTo())
      onView(withId(rv_streaming)).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
      )
    }
  }

  @Test
  fun watchProviderState_whenSuccessfulButEmptyData_hidesTheLayout() {
    context.launchMediaDetailActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        watchProvidersUiState.postValue(
          WatchProvidersUiState.Success(
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
          )
        )
      }
      performClickWatchProvidersButton()

      // check that all watch provider layouts are not displayed
      onView(withId(progress_bar)).check(matches(not(isDisplayed())))
      onView(withId(layout_ads)).check(matches(not(isDisplayed())))
      onView(withId(layout_buy)).check(matches(not(isDisplayed())))
      onView(withId(layout_free)).check(matches(not(isDisplayed())))
      onView(withId(layout_rent)).check(matches(not(isDisplayed())))
      onView(withId(layout_streaming)).check(matches(not(isDisplayed())))
    }
  }

  @Test
  fun watchProviderState_whenErrorOccurs_showsError() {
    context.launchMediaDetailActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        watchProvidersUiState.postValue(WatchProvidersUiState.Error("Error fetching watch providers"))
      }
      performClickWatchProvidersButton()
      onView(withText("Error fetching watch providers"))
        .check(matches(isDisplayed()))

      // check that all watch provider layouts are not displayed
      onView(withId(progress_bar)).check(matches(not(isDisplayed())))
      onView(withId(layout_justwatch)).check(matches(not(isDisplayed())))
      onView(withId(layout_ads)).check(matches(not(isDisplayed())))
      onView(withId(layout_buy)).check(matches(not(isDisplayed())))
      onView(withId(layout_free)).check(matches(not(isDisplayed())))
      onView(withId(layout_rent)).check(matches(not(isDisplayed())))
      onView(withId(layout_streaming)).check(matches(not(isDisplayed())))
    }
  }

  @Test
  fun watchProviderState_whenLoading_showsProgressBar() {
    context.launchMediaDetailActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        watchProvidersUiState.postValue(WatchProvidersUiState.Loading)
      }
      shortDelay()
      onView(withId(progress_bar)).check(matches(isDisplayed()))
      onView(withId(tv_watch_providers_message)).check(matches(not(isDisplayed())))
    }
  }

  private fun performClickWatchProvidersButton() {
    onView(withId(tv_toggle_watch_providers))
      .check(matches(isDisplayed())).perform(click())
  }
}
