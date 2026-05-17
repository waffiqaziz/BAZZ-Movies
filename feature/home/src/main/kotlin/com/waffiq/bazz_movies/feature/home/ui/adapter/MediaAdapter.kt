package com.waffiq.bazz_movies.feature.home.ui.adapter

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemPosterBinding
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.posterSource
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.MediaSource

class MediaAdapter(private val navigator: INavigator, private val source: MediaSource) :
  PagingDataAdapter<MediaItem, MediaAdapter.ViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemPosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = getItem(position)
    if (data != null) {
      holder.bind(data)
      holder.itemView.startAnimation(
        AnimationUtils.loadAnimation(holder.itemView.context, fade_in),
      )
    }
  }

  inner class ViewHolder(private var binding: ItemPosterBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(mediaItem: MediaItem) {
      binding.imgPoster.contentDescription = titleHandler(mediaItem)

      Glide.with(binding.imgPoster)
        .load(mediaItem.posterSource)
        .placeholder(ic_bazz_placeholder_poster)
        .transform(CenterCrop())
        .transition(withCrossFade())
        .error(ic_poster_error)
        .into(binding.imgPoster)

      // image OnClickListener
      binding.imgPoster.setOnClickListener {
        val item = when (source) {
          is MediaSource.Trending -> mediaItem
          is MediaSource.Typed -> mediaItem.copy(mediaType = source.mediaType)
        }
        navigator.openDetails(itemView.context, item)
      }
    }
  }

  companion object {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MediaItem>() {
      override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
        oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
        oldItem == newItem
    }
  }
}
