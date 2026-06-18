package com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.favoritewatchlist.R.id.illustration_error
import com.waffiq.bazz_movies.core.favoritewatchlist.R.id.illustration_no_data_view
import com.waffiq.bazz_movies.core.favoritewatchlist.databinding.FragmentChildBinding
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

  private lateinit var context: Context
  private lateinit var parent: FrameLayout
  private lateinit var inflater: LayoutInflater

  private lateinit var lifecycleOwner: TestLifecycleOwner
  private lateinit var mockAdapter: PagingDataAdapter<Any, RecyclerView.ViewHolder>
  private lateinit var loadStateFlow: MutableStateFlow<CombinedLoadStates>
  private lateinit var onErrorCallback: (Event<String>) -> Unit
  private lateinit var binding: FragmentChildBinding

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies)
    }
    inflater = LayoutInflater.from(context) // Fixed: Initialize inflater
    parent = FrameLayout(context)
    binding = FragmentChildBinding.inflate(inflater, parent, false)

    lifecycleOwner = TestLifecycleOwner()
    lifecycleOwner.registry.currentState = Lifecycle.State.RESUMED

    mockAdapter = mockk(relaxed = true)
    onErrorCallback = mockk(relaxed = true)

    loadStateFlow = MutableStateFlow(createLoadState(refresh = LoadState.NotLoading(false)))
  }

  private fun createLoadState(
    refresh: LoadState = LoadState.NotLoading(false),
    prepend: LoadState = LoadState.NotLoading(false),
    append: LoadState = LoadState.NotLoading(false),
  ): CombinedLoadStates =
    CombinedLoadStates(
      refresh = refresh,
      prepend = prepend,
      append = append,
      source = LoadStates(refresh = refresh, prepend = prepend, append = append),
      mediator = null,
    )

  private fun setupAndRunTest(loadState: CombinedLoadStates, itemCount: Int = 0) =
    runTest {
      every { mockAdapter.itemCount } returns itemCount

      lifecycleOwner.handlePagingLoadState(
        mockAdapter,
        loadStateFlow,
        binding,
        onErrorCallback,
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
    assertEquals(binding.progressBar.isVisible, progressVisible)
    assertEquals(binding.recyclerView.isVisible, recyclerVisible)
    assertEquals(errorVisible, isViewVisible(illustration_error))
    assertEquals(emptyVisible, isViewVisible(illustration_no_data_view))

    if (errorCallbackCalled) {
      verify { onErrorCallback(any()) }
    }
  }

  private fun isViewVisible(@IdRes id: Int): Boolean = binding.root.findViewById<View>(id).isVisible

  @Test
  fun handlePagingLoadState_whenRefreshIsLoading_showsProgressBarAndRecyclerView() =
    runTest {
      val loadState = createLoadState(refresh = LoadState.Loading)

      setupAndRunTest(loadState)

      verifyViewState(
        progressVisible = true,
        recyclerVisible = true,
        errorVisible = false,
        emptyVisible = false,
      )
    }

  @Test
  fun handlePagingLoadState_whenAppendIsLoading_showsProgressBarAndRecyclerView() =
    runTest {
      val loadState = createLoadState(append = LoadState.Loading)

      setupAndRunTest(loadState)

      verifyViewState(
        progressVisible = true,
        recyclerVisible = true,
        errorVisible = false,
        emptyVisible = false,
      )
    }

  @Test
  fun handlePagingLoadState_whenRefreshErrorAndEmptyAdapter_showsErrorView() =
    runTest {
      val error = IOException("Network error")
      val loadState = createLoadState(refresh = LoadState.Error(error))

      setupAndRunTest(loadState, itemCount = 0)

      verifyViewState(
        progressVisible = false,
        recyclerVisible = false,
        errorVisible = true,
        emptyVisible = false,
        errorCallbackCalled = true,
      )
    }

  @Test
  fun handlePagingLoadState_whenRefreshErrorAndNonEmptyAdapter_showsRecyclerView() =
    runTest {
      val loadState = createLoadState(refresh = LoadState.Error(IOException("Network error")))

      setupAndRunTest(loadState, itemCount = 5)

      verifyViewState(
        progressVisible = false,
        recyclerVisible = true,
        errorVisible = false,
        emptyVisible = false,
        errorCallbackCalled = true,
      )
    }

  @Test
  fun handlePagingLoadState_whenEndOfPaginationReachedAndEmptyAdapter_showsEmptyView() =
    runTest {
      val loadState = createLoadState(append = LoadState.NotLoading(true))

      setupAndRunTest(loadState, itemCount = 0)

      verifyViewState(
        progressVisible = false,
        recyclerVisible = false,
        errorVisible = false,
        emptyVisible = true,
      )
    }

  @Test
  fun handlePagingLoadState_whenEndOfPaginationReachedAndNonEmptyAdapter_showsRecyclerView() =
    runTest {
      val loadState = createLoadState(append = LoadState.NotLoading(true))

      setupAndRunTest(loadState, itemCount = 5)

      verifyViewState(
        progressVisible = false,
        recyclerVisible = true,
        errorVisible = false,
        emptyVisible = false,
      )
    }

  @Test
  fun handlePagingLoadState_whenNormalState_showsOnlyRecyclerView() =
    runTest {
      val loadState = createLoadState() // Uses default values for normal state

      setupAndRunTest(loadState)

      verifyViewState(
        progressVisible = false,
        recyclerVisible = true,
        errorVisible = false,
        emptyVisible = false,
      )
    }

  @Test
  fun handlePagingLoadState_whenDebounce_emitsMultipleValuesQuickly() =
    runTest {
      // setup handler first
      lifecycleOwner.handlePagingLoadState(
        mockAdapter,
        loadStateFlow,
        binding,
        onErrorCallback,
      )

      // first state - loading
      loadStateFlow.value = createLoadState(refresh = LoadState.Loading)

      // wait
      advanceTimeBy(DEBOUNCE_SHORT - 100)

      // second state - normal - overwrites the first one before debounce completes
      loadStateFlow.value = createLoadState()

      // complete all pending work
      advanceUntilIdle()

      // not loadin, and back to normal state
      verifyViewState(
        progressVisible = false,
        recyclerVisible = true,
        errorVisible = false,
        emptyVisible = false,
      )
    }

  @Test
  fun handlePagingLoadState_whenDistinctUntilChanged_emitsSameValueTwice() =
    runTest {
      // track number of calls to a callback
      var callCount = 0
      val trackingCallback: (Event<String>) -> Unit = { callCount++ }

      lifecycleOwner.handlePagingLoadState(
        mockAdapter,
        loadStateFlow,
        binding,
        trackingCallback,
      )

      // emit loading state
      loadStateFlow.value = createLoadState(refresh = LoadState.Loading)
      advanceTimeBy(DEBOUNCE_SHORT * 2)

      // loading state should not trigger error callback
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
