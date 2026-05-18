package com.waffiq.bazz_movies.feature.home.utils.helpers

import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.waffiq.bazz_movies.core.designsystem.databinding.IllustrationErrorBinding
import com.waffiq.bazz_movies.feature.home.testutils.BaseFragmentHelperTest
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.detachRecyclerView
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRetryButton
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupSwipeRefresh
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertNotSame
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HomeFragmentHelperTest : BaseFragmentHelperTest() {

  private lateinit var swipeRefresh: SwipeRefreshLayout
  private lateinit var illustrationErrorBinding: IllustrationErrorBinding

  @Before
  override fun setup() {
    super.setup()

    swipeRefresh = SwipeRefreshLayout(context)
    illustrationErrorBinding = IllustrationErrorBinding.inflate(inflater, parentLayout, false)
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

  // Extension to trigger SwipeRefreshLayout listener in tests
  private fun SwipeRefreshLayout.callOnRefresh() {
    // Access the listener via reflection and call it
    val field = SwipeRefreshLayout::class.java.getDeclaredField("mListener")
    field.isAccessible = true
    (field.get(this) as? SwipeRefreshLayout.OnRefreshListener)?.onRefresh()
  }
}
