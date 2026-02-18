package com.waffiq.bazz_movies.feature.search.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.R.id.open_search_view_edit_text
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.designsystem.R.id.btn_try_again
import com.waffiq.bazz_movies.core.designsystem.R.id.progress_circular
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.feature.search.R.id.illustration_error
import com.waffiq.bazz_movies.feature.search.R.id.illustration_search_no_result_view
import com.waffiq.bazz_movies.feature.search.R.id.illustration_search_view
import com.waffiq.bazz_movies.feature.search.R.id.rv_search
import com.waffiq.bazz_movies.feature.search.R.id.search_bar
import com.waffiq.bazz_movies.feature.search.R.id.swipe_refresh
import com.waffiq.bazz_movies.feature.search.testutils.DefaultFragmentTestHelper
import com.waffiq.bazz_movies.feature.search.testutils.Helper.triggerSwipeRefresh
import com.waffiq.bazz_movies.feature.search.testutils.SearchFragmentTestHelper
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.hamcrest.core.IsNot.not
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

    setupFragment(mockNavigator)
    setupToolbar()
    setupViewModelMocks(mockSearchViewModel)
    setupSnackbarMocks(mockSnackbar)
  }

  @Test
  fun searchFragment_whenInitialState_displaysViewsCorrectly() {
    onView(withId(illustration_search_view)).check(matches(isDisplayed()))
    onView(withId(rv_search)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    onView(withId(illustration_error)).check(matches(not(isDisplayed())))
    onView(withId(illustration_search_no_result_view)).check(matches(not(isDisplayed())))
  }

  @Test
  fun searchView_whenSubmitting_triggersSearch() {
    performClickSearchAction()
    performTypeAndSearchAction()

    // verify loading state UI
    onView(withId(rv_search)).check(matches(isDisplayed()))
    onView(withId(illustration_search_view)).check(matches(not(isDisplayed())))
    onView(withId(illustration_error)).check(matches(not(isDisplayed())))

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
  fun searchView_whenSearchWithoutQuery_shouldNotTriggerSearch() {
    performClickSearchAction()

    // perform search without query
    onView(withId(open_search_view_edit_text))
      .check(matches(isDisplayed()))
      .perform(pressImeActionButton())
    // verify search was only called once
    verify(exactly = 0) { mockSearchViewModel.search(testQuery) }
  }

  @Test
  fun swipeRefresh_whenSwiped_triggersRefresh() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      onView(withId(swipe_refresh)).perform(swipeDown())
    } else {
      onView(withId(swipe_refresh)).perform(triggerSwipeRefresh())
    }
    shortDelay()

    onView(withId(illustration_search_view)).check(matches(isDisplayed()))
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
      Bundle()
    )
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
      )
    )

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.handleRefreshState(combinedLoadStates, errorState)
    }

    onView(withId(illustration_error)).check(matches(isDisplayed()))
    onView(withId(btn_try_again)).check(matches(isDisplayed()))

    // perform button try again click
    onView(withId(btn_try_again)).perform(click())

    onView(withId(btn_try_again)).check(matches(not(isDisplayed())))
    onView(withId(progress_circular)).check(matches(isDisplayed()))
    verify(exactly = 1) { searchAdapter.refresh() }
  }

  private fun performClickSearchAction() {
    onView(withId(search_bar)).perform(click())
    onView(withId(open_search_view_edit_text)).check(matches(isDisplayed()))
  }

  private fun performTypeAndSearchAction() {
    onView(withId(open_search_view_edit_text))
      .check(matches(isDisplayed()))
      .perform(clearText(), typeText(testQuery), pressImeActionButton())
  }
}
