package com.waffiq.bazz_movies.feature.search.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.domain.usecase.MultiSearchUseCase
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.differ
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

  private val mockMultiSearchUseCase: MultiSearchUseCase = mockk()
  private lateinit var searchViewModel: SearchViewModel
  private val tv = MultiSearchItem(
    title = "Transformers TV-series",
    mediaType = "tv",
    id = 12345,
    voteCount = 2222.0,
    backdropPath = "/backdrop_path0.jpg",
    posterPath = "/poster_path0.jpg",
    profilePath = "/profile_path0.jpg",
    adult = false
  )
  private val movie = MultiSearchItem(
    title = "Transformers 2",
    mediaType = "movie",
    id = 333111,
    voteCount = 3333.0,
    backdropPath = "/backdrop_path1.jpg",
    posterPath = "/poster_path1.jpg",
    profilePath = "/profile_path1.jpg",
    adult = false
  )

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    searchViewModel = SearchViewModel(mockMultiSearchUseCase)
  }

  @Test
  fun search_fromUseCase_emitsPagingData() = runTest {
    val pagingData = PagingData.from(listOf(tv, movie))
    coEvery { mockMultiSearchUseCase.search("Transformers") } returns flow { emit(pagingData) }

    searchViewModel.searchResults.test {
      searchViewModel.search("Transformers")

      val result = awaitItem()
      differ.submitData(result)
      advanceUntilIdle()

      assertEquals(listOf(tv, movie), differ.snapshot().items)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun searchResults_whenInitialState_shouldBeEmpty() = runTest {
    searchViewModel.searchResults.test {
      val initial = awaitItem()
      differ.submitData(initial)
      advanceUntilIdle()

      assertEquals(emptyList<MultiSearchItem>(), differ.snapshot().items)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun search_whenCancelsPreviousSearch_emitsLatestResult() = runTest {
    val pagingData1 = PagingData.from(listOf(tv))
    val pagingData2 = PagingData.from(listOf(movie))

    coEvery { mockMultiSearchUseCase.search("Transformers") } returns flow { emit(pagingData1) }
    coEvery { mockMultiSearchUseCase.search("Transformers 2") } returns flow { emit(pagingData2) }

    searchViewModel.searchResults.test {
      searchViewModel.search("Transformers")
      advanceUntilIdle()

      searchViewModel.search("Transformers 2")
      advanceUntilIdle()

      // Skip to the last emission
      val result = expectMostRecentItem()
      differ.submitData(result)
      advanceUntilIdle()

      assertEquals(listOf(movie), differ.snapshot().items)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun search_whenEmptyFlow_doesNotUpdateSearchResults() = runTest {
    coEvery { mockMultiSearchUseCase.search("Transformers") } returns emptyFlow()

    searchViewModel.searchResults.test {
      searchViewModel.search("Transformers")
      advanceUntilIdle()

      val result = awaitItem() // should be empty PagingData
      differ.submitData(result)
      advanceUntilIdle()

      assertEquals(emptyList<MultiSearchItem>(), differ.snapshot().items)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `search cancels previous collection when new search is triggered`() = runTest {
    val pagingData1 = PagingData.from(listOf(tv))
    val pagingData2 = PagingData.from(listOf(movie))

    coEvery { mockMultiSearchUseCase.search("Transformers") } returns flow {
      emit(pagingData1)
      delay(Long.MAX_VALUE) // keeps collecting so second search cancels it
    }
    coEvery { mockMultiSearchUseCase.search("Transformers 2") } returns flow {
      emit(pagingData2)
    }

    searchViewModel.searchResults.test {
      searchViewModel.search("Transformers")
      advanceUntilIdle()

      // This triggers the cancellation branch of the first collectLatest
      searchViewModel.search("Transformers 2")
      advanceUntilIdle()

      val result = expectMostRecentItem()
      differ.submitData(result)
      advanceUntilIdle()

      assertEquals(listOf(movie), differ.snapshot().items)
      cancelAndIgnoreRemainingEvents()
    }
  }
}
