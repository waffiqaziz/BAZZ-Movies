package com.waffiq.bazz_movies.feature.search.ui

import android.os.Build
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.R.id.open_search_view_edit_text
import com.waffiq.bazz_movies.core.designsystem.R.id.btn_try_again
import com.waffiq.bazz_movies.core.designsystem.R.id.progress_circular
import com.waffiq.bazz_movies.core.designsystem.R.string.clear_all
import com.waffiq.bazz_movies.core.instrumentationtest.CustomRecyclerViewActions.actionOnItemAt
import com.waffiq.bazz_movies.core.instrumentationtest.CustomRecyclerViewActions.clickItemAt
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performAction
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performSwipeDown
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performSwipeLeft
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performTextClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.clickChildViewWithId
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.feature.search.R.id.browse_genre_container
import com.waffiq.bazz_movies.feature.search.R.id.btn_delete
import com.waffiq.bazz_movies.feature.search.R.id.illustration_error
import com.waffiq.bazz_movies.feature.search.R.id.illustration_search_no_result_view
import com.waffiq.bazz_movies.feature.search.R.id.rv_search
import com.waffiq.bazz_movies.feature.search.R.id.rv_search_history
import com.waffiq.bazz_movies.feature.search.R.id.rv_search_history_row1
import com.waffiq.bazz_movies.feature.search.R.id.search_bar
import com.waffiq.bazz_movies.feature.search.R.id.search_view
import com.waffiq.bazz_movies.feature.search.R.id.swipe_refresh
import com.waffiq.bazz_movies.feature.search.testutils.BaseSearchFragmentTest
import com.waffiq.bazz_movies.feature.search.testutils.DummyData.history1
import com.waffiq.bazz_movies.feature.search.testutils.DummyData.history2
import com.waffiq.bazz_movies.feature.search.testutils.DummyData.history3
import com.waffiq.bazz_movies.feature.search.testutils.Helper.triggerSwipeRefresh
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@HiltAndroidTest
class SearchFragmentTest : BaseSearchFragmentTest() {

  @Test
  fun searchFragment_whenInitialState_displaysViewsCorrectly() {
    browse_genre_container.isDisplayed()
    rv_search.isNotDisplayed()
    illustration_error.isNotDisplayed()
    illustration_search_no_result_view.isNotDisplayed()
  }

  @Test
  fun searchView_whenSubmitting_triggersSearch() {
    stubSearchResult()
    performClickSearchAction()
    performTypeAndSearchAction()

    rv_search.isDisplayed()
    browse_genre_container.isNotDisplayed()
    illustration_error.isNotDisplayed()

    verify { mockSearchViewModel.search(testQuery) }
  }

  @Test
  fun searchView_whenSearchWithSameQuery_callsViewModelSearchEachTime() {
    performClickSearchAction()
    performTypeAndSearchAction()
    performClickSearchAction()
    performTypeAndSearchAction()

    // verify search was called twice
    verify(exactly = 2) { mockSearchViewModel.search(testQuery) }
  }

  @Test
  fun searchWithHistory_whenSameQueryAsCurrentSearch_stillDelegatesToViewModel() {
    historyFlow.value = listOf(history1.copy(query = testQuery))

    performClickSearchAction()
    performTypeAndSearchAction()

    performClickSearchAction()
    rv_search_history.clickItemAt(0)

    verify(exactly = 2) { mockSearchViewModel.search(testQuery) }
  }

  @Test
  fun setupKeyboardScroll_whenSoftKeyboardIsShow_shouldNotCrash() {
    search_bar.performClick()
    pressBack()
  }

  @Test
  fun setupKeyboardScroll_whenSoftKeyboardNotShowing_shouldNotCrash() {
    shortDelay()
    scenario.moveToState(Lifecycle.State.DESTROYED)
  }

  @Test
  fun searchView_whenSearchWithoutQuery_shouldNotTriggerSearch() {
    performClickSearchAction()

    open_search_view_edit_text.isDisplayed()
    open_search_view_edit_text.performAction(pressImeActionButton())

    verify(exactly = 0) { mockSearchViewModel.search(testQuery) }
  }

