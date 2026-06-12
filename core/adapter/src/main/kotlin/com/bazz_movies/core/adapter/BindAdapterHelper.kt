package com.bazz_movies.core.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.imageview.ShapeableImageView
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemMediaBinding
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.posterSource
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.releaseDateHandler
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.core.utils.GenreHelper.getGenre
import com.waffiq.bazz_movies.core.utils.RatingHelper.ratingHandler
import com.waffiq.bazz_movies.core.utils.RatingHelper.setRatingBar

object BindAdapterHelper {

  fun ItemMediaBinding.bindMetaData(mediaItem: MediaItem) {
    apply {
      tvTitle.text = titleHandler(mediaItem)
      tvYearReleased.text = tvYearReleased.context.releaseDateHandler(mediaItem)
      tvGenre.text = tvGenre.context.getGenre(mediaItem.listGenreIds)
      ratingBar.rating = setRatingBar(mediaItem.voteAverage)
      tvRating.text = ratingHandler(mediaItem.voteAverage)
    }
  }

  fun ShapeableImageView.bindPicture(mediaItem: MediaItem) {
    contentDescription = titleHandler(mediaItem)
    tag = titleHandler(mediaItem)

    Glide.with(this)
      .load(mediaItem.posterSource)
      .placeholder(ic_bazz_placeholder_poster)
      .transform(CenterCrop())
      .transition(withCrossFade())
      .error(ic_poster_error)
      .into(this)
  }
}
