package com.waffiq.bazz_movies.feature.detail.ui

import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.instrumentationtest.CustomRecyclerViewActions.clickItemAt
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performScrollTo
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
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
import com.waffiq.bazz_movies.feature.detail.R.id.tv_summary_header
import com.waffiq.bazz_movies.feature.detail.R.id.tv_toggle_watch_providers
import com.waffiq.bazz_movies.feature.detail.R.id.tv_watch_providers_message
import com.waffiq.bazz_movies.feature.detail.testutils.basetest.BaseMediaDetailActivityTest
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.verify
import kotlinx.coroutines.flow.update
import org.junit.Test

@HiltAndroidTest
class MediaDetailActivityWatchProvidersTest : BaseMediaDetailActivityTest() {

  @Test
  fun expandWatchProvider_whenClicked_opensWatchProvider() {
    context.launchMediaDetailActivity {
      tv_toggle_watch_providers.performScrollTo()
      performClickWatchProvidersButton()

      // check that all watch provider layouts are displayed
      layout_ads.isDisplayed()
      layout_free.performScrollTo()
      layout_free.isDisplayed()
      layout_rent.isDisplayed()
      layout_streaming.isDisplayed()
      layout_buy.performScrollTo()
      layout_buy.isDisplayed()
    }
  }

  @Test
  fun expandWatchProvider_doubleClickOnToggleButton_closesWatchProvider() {
    context.launchMediaDetailActivity {
      performClickWatchProvidersButton()
      shortDelay()
      performClickWatchProvidersButton()

      // check that all watch provider layouts are not displayed
      layout_ads.isNotDisplayed()
      layout_buy.isNotDisplayed()
      layout_free.isNotDisplayed()
      layout_rent.isNotDisplayed()
      layout_streaming.isNotDisplayed()
    }
  }

  @Test
  fun buttonJustWatch_whenClicked_opensJustWatch() {
    context.launchMediaDetailActivity {
      performClickWatchProvidersButton()
      btn_justwatch.performScrollTo()
      tv_summary_header.performScrollTo()
      btn_justwatch.performClick()
      verify { mockUriLauncher.launch(any()) }
    }
  }

  @Test
  fun watchProviderItem_whenClicked_opensTMDB() {
    context.launchMediaDetailActivity {
      performClickWatchProvidersButton()
      layout_streaming.performScrollTo()
      rv_streaming.clickItemAt(0)
    }
  }

  @Test
  fun watchProviderState_whenSuccessfulButEmptyData_hidesTheLayout() {
    context.launchMediaDetailActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        uiState.update { state ->
          state.copy(
            detail = state.detail?.copy(
              watchProviders = WatchProvidersUiState.Success(
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
              ),
            ),
          )
        }
      }
      performClickWatchProvidersButton()

      // check that all watch provider layouts are not displayed
      progress_bar.isNotDisplayed()
      layout_ads.isNotDisplayed()
      layout_buy.isNotDisplayed()
      layout_free.isNotDisplayed()
      layout_rent.isNotDisplayed()
      layout_streaming.isNotDisplayed()
    }
  }

  @Test
  fun watchProviderState_whenErrorOccurs_showsError() {
    context.launchMediaDetailActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        uiState.update { state ->
          state.copy(
            detail = state.detail?.copy(
              watchProviders = WatchProvidersUiState.Error("Error fetching watch providers"),
            ),
          )
        }
      }
      performClickWatchProvidersButton()
      "Error fetching watch providers".isDisplayed()

      // check that all watch provider layouts are not displayed
      progress_bar.isNotDisplayed()
      layout_justwatch.isNotDisplayed()
      layout_ads.isNotDisplayed()
      layout_buy.isNotDisplayed()
      layout_free.isNotDisplayed()
      layout_rent.isNotDisplayed()
      layout_streaming.isNotDisplayed()
    }
  }

  @Test
  fun watchProviderState_whenLoading_showsProgressBar() {
    context.launchMediaDetailActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        uiState.update { state ->
          state.copy(
            detail = state.detail?.copy(watchProviders = WatchProvidersUiState.Loading),
          )
        }
      }
      shortDelay()
      progress_bar.isDisplayed()
      tv_watch_providers_message.isNotDisplayed()
    }
  }

  private fun performClickWatchProvidersButton() {
    tv_toggle_watch_providers.performClick()
  }
}
