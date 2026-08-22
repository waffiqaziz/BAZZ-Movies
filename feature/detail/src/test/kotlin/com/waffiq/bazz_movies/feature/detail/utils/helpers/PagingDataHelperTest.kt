package com.waffiq.bazz_movies.feature.detail.utils.helpers

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PagingDataHelperTest {

  private lateinit var adapter: PagingDataAdapter<Any, *>
  private val listenerSlot = slot<(CombinedLoadStates) -> Unit>()

  @Before
  fun setUp() {
    adapter = mockk(relaxed = true)
    every { adapter.addLoadStateListener(capture(listenerSlot)) } returns Unit
  }

  private fun triggerListener(refreshState: LoadState, itemCount: Int) {
    every { adapter.itemCount } returns itemCount
    val loadStates = mockk<CombinedLoadStates>()
    every { loadStates.refresh } returns refreshState
    listenerSlot.captured.invoke(loadStates)
  }

  @Test
  fun observeEmptyState_whenLoadingAndItemCountZero_expectedFalse() {
    with(PagingDataHelper) {
      var result: Boolean? = null
      adapter.observeEmptyState { result = it }

      triggerListener(refreshState = LoadState.Loading, itemCount = 0)

      assertEquals(false, result)
    }
  }

  @Test
  fun observeEmptyState_whenLoadingAndItemCountNonZero_expectedFalse() {
    with(PagingDataHelper) {
      var result: Boolean? = null
      adapter.observeEmptyState { result = it }

      triggerListener(refreshState = LoadState.Loading, itemCount = 5)

      assertEquals(false, result)
    }
  }

  @Test
  fun observeEmptyState_whenNotLoadingAndItemCountZero_expectedTrue() {
    with(PagingDataHelper) {
      var result: Boolean? = null
      adapter.observeEmptyState { result = it }

      triggerListener(
        refreshState = LoadState.NotLoading(endOfPaginationReached = false),
        itemCount = 0,
      )

      assertEquals(true, result)
    }
  }

  @Test
  fun observeEmptyState_whenNotLoadingAndItemCountNonZero_expectedFalse() {
    with(PagingDataHelper) {
      var result: Boolean? = null
      adapter.observeEmptyState { result = it }

      triggerListener(
        refreshState = LoadState.NotLoading(endOfPaginationReached = false),
        itemCount = 10,
      )

      assertEquals(false, result)
    }
  }

  @Test
  fun observeEmptyState_whenErrorAndItemCountZero_expectedTrue() {
    with(PagingDataHelper) {
      var result: Boolean? = null
      adapter.observeEmptyState { result = it }

      val error = LoadState.Error(Throwable("boom"))
      triggerListener(refreshState = error, itemCount = 0)

      assertEquals(true, result)
    }
  }

  @Test
  fun observeEmptyState_always_registersLoadStateListenerExactlyOnce() {
    with(PagingDataHelper) {
      adapter.observeEmptyState { }

      verify(exactly = 1) { adapter.addLoadStateListener(any()) }
    }
  }
}
