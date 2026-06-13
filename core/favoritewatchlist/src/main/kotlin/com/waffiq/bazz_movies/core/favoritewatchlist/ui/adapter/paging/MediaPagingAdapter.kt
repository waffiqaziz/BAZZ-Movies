package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.VisibleForTesting
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bazz_movies.core.adapter.BindAdapterHelper.bindMetaData
import com.bazz_movies.core.adapter.BindAdapterHelper.bindPicture
import com.google.android.material.listitem.ListItemCardView
import com.waffiq.bazz_movies.core.designsystem.databinding.ListItemMediaSwipeBinding
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.SwipeAdapterHelper.bindContent
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.SwipeAdapterHelper.createSwipeCallback
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging.MediaAdapterPagingHelper.DIFF_CALLBACK
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.SwipeConfig
import com.waffiq.bazz_movies.navigation.INavigator

class MediaPagingAdapter(
  private val navigator: INavigator,
  private val mediaType: String,
  private val config: SwipeConfig,
  private val onDelete: (MediaItem) -> Unit,
  private val onActionEnd: (MediaItem) -> Unit, // Add to Watchlist or Add to Favorite
) : PagingDataAdapter<MediaItem, MediaPagingAdapter.ViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ListItemMediaSwipeBinding.inflate(
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

  inner class ViewHolder(private val binding: ListItemMediaSwipeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    lateinit var swipeCallback: ListItemCardView.SwipeCallback

    fun bind(mediaItem: MediaItem) {
      // apply content descriptions and icon from config
      binding.bindContent(config)

      binding.containerResult.setOnClickListener {
        navigator.openDetails(itemView.context, mediaItem.copy(mediaType = mediaType))
      }

      binding.content.bindMetaData(mediaItem)
      binding.content.ivPicture.bindPicture(mediaItem)

      if (::swipeCallback.isInitialized) {
        binding.containerResult.removeSwipeCallback(swipeCallback)
      }
      swipeCallback = createSwipeCallback(
        startLayout = binding.revealLayoutStart,
        endLayout = binding.revealLayoutEnd,
        listItemLayout = binding.listItemLayout,
        onDelete = { onDelete(mediaItem) },
        onAddToWatchlist = { onActionEnd(mediaItem) },
      )
      binding.containerResult.addSwipeCallback(swipeCallback)
    }
  }
}
