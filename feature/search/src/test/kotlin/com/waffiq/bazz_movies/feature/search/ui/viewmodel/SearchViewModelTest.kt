package com.waffiq.bazz_movies.feature.search.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.common.MediaType
import com.waffiq.bazz_movies.core.database.domain.usecase.SearchHistoryLocalDatabaseUseCase
import com.waffiq.bazz_movies.core.models.SearchHistory
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.domain.usecase.MultiSearchUseCase
import com.waffiq.bazz_movies.feature.search.testutils.DumyData.differ
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

class SearchViewModelTest {

  private val mockMultiSearchUseCase: MultiSearchUseCase = mockk()
  private val mockSearchHistoryLocalDatabaseUseCase: SearchHistoryLocalDatabaseUseCase = mockk()
  private lateinit var searchViewModel: SearchViewModel
  private val testQuery = "Transformers"

  private val searchHistory = SearchHistory(1, "query", 100L)
  private val tv = MultiSearchItem(
    title = "Transformers TV-series",
    mediaType = "tv",
    id = 12345,
    voteCount = 2222.0,
    backdropPath = "/backdrop_path0.jpg",
    posterPath = "/poster_path0.jpg",
    profilePath = "/profile_path0.jpg",
    adult = false,
  )
  private val movie = MultiSearchItem(
    title = "Transformers 2",
    mediaType = "movie",
    id = 333111,
    voteCount = 3333.0,
    backdropPath = "/backdrop_path1.jpg",
    posterPath = "/poster_path1.jpg",
    profilePath = "/profile_path1.jpg",
    adult = false,
  )

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    coEvery { mockSearchHistoryLocalDatabaseUseCase.getSearchHistory() } returns
      flowOf(listOf(searchHistory))
    coEvery { mockSearchHistoryLocalDatabaseUseCase.trimHistory() } returns 1
    coEvery { mockSearchHistoryLocalDatabaseUseCase.delete(any<SearchHistory>()) } returns 1
    coEvery { mockSearchHistoryLocalDatabaseUseCase.deleteAll() } returns 1
    coEvery { mockSearchHistoryLocalDatabaseUseCase.insert(any<SearchHistory>()) } just Runs

