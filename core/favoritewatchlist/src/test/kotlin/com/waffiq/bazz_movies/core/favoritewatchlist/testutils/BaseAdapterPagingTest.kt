package com.waffiq.bazz_movies.core.favoritewatchlist.testutils

import android.view.LayoutInflater
import androidx.annotation.IdRes
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import com.google.android.material.listitem.SwipeableListItem
import com.waffiq.bazz_movies.core.designsystem.databinding.ListItemMediaSwipeBinding
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.movieData
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging.MediaPagingAdapter
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

abstract class BaseAdapterPagingTest :
  BaseAdapterTest<MediaItem, PagingDataAdapter<MediaItem, *>>() {

  protected lateinit var adapter: MediaPagingAdapter
  protected lateinit var onDelete: (MediaItem) -> Unit
  protected lateinit var onAddToWatchlist: (MediaItem) -> Unit
  protected lateinit var inflater: LayoutInflater
  protected lateinit var binding: ListItemMediaSwipeBinding
  protected lateinit var viewHolder: MediaPagingAdapter.ViewHolder

  protected val pagingData = PagingData.from(listOf(movieData))

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  override fun setUp() {
    super.setUp()
    onDelete = mockk(relaxed = true)
    onAddToWatchlist = mockk(relaxed = true)
    inflater = LayoutInflater.from(context)
    binding = ListItemMediaSwipeBinding.inflate(inflater, null, false)
  }

  protected suspend fun setupBoundViewHolder() {
    adapter.submitData(pagingData)
    adapter.onBindViewHolder(viewHolder, 0)
  }

  protected fun setupSwipeableViewHolder(): MediaPagingAdapter.ViewHolder {
    provideRecyclerView(adapter) { adapter.submitData(pagingData) }
    return provideViewHolder<MediaPagingAdapter.ViewHolder>()
      ?: error("ViewHolder not found at position 0")
  }

  protected fun MediaPagingAdapter.ViewHolder.swipe(@IdRes revealLayoutId: Int) {
    swipeCallback.onSwipeStateChanged(
      SwipeableListItem.STATE_SWIPE_PRIMARY_ACTION,
      itemView.findViewById(revealLayoutId),
      100,
    )
  }
}
