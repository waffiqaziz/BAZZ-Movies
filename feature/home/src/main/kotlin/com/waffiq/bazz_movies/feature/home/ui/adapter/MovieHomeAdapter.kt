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
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemPosterBinding
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.navigation.INavigator

class MovieHomeAdapter(private val navigator: INavigator) :
  PagingDataAdapter<MediaItem, MovieHomeAdapter.ViewHolder>(DIFF_CALLBACK) {

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

    fun bind(movie: MediaItem) {
      binding.imgPoster.contentDescription =
        movie.name ?: movie.title ?: movie.originalTitle ?: movie.originalName

      Glide.with(binding.imgPoster)
        .load(
          if (!movie.posterPath.isNullOrEmpty()) {
            TMDB_IMG_LINK_POSTER_W185 + movie.posterPath
          } else {
            ic_poster_error
          },
        )
        .placeholder(ic_bazz_placeholder_poster)
        .transform(CenterCrop())
        .transition(withCrossFade())
        .error(ic_poster_error)
        .into(binding.imgPoster)

      // image OnClickListener
      binding.imgPoster.setOnClickListener {
        navigator.openDetails(itemView.context, movie.copy(mediaType = MOVIE_MEDIA_TYPE))
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