    searchViewModel = SearchViewModel(
      mockMultiSearchUseCase,
      mockSearchHistoryLocalDatabaseUseCase,
    )
  }

  @Test
  fun search_fromUseCase_emitsPagingData() =
    runTest {
      stubSearchResult()

      searchViewModel.searchResults.test {
        // peform search and filtering
        searchViewModel.search(testQuery)
        searchViewModel.setFilters(setOf(MediaType.MOVIE))

        // assert the changes
        assertEquals(testQuery, searchViewModel.currentQuery.value)
        assertEquals(setOf(MediaType.MOVIE), searchViewModel.selectedFilters.value)

        searchViewModel.currentQuery.test {
          assertEquals("Transformers", awaitItem())
        }

        differ.submitData(awaitItem())
        advanceUntilIdle()
        assertEquals(listOf(tv, movie), differ.snapshot().items)

        searchViewModel.clearSearch()
        differ.submitData(awaitItem())
        advanceUntilIdle()
        assertEquals(emptyList<MultiSearchItem>(), differ.snapshot().items)

        // should clear filter and last query
        assertEquals(setOf(MediaType.MULTI), searchViewModel.selectedFilters.value)
        assertNull(searchViewModel.currentQuery.value)

        cancelAndIgnoreRemainingEvents()
      }

      coVerify { mockMultiSearchUseCase.search(any(), any()) }
      coVerify { mockSearchHistoryLocalDatabaseUseCase.trimHistory() }
    }

  @Test
  fun search_sameQuery_triggerSearchOnce() =
    runTest {
      stubSearchResult()

      val job = launch {
        searchViewModel.searchResults.collect()
      }

      searchViewModel.search(testQuery)
      searchViewModel.search(testQuery)

      advanceUntilIdle()

      coVerify(exactly = 1) {
        mockMultiSearchUseCase.search(testQuery, any())
      }

      job.cancel()
    }

  @Test
  fun searchResults_whenInitialState_shouldBeEmpty() =
    runTest {
      searchViewModel.searchResults.test {
        val initial = awaitItem()
        differ.submitData(initial)
        advanceUntilIdle()

        assertEquals(emptyList<MultiSearchItem>(), differ.snapshot().items)
        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun searchHistory_whenHistoryAvailable_shouldReturnCorrectly() =
    runTest {
      searchViewModel.searchHistory.test {
        assertEquals(emptyList<SearchHistory>(), awaitItem())

        val emittedData = awaitItem()
        assertEquals(searchHistory, emittedData[0])
      }
    }

  @Test
  fun search_whenCancelsPreviousSearch_emitsLatestResult() =
    runTest {
      val pagingData1 = PagingData.from(listOf(tv))
      val pagingData2 = PagingData.from(listOf(movie))

      coEvery { mockMultiSearchUseCase.search(testQuery) } returns flow { emit(pagingData1) }
      coEvery { mockMultiSearchUseCase.search("Transformers 2") } returns flow { emit(pagingData2) }

      searchViewModel.searchResults.test {
        searchViewModel.search(testQuery)
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
  fun search_whenEmptyFlow_doesNotUpdateSearchResults() =
    runTest {
      coEvery { mockMultiSearchUseCase.search(testQuery, any()) } returns
        emptyFlow()

      searchViewModel.searchResults.test {
        awaitItem() // initial PagingData.empty()

        searchViewModel.search(testQuery)
        advanceUntilIdle()

        expectNoEvents()
        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun search_whenEmptyResult_updatesSearchResultsWithEmptyPagingData() =
    runTest {
      coEvery { mockMultiSearchUseCase.search(testQuery, any()) } returns
        flowOf(PagingData.empty())

      searchViewModel.searchResults.test {
        awaitItem() // initial empty PagingData

        searchViewModel.search(testQuery)
        advanceUntilIdle()

        val result = awaitItem()

        differ.submitData(result)
        advanceUntilIdle()

        assertEquals(emptyList<MultiSearchItem>(), differ.snapshot().items)
        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun search_whenNewSearchTriggered_cancelsPreviousCollection() =
    runTest {
      val pagingData1 = PagingData.from(listOf(tv))
      val pagingData2 = PagingData.from(listOf(movie))

      coEvery { mockMultiSearchUseCase.search(testQuery) } returns flow {
        emit(pagingData1)
        delay(Long.MAX_VALUE.milliseconds) // keeps collecting so second search cancels it
      }
      coEvery { mockMultiSearchUseCase.search("Transformers 2") } returns flow {
        emit(pagingData2)
      }

      searchViewModel.searchResults.test {
        searchViewModel.search(testQuery)
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

  @Test
  fun setFilters_whenEmpty_setMultiType() {
    searchViewModel.setFilters(setOf(MediaType.PERSON))
    assertEquals(setOf(MediaType.PERSON), searchViewModel.selectedFilters.value)
  }

  @Test
  fun setFilters_setAsPerson_setPersonType() {
    searchViewModel.setFilters(setOf())
    assertEquals(setOf(MediaType.MULTI), searchViewModel.selectedFilters.value)
  }

  @Test
  fun deleteHistory_whenCalled_shouldCallsCorrectFunction() =
    runTest {
      searchViewModel.deleteHistory(SearchHistory(1, "query", 100L))
      advanceUntilIdle()
      coVerify { mockSearchHistoryLocalDatabaseUseCase.delete(any()) }
    }

  @Test
  fun deleteAllHistory_whenCalled_shouldCallsCorrectFunction() =
    runTest {
      searchViewModel.deleteAllHistory()
      advanceUntilIdle()
      coVerify { mockSearchHistoryLocalDatabaseUseCase.deleteAll() }
    }

  private fun stubSearchResult() {
    val pagingData = PagingData.from(listOf(tv, movie))
    coEvery { mockMultiSearchUseCase.search(any(), any()) } returns flow { emit(pagingData) }
  }
}
