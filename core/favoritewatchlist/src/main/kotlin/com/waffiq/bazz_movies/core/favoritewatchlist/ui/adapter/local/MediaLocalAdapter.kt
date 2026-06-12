package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.local

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.listitem.ListItemCardView
import com.waffiq.bazz_movies.core.designsystem.databinding.ListItemMediaSwipeBinding
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.SwipeAdapterHelper.bindContent
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.SwipeAdapterHelper.createSwipeCallback
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.local.MediaLocalAdapterHelper.bindMetaData
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.local.MediaLocalAdapterHelper.bindOpenDetail
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.local.MediaLocalAdapterHelper.bindPicture
import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.SwipeConfig
import com.waffiq.bazz_movies.navigation.INavigator

class MediaLocalAdapter(
  private val navigator: INavigator,
  private val config: SwipeConfig,
  private val onDelete: (Favorite, Int) -> Unit,
  private val onAddToWatchlist: (Favorite, Int) -> Unit,
) : ListAdapter<Favorite, MediaLocalAdapter.ViewHolder>(MediaLocalDiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ListItemMediaSwipeBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false,
    )
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position))
    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(holder.itemView.context, fade_in),
    )
  }

  inner class ViewHolder(private val binding: ListItemMediaSwipeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var data: Favorite

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    lateinit var swipeCallback: ListItemCardView.SwipeCallback

    fun bind(fav: Favorite) {
      data = fav

      binding.containerResult.bindOpenDetail(navigator, data)
      binding.bindContent(config)
      binding.bindMetaData(fav)
      binding.content.ivPicture.bindPicture(fav)

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
