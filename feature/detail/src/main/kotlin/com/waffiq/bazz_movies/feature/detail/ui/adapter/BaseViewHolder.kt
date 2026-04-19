package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.domain.MediaCastItem

sealed class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
  abstract fun bind(cast: MediaCastItem)
}
