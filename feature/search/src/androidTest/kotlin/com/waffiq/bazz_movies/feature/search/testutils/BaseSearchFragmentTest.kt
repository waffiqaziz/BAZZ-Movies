package com.waffiq.bazz_movies.feature.search.testutils

import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.instrumentationtest.launchFragmentInHiltContainer
import com.waffiq.bazz_movies.core.models.SearchHistory
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.testutils.TestDummy.history1
import com.waffiq.bazz_movies.feature.search.testutils.TestDummy.history2
import com.waffiq.bazz_movies.feature.search.testutils.TestDummy.history3
import com.waffiq.bazz_movies.feature.search.ui.SearchFragment
import com.waffiq.bazz_movies.feature.search.ui.adapter.SearchAdapter
import com.waffiq.bazz_movies.feature.search.ui.viewmodel.SearchViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

/**
 * Default implementation of [SearchFragmentTestHelper] for testing purposes.
 * Provides default mock implementations and setup for the SearchFragment.
 */
abstract class BaseSearchFragmentTest {

  protected lateinit var scenario: ActivityScenario<*>
  protected lateinit var searchFragment: SearchFragment
  protected lateinit var activity: AppCompatActivity
  protected lateinit var searchAdapter: SearchAdapter

  protected val searchResultsFlow: Flow<PagingData<MultiSearchItem>> = flowOf()
  protected val testQuery = "test_query"

  protected val historyFlow = MutableStateFlow(listOf(history1, history2, history3))

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
    setupSnackbarMocks()
    setupFragment()
    setupToolbar()
  }

  protected fun setupToolbar() {
    activity = searchFragment.requireActivity() as AppCompatActivity
  }

  protected fun setupViewModelMocks() {
    every { mockSearchViewModel.searchResults } returns searchResultsFlow
    every { mockSearchViewModel.search(any()) } just Runs
    every { mockSearchViewModel.searchHistory } returns historyFlow
    every { mockSearchViewModel.deleteHistory(any<SearchHistory>()) } just Runs
    every { mockSearchViewModel.deleteAllHistory() } just Runs
  }

  protected fun setupFragment() {
    every { mockNavigator.openDetails(any(), any()) } just Runs
    every { mockNavigator.openPersonDetails(any(), any()) } just Runs

    val spyAdapter = spyk(SearchAdapter(mockNavigator))
    searchAdapter = spyAdapter

    val result = launchFragmentInHiltContainer<SearchFragment>()
    searchFragment = result.fragment
    scenario = result.scenario

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.setAdapterForTest(spyAdapter)
    }
  }

  protected fun setupSnackbarMocks() {
    every { mockSnackbar.showSnackbarWarning(ofType<String>()) } returns mockk(relaxed = true)
  }
}
