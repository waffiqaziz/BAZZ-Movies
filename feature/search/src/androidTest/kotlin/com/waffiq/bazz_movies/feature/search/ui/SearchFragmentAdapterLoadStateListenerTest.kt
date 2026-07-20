package com.waffiq.bazz_movies.feature.search.ui

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.feature.search.R.id.illustration_error
import com.waffiq.bazz_movies.feature.search.R.id.illustration_search_view
import com.waffiq.bazz_movies.feature.search.R.id.rv_search
import com.waffiq.bazz_movies.feature.search.testutils.BaseSearchFragmentTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class SearchFragmentAdapterLoadStateListenerTest : BaseSearchFragmentTest() {

  private val fakeLoadStateFlow = MutableStateFlow(idleLoadStates())

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
  fun adapterLoadStateListener_whenSameErrorEmittedTwice_shouldUpdateUIOnlyOnce() {
    val error = Throwable("Same error")

    emitLoadState(LoadState.Error(error))
    waitForDebounce()

    emitLoadState(LoadState.Error(error))
    waitForDebounce()

    // error is showing
    illustration_error.isDisplayed()

    // but handleRefreshState was NOT called twice (snackbar only called once)
    verify(exactly = 1) { mockSnackbar.showSnackbarWarning(any<Event<String>>()) }
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
    illustration_search_view.isDisplayed()
  }

  @Test
  fun adapterLoadStateListener_whenDifferentErrorEmitted_shouldUpdateUIAgain() {
    emitLoadState(LoadState.Error(Throwable("Error A")))
    waitForDebounce()

    emitLoadState(LoadState.Error(Throwable("Error B")))
    waitForDebounce()

    illustration_error.isDisplayed()

    // different error message, so it should have been called twice
    verify(exactly = 2) { mockSnackbar.showSnackbarWarning(any<Event<String>>()) }
  }

  @Test
  fun adapterLoadStateListener_whenErrorThenSuccess_shouldShowRecoveredState() {
    emitLoadState(LoadState.Error(Throwable("Temporary error")))
    waitForDebounce()

    illustration_error.isDisplayed()

    emitLoadState(LoadState.NotLoading(endOfPaginationReached = false))
    waitForDebounce()

    // error view should have gone
    illustration_error.isNotDisplayed()
    illustration_search_view.isDisplayed()
  }

  @Test
  fun adapterLoadStateListener_whenLoadingEmitted_shouldShowLoadingState() {
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
    illustration_search_view.isDisplayed()
  }

  private fun emitLoadState(state: LoadState) {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      fakeLoadStateFlow.value = setupCombinedLoadStates(state)
    }
  }

  private fun waitForDebounce() {
    Thread.sleep(DEBOUNCE_SHORT + 200)
  }

  private fun idleLoadStates() =
    setupCombinedLoadStates(LoadState.NotLoading(endOfPaginationReached = false))

  private fun setupCombinedLoadStates(states: LoadState): CombinedLoadStates =
    CombinedLoadStates(
      refresh = states,
      prepend = LoadState.NotLoading(false),
      append = states,
      source = LoadStates(
        refresh = states,
        prepend = LoadState.NotLoading(false),
        append = states,
      ),
      mediator = null,
    )
}
