package com.waffiq.bazz_movies.feature.home.utils.helpers

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.waffiq.bazz_movies.feature.home.databinding.NoFoundLayoutBinding
import com.waffiq.bazz_movies.feature.home.testutils.BaseFragmentHelperTest
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.handleLoadState
import io.mockk.every
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HandleLoadStateTest : BaseFragmentHelperTest() {

  private lateinit var toggleView: View
  private lateinit var noFoundLayoutBinding: NoFoundLayoutBinding

  @Before
  override fun setup() {
    super.setup()

    toggleView = View(context)
    noFoundLayoutBinding = NoFoundLayoutBinding.inflate(inflater, parentLayout, false)
  }

  @Test
  fun handleLoadState_whenListIsEmpty_showsEmptyView() =
    runTest {
      // source.refresh = NotLoading,
      // endOfPaginationReached = true,
      // itemCount = 0
      setLoadState(
        loadState = MutableStateFlow(emptyLoadStates()),
        itemCount = 0,
      )

      noFound()

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
      setLoadState(loadState = loadStateFlow, itemCount = 5)

      noFound()

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
      setLoadState(loadState = loadStateFlow, itemCount = 0)

      noFound()

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
      setLoadState(
        loadState = MutableStateFlow(nonEmptyLoadStates()),
        itemCount = 5,
      )

      lifecycleOwner.handleLoadState(
        adapter = adapter,
        message = "Should not appear",
        view = noFoundLayoutBinding,
      )

      advanceUntilIdle()

      assert(noFoundLayoutBinding.tvMessage.text != "Should not appear")
    }

  private fun setLoadState(loadState: MutableStateFlow<CombinedLoadStates>, itemCount: Int) {
    every { adapter.loadStateFlow } returns loadState
    every { adapter.itemCount } returns itemCount
  }

  private fun noFound() {
    lifecycleOwner.handleLoadState(
      adapter = adapter,
      message = "Nothing here",
      view = noFoundLayoutBinding,
      toggleView,
    )
  }
}
