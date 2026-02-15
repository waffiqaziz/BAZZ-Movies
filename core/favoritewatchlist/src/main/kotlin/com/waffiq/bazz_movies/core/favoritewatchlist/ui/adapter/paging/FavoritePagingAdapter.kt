package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.VisibleForTesting
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.listitem.ListItemCardView
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemPagingFavoriteBinding
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.SwipeCallbackFactory.createSwipeCallback
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging.MediaAdapterPagingHelper.DIFF_CALLBACK
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging.MediaAdapterPagingHelper.bindImagePoster
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging.MediaAdapterPagingHelper.bindMetadata
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

      binding.containerResult.setOnClickListener {
        navigator.openDetails(itemView.context, mediaItem.copy(mediaType = mediaType))
      }

      bindImagePoster(binding.ivPicture, data)
      itemView.context.bindMetadata(
        tvTitle = binding.tvTitle,
        tvYearReleased = binding.tvYearReleased,
        tvGenre = binding.tvGenre,
        ratingBar = binding.ratingBar,
        tvRating = binding.tvRating,
        mediaItem = data,
      )

      swipeCallback = createSwipeCallback(
        startLayout = binding.revealLayoutStart,
        endLayout = binding.revealLayoutEnd,
        listItemLayout = binding.listItemLayout,
        onDelete = { onDelete(data, bindingAdapterPosition) },
        onAddToWatchlist = { onAddToWatchlist(data, bindingAdapterPosition) },
      )
      binding.containerResult.addSwipeCallback(swipeCallback)
    }
  }
}
