package com.waffiq.bazz_movies.feature.search.ui

import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.feature.search.R.id.browse_genre_container
import com.waffiq.bazz_movies.feature.search.R.id.illustration_error
import com.waffiq.bazz_movies.feature.search.R.id.illustration_search_no_result_view
import com.waffiq.bazz_movies.feature.search.R.id.rv_search
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.testutils.BaseSearchFragmentTest
import com.waffiq.bazz_movies.feature.search.ui.adapter.SearchAdapter
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.spyk
import org.junit.Test

@HiltAndroidTest
class SearchFragmentLoadStateTest : BaseSearchFragmentTest() {

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
    browse_genre_container.isNotDisplayed()
    rv_search.isNotDisplayed()
    illustration_search_no_result_view.isNotDisplayed()
  }

  @Test
  fun handleRefreshState_whenLoading_shouldShowLoadingState() {
    // create loading state (not error state for loading test)
    val loadingState = LoadState.Loading
    val loadStates = setupCombinedLoadStates(loadingState)

    performClickSearchAction()
    performTypeAndSearchAction()
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.handleRefreshState(loadStates, loadingState)
    }
    shortDelay()

    illustration_error.isNotDisplayed()
    browse_genre_container.isNotDisplayed()
    rv_search.isDisplayed()
    illustration_search_no_result_view.isNotDisplayed()
  }

  @Test
  fun handleRefreshState_whenReachedEndOfPaging_shouldShowNoResultsView() {
    val spyAdapter = spyk(SearchAdapter(mockNavigator))
    every { spyAdapter.itemCount } returns 0

    performClickSearchAction()
    performTypeAndSearchAction()

    // set adapter
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.setAdapterForTest(spyAdapter)
      searchFragment.handleRefreshState(loadStates, notLoadingState)
    }
    shortDelay()

    illustration_error.isNotDisplayed()
    browse_genre_container.isNotDisplayed()
    rv_search.isNotDisplayed()
    illustration_search_no_result_view.isDisplayed()
  }

  @Test
  fun handleRefreshState_whenNotReachedEndOfPaging_shouldShowsViewCorrectly() {
    val notLoadingState = LoadState.NotLoading(endOfPaginationReached = false)
    val loadStates = setupCombinedLoadStates(notLoadingState)

    performClickSearchAction()
    performTypeAndSearchAction()
    val spyAdapter = spyk(SearchAdapter(mockNavigator))
    every { spyAdapter.itemCount } returns 0

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.setAdapterForTest(spyAdapter)
      searchFragment.handleRefreshState(loadStates, notLoadingState)
    }
    shortDelay()

    illustration_error.isNotDisplayed()
    browse_genre_container.isNotDisplayed()
    rv_search.isNotDisplayed()
    illustration_search_no_result_view.isNotDisplayed()
  }

  @Test
  fun handleRefreshState_whenHasSearchResults_shouldShowActualData() {
    // mock search results
    val mockSearchResults = listOf(
      MultiSearchItem(
        id = 1,
        title = "Test Movie",
        overview = "Test overview",
        posterPath = "/test-poster.jpg",
        mediaType = "movie",
      ),
      MultiSearchItem(
        id = 2,
        title = "Another Movie",
        overview = "Another overview",
        posterPath = "/another-poster.jpg",
        mediaType = "movie",
      ),
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
    browse_genre_container.isNotDisplayed()
    illustration_search_no_result_view.isNotDisplayed()
  }
}
