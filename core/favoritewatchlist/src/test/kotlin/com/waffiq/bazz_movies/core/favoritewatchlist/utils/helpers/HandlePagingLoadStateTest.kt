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

    // setup initial load state flow
    loadStateFlow = MutableStateFlow(createLoadState(refresh = LoadState.NotLoading(false)))
  }

  private fun createLoadState(
    refresh: LoadState = LoadState.NotLoading(false),
    prepend: LoadState = LoadState.NotLoading(false),
    append: LoadState = LoadState.NotLoading(false),
  ): CombinedLoadStates {
    return CombinedLoadStates(
      refresh = refresh,
      prepend = prepend,
      append = append,
      source = LoadStates(
        refresh = refresh,
        prepend = prepend,
        append = append
      ),
      mediator = null
    )
  }

  private fun setupAndRunTest(
    loadState: CombinedLoadStates,
    itemCount: Int = 0,
  ) = runTest {
    every { mockAdapter.itemCount } returns itemCount

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
  }

  private fun verifyViewState(
    progressVisible: Boolean,
    recyclerVisible: Boolean,
    errorVisible: Boolean,
    emptyVisible: Boolean,
    errorCallbackCalled: Boolean = false,
  ) {
    verify { mockProgressBar.isVisible = progressVisible }
    verify { mockRecyclerView.isVisible = recyclerVisible }
    verify { mockErrorView.isVisible = errorVisible }
    verify { mockEmptyView.isVisible = emptyVisible }

    if (errorCallbackCalled) {
      verify { onErrorCallback(any()) }
    }
  }

  @Test
  fun whenRefreshIsLoading_showProgressBarAndRecyclerView() = runTest {
    val loadState = createLoadState(refresh = LoadState.Loading)

    setupAndRunTest(loadState)

    verifyViewState(
      progressVisible = true,
      recyclerVisible = true,
      errorVisible = false,
      emptyVisible = false
    )
  }

  @Test
  fun whenAppendIsLoading_showProgressBarAndRecyclerView() = runTest {
    val loadState = createLoadState(append = LoadState.Loading)

    setupAndRunTest(loadState)

    verifyViewState(
      progressVisible = true,
      recyclerVisible = true,
      errorVisible = false,
      emptyVisible = false
    )
  }

  @Test
  fun whenRefreshError_withEmptyAdapter_showErrorView() = runTest {
    val error = IOException("Network error")
    val loadState = createLoadState(refresh = LoadState.Error(error))

    setupAndRunTest(loadState, itemCount = 0)

    verifyViewState(
      progressVisible = false,
      recyclerVisible = false,
      errorVisible = true,
      emptyVisible = false,
      errorCallbackCalled = true
    )
  }

  @Test
  fun whenRefreshError_withNonEmptyAdapter_showRecyclerView() = runTest {
    val loadState = createLoadState(refresh = LoadState.Error(IOException("Network error")))

    setupAndRunTest(loadState, itemCount = 5)

    verifyViewState(
      progressVisible = false,
      recyclerVisible = true,
      errorVisible = false,
      emptyVisible = false,
      errorCallbackCalled = true
    )
  }

  @Test
  fun whenEndOfPaginationReached_withEmptyAdapter_showEmptyView() = runTest {
    val loadState = createLoadState(append = LoadState.NotLoading(true))

    setupAndRunTest(loadState, itemCount = 0)

    verifyViewState(
      progressVisible = false,
      recyclerVisible = false,
      errorVisible = false,
      emptyVisible = true
    )
  }

  @Test
  fun whenEndOfPaginationReached_withNonEmptyAdapter_showRecyclerView() = runTest {
    val loadState = createLoadState(append = LoadState.NotLoading(true))

    setupAndRunTest(loadState, itemCount = 5)

    verifyViewState(
      progressVisible = false,
      recyclerVisible = true,
      errorVisible = false,
      emptyVisible = false
    )
  }

  @Test
  fun whenNormalState_showOnlyRecyclerView() = runTest {
    val loadState = createLoadState() // Uses default values for normal state

    setupAndRunTest(loadState)

    verifyViewState(
      progressVisible = false,
      recyclerVisible = true,
      errorVisible = false,
      emptyVisible = false
    )
  }

  @Test
  fun tesDebounce_emitMultipleValuesQuickly() = runTest {
    // setup handler first
    lifecycleOwner.handlePagingLoadState(
      mockAdapter,
      loadStateFlow,
      mockRecyclerView,
      mockProgressBar,
      mockErrorView,
      mockEmptyView,
      onErrorCallback
    )

    // first state - loading
    loadStateFlow.value = createLoadState(refresh = LoadState.Loading)

    // advance time but not enough to trigger debounce
    advanceTimeBy(DEBOUNCE_SHORT - 100)

    // second state - normal - overwrites the first one before debounce completes
    loadStateFlow.value = createLoadState()

    // complete all pending work
    advanceUntilIdle()

    // only second state should take effect
    verifyViewState(
      progressVisible = false,
      recyclerVisible = true,
      errorVisible = false,
      emptyVisible = false
    )

    // verify first state didn't take effect, progressbar is not visible
    verify(exactly = 0) { mockProgressBar.isVisible = true }
  }

  @Test
  fun testDistinctUntilChanged_emitSameValueTwice() = runTest {
    // track number of calls to a callback
    var callCount = 0
    val trackingCallback: (Event<String>?) -> Unit = { callCount++ }

    // setup handler with tracking callback
    lifecycleOwner.handlePagingLoadState(
      mockAdapter,
      loadStateFlow,
      mockRecyclerView,
      mockProgressBar,
      mockErrorView,
      mockEmptyView,
      trackingCallback
    )

    // emit loading state
    loadStateFlow.value = createLoadState(refresh = LoadState.Loading)
    advanceTimeBy(DEBOUNCE_SHORT * 2)

    // loading state doesn't trigger error callback
    assertEquals(0, callCount)

    // create an error state
    val errorState = createLoadState(refresh = LoadState.Error(IOException("Test error")))

    // emit error state
    loadStateFlow.value = errorState
    advanceTimeBy(DEBOUNCE_SHORT * 2)

    // error callback should be called once
    assertEquals(1, callCount)

    // emit identical error state again
    loadStateFlow.value = errorState
    advanceTimeBy(DEBOUNCE_SHORT * 2)

    // count should still be 1 due to distinctUntilChanged
    assertEquals(1, callCount)
  }

  // helper class for testing with lifecycle
  class TestLifecycleOwner : LifecycleOwner {
    val registry = LifecycleRegistry(this)

    override val lifecycle: Lifecycle
      get() = registry
  }
}
