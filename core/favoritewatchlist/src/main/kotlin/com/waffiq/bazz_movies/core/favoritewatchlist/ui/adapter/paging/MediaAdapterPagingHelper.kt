package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging

import androidx.recyclerview.widget.DiffUtil
import com.waffiq.bazz_movies.core.models.MediaItem

object MediaAdapterPagingHelper {

  val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MediaItem>() {
    override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
      oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
      oldItem.id == newItem.id
  }
}
