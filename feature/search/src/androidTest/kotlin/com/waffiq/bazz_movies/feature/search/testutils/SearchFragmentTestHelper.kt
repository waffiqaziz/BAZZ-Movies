package com.waffiq.bazz_movies.feature.search.testutils

import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.models.SearchHistory
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.ui.SearchFragment
import com.waffiq.bazz_movies.feature.search.ui.adapter.SearchAdapter
import com.waffiq.bazz_movies.feature.search.ui.viewmodel.SearchViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Interface to help with setting up the SearchFragment for testing purposes.
 * */
interface SearchFragmentTestHelper {
  var searchFragment: SearchFragment
  var activity: AppCompatActivity
  var searchAdapter: SearchAdapter

  val searchResultsFlow: Flow<PagingData<MultiSearchItem>>
  val testQuery: String

  val historyFlow: MutableStateFlow<List<SearchHistory>>

  fun setupToolbar()
  fun setupViewModelMocks(viewModel: SearchViewModel)
  fun setupFragment(navigator: INavigator)
  fun setupSnackbarMocks(snackBar: ISnackbar)
}
