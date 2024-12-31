package com.waffiq.bazz_movies.feature.home.ui.adapter

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
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_BACKDROP_W780
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error_filled
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_search
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreIdsToJoinName
import com.waffiq.bazz_movies.feature.home.databinding.ItemWideBinding
import com.waffiq.bazz_movies.navigation.INavigator

class ItemWIdeAdapter(private val navigator: INavigator) :
  PagingDataAdapter<ResultItem, ItemWIdeAdapter.ViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemWideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = getItem(position)
    if (data != null) {
      holder.bind(data)
      holder.itemView.startAnimation(
        AnimationUtils.loadAnimation(holder.itemView.context, fade_in)
      )
    }
  }

  inner class ViewHolder(private var binding: ItemWideBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: ResultItem) {
      binding.ivBackdrop.contentDescription =
        data.name ?: data.title ?: data.originalTitle ?: data.originalName

      Glide.with(binding.ivBackdrop)
        .load(
          if (!data.posterPath.isNullOrEmpty()) {
            TMDB_IMG_LINK_BACKDROP_W780 + data.backdropPath
          } else {
            ic_backdrop_error_filled
          }
        )
        .placeholder(ic_bazz_placeholder_search)
        .transform(CenterCrop())
        .transition(withCrossFade())
        .error(ic_backdrop_error_filled)
        .into(binding.ivBackdrop)

      binding.tvTitle.text =
        data.name ?: data.title ?: data.originalTitle ?: data.originalName
      binding.tvGenre.text = data.listGenreIds?.let { transformListGenreIdsToJoinName(it) }
        ?: itemView.context.getString(not_available)
      binding.tvYear.text =
        data.releaseDate?.take(n = 4) ?: data.firstAirDate?.take(n = 4).toString()
      binding.ratingBar.rating = (data.voteAverage ?: 0F) / 2

      // image OnClickListener
      itemView.setOnClickListener {
        navigator.openDetails(itemView.context, data)
      }
    }
  }

  companion object {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultItem>() {
      override fun areItemsTheSame(
        oldItem: ResultItem,
        newItem: ResultItem
      ): Boolean {
        return oldItem.id == newItem.id
      }

      override fun areContentsTheSame(
        oldItem: ResultItem,
        newItem: ResultItem
      ): Boolean {
        return oldItem == newItem
      }
    }
  }
}
