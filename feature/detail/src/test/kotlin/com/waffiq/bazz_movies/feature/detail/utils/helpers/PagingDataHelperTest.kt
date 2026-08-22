package com.waffiq.bazz_movies.feature.detail.utils.helpers

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PagingDataHelperTest {

  private lateinit var loadStateFlow: MutableStateFlow<CombinedLoadStates>
  private lateinit var onPagesUpdatedFlow: MutableSharedFlow<Unit>
  private var itemCount = 0

  @Before
  fun setUp() {
    itemCount = 0
    loadStateFlow = MutableStateFlow(
      combinedLoadStatesOf(LoadState.NotLoading(endOfPaginationReached = false)),
    )
    onPagesUpdatedFlow = MutableSharedFlow(extraBufferCapacity = 1)
  }

  private fun combinedLoadStatesOf(refresh: LoadState): CombinedLoadStates =
    mockk { every { this@mockk.refresh } returns refresh }

  private fun buildFlow(): Flow<Boolean> =
    PagingDataHelper.emptyStateFlow(
      loadStateFlow = loadStateFlow,
      onPagesUpdatedFlow = onPagesUpdatedFlow,
      itemCountProvider = { itemCount },
    )

  @Test
  fun emptyStateFlow_whenLoadingAndItemCountZero_emitsFalse() =
    runTest {
      itemCount = 0
      loadStateFlow.value = combinedLoadStatesOf(LoadState.Loading)

      val result = buildFlow().first()

      assertEquals(false, result)
    }

  @Test
  fun emptyStateFlow_whenLoadingAndItemCountNonZero_emitsFalse() =
    runTest {
      itemCount = 5
      loadStateFlow.value = combinedLoadStatesOf(LoadState.Loading)

      val result = buildFlow().first()

      assertEquals(false, result)
    }

  @Test
  fun emptyStateFlow_whenNotLoadingAndItemCountZero_emitsTrue() =
    runTest {
      itemCount = 0
      loadStateFlow.value =
        combinedLoadStatesOf(LoadState.NotLoading(endOfPaginationReached = false))

      val result = buildFlow().first()

      assertEquals(true, result)
    }

  @Test
  fun emptyStateFlow_whenNotLoadingAndItemCountNonZero_emitsFalse() =
    runTest {
      itemCount = 10
      loadStateFlow.value =
        combinedLoadStatesOf(LoadState.NotLoading(endOfPaginationReached = false))

      val result = buildFlow().first()

      assertEquals(false, result)
    }

  @Test
  fun emptyStateFlow_whenErrorAndItemCountZero_emitsTrue() =
    runTest {
      itemCount = 0
      loadStateFlow.value = combinedLoadStatesOf(LoadState.Error(Throwable("boom")))

      val result = buildFlow().first()

      assertEquals(true, result)
    }

  @Test
  fun emptyStateFlow_whenPagesUpdatedWithoutLoadStateChange_reevaluatesItemCount() =
    runTest {
      val results = mutableListOf<Boolean>()
      val job = launch { buildFlow().toList(results) }

      // initial should be NotLoading + itemCount 0, should true
      runCurrent()

      // itemCount changes but refresh state stays same value
      itemCount = 5
      onPagesUpdatedFlow.emit(Unit)
      runCurrent()

      itemCount = 0
      onPagesUpdatedFlow.emit(Unit)
      runCurrent()

      job.cancel()

      assertEquals(listOf(true, false, true), results)
    }
}
