package com.waffiq.bazz_movies.feature.home.utils.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.testing.TestLifecycleOwner
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.designsystem.databinding.IllustrationErrorBinding
import com.waffiq.bazz_movies.feature.home.databinding.NoFoundLayoutBinding
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.detachRecyclerView
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.handleLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.observeLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRecyclerWideItem
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRetryButton
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupSwipeRefresh
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotSame
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HomeFragmentHelperTest {

  private lateinit var lifecycleOwner: TestLifecycleOwner
  private val adapter = mockk<PagingDataAdapter<String, RecyclerView.ViewHolder>>(relaxed = true)

  private lateinit var context: Context
  private lateinit var inflater: LayoutInflater
  private lateinit var toggleView: View
  private lateinit var recyclerView: RecyclerView
  private lateinit var swipeRefresh: SwipeRefreshLayout
  private lateinit var parentLayout: FrameLayout
  private lateinit var noFoundLayoutBinding: NoFoundLayoutBinding
  private lateinit var illustrationErrorBinding: IllustrationErrorBinding

  @Before
  fun setup() {
    Dispatchers.setMain(UnconfinedTestDispatcher())
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies)
    }
    lifecycleOwner = TestLifecycleOwner(
      initialState = Lifecycle.State.RESUMED,
      coroutineDispatcher = UnconfinedTestDispatcher(),
    )
    toggleView = View(context)
    recyclerView = RecyclerView(context)
    swipeRefresh = SwipeRefreshLayout(context)

    inflater = LayoutInflater.from(context)
    parentLayout = FrameLayout(context)
    noFoundLayoutBinding = NoFoundLayoutBinding.inflate(inflater, parentLayout, false)
    illustrationErrorBinding = IllustrationErrorBinding.inflate(inflater, parentLayout, false)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun handleLoadState_whenListIsEmpty_showsEmptyView() =
    runTest {
      // source.refresh = NotLoading,
      // endOfPaginationReached = true,
      // itemCount = 0
      val loadStateFlow = MutableStateFlow(emptyLoadStates())
      every { adapter.loadStateFlow } returns loadStateFlow
      every { adapter.itemCount } returns 0

      lifecycleOwner.handleLoadState(
        adapter = adapter,
        message = "Nothing here",
        view = noFoundLayoutBinding,
        toggleView,
      )

      advanceUntilIdle()

      assert(noFoundLayoutBinding.root.isVisible)
      assert(noFoundLayoutBinding.tvMessage.text == "Nothing here")
      assert(toggleView.isGone)
    }

  @Test
  fun handleLoadState_whenEndOfPaginationReachedButHasItems_hidesEmptyView() =
    runTest {
      // source.refresh = NotLoading,
      // endOfPaginationReached = true,
      // item count 5
      val loadStateFlow = MutableStateFlow(
        buildCombinedLoadStates(
          refresh = LoadState.NotLoading(endOfPaginationReached = false),
          append = LoadState.NotLoading(endOfPaginationReached = true),
        ),
      )
      every { adapter.loadStateFlow } returns loadStateFlow
      every { adapter.itemCount } returns 5

      lifecycleOwner.handleLoadState(
        adapter = adapter,
        message = "Nothing here",
        view = noFoundLayoutBinding,
        toggleView,
      )

      advanceUntilIdle()

      assert(!noFoundLayoutBinding.root.isVisible)
      assert(!toggleView.isGone)
    }

  @Test
  fun handleLoadState_whenRefreshIsLoading_hidesEmptyView() =
    runTest {
      // source.refresh = refresh,
      // endOfPaginationReached = true,
      // item count 0
      val loadStateFlow = MutableStateFlow(
        buildCombinedLoadStates(
          refresh = LoadState.Loading,
          append = LoadState.NotLoading(endOfPaginationReached = true),
        ),
      )
      every { adapter.loadStateFlow } returns loadStateFlow
      every { adapter.itemCount } returns 0

      lifecycleOwner.handleLoadState(
        adapter = adapter,
        message = "Nothing here",
        view = noFoundLayoutBinding,
        toggleView,
      )

      advanceUntilIdle()

      assert(!noFoundLayoutBinding.root.isVisible)
      assert(!toggleView.isGone)
    }

  @Test
  fun handleLoadState_whenListIsNotEmpty_noSetMessage() =
    runTest {
      // source.refresh = NotLoading,
      // endOfPaginationReached = false,
      // item count 5
      val loadStateFlow = MutableStateFlow(nonEmptyLoadStates())
      every { adapter.loadStateFlow } returns loadStateFlow
      every { adapter.itemCount } returns 5

      lifecycleOwner.handleLoadState(
        adapter = adapter,
        message = "Should not appear",
        view = noFoundLayoutBinding,
      )

      advanceUntilIdle()

      assert(noFoundLayoutBinding.tvMessage.text != "Should not appear")
    }

  @Test
  fun setupSwipeRefresh_calls_stopsRefreshing() {
    swipeRefresh.isRefreshing = true

    setupSwipeRefresh(swipeRefresh, adapter)

    swipeRefresh.isRefreshing = true
    swipeRefresh.performClick()
    swipeRefresh.callOnRefresh()

    verify(exactly = 1) { adapter.refresh() }
    assert(!swipeRefresh.isRefreshing)
  }

  @Test
  fun setupRetryButton_invokesAction_showsViewsCorrectly() {
    var actionCalled = false

    setupRetryButton(
      binding = illustrationErrorBinding,
      adapter,
      action = { actionCalled = true },
    )

    illustrationErrorBinding.btnTryAgain.performClick()

    verify(exactly = 1) { adapter.refresh() }
    assert(!illustrationErrorBinding.btnTryAgain.isVisible)
    assert(illustrationErrorBinding.progressCircular.isVisible)
    assert(actionCalled)
  }

  @Test
  fun setupRetryButton_defaultAction_doesNothing() {
    // action not provided
    setupRetryButton(binding = illustrationErrorBinding, adapter)
    illustrationErrorBinding.btnTryAgain.performClick()

    // no exception thrown, button hidden, progress visible
    assert(!illustrationErrorBinding.btnTryAgain.isVisible)
    assert(illustrationErrorBinding.progressCircular.isVisible)
  }

  @Test
  fun detachRecyclerView_invokeCalls_removesRecyclerViewFromParent() {
    parentLayout.addView(recyclerView)
    assert(recyclerView.parent == parentLayout)

    recyclerView.detachRecyclerView()

    assert(recyclerView.parent == null)
  }

  @Test
  fun detachRecyclerView_whenRecyclerViewNoParent_doesNothing() {
    recyclerView.detachRecyclerView()
  }

  @Test
  fun setupLoadState_whenAdapterNotSet_setsAnimatorAndWrapsWithFooter() {
    recyclerView.layoutManager = LinearLayoutManager(context)

    recyclerView.setupLoadState(adapter)

    assert(recyclerView.itemAnimator is DefaultItemAnimator)
    assert(recyclerView.adapter is ConcatAdapter)
  }

  @Test
  fun setupLoadState_whenDifferentAdapterAlreadySet_replacesAdapter() {
    recyclerView.layoutManager = LinearLayoutManager(context)
    val differentAdapter = mockk<PagingDataAdapter<*, *>>(relaxed = true)

    recyclerView.setupLoadState(differentAdapter)
    val firstAdapter = recyclerView.adapter

    recyclerView.setupLoadState(adapter) // different adapter, should replace
    val secondAdapter = recyclerView.adapter

    assertNotSame(firstAdapter, secondAdapter)
  }

  @Test
  fun observeLoadState_whenRefreshIsLoadingAndEndOfPaginationReached_callsOnLoading() =
    runTest {
      var loadingCalled = false

      val loadStateFlow = MutableStateFlow(
        buildCombinedLoadStates(
          refresh = LoadState.Loading,
          append = LoadState.NotLoading(endOfPaginationReached = true),
        ),
      )

      lifecycleOwner.observeLoadState(
        loadStateFlow = loadStateFlow,
        onLoading = { loadingCalled = true },
        onSuccess = {},
        onError = {},
      )

      advanceUntilIdle()
      assert(loadingCalled)

      // refresh=NotLoading,
      // append=NotLoading(false)
      loadStateFlow.emit(
        buildCombinedLoadStates(
          refresh = LoadState.NotLoading(false),
          append = LoadState.Loading,
        ),
      )
      advanceUntilIdle()

      // refresh=Loading
      // endOfPaginationReached=false
      loadStateFlow.emit(
        buildCombinedLoadStates(
          refresh = LoadState.Loading,
          append = LoadState.NotLoading(endOfPaginationReached = false),
        ),
      )
      advanceUntilIdle()
    }

  @Test
  fun observeLoadState_whenAllStatesAreNotLoading_callsOnSuccess() =
    runTest {
      var successCalled = false

      val loadStateFlow = MutableStateFlow(
        buildCombinedLoadStates(
          refresh = LoadState.NotLoading(false),
          prepend = LoadState.NotLoading(false),
          append = LoadState.NotLoading(false),
        ),
      )

      lifecycleOwner.observeLoadState(
        loadStateFlow = loadStateFlow,
        onLoading = {},
        onSuccess = { successCalled = true },
        onError = {},
      )

      advanceUntilIdle()
      assert(successCalled)

      // prepend is NOT NotLoading
      loadStateFlow.emit(
        buildCombinedLoadStates(
          refresh = LoadState.NotLoading(false),
          prepend = LoadState.Loading,
          append = LoadState.NotLoading(false),
        ),
      )
      advanceUntilIdle()

      // prepend passes, append is NOT NotLoading
      loadStateFlow.emit(
        buildCombinedLoadStates(
          refresh = LoadState.NotLoading(false),
          prepend = LoadState.NotLoading(false),
          append = LoadState.Loading,
        ),
      )
      advanceUntilIdle()
    }

  @Test
  fun observeLoadState_whenRefreshIsError_callsOnError() =
    runTest {
      var errorEvent: Event<String>? = null
      val exception = RuntimeException("network error")

      val loadStateFlow = MutableStateFlow(
        buildCombinedLoadStates(refresh = LoadState.Error(exception)),
      )

      lifecycleOwner.observeLoadState(
        loadStateFlow = loadStateFlow,
        onLoading = {},
        onSuccess = {},
        onError = { errorEvent = it },
      )

      advanceUntilIdle()
      assert(errorEvent != null)
    }

  @Test
  fun setupRecyclerWideItem_invokeCalls_attachesSnapHelperAndSetsHorizontalLinearLayoutManager() {
    val recyclerView = RecyclerView(context)

    setupRecyclerWideItem(recyclerView)

    assert(recyclerView.layoutManager is LinearLayoutManager)
    val lm = recyclerView.layoutManager as LinearLayoutManager
    assert(lm.orientation == LinearLayoutManager.HORIZONTAL)
    assert(!lm.reverseLayout)
    assert(recyclerView.onFlingListener != null)
  }

  @Test
  fun setupRecyclerWideItem_whenLayoutManagerIsProvided_usesProvidedLayoutManager() {
    val recyclerView = RecyclerView(context)
    val customLm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    setupRecyclerWideItem(recyclerView, layoutManager = customLm)

    assert(recyclerView.layoutManager === customLm)
  }

  @Test
  fun setupRecyclerWideItem_whenFlingListenerAlreadySet_doesNotReAttactSnapHelper() {
    val recyclerView = RecyclerView(context)
    // first call attaches the snap helper (sets fling listener)
    setupRecyclerWideItem(recyclerView)
    val originalFlingListener = recyclerView.onFlingListener

    // second call should be a no-op
    setupRecyclerWideItem(recyclerView)

    assert(recyclerView.onFlingListener === originalFlingListener)
  }

  private fun emptyLoadStates() =
    buildCombinedLoadStates(
      refresh = LoadState.NotLoading(endOfPaginationReached = false),
      append = LoadState.NotLoading(endOfPaginationReached = true),
    )

  private fun nonEmptyLoadStates() =
    buildCombinedLoadStates(
      refresh = LoadState.NotLoading(endOfPaginationReached = false),
      append = LoadState.NotLoading(endOfPaginationReached = false),
    )

  private fun buildCombinedLoadStates(
    refresh: LoadState = LoadState.NotLoading(false),
    prepend: LoadState = LoadState.NotLoading(false),
    append: LoadState = LoadState.NotLoading(false),
  ) = CombinedLoadStates(
    refresh = refresh,
    prepend = prepend,
    append = append,
    source = LoadStates(
      refresh = refresh,
      prepend = prepend,
      append = append,
    ),
    mediator = null,
  )
}

// Extension to trigger SwipeRefreshLayout listener in tests
private fun SwipeRefreshLayout.callOnRefresh() {
  // Access the listener via reflection and call it
  val field = SwipeRefreshLayout::class.java.getDeclaredField("mListener")
  field.isAccessible = true
  (field.get(this) as? SwipeRefreshLayout.OnRefreshListener)?.onRefresh()
}
