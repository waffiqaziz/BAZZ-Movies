package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.local

import androidx.recyclerview.widget.DiffUtil
import com.waffiq.bazz_movies.core.models.Favorite

class MediaLocalDiffCallback : DiffUtil.ItemCallback<Favorite>() {
  override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean =
    oldItem.mediaId == newItem.mediaId &&
      oldItem.isFavorite == newItem.isFavorite &&
      oldItem.isWatchlist == newItem.isWatchlist &&
      oldItem.mediaType == newItem.mediaType

  override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean =
    oldItem == newItem
}
