package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging

import android.content.Context
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.imageview.ShapeableImageView
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.FavWatchlistHelper.ratingHandler
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.posterSource
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.releaseDateHandler
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.core.utils.GenreHelper.getGenre

object MediaAdapterPagingHelper {

  fun Context.bindMetadata(
    tvTitle: TextView,
    tvYearReleased: TextView,
    tvGenre: TextView,
    ratingBar: RatingBar,
    tvRating: TextView,
    mediaItem: MediaItem,
  ) {
    tvTitle.text = titleHandler(mediaItem)
    tvYearReleased.text = releaseDateHandler(mediaItem)
    tvGenre.text = getGenre(mediaItem.listGenreIds)
    ratingBar.rating = (mediaItem.voteAverage ?: 0F) / 2
    tvRating.text = ratingHandler(mediaItem.voteAverage)
  }

  fun bindImagePoster(ivPicture: ShapeableImageView, data: MediaItem) {
    ivPicture.contentDescription = titleHandler(data)
    ivPicture.tag = titleHandler(data)

    Glide.with(ivPicture)
      .load(data.posterSource)
      .placeholder(ic_bazz_placeholder_poster)
      .transform(CenterCrop())
      .transition(withCrossFade())
      .error(ic_poster_error)
      .into(ivPicture)
  }

  val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MediaItem>() {
    override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
      oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
      oldItem.id == newItem.id
  }
}
