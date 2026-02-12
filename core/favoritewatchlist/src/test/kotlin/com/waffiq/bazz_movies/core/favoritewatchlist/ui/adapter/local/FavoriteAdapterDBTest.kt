package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.local

import android.os.Looper
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.test.core.app.ApplicationProvider
import com.google.android.material.listitem.SwipeableListItem
import com.waffiq.bazz_movies.core.designsystem.R.id.container_result
import com.waffiq.bazz_movies.core.designsystem.R.id.reveal_layout_end
import com.waffiq.bazz_movies.core.designsystem.R.id.reveal_layout_start
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemFavoriteBinding
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.BaseAdapterDBTest
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.favorite
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.indonesianMovie
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.indonesianMovie2
import io.mockk.slot
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowLooper

class FavoriteAdapterDBTest : BaseAdapterDBTest() {
  private lateinit var adapter: FavoriteAdapterDB

  @Before
  fun setup() {
    super.setUp()
    adapter = FavoriteAdapterDB(navigator, onDelete, onAddToWatchlist)
  }

  @Test
  fun submitList_whenCalledTwice_updatesListAndItemCount() {
    val oldList = listOf(favorite)
    val newList = listOf(
      favorite.copy(id = 2, mediaId = 2, title = indonesianMovie2),
      favorite.copy(id = 3, mediaId = 3, title = "Indonesian Movie 3")
    )

    adapter.submitList(oldList)
    Thread.sleep(100)
    ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
    assertEquals(1, adapter.itemCount)

    adapter.submitList(newList)
    Thread.sleep(100)
    ShadowLooper.runUiThreadTasksIncludingDelayedTasks()

    assertEquals(2, adapter.itemCount)
    assertEquals(newList, adapter.currentList)
  }

  @Test
  fun onBindViewHolder_whenCalled_bindsCorrectDataForAllCases() {
    val inflater = LayoutInflater.from(context)
    val binding = ItemFavoriteBinding.inflate(inflater, null, false)
    val viewHolder = adapter.ViewHolder(binding)

    // valid data
    adapter.submitList(listOf(favorite))
    ShadowLooper.runUiThreadTasksIncludingDelayedTasks()

    adapter.onBindViewHolder(viewHolder, 0)
    assertEquals(indonesianMovie, binding.tvTitle.text.toString())
    assertEquals("Action", binding.tvGenre.text.toString())
    assertEquals("Apr 04, 1979", binding.tvYearReleased.text.toString())

    // released date empty
    adapter.submitList(listOf(favorite.copy(releaseDate = "")))
    ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
    adapter.onBindViewHolder(viewHolder, 0)
    assertEquals("N/A", binding.tvYearReleased.text.toString())
  }

  @Test
  fun onCreateViewHolder_whenCalled_createsViewHolderCorrectly() {
    val parent = FrameLayout(context)
    val viewHolder = adapter.onCreateViewHolder(parent, 0)
    assertNotNull(viewHolder)

    // verify ViewHolder is correctly inflated
    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
  }

  @Test
  fun bind_whenCalled_loadsCorrectImageOrPlaceholder() {
    val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(context))
    val viewHolder = adapter.ViewHolder(binding)

    val testCases = listOf(
      favorite.copy(
        backDrop = "N/A",
        poster = "N/A",
        title = indonesianMovie2
      ) to indonesianMovie2,
      favorite.copy(backDrop = "N/A") to indonesianMovie
    )

