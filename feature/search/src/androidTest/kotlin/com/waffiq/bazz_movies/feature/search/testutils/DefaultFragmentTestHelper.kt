package com.waffiq.bazz_movies.feature.search.testutils

import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
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
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

/**
 * Default implementation of [SearchFragmentTestHelper] for testing purposes.
 * Provides default mock implementations and setup for the SearchFragment.
 */
class DefaultFragmentTestHelper : SearchFragmentTestHelper {

  override lateinit var searchFragment: SearchFragment
  override lateinit var activity: AppCompatActivity
  override lateinit var searchAdapter: SearchAdapter

  override val searchResultsFlow: Flow<PagingData<MultiSearchItem>> = flowOf()
  override val testQuery = "test_query"

  override val historyFlow = MutableStateFlow(listOf(history1, history2, history3))

  override fun setupToolbar() {
    activity = searchFragment.requireActivity() as AppCompatActivity
  }

  override fun setupViewModelMocks(viewModel: SearchViewModel) {
    every { viewModel.searchResults } returns searchResultsFlow
    every { viewModel.search(any()) } just Runs
    every { viewModel.searchHistory } returns historyFlow
    every { viewModel.deleteHistory(any<SearchHistory>()) } just Runs
    every { viewModel.deleteAllHistory() } just Runs
  }

  override fun setupFragment(navigator: INavigator) {
    every { navigator.openDetails(any(), any()) } just Runs
    every { navigator.openPersonDetails(any(), any()) } just Runs

    val spyAdapter = spyk(SearchAdapter(navigator))
    searchAdapter = spyAdapter

    searchFragment = launchFragmentInHiltContainer<SearchFragment>().fragment
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.setAdapterForTest(spyAdapter)
    }
  }

  override fun setupSnackbarMocks(snackBar: ISnackbar) {
    every { snackBar.showSnackbarWarning(ofType<String>()) } returns mockk(relaxed = true)
  }
}
