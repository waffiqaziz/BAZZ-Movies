package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.local

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.listitem.ListItemCardView
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemFavoriteBinding
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.SwipeCallbackFactory.createSwipeCallback
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.local.MediaAdapterDBHelper.bindImageBackdrop
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.local.MediaAdapterDBHelper.bindMetadata
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.local.MediaAdapterDBHelper.bindOpenDetail
import com.waffiq.bazz_movies.navigation.INavigator

class FavoriteAdapterDB(
  private val navigator: INavigator,
  private val onDelete: (Favorite, Int) -> Unit,
  private val onAddToWatchlist: (Favorite, Int) -> Unit,
) : ListAdapter<Favorite, FavoriteAdapterDB.ViewHolder>(
  MediaAdapterDBHelper.FavoriteDiffCallback(),
) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position))
    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(holder.itemView.context, fade_in),
    )
  }

  inner class ViewHolder(private val binding: ItemFavoriteBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var data: Favorite

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    lateinit var swipeCallback: ListItemCardView.SwipeCallback

    fun bind(fav: Favorite) {
      data = fav

      bindOpenDetail(binding.containerResult, navigator, data)

      bindMetadata(binding.tvTitle, binding.tvYearReleased, binding.tvGenre, data)
      bindImageBackdrop(binding.ivPicture, data)

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
