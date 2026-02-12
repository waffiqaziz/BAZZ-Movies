package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging

import android.content.Context
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.listitem.ListItemCardView
import com.google.android.material.listitem.ListItemLayout
import com.google.android.material.listitem.ListItemRevealLayout
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.SwipeCallbackFactory.createSwipeCallback
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.FavWatchlistHelper.ratingHandler
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.releaseDateHandler
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreIdsToJoinName
import com.waffiq.bazz_movies.navigation.INavigator

object MediaAdapterPagingHelper {

  /**
   * General binding function for MediaItem that can be used across different adapters
   */
  fun bindPagingItem(
    mediaItem: MediaItem,
    ivPicture: ShapeableImageView,
    tvTitle: TextView,
    tvYearReleased: TextView,
    tvGenre: TextView,
    ratingBar: RatingBar,
    tvRating: TextView,
    containerResult: ListItemCardView,
    revealLayoutStart: ListItemRevealLayout,
    revealLayoutEnd: ListItemRevealLayout,
    listItemLayout: ListItemLayout,
    context: Context,
    navigator: INavigator,
    mediaType: String,
    dataProvider: () -> MediaItem,
    positionProvider: () -> Int,
    onDelete: (MediaItem, Int) -> Unit,
    onAddToWatchlist: (MediaItem, Int) -> Unit,
  ): ListItemCardView.SwipeCallback {
    // Image loading
    setImagePoster(ivPicture, mediaItem)

    // Text binding
    context.setTitleYearGenreRating(
      tvTitle = tvTitle,
      tvYearReleased = tvYearReleased,
      tvGenre = tvGenre,
      ratingBar = ratingBar,
      tvRating = tvRating,
      mediaItem = mediaItem,
    )

    // OnClickListener
    containerResult.setOnClickListener {
      navigator.openDetails(context, mediaItem.copy(mediaType = mediaType))
    }

    // Setup swipe callback
    val swipeCallback = createSwipeCallback(
      startLayoutProvider = { revealLayoutStart },
      endLayoutProvider = { revealLayoutEnd },
      listItemLayoutProvider = { listItemLayout },
      dataProvider = dataProvider,
      positionProvider = positionProvider,
      onDelete = onDelete,
      onAddToWatchlist = onAddToWatchlist,
    )
    containerResult.addSwipeCallback(swipeCallback)

    return swipeCallback
  }

  private fun Context.setTitleYearGenreRating(
    tvTitle: TextView,
    tvYearReleased: TextView,
    tvGenre: TextView,
    ratingBar: RatingBar,
    tvRating: TextView,
    mediaItem: MediaItem,
  ) {
    tvTitle.text = titleHandler(mediaItem)
    tvYearReleased.text = releaseDateHandler(mediaItem)
    tvGenre.text = mediaItem.listGenreIds?.let { transformListGenreIdsToJoinName(it) }
      .takeUnless { it.isNullOrBlank() } ?: getString(not_available)
    ratingBar.rating = (mediaItem.voteAverage ?: 0F) / 2
    tvRating.text = ratingHandler(mediaItem.voteAverage)
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun setImagePoster(ivPicture: ShapeableImageView, data: MediaItem) {
    ivPicture.contentDescription = titleHandler(data)
    Glide.with(ivPicture)
      .load(
        if (!data.posterPath.isNullOrEmpty()) {
          TMDB_IMG_LINK_POSTER_W185 + data.posterPath
        } else {
          ic_poster_error
        },
      )
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