    testCases.forEach { (favorite, favoriteTitle) ->
      viewHolder.bind(favorite)
      assertNotNull(viewHolder.data)

      // expect the tag has correct ID
      assertEquals(favoriteTitle, binding.ivPicture.tag)
    }
  }

  @Test
  fun onBindViewHolder_whenClicked_callsNavigator() {
    val inflater = LayoutInflater.from(ApplicationProvider.getApplicationContext())
    val binding = ItemFavoriteBinding.inflate(inflater, FrameLayout(inflater.context), false)
    val viewHolder = adapter.ViewHolder(binding)

    adapter.submitList(listOf(favorite))
    adapter.onBindViewHolder(viewHolder, 0)

    val resultSlot = slot<MediaItem>()
    binding.containerResult.performClick()

    // wait hte UI
    shadowOf(Looper.getMainLooper()).idle()

    // verify navigator.openDetails() is called with the correct argument
    verify { navigator.openDetails(any(), capture(resultSlot)) }

    // expect captured MediaItem matches expected values
    val capturedResult = resultSlot.captured
    assertEquals(favorite.mediaId, capturedResult.id)
    assertEquals(favorite.title, capturedResult.title)
    assertEquals(favorite.poster, capturedResult.posterPath)
    assertEquals(favorite.overview, capturedResult.overview)
    assertEquals(favorite.title, capturedResult.originalTitle)
    assertEquals(favorite.mediaType, capturedResult.mediaType)
    assertEquals(favorite.releaseDate, capturedResult.releaseDate)
  }

  @Test
  fun bind_whenSwipedRight_callsOnDelete() {
    provideRecyclerView(adapter, submitData = { adapter.submitList(listOf(favorite)) })
    val viewHolder = provideViewHolder<FavoriteAdapterDB.ViewHolder>()

    assertNotNull(viewHolder)
    assertNotNull(viewHolder!!.swipeCallback)

    // Trigger the actual callback
    viewHolder.swipeCallback.onSwipeStateChanged(
      SwipeableListItem.STATE_SWIPE_PRIMARY_ACTION,
      viewHolder.itemView.findViewById(reveal_layout_start),
      100
    )

    verify { onDelete(favorite, 0) }
  }

  @Test
  fun bind_whenSwipedLeft_callsOnAddToWatchlist() {
    provideRecyclerView(adapter, submitData = { adapter.submitList(listOf(favorite)) })
    val viewHolder = provideViewHolder<FavoriteAdapterDB.ViewHolder>()

    assertNotNull(viewHolder)
    assertNotNull(viewHolder!!.swipeCallback)

    // Trigger the actual callback
    viewHolder.swipeCallback.onSwipeStateChanged(
      SwipeableListItem.STATE_SWIPE_PRIMARY_ACTION,
      viewHolder.itemView.findViewById(reveal_layout_end),
      100
    )

    verify { onAddToWatchlist(favorite, 0) }
  }

  @Test
  fun bind_whenSwiped_othersState() {
    provideRecyclerView(adapter, submitData = { adapter.submitList(listOf(favorite)) })
    val viewHolder = provideViewHolder<FavoriteAdapterDB.ViewHolder>()

    assertNotNull(viewHolder)
    assertNotNull(viewHolder!!.swipeCallback)

    // simulate dragging
    viewHolder.swipeCallback.onSwipeStateChanged(
      SwipeableListItem.STATE_DRAGGING,
      viewHolder.itemView.findViewById(reveal_layout_end),
      100
    )

    // simulate swipe wrong item
    viewHolder.swipeCallback.onSwipeStateChanged(
      SwipeableListItem.STATE_SWIPE_PRIMARY_ACTION,
      viewHolder.itemView.findViewById(container_result),
      100
    )
  }

  @Test
  fun viewHolder_whenUnbound_throwsThenInitializes() {
    val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(context))
    val viewHolder = adapter.ViewHolder(binding)

    // uninitialized access should throw exception
    assertThrows(UninitializedPropertyAccessException::class.java) { viewHolder.data }

    // initialize with bind()
    viewHolder.bind(favorite)

    // explicitly access data again to hit both branches
    assertNotNull(viewHolder.data)
    assertEquals(favorite, viewHolder.data)
    assertEquals("movie", viewHolder.data.mediaType)

    // clickable
    viewHolder.itemView.performClick()
  }
}
