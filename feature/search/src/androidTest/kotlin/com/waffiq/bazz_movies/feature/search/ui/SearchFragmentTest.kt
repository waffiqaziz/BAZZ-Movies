package com.waffiq.bazz_movies.feature.search.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.R.id.open_search_view_edit_text
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.designsystem.R.id.btn_try_again
import com.waffiq.bazz_movies.core.designsystem.R.id.progress_circular
import com.waffiq.bazz_movies.core.designsystem.R.string.clear_all
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performAction
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performSwipeDown
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performTextClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performType
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.clickChildViewWithId
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesNotExist
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomVisibilityMatchers.isVisible
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.feature.search.R.id.btn_delete
import com.waffiq.bazz_movies.feature.search.R.id.illustration_error
import com.waffiq.bazz_movies.feature.search.R.id.illustration_search_no_result_view
import com.waffiq.bazz_movies.feature.search.R.id.illustration_search_view
import com.waffiq.bazz_movies.feature.search.R.id.rv_search
import com.waffiq.bazz_movies.feature.search.R.id.rv_search_history
import com.waffiq.bazz_movies.feature.search.R.id.search_bar
import com.waffiq.bazz_movies.feature.search.R.id.search_view
import com.waffiq.bazz_movies.feature.search.R.id.swipe_refresh
import com.waffiq.bazz_movies.feature.search.testutils.DefaultFragmentTestHelper
import com.waffiq.bazz_movies.feature.search.testutils.Helper.triggerSwipeRefresh
import com.waffiq.bazz_movies.feature.search.testutils.SearchFragmentTestHelper
import com.waffiq.bazz_movies.feature.search.testutils.TestDummy.history1
import com.waffiq.bazz_movies.feature.search.testutils.TestDummy.history2
import com.waffiq.bazz_movies.feature.search.testutils.TestDummy.history3
import com.waffiq.bazz_movies.feature.search.ui.viewmodel.SearchViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SearchFragmentTest : SearchFragmentTestHelper by DefaultFragmentTestHelper() {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @BindValue
  @JvmField
  val mockNavigator: INavigator = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockSnackbar: ISnackbar = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockSearchViewModel: SearchViewModel = mockk(relaxed = true)

  @Before
  fun setUp() {
    hiltRule.inject()

    setupViewModelMocks(mockSearchViewModel)
    setupSnackbarMocks(mockSnackbar)
    setupFragment(mockNavigator)
    setupToolbar()
  }

  @Test
  fun searchFragment_whenInitialState_displaysViewsCorrectly() {
    illustration_search_view.isDisplayed()
    rv_search.isVisible()
    illustration_error.isNotDisplayed()
    illustration_search_no_result_view.isNotDisplayed()
  }

  @Test
  fun searchView_whenSubmitting_triggersSearch() {
    performClickSearchAction()
    performTypeAndSearchAction()

    // verify loading state UI
    rv_search.isDisplayed()
    illustration_search_view.isNotDisplayed()
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
    onView(withId(rv_search_history)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()),
    )

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
    onView(withId(rv_search_history))
      .perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
          0,
          clickChildViewWithId(btn_delete),
        ),
      )
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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      swipe_refresh.performSwipeDown()
    } else {
      swipe_refresh.performAction(triggerSwipeRefresh())
    }
    shortDelay()

    illustration_search_view.isDisplayed()
    verify(exactly = 1) { searchAdapter.refresh() }
  }

  @Test
  fun onConfigurationChanged_whenKeyboardHidden_callsExpandMethod() {
    val spyFragment = spyk(searchFragment)

    val newConfig = Configuration().apply {
      keyboardHidden = Configuration.KEYBOARDHIDDEN_YES
    }

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      spyFragment.onConfigurationChanged(newConfig)
    }

    verify { spyFragment.onKeyboardHidden() }
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
  fun fragmentResultListener_opensSearchView() {
    // simulate fragment result
    searchFragment.parentFragmentManager.setFragmentResult(
      "open_search_view",
      Bundle(),
    )
    shortDelay()
    search_view.isDisplayed()
  }

  @Test
  fun recyclerView_whenInitialized_shouldHasCorrectLayoutManager() {
    onView(withId(rv_search))
      .check { view, _ ->
        val recyclerView = view as RecyclerView
        assertThat(recyclerView.layoutManager).isInstanceOf(LinearLayoutManager::class.java)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        assertThat(layoutManager.orientation).isEqualTo(LinearLayoutManager.VERTICAL)
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

  private fun performClickSearchAction() {
    search_bar.performClick()
    open_search_view_edit_text.isDisplayed()
  }

  private fun performTypeAndSearchAction() {
    open_search_view_edit_text.isDisplayed()
    open_search_view_edit_text.performAction(clearText())
    shortDelay()
    open_search_view_edit_text.performType(testQuery)
    open_search_view_edit_text.performAction(pressImeActionButton())
  }
}
