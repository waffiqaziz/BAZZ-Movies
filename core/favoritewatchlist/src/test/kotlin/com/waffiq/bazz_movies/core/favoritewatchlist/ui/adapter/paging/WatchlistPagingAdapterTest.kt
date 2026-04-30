package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging

import android.widget.FrameLayout
import androidx.annotation.IdRes
import com.google.android.material.listitem.SwipeableListItem
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.id.container_result
import com.waffiq.bazz_movies.core.designsystem.R.id.reveal_layout_end
import com.waffiq.bazz_movies.core.designsystem.R.id.reveal_layout_start
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemPagingWatchlistBinding
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.BaseAdapterPagingTest
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.TEST_DATE_FORMATTED
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WatchlistPagingAdapterTest : BaseAdapterPagingTest() {
  private lateinit var adapter: WatchlistPagingAdapter
  private lateinit var binding: ItemPagingWatchlistBinding
  private lateinit var viewHolder: WatchlistPagingAdapter.ViewHolder


  @Before
  fun setup() {
    super.setUp()
    adapter = WatchlistPagingAdapter(
      navigator,
      MOVIE_MEDIA_TYPE,
      onDelete,
      onAddToWatchlist,
    )
    binding = ItemPagingWatchlistBinding.inflate(inflater, null, false)
  }

  private suspend fun TestScope.setupBoundViewHolder() {
    adapter.submitData(pagingData)
    adapter.onBindViewHolder(viewHolder, 0)
  }

  private fun setupSwipeableViewHolder(): WatchlistPagingAdapter.ViewHolder {
    provideRecyclerView(adapter) { adapter.submitData(pagingData) }
    return provideViewHolder<WatchlistPagingAdapter.ViewHolder>()
      ?: error("ViewHolder not found at position 0")
  }

  private fun WatchlistPagingAdapter.ViewHolder.swipe(@IdRes revealLayoutId: Int) {
    swipeCallback.onSwipeStateChanged(
      SwipeableListItem.STATE_SWIPE_PRIMARY_ACTION,
      itemView.findViewById(revealLayoutId),
      100,
    )
  }


  @Test
  fun submitData_withPagingData_updateAdapter() =
    runTest {
      adapter.submitData(pagingData)
      assertEquals(1, adapter.itemCount)
    }

  @Test
  fun onBindViewHolder_whenAllDataIsValid_bindsCorrectMovieData() =
    runTest {
      viewHolder = adapter.ViewHolder(binding)
      setupBoundViewHolder()

      assertEquals("Test Movie Name", binding.tvTitle.text.toString())
      assertEquals("Adventure", binding.tvGenre.text.toString())
      assertEquals(TEST_DATE_FORMATTED, binding.tvYearReleased.text.toString())
      assertEquals("10/10", binding.tvRating.text.toString())
      assertEquals("5.0", binding.ratingBar.rating.toString())
    }

  @Test
  fun onCreateViewHolder_whenCalled_createsViewHolderCorrectly() {
    val parent = FrameLayout(context)
    viewHolder = adapter.onCreateViewHolder(parent, 0)
    assertNotNull(viewHolder)

    // verify ViewHolder is correctly inflated
    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
  }

  @Test
  fun onClick_whenClicked_opensMovieDetails() =
    runTest {
      viewHolder = adapter.ViewHolder(binding)
      setupBoundViewHolder()
      advanceUntilIdle()

      adapter.onBindViewHolder(viewHolder, 0)
      binding.containerResult.performClick()

      verify { navigator.openDetails(eq(context), eq(movieData)) }
    }

  @Test
  fun bind_whenSwipedRight_callsOnDelete() =
    runTest {
      setupSwipeableViewHolder().swipe(reveal_layout_start)
      verify { onDelete(movieData) }
    }

  @Test
  fun bind_whenSwipedLeft_callsOnAddToWatchlist() =
    runTest {
      setupSwipeableViewHolder().swipe(reveal_layout_end)
      verify { onAddToWatchlist(movieData) }
    }

  @Test
  fun bind_whenSwiped_othersState() =
    runTest {
      provideRecyclerView(adapter) { adapter.submitData(pagingData) }
      val viewHolder = provideViewHolder<WatchlistPagingAdapter.ViewHolder>()

      assertNotNull(viewHolder)
      assertNotNull(viewHolder?.swipeCallback)

      // simulate dragging
      viewHolder?.swipeCallback?.onSwipeStateChanged(
        SwipeableListItem.STATE_DRAGGING,
        viewHolder.itemView.findViewById(reveal_layout_end),
        100,
      )

      // simulate swipe wrong item
      viewHolder?.swipeCallback?.onSwipeStateChanged(
        SwipeableListItem.STATE_SWIPE_PRIMARY_ACTION,
        viewHolder.itemView.findViewById(container_result),
        100,
      )
    }
}
