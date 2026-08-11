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
  fun screenState_whenErrorOccurs_showsErrorIllustrationAndHidesEverythingElse() {
    setActiveQuery()
    emitLoadState(LoadState.Error(Throwable("Test error")))
    waitForDebounce()

    illustration_error.isDisplayed()
    browse_genre_container.isNotDisplayed()
    rv_search.isNotDisplayed()
    illustration_search_no_result_view.isNotDisplayed()
  }

  @Test
  fun screenState_whenRefreshLoading_showsShimmerLoadingState() {
    setActiveQuery()
    emitLoadState(LoadState.Loading)
    waitForDebounce()

    illustration_error.isNotDisplayed()
    browse_genre_container.isNotDisplayed()
    rv_search.isDisplayed()
    illustration_search_no_result_view.isNotDisplayed()
  }

  @Test
  fun screenState_whenReachedEndOfPagingWithZeroItems_showsNoResultsView() {
    val spyAdapter = spyk(SearchAdapter(mockNavigator))
    every { spyAdapter.itemCount } returns 0

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.setAdapterForTest(spyAdapter)
    }
    setActiveQuery()
    emitLoadStates(loadStates)
    waitForDebounce()

    illustration_error.isNotDisplayed()
    browse_genre_container.isNotDisplayed()
    rv_search.isNotDisplayed()
    illustration_search_no_result_view.isDisplayed()
  }

  @Test
  fun screenState_whenStillFetchingMoreWithZeroItems_showsNothingYet() {
    val spyAdapter = spyk(SearchAdapter(mockNavigator))
    every { spyAdapter.itemCount } returns 0

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.setAdapterForTest(spyAdapter)
    }
    setActiveQuery()
    // refresh finished, append still in progress (not end of pagination), no items yet
    emitLoadState(LoadState.NotLoading(endOfPaginationReached = false))
    waitForDebounce()

    illustration_error.isNotDisplayed()
    browse_genre_container.isNotDisplayed()
    rv_search.isNotDisplayed()
    illustration_search_no_result_view.isNotDisplayed()
  }

  @Test
  fun screenState_whenHasSearchResults_showsActualData() {
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
    val pagingData = PagingData.from(mockSearchResults)

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      val adapter = SearchAdapter(mockNavigator)
      searchFragment.setAdapterForTest(adapter)
      adapter.submitData(searchFragment.lifecycle, pagingData)
    }
    shortDelay()

    setActiveQuery()
    emitLoadStates(loadStates)
    waitForDebounce()

    illustration_error.isNotDisplayed()
    rv_search.isDisplayed()
    browse_genre_container.isNotDisplayed()
    illustration_search_no_result_view.isNotDisplayed()
  }
}