  @Test
  fun clickSearchHistory_whenHistoryAvailable_shouldSearchWithSelectedQuery() {
    performClickSearchAction()

    "dear".performClick(search_view)
    verify { mockSearchViewModel.search("dear") }
  }

  @Test
  fun deleteSearchHistory_whenHistoryAvailable_shouldDeleteTheSelected() {
    performClickSearchAction()

    "transformers".isDisplayed(search_view)

    every { mockSearchViewModel.deleteHistory(any()) } answers {
      historyFlow.value = listOf(history2, history3)
    }

    rv_search_history.actionOnItemAt(0, clickChildViewWithId(btn_delete))
    shortDelay()

    "transformers".isNotDisplayed(search_view)

    verify {
      mockSearchViewModel.deleteHistory(match { it.query == "transformers" })
    }
  }

  @Test
  fun clearAllHistory_whenHistoryAvailable_shouldDeleteAllTheHistory() {
    performClickSearchAction()

    // check initial history inside the search view
    "transformers".isDisplayed(search_view)

    // perform clear all
    every { mockSearchViewModel.deleteAllHistory() } answers {
      historyFlow.value = emptyList()
    }
    clear_all.performTextClick()
    shortDelay()

    // check the history is cleared
    "transformers".isNotDisplayed()

    // also check the history on browse page
    onView(withContentDescription("Back")).perform(click())
    shortDelay()
    "transformers".isNotDisplayed()

    verify { mockSearchViewModel.deleteAllHistory() }
  }

  @Test
  fun horizontalHistory_whenSwiping_shouldSwipeTogether() {
    rv_search_history_row1.performSwipeLeft()
  }

  @Test
  fun swipeRefresh_whenSwiped_triggersRefresh() {
    stubSearchResult()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      swipe_refresh.performSwipeDown()
    } else {
      swipe_refresh.performAction(triggerSwipeRefresh())
    }
  }

  @Test
  fun onResume_whenCalled_shouldPassed() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.onResume()
    }
  }

  @Test
  fun onPause_whenCalled_shouldPassed() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.onPause()
    }
  }

  @Test
  fun fragmentResultListener_successfull_opensSearchView() {
    searchFragment.parentFragmentManager.setFragmentResult(
      "open_search_view",
      Bundle(),
    )
    shortDelay()
    search_view.isDisplayed()
  }

  @Test
  fun openSearchView_error_catchesIllegalStateException() {
    mockkStatic(WindowCompat::class)
    every { WindowCompat.getInsetsController(any(), any()) } throws IllegalStateException("boom")

    searchFragment.parentFragmentManager.setFragmentResult("open_search_view", Bundle())

    InstrumentationRegistry.getInstrumentation().waitForIdleSync()

    unmockkStatic(WindowCompat::class)
  }

  @Test
  fun fragmentResultListener_clearSearch_delegatesToViewModelAndResetsBrowseState() {
    // setup fragment after user searching
    stubSearchResult()
    performClickSearchAction()
    performTypeAndSearchAction()
    rv_search.isDisplayed()

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.parentFragmentManager.setFragmentResult(
        "clear_search_view",
        Bundle(),
      )
    }
    waitForDebounce()

    // verify that back to Browse genre
    verify { mockSearchViewModel.clearSearch() }
    browse_genre_container.isDisplayed()
  }

  @Test
  fun recyclerView_whenInitialized_shouldHasCorrectLayoutManager() {
    onView(withId(rv_search))
      .check { view, _ ->
        val recyclerView = view as RecyclerView
        assertTrue(recyclerView.layoutManager is LinearLayoutManager)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        assertEquals(layoutManager.orientation, LinearLayoutManager.VERTICAL)
      }
  }

  @Test
  fun btnTryAgain_whenClicked_triggersRefreshAndShowsShimmer() {
    setActiveQuery()
    emitLoadState(LoadState.Error(Exception("Network error")))
    waitForDebounce()

    illustration_error.isDisplayed()
    btn_try_again.isDisplayed()

    btn_try_again.performClick()

    btn_try_again.isNotDisplayed()
    progress_circular.isDisplayed()
    verify(exactly = 1) { searchAdapter.refresh() }
  }
}
