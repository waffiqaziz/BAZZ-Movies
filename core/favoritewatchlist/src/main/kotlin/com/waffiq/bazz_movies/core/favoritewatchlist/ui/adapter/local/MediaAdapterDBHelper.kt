package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.local

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.listitem.ListItemCardView
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_BACKDROP_W300
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_search
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.utils.DateFormatter.dateFormatterStandard
import com.waffiq.bazz_movies.navigation.INavigator

object MediaAdapterDBHelper {

  fun bindMetadata(
    tvTitle: TextView,
    tvYearReleased: TextView,
    tvGenre: TextView,
    fav: Favorite,
  ) {
    tvTitle.text = fav.title
    tvGenre.text = fav.genre
    tvYearReleased.text = dateFormatterStandard(fav.releaseDate)
      .ifEmpty { tvYearReleased.context.getString(not_available) }
  }

  fun bindImageBackdrop(ivPicture: ShapeableImageView, fav: Favorite) {
    ivPicture.contentDescription = fav.title
    ivPicture.tag = fav.title

    Glide.with(ivPicture)
      .load(
        if (fav.backDrop != ivPicture.context.getString(not_available)) {
          TMDB_IMG_LINK_BACKDROP_W300 + fav.backDrop
        } else if (fav.poster != ivPicture.context.getString(not_available)) {
          TMDB_IMG_LINK_POSTER_W185 + fav.poster
        } else {
          ic_backdrop_error
        },
      )
      .placeholder(ic_bazz_placeholder_search)
      .transition(withCrossFade())
      .error(ic_backdrop_error)
      .into(ivPicture)
  }

  fun bindOpenDetail(
    containerResult: ListItemCardView,
    navigator: INavigator,
    fav: Favorite,
  ) {
    containerResult.setOnClickListener {
      navigator.openDetails(
        containerResult.context,
        MediaItem(
          backdropPath = fav.backDrop,
          posterPath = fav.poster,
          releaseDate = fav.releaseDate,
          overview = fav.overview,
          title = fav.title,
          originalTitle = fav.title,
          mediaType = fav.mediaType,
          id = fav.mediaId,
        ),
      )
    }
  }

  class FavoriteDiffCallback : DiffUtil.ItemCallback<Favorite>() {
    override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean =
      oldItem.mediaId == newItem.mediaId &&
        oldItem.isFavorite == newItem.isFavorite &&
        oldItem.isWatchlist == newItem.isWatchlist &&
        oldItem.mediaType == newItem.mediaType

    override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean =
      oldItem == newItem
  }
}
