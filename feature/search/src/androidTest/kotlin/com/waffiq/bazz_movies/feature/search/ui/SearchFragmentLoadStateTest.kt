package com.waffiq.bazz_movies.feature.search.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.waitFor
import com.waffiq.bazz_movies.core.instrumentationtest.launchFragmentInHiltContainer
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.feature.search.R.id.illustration_error
import com.waffiq.bazz_movies.feature.search.R.id.illustration_search_no_result_view
import com.waffiq.bazz_movies.feature.search.R.id.illustration_search_view
import com.waffiq.bazz_movies.feature.search.R.id.rv_search
import com.waffiq.bazz_movies.feature.search.domain.model.ResultsItemSearch
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
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SearchFragmentLoadStateTest {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @Rule
  @JvmField
  var mInstantTaskExecutorRule = InstantTaskExecutorRule()

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
    every { mockSearchViewModel.query } returns MutableLiveData()
    every { mockSearchViewModel.expandSearchView } returns MutableLiveData()
    every { mockSearchViewModel.search(any()) } just Runs
    every { mockSearchViewModel.setExpandSearchView(any()) } just Runs
    every { mockSnackbar.showSnackbarWarning(any<String>()) } returns mockk(relaxed = true)
  }

  @Test
  fun handleRefreshState_whenErrorOccurs_shouldHiddenRecyclerView() {
    val searchFragment = launchFragmentInHiltContainer<SearchFragment>()

    // wait for fragment to initialize
    onView(isRoot()).perform(waitFor(1000))

    // get the fragment instance and call the error method directly
    val testError = Throwable("Test error")
    val errorState = LoadState.Error(testError)

    val loadStates = CombinedLoadStates(
      refresh = errorState,
      prepend = LoadState.NotLoading(false),
      append = LoadState.NotLoading(false),
      source = LoadStates(
        refresh = errorState,
        prepend = LoadState.NotLoading(false),
        append = LoadState.NotLoading(false)
      ),
      mediator = null
    )

    // call the method directly instead of relying on adapter flow
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.handleRefreshState(loadStates, errorState)
    }
    // small delay for UI update
    onView(isRoot()).perform(waitFor(1000))

    onView(withId(illustration_error)).check(matches(isDisplayed()))
    onView(withId(illustration_search_view)).check(matches(not(isDisplayed())))
    onView(withId(rv_search)).check(matches(not(isDisplayed())))
    onView(withId(illustration_search_no_result_view)).check(matches(not(isDisplayed())))
  }

  @Test
  fun handleRefreshState_whenLoading_shouldShowLoadingState() {
    val searchFragment = launchFragmentInHiltContainer<SearchFragment>()
    onView(isRoot()).perform(waitFor(1000))

    // create loading state (not error state for loading test)
    val loadingState = LoadState.Loading
    val loadStates = CombinedLoadStates(
      refresh = loadingState,
      prepend = LoadState.NotLoading(false),
      append = LoadState.NotLoading(false),
      source = LoadStates(
        refresh = loadingState,
        prepend = LoadState.NotLoading(false),
        append = LoadState.NotLoading(false)
      ),
      mediator = null
    )

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.handleRefreshState(loadStates, loadingState)
    }

    onView(isRoot()).perform(waitFor(1000))

    onView(withId(illustration_error)).check(matches(not(isDisplayed())))
    onView(withId(illustration_search_view)).check(matches(not(isDisplayed())))
    onView(withId(rv_search)).check(matches(isDisplayed()))
    onView(withId(illustration_search_no_result_view)).check(matches(not(isDisplayed())))
  }

  @Test
  fun handleRefreshState_whenReachedEndOfPaging_shouldShowNoResultsView() {
    val searchFragment = launchFragmentInHiltContainer<SearchFragment>()
    onView(isRoot()).perform(waitFor(1000))

    val notLoadingState = LoadState.NotLoading(endOfPaginationReached = true)
    val loadStates = CombinedLoadStates(
      refresh = notLoadingState,
      prepend = LoadState.NotLoading(false),
      append = notLoadingState, // needs for endOfPaginationReached = true
      source = LoadStates(
        refresh = notLoadingState,
        prepend = LoadState.NotLoading(false),
        append = notLoadingState
      ),
      mediator = null
    )

    val spyAdapter = spyk(SearchAdapter(mockNavigator))
    every { spyAdapter.itemCount } returns 0


    // set adapter
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.setAdapterForTest(spyAdapter)
    }
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.handleRefreshState(loadStates, notLoadingState)
    }

    onView(isRoot()).perform(waitFor(1000))

    onView(withId(illustration_error)).check(matches(not(isDisplayed())))
    onView(withId(illustration_search_view)).check(matches(not(isDisplayed())))
    onView(withId(rv_search)).check(matches(not(isDisplayed())))

    // no result should show
    onView(withId(illustration_search_no_result_view)).check(matches(isDisplayed()))
  }

  @Test
  fun handleRefreshState_whenNotReachedEndOfPaging_shouldShowsViewCorrectly() {
    val searchFragment = launchFragmentInHiltContainer<SearchFragment>()
    onView(isRoot()).perform(waitFor(1000))

    val notLoadingState = LoadState.NotLoading(endOfPaginationReached = false)
    val loadStates = CombinedLoadStates(
      refresh = notLoadingState,
      prepend = LoadState.NotLoading(false),
      append = notLoadingState,
      source = LoadStates(
        refresh = notLoadingState,
        prepend = LoadState.NotLoading(false),
        append = notLoadingState
      ),
      mediator = null
    )
    val spyAdapter = spyk(SearchAdapter(mockNavigator))
    every { spyAdapter.itemCount } returns 0

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.setAdapterForTest(spyAdapter)
      searchFragment.handleRefreshState(loadStates, notLoadingState)
    }

    onView(isRoot()).perform(waitFor(1000))

    onView(withId(illustration_error)).check(matches(not(isDisplayed())))
    onView(withId(illustration_search_view)).check(matches(isDisplayed()))
    onView(withId(rv_search)).check(matches(not(isDisplayed())))

    // no result should not show
    onView(withId(illustration_search_no_result_view)).check(matches(not(isDisplayed())))
  }

  @Test
  fun handleRefreshState_whenHasSearchResults_shouldShowActualData() {
    val searchFragment = launchFragmentInHiltContainer<SearchFragment>()
    onView(isRoot()).perform(waitFor(1000))

    val notLoadingState = LoadState.NotLoading(endOfPaginationReached = false)
    val loadStates = CombinedLoadStates(
      refresh = notLoadingState,
      prepend = LoadState.NotLoading(false),
      append = notLoadingState,
      source = LoadStates(
        refresh = notLoadingState,
        prepend = LoadState.NotLoading(false),
        append = notLoadingState
      ),
      mediator = null
    )

    // mock search results
    val mockSearchResults = listOf(
      ResultsItemSearch(
        id = 1,
        title = "Test Movie",
        overview = "Test overview",
        posterPath = "/test-poster.jpg",
        mediaType = "movie"
      ),
      ResultsItemSearch(
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

    onView(isRoot()).perform(waitFor(2000))

    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      searchFragment.handleRefreshState(loadStates, notLoadingState)
    }

    onView(isRoot()).perform(waitFor(1000))

    onView(withId(illustration_error)).check(matches(not(isDisplayed())))
    onView(withId(illustration_search_view)).check(matches(not(isDisplayed())))
    onView(withId(illustration_search_no_result_view)).check(matches(not(isDisplayed())))

    // verify actual data is shown
    onView(withId(rv_search)).check(matches(isDisplayed()))
  }
}
