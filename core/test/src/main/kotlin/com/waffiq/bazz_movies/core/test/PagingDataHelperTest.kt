package com.waffiq.bazz_movies.core.test

import android.annotation.SuppressLint
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import io.mockk.coEvery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue

object PagingDataHelperTest {

  /**
   * Generic test for successful paging data with non-empty results
   */
  fun testSuccessfulPagingData(
    mockPagingData: PagingData<MediaResponseItem>,
    dataSourceCall: suspend () -> Flow<PagingData<MediaResponseItem>>,
    repositoryCall: suspend () -> Flow<PagingData<MediaItem>>,
    verifyDataSourceCall: () -> Unit,
  ) = runTest {
    val differ = differ<MediaItem>()

    coEvery { dataSourceCall() } returns flowOf(mockPagingData)

    repositoryCall().test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      val listMediaItem = differ.snapshot().items
      assertTrue(listMediaItem.isEmpty().not())
      job.cancel()

      cancelAndIgnoreRemainingEvents()
    }

    verifyDataSourceCall()
  }

  /**
   * Generic test for successful paging data with empty results
   */
  fun testEmptyPagingData(
    dataSourceCall: suspend () -> Flow<PagingData<MediaResponseItem>>,
    repositoryCall: suspend () -> Flow<PagingData<MediaItem>>,
    verifyDataSourceCall: () -> Unit,
  ) = runTest {
    val differ = differ<MediaItem>()
    val emptyPagingData = PagingData.from(emptyList<MediaResponseItem>())
    coEvery { dataSourceCall() } returns flowOf(emptyPagingData)

    repositoryCall().test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      assertTrue(differ.snapshot().items.isEmpty())
      job.cancel()

      cancelAndIgnoreRemainingEvents()
    }

    verifyDataSourceCall()
  }

  class TestListCallback : ListUpdateCallback {
    override fun onChanged(
      position: Int,
      count: Int,
      payload: Any?,
    ) {
      /* unused */
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
      /* unused */
    }

    override fun onInserted(position: Int, count: Int) {
      /* unused */
    }

    override fun onRemoved(position: Int, count: Int) {
      /* unused */
    }
  }

  class TestDiffCallback<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean = oldItem == newItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean =
      oldItem == newItem
  }

  /**
   * Create an AsyncPagingDataDiffer for testing purposes.
   *
   * @return An instance of AsyncPagingDataDiffer with a test diff callback and list callback.
   */
  fun <T : Any> differ(): AsyncPagingDataDiffer<T> =
    AsyncPagingDataDiffer(
      diffCallback = TestDiffCallback(),
      updateCallback = TestListCallback(),
      workerDispatcher = Dispatchers.Main,
    )
}
