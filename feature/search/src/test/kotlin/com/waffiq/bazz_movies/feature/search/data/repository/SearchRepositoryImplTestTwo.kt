package com.waffiq.bazz_movies.feature.search.data.repository

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponseItem
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchRepositoryImplTestTwo {
  private val testDispatcher = StandardTestDispatcher()
  private lateinit var mockMovieDataSource: MovieDataSource
  private lateinit var searchRepository: SearchRepositoryImpl

  @Before
  fun setup() {
    Dispatchers.setMain(testDispatcher)
    mockMovieDataSource = mockk()
    searchRepository = SearchRepositoryImpl(mockMovieDataSource)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun search_whenSearching_shouldTransformDataCorrectly() = runTest {
    val query = "Avengers"

    val responseItem1 = MultiSearchResponseItem(id = 1, title = "Mock Movie 1")
    val responseItem2 = MultiSearchResponseItem(id = 2, title = "Mock Movie 2")

    val mockPagingData = PagingData.from(listOf(responseItem1, responseItem2))

    val expectedItem1 = MultiSearchItem(id = 1, title = "Mock Movie 1")
    val expectedItem2 = MultiSearchItem(id = 2, title = "Mock Movie 2")

    every { mockMovieDataSource.search(query) } returns flowOf(mockPagingData)
    val resultPagingData = searchRepository.search(query).first()

    // Then
    // Use AsyncPagingDataDiffer to extract the actual items
    val differ = AsyncPagingDataDiffer(
      diffCallback = object : DiffUtil.ItemCallback<MultiSearchItem>() {
        override fun areItemsTheSame(
          oldItem: MultiSearchItem,
          newItem: MultiSearchItem
        ): Boolean {
          return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
          oldItem: MultiSearchItem,
          newItem: MultiSearchItem
        ): Boolean {
          return oldItem == newItem
        }
      },
      updateCallback = NoopListUpdateCallback(),
      workerDispatcher = Dispatchers.Main
    )

    differ.submitData(resultPagingData)
    testDispatcher.scheduler.advanceUntilIdle() // Wait for async operations

    // Verify the transformed items
    val mediaItems = differ.snapshot().items
    assertEquals(2, mediaItems.size)
    assertEquals(expectedItem1, mediaItems[0])
    assertEquals(expectedItem2, mediaItems[1])

    // Verify method was called
    verify(exactly = 1) { mockMovieDataSource.search(query) }
    confirmVerified(mockMovieDataSource)
  }

  // No-op implementation of ListUpdateCallback for testing
  private class NoopListUpdateCallback : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) { /* no used */ }
    override fun onRemoved(position: Int, count: Int) { /* no used */ }
    override fun onMoved(fromPosition: Int, toPosition: Int) { /* no used */ }
    override fun onChanged(position: Int, count: Int, payload: Any?) { /* no used */ }
  }
}
