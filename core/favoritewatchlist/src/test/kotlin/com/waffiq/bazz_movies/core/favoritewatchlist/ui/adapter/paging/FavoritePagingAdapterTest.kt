package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging

import android.widget.FrameLayout
import com.google.android.material.listitem.SwipeableListItem
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.id.container_result
import com.waffiq.bazz_movies.core.designsystem.R.id.reveal_layout_end
import com.waffiq.bazz_movies.core.designsystem.R.id.reveal_layout_start
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.BaseAdapterPagingTest
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.TEST_DATE_FORMATTED
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.movieData
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.SwipeConfig
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FavoritePagingAdapterTest : BaseAdapterPagingTest() {

  @Before
  fun setup() {
    super.setUp()
    adapter = MediaPagingAdapter(
      navigator,
      MOVIE_MEDIA_TYPE,
      SwipeConfig.forFavorite(),
      onDelete,
      onAddToWatchlist,
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

      assertEquals("Test Movie Name", binding.content.tvTitle.text.toString())
      assertEquals("Adventure", binding.content.tvGenre.text.toString())
      assertEquals(TEST_DATE_FORMATTED, binding.content.tvYearReleased.text.toString())
      assertEquals("10/10", binding.content.tvRating.text.toString())
      assertEquals("5.0", binding.content.ratingBar.rating.toString())
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
      val viewHolder = provideViewHolder<MediaPagingAdapter.ViewHolder>()

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
