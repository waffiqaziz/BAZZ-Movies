package com.waffiq.bazz_movies.feature.search.testutils

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.instrumentationtest.launchFragmentInHiltContainer
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.ui.SearchAdapter
import com.waffiq.bazz_movies.feature.search.ui.SearchFragment
import com.waffiq.bazz_movies.feature.search.ui.SearchViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DefaultFragmentTestHelper : SearchFragmentTestHelper {

  override lateinit var searchFragment: SearchFragment
  override lateinit var activity: AppCompatActivity
  override lateinit var toolbar: ActionBar
  override lateinit var searchAdapter: SearchAdapter

  override val searchResultsFlow: Flow<PagingData<MultiSearchItem>> = flowOf()
  override val queryLiveData = MutableLiveData<String>()
  override val expandSearchViewLiveData = MutableLiveData<Boolean>()
  override val testQuery = "test_query"

  override fun setupToolbar() {
    activity = searchFragment.requireActivity() as AppCompatActivity
    toolbar = activity.supportActionBar ?: return
  }

  override fun setupViewModelMocks(mockSearchViewModel: SearchViewModel) {
    every { mockSearchViewModel.searchResults } returns searchResultsFlow
    every { mockSearchViewModel.query } returns queryLiveData
    every { mockSearchViewModel.expandSearchView } returns expandSearchViewLiveData
    every { mockSearchViewModel.search(any()) } just Runs
    every { mockSearchViewModel.setExpandSearchView(any()) } just Runs
  }

  override fun setupFragment(mockNavigator: INavigator) {
    every { mockNavigator.openDetails(any(), any()) } just Runs
    every { mockNavigator.openPersonDetails(any(), any()) } just Runs

    val spyAdapter = spyk(SearchAdapter(mockNavigator))
    searchAdapter = spyAdapter

    searchFragment = launchFragmentInHiltContainer<SearchFragment> {
      this.setAdapterForTest(spyAdapter)
    }
  }

  override fun setupSnackbarMocks(mockSnackbar: ISnackbar) {
    every { mockSnackbar.showSnackbarWarning(ofType<String>()) } returns mockk(relaxed = true)
  }
}
