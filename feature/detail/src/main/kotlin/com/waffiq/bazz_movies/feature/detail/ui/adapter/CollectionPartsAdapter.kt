package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.utils.DateFormatter.dateFormatterStandard
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.posterSource
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.core.utils.RatingHelper.ratingHandler
import com.waffiq.bazz_movies.feature.detail.databinding.ItemCollectionPartBinding
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.PartsItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MovieMapper.toMediaItem
import com.waffiq.bazz_movies.navigation.INavigator

class CollectionPartsAdapter(private val navigator: INavigator) :
  ListAdapter<PartsItem, CollectionPartsAdapter.ViewHolder>(CollectionsDiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemCollectionPartBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false,
    )
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class ViewHolder(private val binding: ItemCollectionPartBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PartsItem) {
      binding.apply {
        tvMovieTitle.text = titleHandler(item.title, item.originalTitle)
        tvMovieOverview.text = item.overview
        tvMovieYear.text = dateFormatterStandard(item.releaseDate)

        if (item.voteAverage != null && item.voteAverage > 0) {
          tvMovieRating.isVisible = true
          metaDivider.isVisible = true
          tvMovieRating.text = ratingHandler(item.voteAverage)
        } else {
          tvMovieRating.isVisible = false
          metaDivider.isVisible = false
        }

        Glide.with(ivMoviePoster)
          .load(item.posterSource)
          .placeholder(ic_bazz_placeholder_poster)
          .transform(CenterCrop())
          .transition(withCrossFade())
          .error(ic_poster_error)
          .into(ivMoviePoster)

        container.setOnClickListener {
          navigator.openDetails(itemView.context, item.toMediaItem())
        }
      }
    }
  }

  class CollectionsDiffCallback : DiffUtil.ItemCallback<PartsItem>() {
    override fun areItemsTheSame(oldItem: PartsItem, newItem: PartsItem): Boolean =
      oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PartsItem, newItem: PartsItem): Boolean =
      oldItem == newItem
  }
}
