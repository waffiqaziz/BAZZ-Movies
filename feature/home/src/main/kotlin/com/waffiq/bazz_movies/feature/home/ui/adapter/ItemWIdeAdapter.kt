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
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
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

    fun bind(movie: ResultItem) {
      binding.ivBackdrop.contentDescription =
        movie.name ?: movie.title ?: movie.originalTitle ?: movie.originalName

      Glide.with(binding.ivBackdrop)
        .load(
          if (!movie.posterPath.isNullOrEmpty()) {
            TMDB_IMG_LINK_BACKDROP_W780 + movie.backdropPath
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
        movie.name ?: movie.title ?: movie.originalTitle ?: movie.originalName
      binding.tvGenre.text = movie.listGenreIds?.let { transformListGenreIdsToJoinName(it) }
        ?: itemView.context.getString(not_available)
      binding.tvYear.text =
        movie.releaseDate?.take(n = 4) ?: movie.firstAirDate?.take(n = 4).toString()
      binding.ratingBar.rating = (movie.voteAverage ?: 0F) / 2

      // image OnClickListener
      itemView.setOnClickListener {
        navigator.openDetails(itemView.context, movie.copy(mediaType = MOVIE_MEDIA_TYPE))
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
