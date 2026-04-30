package com.waffiq.bazz_movies.core.test

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.TestDiffCallback
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.differ
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.testEmptyPagingData
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.testSuccessfulPagingData
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFailsWith

class PagingDataHelperUnitTest {

  @get:Rule
  val dispatcherRule = UnconfinedDispatcherRule()

  private val fakeResponseItems = listOf(
    MediaResponseItem(id = 1, title = "Movie 1", mediaType = "movie"),
    MediaResponseItem(id = 2, title = "Movie 2", mediaType = "movie"),
    MediaResponseItem(id = 3, name = "Show 1", mediaType = "tv"),
  )

  private val fakeMappedItems = listOf(
    MediaItem(id = 1, title = "Movie 1", mediaType = "movie"),
    MediaItem(id = 2, title = "Movie 2", mediaType = "movie"),
    MediaItem(id = 3, name = "Show 1", mediaType = "tv"),
  )

  private val mockDataSourceCall: suspend () -> Flow<PagingData<MediaResponseItem>> = mockk()
  private val mockRepositoryCall: suspend () -> Flow<PagingData<MediaItem>> = mockk()
  private val mockVerify: () -> Unit = mockk(relaxed = true)

  @Test
  fun testSuccessfulPagingData_whenRepositoryReturnsMappedData_shouldPass() =
    runTest {
      val mockPagingData = PagingData.from(fakeResponseItems)

      coEvery { mockDataSourceCall() } returns flowOf(mockPagingData)
      coEvery { mockRepositoryCall() } returns flowOf(PagingData.from(fakeMappedItems))

      testSuccessfulPagingData(
        mockPagingData = mockPagingData,
        dataSourceCall = mockDataSourceCall,
        repositoryCall = mockRepositoryCall,
        verifyDataSourceCall = mockVerify,
      )

      verify { mockVerify() }
    }

  @Test
  fun testSuccessfulPagingData_whenRepositoryReturnsEmptyData_shouldFail() =
    runTest {
      val mockPagingData = PagingData.from(fakeResponseItems)

      coEvery { mockDataSourceCall() } returns flowOf(mockPagingData)
      coEvery { mockRepositoryCall() } returns flowOf(PagingData.from(emptyList()))

      assertFailsWith<AssertionError> {
        testSuccessfulPagingData(
          mockPagingData = mockPagingData,
          dataSourceCall = mockDataSourceCall,
          repositoryCall = mockRepositoryCall,
          verifyDataSourceCall = mockVerify,
        )
      }
    }

  @Test
  fun testEmptyPagingData_whenRepositoryReturnsEmptyData_shouldPass() =
    runTest {
      coEvery { mockDataSourceCall() } returns flowOf(PagingData.from(emptyList()))
      coEvery { mockRepositoryCall() } returns flowOf(PagingData.from(emptyList()))

      testEmptyPagingData(
        dataSourceCall = mockDataSourceCall,
        repositoryCall = mockRepositoryCall,
        verifyDataSourceCall = mockVerify,
      )

      verify { mockVerify() }
    }

  @Test
  fun testEmptyPagingData_whenRepositoryReturnsNonEmptyData_shouldFail() =
    runTest {
      coEvery { mockDataSourceCall() } returns flowOf(PagingData.from(emptyList()))
      coEvery { mockRepositoryCall() } returns flowOf(PagingData.from(fakeMappedItems))

      assertFailsWith<AssertionError> {
        testEmptyPagingData(
          dataSourceCall = mockDataSourceCall,
          repositoryCall = mockRepositoryCall,
          verifyDataSourceCall = mockVerify,
        )
      }
    }

  @Test
  fun areItemsTheSame_whenItemsAreIdentical_shouldReturnTrue() {
    val callback = TestDiffCallback<MediaItem>()
    val item = MediaItem(id = 1, title = "Movie")
    assertTrue(callback.areItemsTheSame(item, item))
  }

  @Test
  fun areItemsTheSame_whenItemsAreDifferent_shouldReturnFalse() {
    val callback = TestDiffCallback<MediaItem>()
    val item1 = MediaItem(id = 1, title = "Movie 1")
    val item2 = MediaItem(id = 2, title = "Movie 2")
    assertFalse(callback.areItemsTheSame(item1, item2))
  }

  @Test
  fun areContentsTheSame_whenContentsAreIdentical_shouldReturnTrue() {
    val callback = TestDiffCallback<MediaItem>()
    val item = MediaItem(id = 1, title = "Movie")
    assertTrue(callback.areContentsTheSame(item, item))
  }

  @Test
  fun areContentsTheSame_whenContentsAreDifferent_shouldReturnFalse() {
    val callback = TestDiffCallback<MediaItem>()
    val item1 = MediaItem(id = 1, title = "Movie 1")
    val item2 = MediaItem(id = 1, title = "Movie 2") // same id, different content
    assertFalse(callback.areContentsTheSame(item1, item2))
  }

  @Test
  fun testListCallback_whenInvoked_shouldNotThrow() {
    val callback = PagingDataHelperTest.TestListCallback()
    // All methods are no-ops — just verify they don't throw
    callback.onChanged(0, 1, null)
    callback.onMoved(0, 1)
    callback.onInserted(0, 1)
    callback.onRemoved(0, 1)
  }

  @Test
  fun differ_whenInitialized_shouldHaveEmptySnapshot() =
    runTest {
      val differ = differ<MediaItem>()
      assertTrue(differ.snapshot().items.isEmpty())
    }

  @Test
  fun differ_whenSubmittingPagingData_shouldContainSubmittedItems() =
    runTest {
      val differ = differ<MediaItem>()

      val job = launch { differ.submitData(PagingData.from(fakeMappedItems)) }
      advanceUntilIdle()

      assertEquals(fakeMappedItems, differ.snapshot().items)
      job.cancel()
    }
}
