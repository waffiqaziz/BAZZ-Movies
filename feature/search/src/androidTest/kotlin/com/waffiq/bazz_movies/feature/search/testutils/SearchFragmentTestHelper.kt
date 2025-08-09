package com.waffiq.bazz_movies.feature.search.testutils

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.ui.SearchAdapter
import com.waffiq.bazz_movies.feature.search.ui.SearchFragment
import com.waffiq.bazz_movies.feature.search.ui.SearchViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import kotlinx.coroutines.flow.Flow

/**
 * Interface to help with setting up the SearchFragment for testing purposes.
 * */
interface SearchFragmentTestHelper {
  var searchFragment: SearchFragment
  var activity: AppCompatActivity
  var toolbar: ActionBar
  var searchAdapter: SearchAdapter

  val searchResultsFlow: Flow<PagingData<MultiSearchItem>>
  val queryLiveData: MutableLiveData<String>
  val expandSearchViewLiveData: MutableLiveData<Boolean>
  val testQuery: String

  fun setupToolbar()
  fun setupViewModelMocks(mockSearchViewModel: SearchViewModel)
  fun setupFragment(mockNavigator: INavigator)
  fun setupSnackbarMocks(mockSnackbar: ISnackbar)
}
