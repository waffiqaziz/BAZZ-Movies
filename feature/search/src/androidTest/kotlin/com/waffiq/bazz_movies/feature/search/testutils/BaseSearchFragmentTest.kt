package com.waffiq.bazz_movies.feature.search.testutils

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
import io.mockk.spyk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseSearchFragmentTest {

  protected lateinit var scenario: ActivityScenario<*>
  protected lateinit var searchFragment: SearchFragment
  protected lateinit var searchAdapter: SearchAdapter

  private val searchResultsFlow: Flow<PagingData<MultiSearchItem>> = flowOf()
  protected val historyFlow = MutableStateFlow(listOf(history1, history2, history3))
  protected val testQuery = "test_query"

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
    val spyAdapter = spyk(SearchAdapter(mockNavigator))
    searchAdapter = spyAdapter

    val result = launchFragmentInHiltContainer<SearchFragment>()
    searchFragment = result.fragment
    scenario = result.scenario

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.setAdapterForTest(spyAdapter)
    }
  }
}
