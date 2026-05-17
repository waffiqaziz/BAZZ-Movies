package com.waffiq.bazz_movies.feature.list.ui.adapter

import android.R.anim.fade_in
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_search
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemListBinding
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemResultBinding
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.dateOf
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.imageSource
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.posterSource
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.core.utils.GenreHelper.getGenre
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.MediaSource

class ListAdapter(private val navigator: INavigator, private val source: MediaSource) :
  PagingDataAdapter<MediaItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

  private var isGridMode = true

  fun setGridMode(isGrid: Boolean) {
    if (isGridMode != isGrid) {
      isGridMode = isGrid
      notifyItemRangeChanged(0, itemCount)
    }
  }

  fun isGridMode() = isGridMode

  override fun getItemViewType(position: Int) = if (isGridMode) VIEW_TYPE_GRID else VIEW_TYPE_LIST

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
    if (viewType == VIEW_TYPE_GRID) {
      GridViewHolder(ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    } else {
      ListViewHolder(ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val data = getItem(position) ?: return
    holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, fade_in))
    (holder as BaseViewHolder).bind(data)
  }

  private fun Context.openDetails(mediaItem: MediaItem) {
    val item = when (source) {
      is MediaSource.Trending -> mediaItem
      is MediaSource.Typed -> mediaItem.copy(mediaType = source.mediaType)
    }
    navigator.openDetails(this, item)
  }

  inner class GridViewHolder(private val binding: ItemListBinding) :
    BaseViewHolder(binding.root) {

    override fun bind(mediaItem: MediaItem) {
      binding.imgPoster.contentDescription = titleHandler(mediaItem)
      Glide.with(binding.imgPoster)
        .load(mediaItem.posterSource)
        .placeholder(ic_bazz_placeholder_poster)
        .transform(CenterCrop())
        .transition(withCrossFade())
        .error(ic_poster_error)
        .into(binding.imgPoster)

      binding.imgPoster.setOnClickListener {
        itemView.context.openDetails(mediaItem)
      }
    }
  }

  inner class ListViewHolder(internal val binding: ItemResultBinding) :
    BaseViewHolder(binding.root) {

    override fun bind(mediaItem: MediaItem) {
      binding.ivPicture.contentDescription = titleHandler(mediaItem)
      Glide.with(binding.ivPicture)
        .load(mediaItem.imageSource)
        .placeholder(ic_bazz_placeholder_search)
        .transform(CenterCrop())
        .transition(withCrossFade())
        .error(ic_poster_error)
        .into(binding.ivPicture)

      binding.tvTitle.text = titleHandler(mediaItem)
      binding.tvYearReleased.text = itemView.context.dateOf(mediaItem)
      binding.tvGenre.text = itemView.context.getGenre(mediaItem.listGenreIds)

      binding.containerResult.setOnClickListener {
        itemView.context.openDetails(mediaItem)
      }
    }
  }

  companion object {
    const val VIEW_TYPE_GRID = 0
    const val VIEW_TYPE_LIST = 1

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MediaItem>() {
      override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem) =
        oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem) = oldItem == newItem
    }
  }
}
