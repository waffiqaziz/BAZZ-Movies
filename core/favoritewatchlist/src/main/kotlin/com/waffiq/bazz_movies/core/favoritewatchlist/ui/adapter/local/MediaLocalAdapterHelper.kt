package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.local

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.listitem.ListItemCardView
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.designsystem.databinding.ListItemMediaSwipeBinding
import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.utils.DateFormatter.dateFormatterStandard
import com.waffiq.bazz_movies.core.utils.RatingHelper.ratingHandler
import com.waffiq.bazz_movies.core.utils.RatingHelper.setRatingBar
import com.waffiq.bazz_movies.navigation.INavigator

object MediaLocalAdapterHelper {

  fun ListItemMediaSwipeBinding.bindMetaData(fav: Favorite) {
    content.apply {
      tvTitle.text = fav.title
      tvYearReleased.text = dateFormatterStandard(fav.releaseDate)
        .ifEmpty { tvYearReleased.context.getString(not_available) }
      tvGenre.text = fav.genre
      ratingBar.rating = setRatingBar(fav.rating)
      tvRating.text = ratingHandler(fav.rating)
    }
  }

  fun ShapeableImageView.bindPicture(fav: Favorite) {
    contentDescription = fav.title
    tag = fav.title

    Glide.with(this)
      .load(TMDB_IMG_LINK_POSTER_W185 + fav.poster)
      .placeholder(ic_bazz_placeholder_poster)
      .transform(CenterCrop())
      .transition(withCrossFade())
      .error(ic_poster_error)
      .into(this)
  }

  fun ListItemCardView.bindOpenDetail(navigator: INavigator, fav: Favorite) {
    setOnClickListener {
      navigator.openDetails(
        context,
        MediaItem(
          backdropPath = fav.backDrop,
          posterPath = fav.poster,
          releaseDate = fav.releaseDate,
          overview = fav.overview,
          title = fav.title,
          voteAverage = fav.rating,
          originalTitle = fav.title,
          mediaType = fav.mediaType,
          id = fav.mediaId,
        ),
      )
    }
  }
}
