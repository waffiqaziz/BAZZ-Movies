package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging

import android.R.anim.fade_in
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.VisibleForTesting
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.listitem.ListItemCardView
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemPagingFavoriteBinding
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.SwipeCallbackFactory.createSwipeCallback
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging.MediaAdapterPagingHelper.DIFF_CALLBACK
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.FavWatchlistHelper.ratingHandler
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.releaseDateHandler
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreIdsToJoinName
import com.waffiq.bazz_movies.navigation.INavigator

class FavoritePagingAdapter(
  private val navigator: INavigator,
  private val mediaType: String,
  private val onDelete: (MediaItem, Int) -> Unit,
  private val onAddToWatchlist: (MediaItem, Int) -> Unit,
) : PagingDataAdapter<MediaItem, FavoritePagingAdapter.ViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding =
      ItemPagingFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

  inner class ViewHolder(private var binding: ItemPagingFavoriteBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var data: MediaItem

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    lateinit var swipeCallback: ListItemCardView.SwipeCallback

    fun bind(mediaItem: MediaItem) {
      data = mediaItem
      setImagePoster(binding, data)
      itemView.context.setTitleYearGenreRating(binding, mediaItem)

      // OnClickListener
      binding.containerResult.setOnClickListener {
        navigator.openDetails(itemView.context, mediaItem.copy(mediaType = mediaType))
      }

      // Setup swipe callback
      swipeCallback = createSwipeCallback(
        startLayoutProvider = { binding.revealLayoutStart },
        endLayoutProvider = { binding.revealLayoutEnd },
        listItemLayoutProvider = { binding.listItemLayout },
        dataProvider = { data },
        positionProvider = { bindingAdapterPosition },
        onDelete = onDelete,
        onAddToWatchlist = onAddToWatchlist,
      )
      binding.containerResult.addSwipeCallback(swipeCallback)
    }
  }

  private fun Context.setTitleYearGenreRating(
    binding: ItemPagingFavoriteBinding,
    mediaItem: MediaItem,
  ) {
    binding.tvTitle.text = titleHandler(mediaItem)
    binding.tvYearReleased.text = releaseDateHandler(mediaItem)
    binding.tvGenre.text = mediaItem.listGenreIds?.let { transformListGenreIdsToJoinName(it) }
      .takeUnless { it.isNullOrBlank() } ?: getString(not_available)
    binding.ratingBar.rating = (mediaItem.voteAverage ?: 0F) / 2
    binding.tvRating.text = ratingHandler(mediaItem.voteAverage)
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun setImagePoster(binding: ItemPagingFavoriteBinding, data: MediaItem) {
    binding.ivPicture.contentDescription = titleHandler(data)
    Glide.with(binding.ivPicture)
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
      .into(binding.ivPicture)
  }
}
