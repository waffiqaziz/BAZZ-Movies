package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.content.res.AppCompatResources
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.listitem.ListItemCardView
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_hearth
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_watchlist_outlined
import com.waffiq.bazz_movies.core.designsystem.R.string.add_to_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.add_to_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.remove_from_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.remove_from_watchlist
import com.waffiq.bazz_movies.core.designsystem.databinding.ListItemMediaBinding
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.SwipeCallbackFactory.createSwipeCallback
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging.MediaAdapterPagingHelper.DIFF_CALLBACK
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging.MediaAdapterPagingHelper.bindImagePoster
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging.MediaAdapterPagingHelper.bindMetadata
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.navigation.INavigator

class MediaPagingAdapter(
  private val navigator: INavigator,
  private val mediaType: String,
  private val config: SwipeConfig,
  private val onDelete: (MediaItem) -> Unit,
  private val onActionEnd: (MediaItem) -> Unit, // Add to Watchlist or Add to Favorite
) : PagingDataAdapter<MediaItem, MediaPagingAdapter.ViewHolder>(DIFF_CALLBACK) {

  /**
   * Used to differ between Favorite and Watchlist screens:
   * the button labels and the end-side icon.
   */
  data class SwipeConfig(
    @StringRes val deleteContentDescription: Int,
    @StringRes val endActionContentDescription: Int,
    @DrawableRes val endActionIcon: Int,
  ) {
    companion object {
      fun forFavorite() =
        SwipeConfig(
          deleteContentDescription = remove_from_favorite,
          endActionContentDescription = add_to_watchlist,
          endActionIcon = ic_watchlist_outlined,
        )

      fun forWatchlist() =
        SwipeConfig(
          deleteContentDescription = remove_from_watchlist,
          endActionContentDescription = add_to_favorite,
          endActionIcon = ic_hearth,
        )
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ListItemMediaBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false,
    )
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = getItem(position) ?: return
    holder.bind(data)
    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(holder.itemView.context, fade_in),
    )
  }

  inner class ViewHolder(private val binding: ListItemMediaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var data: MediaItem

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    lateinit var swipeCallback: ListItemCardView.SwipeCallback

    fun bind(mediaItem: MediaItem) {
      data = mediaItem

      // apply content descriptions and icon from config
      bindContent()

      binding.containerResult.setOnClickListener {
        navigator.openDetails(itemView.context, mediaItem.copy(mediaType = mediaType))
      }

      bindImagePoster(binding.content.ivPicture, data)
      itemView.context.bindMetadata(
        tvTitle = binding.content.tvTitle,
        tvYearReleased = binding.content.tvYearReleased,
        tvGenre = binding.content.tvGenre,
        ratingBar = binding.content.ratingBar,
        tvRating = binding.content.tvRating,
        mediaItem = data,
      )

      if (::swipeCallback.isInitialized) {
        binding.containerResult.removeSwipeCallback(swipeCallback)
      }
      swipeCallback = createSwipeCallback(
        startLayout = binding.revealLayoutStart,
        endLayout = binding.revealLayoutEnd,
        listItemLayout = binding.listItemLayout,
        onDelete = { onDelete(data) },
        onAddToWatchlist = { onActionEnd(data) },
      )
      binding.containerResult.addSwipeCallback(swipeCallback)
    }

    private fun bindContent() {
      binding.btnActionStart.contentDescription =
        itemView.context.getString(config.deleteContentDescription)
      binding.btnActionEnd.contentDescription =
        itemView.context.getString(config.endActionContentDescription)
      binding.btnActionEnd.icon =
        AppCompatResources.getDrawable(itemView.context, config.endActionIcon)
    }
  }
}
