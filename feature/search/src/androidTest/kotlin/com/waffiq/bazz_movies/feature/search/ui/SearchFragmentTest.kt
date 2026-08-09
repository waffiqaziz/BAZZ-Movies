package com.waffiq.bazz_movies.feature.search.ui

import android.os.Build
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.pressImeActionButton
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
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performTextClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.clickChildViewWithId
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesNotExist
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.feature.search.R.id.browse_genre_container
import com.waffiq.bazz_movies.feature.search.R.id.btn_delete
import com.waffiq.bazz_movies.feature.search.R.id.illustration_error
import com.waffiq.bazz_movies.feature.search.R.id.illustration_search_no_result_view
import com.waffiq.bazz_movies.feature.search.R.id.rv_search
import com.waffiq.bazz_movies.feature.search.R.id.rv_search_history
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

    // verify loading state UI
    rv_search.isDisplayed()
    browse_genre_container.isNotDisplayed()
    illustration_error.isNotDisplayed()

    verify { mockSearchViewModel.search(testQuery) }
  }

  @Test
  fun searchView_whenSearchWithSameQuery_onlyTriggerSearchOnce() {
    performClickSearchAction()
    performTypeAndSearchAction()
    performClickSearchAction()
    performTypeAndSearchAction()

    // verify search was only called once
    verify(exactly = 1) { mockSearchViewModel.search(testQuery) }
  }

  @Test
  fun searchWithHistory_whenSameQuery_onlyTriggerSearchOnce() {
    historyFlow.value = listOf(history1.copy(query = testQuery))

    // perform first search
    performClickSearchAction()
    performTypeAndSearchAction()

    // perform search using history search
    performClickSearchAction()
    rv_search_history.clickItemAt(0)

    // only perform search once
    verify(exactly = 1) { mockSearchViewModel.search(testQuery) }
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

    // perform search without query
    open_search_view_edit_text.isDisplayed()
    open_search_view_edit_text.performAction(pressImeActionButton())

    // verify search not run
    verify(exactly = 0) { mockSearchViewModel.search(testQuery) }
  }

  @Test
  fun clickSearchHistory_whenHistoryAvailable_shouldSearchWithSelectedQuery() {
    performClickSearchAction()

    "dear".performClick()
    verify { mockSearchViewModel.search("dear") }
  }

  @Test
  fun deleteSearchHistory_whenHistoryAvailable_shouldDeleteTheSelected() {
    performClickSearchAction()

    "transformers".isDisplayed()

    every { mockSearchViewModel.deleteHistory(any()) } answers {
      historyFlow.value = listOf(history2, history3)
    }

    // perform delete first history search
    rv_search_history.actionOnItemAt(0, clickChildViewWithId(btn_delete))
    shortDelay()

    "transformers".doesNotExist()

    verify {
      mockSearchViewModel.deleteHistory(
        match { it.query == "transformers" },
      )
    }
  }

  @Test
  fun clearAllHistory_whenHistoryAvailable_shouldDeleteAllTheHistory() {
    performClickSearchAction()

    every { mockSearchViewModel.deleteAllHistory() } answers {
      historyFlow.value = emptyList()
    }

    clear_all.performTextClick()
    shortDelay()

    "transformers".doesNotExist()
    verify { mockSearchViewModel.deleteAllHistory() }
  }

  @Test
  fun swipeRefresh_whenSwiped_triggersRefresh() {
    stubSearchResult()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      swipe_refresh.performSwipeDown()
    } else {
      swipe_refresh.performAction(triggerSwipeRefresh())
    }

//    verify(timeout = 3_000, exactly = 1) {
//      searchAdapter.refresh()
//    }
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
    // simulate fragment result
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
  fun fragmentResultListener_clearSearch() {
    // simulate fragment result
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.parentFragmentManager.setFragmentResult(
        "clear_search_view",
        Bundle(),
      )
    }
    shortDelay()
    verify { mockSearchViewModel.clearSearch() }
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
    // error state
    val errorState = LoadState.Error(Exception("Network error"))
    val combinedLoadStates = CombinedLoadStates(
      refresh = errorState,
      prepend = LoadState.NotLoading(endOfPaginationReached = false),
      append = LoadState.NotLoading(endOfPaginationReached = false),
      source = LoadStates(
        refresh = errorState,
        prepend = LoadState.NotLoading(endOfPaginationReached = false),
        append = LoadState.NotLoading(endOfPaginationReached = false),
      ),
    )

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.handleRefreshState(combinedLoadStates, errorState)
    }

    illustration_error.isDisplayed()
    btn_try_again.isDisplayed()

    // perform button try again click
    btn_try_again.performClick()

    btn_try_again.isNotDisplayed()
    progress_circular.isDisplayed()
    verify(exactly = 1) { searchAdapter.refresh() }
  }
}
