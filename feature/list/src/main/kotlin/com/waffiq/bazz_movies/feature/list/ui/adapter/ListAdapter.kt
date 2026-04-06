package com.waffiq.bazz_movies.feature.list.ui.adapter

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_search
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemListBinding
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemResultBinding
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.dateOf
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.imageSource
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.posterSource
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.core.utils.GenreHelper.getGenre
import com.waffiq.bazz_movies.navigation.INavigator

class ListAdapter(private val navigator: INavigator) :
  PagingDataAdapter<MediaItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

  private var mediaType = MOVIE_MEDIA_TYPE
  private var isGridMode = true

  fun setMediaType(type: String) {
    mediaType = type
  }

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

  inner class GridViewHolder(private val binding: ItemListBinding) :
    BaseViewHolder(binding.root) {

    override fun bind(media: MediaItem) {
      binding.imgPoster.contentDescription = titleHandler(media)
      Glide.with(binding.imgPoster)
        .load(media.posterSource)
        .placeholder(ic_bazz_placeholder_poster)
        .transform(CenterCrop())
        .transition(withCrossFade())
        .error(ic_poster_error)
        .into(binding.imgPoster)

      binding.imgPoster.setOnClickListener {
        navigator.openDetails(itemView.context, media.copy(mediaType = mediaType))
      }
    }
  }

  inner class ListViewHolder(internal val binding: ItemResultBinding) :
    BaseViewHolder(binding.root) {

    override fun bind(media: MediaItem) {
      binding.ivPicture.contentDescription = titleHandler(media)
      Glide.with(binding.ivPicture)
        .load(media.imageSource)
        .placeholder(ic_bazz_placeholder_search)
        .transform(CenterCrop())
        .transition(withCrossFade())
        .error(ic_poster_error)
        .into(binding.ivPicture)

      binding.tvTitle.text = titleHandler(media)
      binding.tvYearReleased.text = itemView.context.dateOf(media)
      binding.tvGenre.text = itemView.context.getGenre(media.listGenreIds)

      binding.containerResult.setOnClickListener {
        navigator.openDetails(itemView.context, media.copy(mediaType = mediaType))
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
