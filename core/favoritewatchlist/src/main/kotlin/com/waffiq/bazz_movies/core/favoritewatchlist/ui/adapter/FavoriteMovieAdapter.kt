package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter

import android.R.anim.fade_in
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.VisibleForTesting
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemMulmedBinding
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.utils.DateFormatter.dateFormatterStandard
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreIdsToJoinName
import com.waffiq.bazz_movies.navigation.INavigator
import java.text.DecimalFormat

class FavoriteMovieAdapter(private val navigator: INavigator) :
  PagingDataAdapter<ResultItem, FavoriteMovieAdapter.ViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemMulmedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

  inner class ViewHolder(private var binding: ItemMulmedBinding) :
    RecyclerView.ViewHolder(binding.root) {
    lateinit var data: ResultItem

    fun bind(resultItem: ResultItem) {
      data = resultItem
      setImagePoster(binding, data)
      setTitleYearGenreRating(binding, resultItem, itemView.context)

      // OnClickListener
      binding.container.setOnClickListener {
        navigator.openDetails(itemView.context, resultItem.copy(mediaType = MOVIE_MEDIA_TYPE))
      }
    }
  }

  private fun setTitleYearGenreRating(
    binding: ItemMulmedBinding,
    resultItem: ResultItem,
    context: Context
  ) {
    binding.tvTitle.text =
      resultItem.name ?: resultItem.title ?: resultItem.originalTitle ?: resultItem.originalName
        ?: context.getString(not_available)
    binding.tvYearReleased.text = (resultItem.firstAirDate ?: resultItem.releaseDate)?.let {
      dateFormatterStandard(it)
    } ?: context.getString(not_available)
    binding.tvGenre.text = resultItem.listGenreIds?.let { transformListGenreIdsToJoinName(it) }
      ?: context.getString(not_available)
    binding.ratingBar.rating = (resultItem.voteAverage ?: 0F) / 2

    (DecimalFormat("#.#").format((resultItem.voteAverage ?: 0F)).toString() + "/10").also {
      binding.tvRating.text = it
    }
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun setImagePoster(binding: ItemMulmedBinding, data: ResultItem) {
    Glide.with(binding.ivPicture)
      .load(
        if (!data.posterPath.isNullOrEmpty()) {
          TMDB_IMG_LINK_POSTER_W185 + data.posterPath
        } else {
          ic_poster_error
        }
      )
      .placeholder(ic_bazz_placeholder_poster)
      .transform(CenterCrop())
      .transition(withCrossFade())
      .error(ic_poster_error)
      .into(binding.ivPicture)

    binding.ivPicture.contentDescription =
      data.name ?: data.title ?: data.originalTitle ?: data.originalName
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
        return oldItem.id == newItem.id
      }
    }
  }
}
