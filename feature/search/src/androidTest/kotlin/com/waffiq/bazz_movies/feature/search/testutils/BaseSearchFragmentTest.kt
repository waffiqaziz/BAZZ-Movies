package com.waffiq.bazz_movies.feature.search.testutils

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.R.id.open_search_view_edit_text
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performAction
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performType
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.instrumentationtest.launchFragmentInHiltContainer
import com.waffiq.bazz_movies.core.models.SearchHistory
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.feature.search.R.id.search_bar
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.testutils.DummyData.fakeSearchResult
import com.waffiq.bazz_movies.feature.search.testutils.DummyData.history1
import com.waffiq.bazz_movies.feature.search.testutils.DummyData.history2
import com.waffiq.bazz_movies.feature.search.testutils.DummyData.history3
import com.waffiq.bazz_movies.feature.search.ui.SearchFragment
import com.waffiq.bazz_movies.feature.search.ui.adapter.SearchAdapter
import com.waffiq.bazz_movies.feature.search.ui.viewmodel.SearchViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.spyk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseSearchFragmentTest {

  protected lateinit var scenario: ActivityScenario<*>
  protected lateinit var searchFragment: SearchFragment
  protected lateinit var searchAdapter: SearchAdapter
  protected lateinit var spyAdapter: SearchAdapter
  protected val fakeLoadStateFlow = MutableStateFlow(idleLoadStates())

  private val searchResultsFlow: Flow<PagingData<MultiSearchItem>> = flowOf(fakeSearchResult)

  protected val historyFlow = MutableStateFlow(listOf(history1, history2, history3))
  protected val testQuery = "test_query"

  protected val notLoadingState = LoadState.NotLoading(endOfPaginationReached = true)
  protected val loadStates = setupCombinedLoadStates(notLoadingState)

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var mockNavigator: INavigator

  @Inject
  lateinit var mockSnackbar: ISnackbar

  @Inject
  lateinit var mockSearchViewModel: SearchViewModel

  @Before
  open fun setup() {
    hiltRule.inject()
    setupViewModelMocks()
    setupFragment()
  }

  private fun setupViewModelMocks() {
    every { mockSearchViewModel.searchResults } returns searchResultsFlow
    every { mockSearchViewModel.search(any()) } just Runs
    every { mockSearchViewModel.searchHistory } returns historyFlow
    every { mockSearchViewModel.deleteHistory(any<SearchHistory>()) } just Runs
    every { mockSearchViewModel.deleteAllHistory() } just Runs
  }

  private fun setupFragment() {
    spyAdapter = spyk(SearchAdapter(mockNavigator))
    searchAdapter = spyAdapter

    val result = launchFragmentInHiltContainer<SearchFragment>()
    searchFragment = result.fragment
    scenario = result.scenario

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.setAdapterForTest(spyAdapter)
    }
  }

  protected fun performClickSearchAction() {
    search_bar.performClick()
    open_search_view_edit_text.isDisplayed()
  }

  protected fun performTypeAndSearchAction() {
    open_search_view_edit_text.isDisplayed()
    open_search_view_edit_text.performAction(clearText())
    shortDelay()
    open_search_view_edit_text.performType(testQuery)
    open_search_view_edit_text.performAction(pressImeActionButton())
  }

  private fun idleLoadStates() =
    setupCombinedLoadStates(LoadState.NotLoading(endOfPaginationReached = false))

  protected fun setupCombinedLoadStates(states: LoadState): CombinedLoadStates =
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

  protected fun stubSearchResult() =
    runTest {
      spyAdapter.submitData(fakeSearchResult)
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        searchFragment.loadStateFlowProvider = fakeLoadStateFlow
        fakeLoadStateFlow.value =
          setupCombinedLoadStates(notLoadingState)
        searchFragment.handleRefreshState(loadStates, notLoadingState)
      }
      shortDelay(500)
    }
}
