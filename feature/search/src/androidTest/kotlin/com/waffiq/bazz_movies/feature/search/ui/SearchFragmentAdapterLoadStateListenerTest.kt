package com.waffiq.bazz_movies.feature.search.ui

import androidx.paging.LoadState
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.feature.search.R.id.browse_genre_container
import com.waffiq.bazz_movies.feature.search.R.id.illustration_error
import com.waffiq.bazz_movies.feature.search.R.id.rv_search
import com.waffiq.bazz_movies.feature.search.testutils.BaseSearchFragmentTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.verify
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class SearchFragmentAdapterLoadStateListenerTest : BaseSearchFragmentTest() {

  @Before
  override fun setup() {
    super.setup()

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.loadStateFlowProvider = fakeLoadStateFlow
      searchFragment.adapterLoadStateListener()
    }
  }

  @Test
  fun adapterLoadStateListener_whenFirstErrorEmitted_shouldUpdateUI() {
    emitLoadState(LoadState.Error(Throwable("Network error")))
    waitForDebounce()

    illustration_error.isDisplayed()
    rv_search.isNotDisplayed()
  }

  @Test
  fun adapterLoadStateListener_whenSameErrorMessageEmittedTwice_shouldUpdateUIOnlyOnce() {
    emitLoadState(LoadState.Error(Throwable("Same error")))
    waitForDebounce()

    emitLoadState(LoadState.Error(Throwable("Same error")))
    waitForDebounce()

    // error is showing
    illustration_error.isDisplayed()

    // but handleRefreshState was NOT called twice (snackbar only called once)
    verify(exactly = 1) { mockSnackbar.showSnackbarWarning(any<String>()) }
  }

  @Test
  fun adapterLoadStateListener_whenLoadingAfterPreviousError_shouldStillProcess() {
    emitLoadState(LoadState.Error(Throwable("Previous error")))
    waitForDebounce()

    emitLoadState(LoadState.Loading)
    waitForDebounce()

    illustration_error.isNotDisplayed()
    rv_search.isDisplayed()
  }

  @Test
  fun adapterLoadStateListener_whenNotLoadingWithNoStoredError_shouldProcess() {
    emitLoadState(LoadState.NotLoading(endOfPaginationReached = false))
    waitForDebounce()

    illustration_error.isNotDisplayed()
    browse_genre_container.isDisplayed()
  }

  @Test
  fun adapterLoadStateListener_whenDifferentErrorEmitted_shouldUpdateUIAgain() {
    emitLoadState(LoadState.Error(Throwable("Error A")))
    waitForDebounce()

    emitLoadState(LoadState.Error(Throwable("Error B")))
    waitForDebounce()

    illustration_error.isDisplayed()

    // different error message, so it should have been called twice
    verify(exactly = 2) { mockSnackbar.showSnackbarWarning(any<String>()) }
  }

  @Test
  fun adapterLoadStateListener_whenErrorThenSuccess_shouldShowRecoveredState() {
    performClickSearchAction()
    performTypeAndSearchAction()
    emitLoadState(LoadState.Error(Throwable("Temporary error")))
    waitForDebounce()

    illustration_error.isDisplayed()

    emitLoadState(LoadState.NotLoading(endOfPaginationReached = false))
    waitForDebounce()

    // error view should have gone
    illustration_error.isNotDisplayed()
    browse_genre_container.isNotDisplayed()
  }

  @Test
  fun adapterLoadStateListener_whenLoadingEmitted_shouldShowLoadingState() {
    performClickSearchAction()
    performTypeAndSearchAction()
    emitLoadState(LoadState.Loading)
    waitForDebounce()

    illustration_error.isNotDisplayed()
    rv_search.isDisplayed()
  }

  @Test
  fun adapterLoadStateListener_whenRapidEmissions_shouldOnlyProcessLastState() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      fakeLoadStateFlow.value = setupCombinedLoadStates(LoadState.Loading)
      fakeLoadStateFlow.value = setupCombinedLoadStates(LoadState.Loading)
      fakeLoadStateFlow.value = setupCombinedLoadStates(
        LoadState.NotLoading(endOfPaginationReached = false),
      )
    }
    waitForDebounce()

    // only the final NotLoading state should have been processed
    illustration_error.isNotDisplayed()
    browse_genre_container.isDisplayed()
  }

  private fun emitLoadState(state: LoadState) {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      fakeLoadStateFlow.value = setupCombinedLoadStates(state)
    }
  }

  private fun waitForDebounce() {
    Thread.sleep(DEBOUNCE_SHORT + 200)
  }
}
