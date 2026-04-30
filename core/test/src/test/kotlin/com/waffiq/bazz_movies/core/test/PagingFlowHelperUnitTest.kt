package com.waffiq.bazz_movies.core.test

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.test.PagingFlowHelperTest.testPagingFlowAwaitComplete
import com.waffiq.bazz_movies.core.test.PagingFlowHelperTest.testPagingFlowCancelRemaining
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PagingFlowHelperUnitTest {

  @get:Rule
  val dispatcherRule = UnconfinedDispatcherRule()

  private val fakeItems = listOf(
    MediaItem(id = 1, title = "Movie 1", mediaType = "movie"),
    MediaItem(id = 2, title = "Movie 2", mediaType = "movie"),
    MediaItem(id = 3, name = "Show 1", mediaType = "tv"),
  )

  private fun fakePagingFlow(items: List<MediaItem>): Flow<PagingData<MediaItem>> =
    flowOf(PagingData.from(items))

  @Test
  fun testPagingFlowAwaitComplete_whenPagingContainsData_shouldReturnAllItems() =
    runTest {
      testPagingFlowAwaitComplete(
        flow = fakePagingFlow(fakeItems),
      ) { items ->
        assertEquals(fakeItems.size, items.size)
        assertEquals(fakeItems, items)
      }
    }

  @Test
  fun testPagingFlowAwaitComplete_whenPagingIsEmpty_shouldReturnEmptyList() =
    runTest {
      testPagingFlowAwaitComplete(
        flow = fakePagingFlow(emptyList()),
      ) { items ->
        assertTrue(items.isEmpty())
      }
    }

  @Test
  fun testPagingFlowAwaitComplete_whenPagingContainsData_shouldPreserveItemOrder() =
    runTest {
      val orderedItems = listOf(
        MediaItem(id = 1, title = "First"),
        MediaItem(id = 2, title = "Second"),
        MediaItem(id = 3, title = "Third"),
      )

      testPagingFlowAwaitComplete(
        flow = fakePagingFlow(orderedItems),
      ) { items ->
        assertEquals(orderedItems[0], items[0])
        assertEquals(orderedItems[1], items[1])
        assertEquals(orderedItems[2], items[2])
      }
    }

  @Test
  fun testPagingFlowAwaitComplete_whenPagingContainsSingleItem_shouldReturnSingleItem() =
    runTest {
      val singleItem = listOf(MediaItem(id = 42, title = "Only Movie"))

      testPagingFlowAwaitComplete(
        flow = fakePagingFlow(singleItem),
      ) { items ->
        assertEquals(1, items.size)
        assertEquals(42, items.first().id)
      }
    }

  @Test
  fun testPagingFlowCancelRemaining_whenPagingContainsData_shouldReturnAllItems() =
    runTest {
      testPagingFlowCancelRemaining(
        flow = fakePagingFlow(fakeItems),
      ) { items ->
        assertEquals(fakeItems.size, items.size)
        assertEquals(fakeItems, items)
      }
    }

  @Test
  fun testPagingFlowCancelRemaining_whenPagingIsEmpty_shouldReturnEmptyList() =
    runTest {
      testPagingFlowCancelRemaining(
        flow = fakePagingFlow(emptyList()),
      ) { items ->
        assertTrue(items.isEmpty())
      }
    }

  @Test
  fun testPagingFlowCancelRemaining_whenFlowDoesNotComplete_shouldStillReturnEmittedItems() =
    runTest {
      // A flow that never completes, simulates a real paging source
      val continuousFlow = flow {
        emit(PagingData.from(fakeItems))
        delay(Long.MAX_VALUE) // never completes
      }

      testPagingFlowCancelRemaining(
        flow = continuousFlow,
      ) { items ->
        assertEquals(fakeItems.size, items.size)
      }
    }

  @Test
  fun testPagingFlowAwaitComplete_whenPagingContainsMediaItem_shouldMapFieldsCorrectly() =
    runTest {
      val expected = MediaItem(
        id = 99,
        title = "Inception",
        mediaType = "movie",
        voteAverage = 8.8f,
        popularity = 100.0,
        adult = false,
      )

      testPagingFlowAwaitComplete(
        flow = fakePagingFlow(listOf(expected)),
      ) { items ->
        val actual = items.first()
        assertEquals(expected.id, actual.id)
        assertEquals(expected.title, actual.title)
        assertEquals(expected.mediaType, actual.mediaType)
        assertEquals(expected.voteAverage, actual.voteAverage)
        assertEquals(expected.popularity, actual.popularity)
        assertEquals(expected.adult, actual.adult)
      }
    }
}
