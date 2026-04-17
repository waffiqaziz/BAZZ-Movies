package com.waffiq.bazz_movies.feature.search.ui

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.launchFragmentInHiltContainer
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.feature.search.R.id.illustration_error
import com.waffiq.bazz_movies.feature.search.R.id.illustration_search_no_result_view
import com.waffiq.bazz_movies.feature.search.R.id.illustration_search_view
import com.waffiq.bazz_movies.feature.search.R.id.rv_search
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SearchFragmentLoadStateTest {

  private lateinit var searchFragment: SearchFragment

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
    every { mockSearchViewModel.searchResults } returns flowOf()
    every { mockSearchViewModel.search(any()) } just Runs
    every { mockSnackbar.showSnackbarWarning(any<String>()) } returns mockk(relaxed = true)

    searchFragment = launchFragmentInHiltContainer<SearchFragment>()
    shortDelay()
  }

  @Test
  fun handleRefreshState_whenErrorOccurs_shouldHiddenRecyclerView() {
    // get the fragment instance and call the error method directly
    val errorState = LoadState.Error(Throwable("Test error"))
    val loadStates = setupCombinedLoadStates(errorState)

    // call the method directly instead of relying on adapter flow
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.handleRefreshState(loadStates, errorState)
    }
    shortDelay()

    illustration_error.isDisplayed()
    illustration_search_view.isNotDisplayed()
    rv_search.isNotDisplayed()
    illustration_search_no_result_view.isNotDisplayed()
  }

  @Test
  fun handleRefreshState_whenLoading_shouldShowLoadingState() {
    // create loading state (not error state for loading test)
    val loadingState = LoadState.Loading
    val loadStates = setupCombinedLoadStates(loadingState)

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.handleRefreshState(loadStates, loadingState)
    }
    shortDelay()

    illustration_error.isNotDisplayed()
    illustration_search_view.isNotDisplayed()
    rv_search.isDisplayed()
    illustration_search_no_result_view.isNotDisplayed()
  }

  @Test
  fun handleRefreshState_whenReachedEndOfPaging_shouldShowNoResultsView() {
    val notLoadingState = LoadState.NotLoading(endOfPaginationReached = true)
    val loadStates = setupCombinedLoadStates(notLoadingState)

    val spyAdapter = spyk(SearchAdapter(mockNavigator))
    every { spyAdapter.itemCount } returns 0

    // set adapter
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.setAdapterForTest(spyAdapter)
      searchFragment.handleRefreshState(loadStates, notLoadingState)
    }
    shortDelay()

    illustration_error.isNotDisplayed()
    illustration_search_view.isNotDisplayed()
    rv_search.isNotDisplayed()
    illustration_search_no_result_view.isDisplayed()
  }

  @Test
  fun handleRefreshState_whenNotReachedEndOfPaging_shouldShowsViewCorrectly() {
    val notLoadingState = LoadState.NotLoading(endOfPaginationReached = false)
    val loadStates = setupCombinedLoadStates(notLoadingState)
    val spyAdapter = spyk(SearchAdapter(mockNavigator))
    every { spyAdapter.itemCount } returns 0

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.setAdapterForTest(spyAdapter)
      searchFragment.handleRefreshState(loadStates, notLoadingState)
    }
    shortDelay()

    illustration_error.isNotDisplayed()
    illustration_search_view.isDisplayed()
    rv_search.isNotDisplayed()
    illustration_search_no_result_view.isNotDisplayed()
  }

  @Test
  fun handleRefreshState_whenHasSearchResults_shouldShowActualData() {
    val notLoadingState = LoadState.NotLoading(endOfPaginationReached = false)
    val loadStates = setupCombinedLoadStates(notLoadingState)

    // mock search results
    val mockSearchResults = listOf(
      MultiSearchItem(
        id = 1,
        title = "Test Movie",
        overview = "Test overview",
        posterPath = "/test-poster.jpg",
        mediaType = "movie"
      ),
      MultiSearchItem(
        id = 2,
        title = "Another Movie",
        overview = "Another overview",
        posterPath = "/another-poster.jpg",
        mediaType = "movie"
      )
    )

    // use PagingData with actual data
    val pagingData = PagingData.from(mockSearchResults)
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      val adapter = SearchAdapter(mockNavigator)
      searchFragment.setAdapterForTest(adapter)

      // submit the actual paging data to the adapter
      adapter.submitData(searchFragment.lifecycle, pagingData)
    }
    shortDelay()

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.handleRefreshState(loadStates, notLoadingState)
    }
    shortDelay()

    illustration_error.isNotDisplayed()
    rv_search.isDisplayed()
    illustration_search_view.isNotDisplayed()
    illustration_search_no_result_view.isNotDisplayed()
  }

  private fun setupCombinedLoadStates(states: LoadState): CombinedLoadStates {
    return CombinedLoadStates(
      refresh = states,
      prepend = LoadState.NotLoading(false),
      append = states, // needs for endOfPaginationReached = true
      source = LoadStates(
        refresh = states,
        prepend = LoadState.NotLoading(false),
        append = states
      ),
      mediator = null
    )
  }
}
