package com.waffiq.bazz_movies.feature.home.utils.helpers

import androidx.recyclerview.widget.LinearLayoutManager
import com.waffiq.bazz_movies.feature.home.testutils.BaseFragmentHelperTest
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRecyclerWideItem
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SetupRecyclerWideItemTest : BaseFragmentHelperTest() {

  @Test
  fun setupRecyclerWideItem_invokeCalls_attachesSnapHelperAndSetsHorizontalLinearLayoutManager() {
    setupRecyclerWideItem(recyclerView)

    assert(recyclerView.layoutManager is LinearLayoutManager)
    val lm = recyclerView.layoutManager as LinearLayoutManager
    assert(lm.orientation == LinearLayoutManager.HORIZONTAL)
    assert(!lm.reverseLayout)
    assert(recyclerView.onFlingListener != null)
  }

  @Test
  fun setupRecyclerWideItem_whenLayoutManagerIsProvided_usesProvidedLayoutManager() {
    val customLm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    setupRecyclerWideItem(recyclerView, layoutManager = customLm)

    assert(recyclerView.layoutManager === customLm)
  }

  @Test
  fun setupRecyclerWideItem_whenFlingListenerAlreadySet_doesNotReAttactSnapHelper() {
    // first call attaches the snap helper (sets fling listener)
    setupRecyclerWideItem(recyclerView)
    val originalFlingListener = recyclerView.onFlingListener

    // second call should be a no-op
    setupRecyclerWideItem(recyclerView)

    assert(recyclerView.onFlingListener === originalFlingListener)
  }
}
