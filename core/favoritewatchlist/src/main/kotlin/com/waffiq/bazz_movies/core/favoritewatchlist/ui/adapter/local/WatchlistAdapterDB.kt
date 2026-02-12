package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.local

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.listitem.ListItemCardView
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemWatchlistBinding
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.navigation.INavigator

class WatchlistAdapterDB(
  private val navigator: INavigator,
  private val onDelete: (Favorite, Int) -> Unit,
  private val onAddToWatchlist: (Favorite, Int) -> Unit,
) : ListAdapter<Favorite, WatchlistAdapterDB.ViewHolder>(
  MediaAdapterDBHelper.FavoriteDiffCallback(),
) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemWatchlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position))
    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(holder.itemView.context, fade_in),
    )
  }

  inner class ViewHolder(private val binding: ItemWatchlistBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var data: Favorite

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    lateinit var swipeCallback: ListItemCardView.SwipeCallback

    fun bind(fav: Favorite) {
      data = fav
      swipeCallback = MediaAdapterDBHelper.bindMediaItem(
        fav = fav,
        ivPicture = binding.ivPicture,
        tvTitle = binding.tvTitle,
        tvGenre = binding.tvGenre,
        tvYearReleased = binding.tvYearReleased,
        containerResult = binding.containerResult,
        revealLayoutStart = binding.revealLayoutStart,
        revealLayoutEnd = binding.revealLayoutEnd,
        listItemLayout = binding.listItemLayout,
        navigator = navigator,
        dataProvider = { data },
        positionProvider = { bindingAdapterPosition },
        onDelete = onDelete,
        onAddToWatchlist = onAddToWatchlist,
      )
    }
  }
}
