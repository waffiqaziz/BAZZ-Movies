package com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers

import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.FavWatchlistHelper.handlePagingLoadState
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class HandlePagingLoadStateTest {

  private lateinit var lifecycleOwner: TestLifecycleOwner
  private lateinit var mockAdapter: PagingDataAdapter<Any, RecyclerView.ViewHolder>
  private lateinit var mockRecyclerView: RecyclerView
  private lateinit var mockProgressBar: ProgressBar
  private lateinit var mockErrorView: View
  private lateinit var mockEmptyView: View
  private lateinit var loadStateFlow: MutableStateFlow<CombinedLoadStates>
  private lateinit var onErrorCallback: (Event<String>?) -> Unit

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    lifecycleOwner = TestLifecycleOwner()
    lifecycleOwner.registry.currentState = Lifecycle.State.RESUMED

    // setup mocks
    mockAdapter = mockk(relaxed = true)
    mockRecyclerView = mockk(relaxed = true)
    mockProgressBar = mockk(relaxed = true)
    mockErrorView = mockk(relaxed = true)
    mockEmptyView = mockk(relaxed = true)
    onErrorCallback = mockk(relaxed = true)

    // setup load state flow with proper source parameter
    loadStateFlow = MutableStateFlow(
      CombinedLoadStates(
        refresh = LoadState.NotLoading(false),
        prepend = LoadState.NotLoading(false),
        append = LoadState.NotLoading(false),
        source = LoadStates(
          refresh = LoadState.NotLoading(false),
          prepend = LoadState.NotLoading(false),
          append = LoadState.NotLoading(false)
        ),
        mediator = null
      )
    )
  }

  @Test
  fun whenRefreshIsLoading_showProgressBarAndRecyclerView() = runTest {
    val loadState = CombinedLoadStates(
      refresh = LoadState.Loading,
      prepend = LoadState.NotLoading(false),
      append = LoadState.NotLoading(false),
      source = LoadStates(
        refresh = LoadState.Loading,
        prepend = LoadState.NotLoading(false),
        append = LoadState.NotLoading(false)
      ),
      mediator = null
    )

    lifecycleOwner.handlePagingLoadState(
      mockAdapter,
      loadStateFlow,
      mockRecyclerView,
      mockProgressBar,
      mockErrorView,
      mockEmptyView,
      onErrorCallback
    )

    loadStateFlow.value = loadState
    advanceUntilIdle()

    verify { mockProgressBar.isVisible = true }
    verify { mockRecyclerView.isVisible = true }
    verify { mockErrorView.isVisible = false }
    verify { mockEmptyView.isVisible = false }
  }

  @Test
  fun whenAppendIsLoading_showProgressBarAndRecyclerView() = runTest {
    val loadState = CombinedLoadStates(
      refresh = LoadState.NotLoading(false),
      prepend = LoadState.NotLoading(false),
      append = LoadState.Loading,
      source = LoadStates(
        refresh = LoadState.NotLoading(false),
        prepend = LoadState.NotLoading(false),
        append = LoadState.Loading
      ),
      mediator = null
    )

    lifecycleOwner.handlePagingLoadState(
      mockAdapter,
      loadStateFlow,
      mockRecyclerView,
      mockProgressBar,
      mockErrorView,
      mockEmptyView,
      onErrorCallback
    )

    loadStateFlow.value = loadState
    advanceUntilIdle()

    verify { mockProgressBar.isVisible = true }
    verify { mockRecyclerView.isVisible = true }
    verify { mockErrorView.isVisible = false }
    verify { mockEmptyView.isVisible = false }
  }

  @Test
  fun whenRefreshError_withEmptyAdapter_showErrorView() = runTest {
    val error = IOException("Network error")
    val loadState = CombinedLoadStates(
      refresh = LoadState.Error(error),
      prepend = LoadState.NotLoading(false),
      append = LoadState.NotLoading(false),
      source = LoadStates(
        refresh = LoadState.Error(error),
        prepend = LoadState.NotLoading(false),
        append = LoadState.NotLoading(false)
      ),
      mediator = null
    )

    every { mockAdapter.itemCount } returns 0

    lifecycleOwner.handlePagingLoadState(
      mockAdapter,
      loadStateFlow,
      mockRecyclerView,
      mockProgressBar,
      mockErrorView,
      mockEmptyView,
      onErrorCallback
    )

    loadStateFlow.value = loadState
    advanceUntilIdle()

    verify { mockProgressBar.isVisible = false }
    verify { mockRecyclerView.isVisible = false }
    verify { mockErrorView.isVisible = true }
    verify { mockEmptyView.isVisible = false }
    verify { onErrorCallback(any()) }
  }

  @Test
  fun whenRefreshError_withNonEmptyAdapter_showRecyclerView() = runTest {
    val error = IOException("Network error")
    val loadState = CombinedLoadStates(
      refresh = LoadState.Error(error),
      prepend = LoadState.NotLoading(false),
      append = LoadState.NotLoading(false),
      source = LoadStates(
        refresh = LoadState.Error(error),
        prepend = LoadState.NotLoading(false),
        append = LoadState.NotLoading(false)
      ),
      mediator = null
    )

    every { mockAdapter.itemCount } returns 5

    lifecycleOwner.handlePagingLoadState(
      mockAdapter,
      loadStateFlow,
      mockRecyclerView,
      mockProgressBar,
      mockErrorView,
      mockEmptyView,
      onErrorCallback
    )

    loadStateFlow.value = loadState
    advanceUntilIdle()

    // Then
    verify { mockProgressBar.isVisible = false }
    verify { mockRecyclerView.isVisible = true }
    verify { mockErrorView.isVisible = false }
    verify { mockEmptyView.isVisible = false }
    verify { onErrorCallback(any()) }
  }

  @Test
  fun whenEndOfPaginationReached_withEmptyAdapter_showEmptyView() = runTest {
    val loadState = CombinedLoadStates(
      refresh = LoadState.NotLoading(false),
      prepend = LoadState.NotLoading(false),
      append = LoadState.NotLoading(true),
      source = LoadStates(
        refresh = LoadState.NotLoading(false),
        prepend = LoadState.NotLoading(false),
        append = LoadState.NotLoading(true)
      ),
      mediator = null
    )

    every { mockAdapter.itemCount } returns 0

    lifecycleOwner.handlePagingLoadState(
      mockAdapter,
      loadStateFlow,
      mockRecyclerView,
      mockProgressBar,
      mockErrorView,
      mockEmptyView,
      onErrorCallback
    )

    loadStateFlow.value = loadState
    advanceUntilIdle()

    verify { mockProgressBar.isVisible = false }
    verify { mockRecyclerView.isVisible = false }
    verify { mockErrorView.isVisible = false }
    verify { mockEmptyView.isVisible = true }
  }

  @Test
  fun whenEndOfPaginationReached_withNonEmptyAdapter_showRecyclerView() = runTest {
    // Given
    val loadState = CombinedLoadStates(
      refresh = LoadState.NotLoading(false),
      prepend = LoadState.NotLoading(false),
      append = LoadState.NotLoading(true),
      source = LoadStates(
        refresh = LoadState.NotLoading(false),
        prepend = LoadState.NotLoading(false),
        append = LoadState.NotLoading(true)
      ),
      mediator = null
    )

    every { mockAdapter.itemCount } returns 5

    // When
    lifecycleOwner.handlePagingLoadState(
      mockAdapter,
      loadStateFlow,
      mockRecyclerView,
      mockProgressBar,
      mockErrorView,
      mockEmptyView,
      onErrorCallback
    )

    loadStateFlow.value = loadState
    advanceUntilIdle()

    // Then
    verify { mockProgressBar.isVisible = false }
    verify { mockRecyclerView.isVisible = true }
    verify { mockErrorView.isVisible = false }
    verify { mockEmptyView.isVisible = false }
  }

  @Test
  fun whenNormalState_showOnlyRecyclerView() = runTest {
    val loadState = CombinedLoadStates(
      refresh = LoadState.NotLoading(false),
      prepend = LoadState.NotLoading(false),
      append = LoadState.NotLoading(false),
      source = LoadStates(
        refresh = LoadState.NotLoading(false),
        prepend = LoadState.NotLoading(false),
        append = LoadState.NotLoading(false)
      ),
      mediator = null
    )

    lifecycleOwner.handlePagingLoadState(
      mockAdapter,
      loadStateFlow,
      mockRecyclerView,
      mockProgressBar,
      mockErrorView,
      mockEmptyView,
      onErrorCallback
    )

    loadStateFlow.value = loadState
    advanceUntilIdle()

    verify { mockProgressBar.isVisible = false }
    verify { mockRecyclerView.isVisible = true }
    verify { mockErrorView.isVisible = false }
    verify { mockEmptyView.isVisible = false }
  }

  @Test
  fun tesDebounce_emitMultipleValuesQuickly() = runTest {
    // Given - first state
    val loadState1 = CombinedLoadStates(
      refresh = LoadState.Loading,
      prepend = LoadState.NotLoading(false),
      append = LoadState.NotLoading(false),
      source = LoadStates(
        refresh = LoadState.Loading,
        prepend = LoadState.NotLoading(false),
        append = LoadState.NotLoading(false)
      ),
      mediator = null
    )

    // Second state that comes before debounce completes
    val loadState2 = CombinedLoadStates(
      refresh = LoadState.NotLoading(false),
      prepend = LoadState.NotLoading(false),
      append = LoadState.NotLoading(false),
      source = LoadStates(
        refresh = LoadState.NotLoading(false),
        prepend = LoadState.NotLoading(false),
        append = LoadState.NotLoading(false)
      ),
      mediator = null
    )

    // When
    lifecycleOwner.handlePagingLoadState(
      mockAdapter,
      loadStateFlow,
      mockRecyclerView,
      mockProgressBar,
      mockErrorView,
      mockEmptyView,
      onErrorCallback
    )

    // Emit first value
    loadStateFlow.value = loadState1

    // Advance time but not enough to trigger debounce
    advanceTimeBy(DEBOUNCE_SHORT - 100)

    // Emit second value before debounce completes
    loadStateFlow.value = loadState2

    // Now advance until all work is complete
    advanceUntilIdle()

    // Then - should only see the effect of the second value
    verify { mockProgressBar.isVisible = false }
    verify { mockRecyclerView.isVisible = true }
    verify { mockErrorView.isVisible = false }
    verify { mockEmptyView.isVisible = false }

    // Verify we didn't set progress to visible (which would have happened for the first state)
    verify(exactly = 0) { mockProgressBar.isVisible = true }
  }

  @Test
  fun testDistinctUntilChanged_emitSameValueTwice() = runTest {
    // Given
    val loadState = CombinedLoadStates(
      refresh = LoadState.Loading,
      prepend = LoadState.NotLoading(false),
      append = LoadState.NotLoading(false),
      source = LoadStates(
        refresh = LoadState.Loading,
        prepend = LoadState.NotLoading(false),
        append = LoadState.NotLoading(false)
      ),
      mediator = null
    )

    // Track number of calls to a callback
    var callCount = 0
    val trackingCallback: (Event<String>?) -> Unit = { callCount++ }

    // When
    lifecycleOwner.handlePagingLoadState(
      mockAdapter,
      loadStateFlow,
      mockRecyclerView,
      mockProgressBar,
      mockErrorView,
      mockEmptyView,
      trackingCallback
    )

    // Emit first value and wait for debounce
    loadStateFlow.value = loadState
    advanceTimeBy(DEBOUNCE_SHORT * 2)

    // Count should still be 0 since loading state doesn't trigger error callback
    assertEquals(0, callCount)

    // Create an error state
    val errorState = CombinedLoadStates(
      refresh = LoadState.Error(IOException("Test error")),
      prepend = LoadState.NotLoading(false),
      append = LoadState.NotLoading(false),
      source = LoadStates(
        refresh = LoadState.Error(IOException("Test error")),
        prepend = LoadState.NotLoading(false),
        append = LoadState.NotLoading(false)
      ),
      mediator = null
    )

    // Emit error state and wait
    loadStateFlow.value = errorState
    advanceTimeBy(DEBOUNCE_SHORT * 2)

    // Error callback should be called once
    assertEquals(1, callCount)

    // Emit identical error state again
    loadStateFlow.value = errorState
    advanceTimeBy(DEBOUNCE_SHORT * 2)

    // Count should still be 1 due to distinctUntilChanged
    assertEquals(1, callCount)
  }

  // helper class for testing with lifecycle
  class TestLifecycleOwner : LifecycleOwner {
    val registry = LifecycleRegistry(this)

    override val lifecycle: Lifecycle
      get() = registry
  }
}
