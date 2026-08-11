package com.waffiq.bazz_movies.feature.search.ui

import androidx.paging.LoadState
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.feature.search.R.id.browse_genre_container
import com.waffiq.bazz_movies.feature.search.R.id.illustration_error
import com.waffiq.bazz_movies.feature.search.R.id.illustration_search_no_result_view
import com.waffiq.bazz_movies.feature.search.R.id.rv_search
import com.waffiq.bazz_movies.feature.search.testutils.BaseSearchFragmentTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.verify
import org.junit.Test

@HiltAndroidTest
class SearchFragmentScreenStateTest : BaseSearchFragmentTest() {

  @Test
  fun screenState_whenFirstErrorEmitted_updatesUi() {
    setActiveQuery()
    emitLoadState(LoadState.Error(Throwable("Network error")))
    waitForDebounce()

    illustration_error.isDisplayed()
    rv_search.isNotDisplayed()
  }

  @Test
  fun screenState_whenSameErrorMessageEmittedTwice_emitsSnackbarWarningTwice() {
    setActiveQuery()

    emitLoadState(LoadState.Error(Throwable("Same error")))
    waitForDebounce()

    emitLoadState(LoadState.Error(Throwable("Same error")))
    waitForDebounce()

    illustration_error.isDisplayed()
    verify(exactly = 2) { mockSnackbar.showSnackbarWarning(any<String>()) }
  }

  @Test
  fun screenState_whenLoadingAfterPreviousError_stillProcessesNewState() {
    setActiveQuery()

    emitLoadState(LoadState.Error(Throwable("Previous error")))
    waitForDebounce()
    illustration_error.isDisplayed()

    emitLoadState(LoadState.Loading)
    waitForDebounce()

    illustration_error.isNotDisplayed()
    rv_search.isDisplayed()
  }

  @Test
  fun screenState_whenNoActiveQuery_alwaysShowsBrowse() {
    // no setActiveQuery() call — currentQuery stays null regardless of load state
    emitLoadState(LoadState.NotLoading(endOfPaginationReached = false))
    waitForDebounce()

    illustration_error.isNotDisplayed()
    browse_genre_container.isDisplayed()
  }

  @Test
  fun screenState_whenDifferentErrorsEmitted_reportsEachToSnackbar() {
    setActiveQuery()

    emitLoadState(LoadState.Error(Throwable("Error A")))
    waitForDebounce()

    emitLoadState(LoadState.Error(Throwable("Error B")))
    waitForDebounce()

    illustration_error.isDisplayed()
    verify(exactly = 2) { mockSnackbar.showSnackbarWarning(any<String>()) }
  }

  @Test
  fun screenState_whenErrorThenBackToNoActiveQuery_showsBrowseAgain() {
    setActiveQuery()
    emitLoadState(LoadState.Error(Throwable("Temporary error")))
    waitForDebounce()

    illustration_error.isDisplayed()

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      fakeCurrentQueryFlow.value = null
    }
    waitForDebounce()

    illustration_error.isNotDisplayed()
    browse_genre_container.isDisplayed()
  }

  @Test
  fun screenState_whenLoadingEmittedWithActiveQuery_showsLoadingState() {
    setActiveQuery()
    emitLoadState(LoadState.Loading)
    waitForDebounce()

    illustration_error.isNotDisplayed()
    rv_search.isDisplayed()
  }

  @Test
  fun screenState_whenRapidEmissions_onlyProcessesLastState() {
    setActiveQuery()

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      fakeLoadStateFlow.value = setupCombinedLoadStates(LoadState.Loading)
      fakeLoadStateFlow.value = setupCombinedLoadStates(LoadState.Loading)
      fakeLoadStateFlow.value = setupCombinedLoadStates(
        LoadState.NotLoading(endOfPaginationReached = false),
      )
    }
    waitForDebounce()

    // only the final state should have been processed: refresh finished, still fetching more,
    // zero items, FetchingMore. So nothing is shown (no error illustration and no browse).
    illustration_error.isNotDisplayed()
    browse_genre_container.isNotDisplayed()
    rv_search.isNotDisplayed()
    illustration_search_no_result_view.isNotDisplayed()
  }
}
