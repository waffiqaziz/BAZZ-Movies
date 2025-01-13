package com.waffiq.bazz_movies.feature.search.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.search.domain.model.ResultsItemSearch
import com.waffiq.bazz_movies.feature.search.domain.usecase.MultiSearchUseCase
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.QUERY
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.differ
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

  private val mockMultiSearchUseCase: MultiSearchUseCase = mockk()
  private lateinit var searchViewModel: SearchViewModel

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    searchViewModel = SearchViewModel(mockMultiSearchUseCase)
  }

  @Test
  fun search_shouldUpdateSearchResultsFlowAndQueryLiveDataCorrectly() = runTest {
    val fakePagingData = PagingData.from(
      listOf(
        ResultsItemSearch(
          title = "Transformers TV-series",
          mediaType = "tv",
          id = 12345,
          voteCount = 2222.0,
          backdropPath = "/backdrop_path0.jpg",
          posterPath = "/poster_path0.jpg",
          profilePath = "/profile_path0.jpg",
          adult = false
        ),
        ResultsItemSearch(
          title = "Transformers 2",
          mediaType = "movie",
          id = 333111,
          voteCount = 3333.0,
          backdropPath = "/backdrop_path1.jpg",
          posterPath = "/poster_path1.jpg",
          profilePath = "/profile_path1.jpg",
          adult = false
        )
      )
    )

    // mock the behavior of multiSearchUseCase.search
    every { mockMultiSearchUseCase.search(QUERY) } returns flowOf(fakePagingData)

    // assert query before search
    assertEquals("", searchViewModel.query.value)

    // call the function to be tested
    searchViewModel.search(QUERY)

    // assert query after search
    assertEquals(QUERY, searchViewModel.query.value)

    searchViewModel.searchResults.test {
      val emittedPagingData = awaitItem()
      differ.submitData(emittedPagingData)
      advanceUntilIdle()

      // Extract the items for assertions
      val pagingList = differ.snapshot().items
      assertTrue(pagingList.isNotEmpty())
      assertEquals("Transformers TV-series", pagingList[0].title)
      assertEquals("tv", pagingList[0].mediaType)
      assertEquals(12345, pagingList[0].id)
      assertEquals(2222.0, pagingList[0].voteCount)
      assertEquals("/backdrop_path0.jpg", pagingList[0].backdropPath)

      assertEquals("Transformers 2", pagingList[1].title)
      assertEquals("movie", pagingList[1].mediaType)
      assertEquals(333111, pagingList[1].id)
      assertEquals(3333.0, pagingList[1].voteCount)
      assertEquals("/backdrop_path1.jpg", pagingList[1].backdropPath)
    }

    verify { mockMultiSearchUseCase.search(QUERY) }
  }

  @Test
  fun setExpandSearchView_shouldUpdateLiveData() {
    searchViewModel.setExpandSearchView(true)
    assertEquals(true, searchViewModel.expandSearchView.value)

    searchViewModel.setExpandSearchView(false)
    assertEquals(false, searchViewModel.expandSearchView.value)
  }
}
