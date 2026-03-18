package com.waffiq.bazz_movies.core.test

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.differ
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

object PagingFlowHelperTest {

  /**
   * Test a PagingData flow and assert the expected items.
   */
  suspend fun <T : Any> testPagingFlowAwaitComplete(
    flow: Flow<PagingData<T>>,
    differ: AsyncPagingDataDiffer<T> = differ(),
    expectedAssertions: (List<T>) -> Unit,
  ) {
    flow.test {
      val actualPagingData = awaitItem()
      val job = launch { differ.submitData(actualPagingData) }
      job.join()
      yield()

      val pagingList = differ.snapshot().items
      expectedAssertions(pagingList)

      job.cancel()
      awaitComplete()
    }
  }

  /**
   * Test a PagingData flow with a custom dispatcher and assert the expected items.
   */
  suspend fun testPagingFlowCancelRemaining(
    flow: Flow<PagingData<MediaItem>>,
    differ: AsyncPagingDataDiffer<MediaItem> = differ(),
    expectedAssertions: (List<MediaItem>) -> Unit,
  ) {
    flow.test {
      val actualPagingData = awaitItem()
      val job = launch { differ.submitData(actualPagingData) }
      job.join()
      yield()

      val pagingList = differ.snapshot().items
      expectedAssertions(pagingList)

      job.cancel()
      cancelAndIgnoreRemainingEvents()
    }
  }
}
