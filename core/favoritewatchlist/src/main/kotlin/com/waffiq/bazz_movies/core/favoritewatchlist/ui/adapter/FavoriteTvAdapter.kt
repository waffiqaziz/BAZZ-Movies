package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemMulmedBinding
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.FavWatchlistHelper.ratingHandler
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.releaseDateHandler
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreIdsToJoinName
import com.waffiq.bazz_movies.navigation.INavigator

class FavoriteTvAdapter(private val navigator: INavigator) :
  androidx.paging.PagingDataAdapter<MediaItem, FavoriteTvAdapter.ViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemMulmedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = getItem(position)
    if (data != null) {
      holder.bind(data)
      holder.itemView.startAnimation(
        AnimationUtils.loadAnimation(holder.itemView.context, fade_in)
      )
    }
  }

  inner class ViewHolder(private var binding: ItemMulmedBinding) :
    RecyclerView.ViewHolder(binding.root) {
    lateinit var data: MediaItem

    fun bind(mediaItem: MediaItem) {
      data = mediaItem
      showImage(binding, mediaItem)

      binding.tvTitle.text = itemView.context.titleHandler(mediaItem)
      binding.tvYearReleased.text = itemView.context.releaseDateHandler(mediaItem)
      binding.tvGenre.text = mediaItem.listGenreIds?.let { transformListGenreIdsToJoinName(it) }
        .takeUnless { it.isNullOrBlank() } ?: itemView.context.getString(not_available)
      binding.ratingBar.rating = (mediaItem.voteAverage ?: 0F) / 2
      binding.tvRating.text = ratingHandler(mediaItem.voteAverage)

      // OnClickListener
      binding.container.setOnClickListener {
        navigator.openDetails(itemView.context, mediaItem.copy(mediaType = TV_MEDIA_TYPE))
      }
    }

    private fun showImage(binding: ItemMulmedBinding, mediaItem: MediaItem) {
      binding.ivPicture.contentDescription = titleHandler(mediaItem)
      Glide.with(binding.ivPicture)
        .load(
          if (!mediaItem.posterPath.isNullOrEmpty()) {
            TMDB_IMG_LINK_POSTER_W185 + mediaItem.posterPath
          } else {
            ic_poster_error
          }
        )
        .placeholder(ic_bazz_placeholder_poster)
        .transform(CenterCrop())
        .transition(withCrossFade())
        .error(ic_poster_error)
        .into(binding.ivPicture)
    }
  }

  companion object {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MediaItem>() {
      override fun areItemsTheSame(
        oldItem: MediaItem,
        newItem: MediaItem
      ): Boolean {
        return oldItem.id == newItem.id
      }

      override fun areContentsTheSame(
        oldItem: MediaItem,
        newItem: MediaItem
      ): Boolean {
        return oldItem.id == newItem.id
      }
    }
  }
}
