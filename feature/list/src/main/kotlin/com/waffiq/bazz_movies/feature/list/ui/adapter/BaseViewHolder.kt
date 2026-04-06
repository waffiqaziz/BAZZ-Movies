package com.waffiq.bazz_movies.feature.list.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.domain.MediaItem

sealed class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
  abstract fun bind(media: MediaItem)
}
