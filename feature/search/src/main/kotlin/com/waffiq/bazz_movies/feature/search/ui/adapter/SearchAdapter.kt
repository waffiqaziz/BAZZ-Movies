package com.waffiq.bazz_movies.feature.search.ui.adapter

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.designsystem.R.string.known_for
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemMediaBinding
import com.waffiq.bazz_movies.core.designsystem.databinding.ListItemMediaNoSwipeBinding
import com.waffiq.bazz_movies.core.utils.DateFormatter.dateFormatterStandard
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.displayDate
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.posterSource
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.validName
import com.waffiq.bazz_movies.core.utils.GenreHelper.getGenre
import com.waffiq.bazz_movies.core.utils.RatingHelper.ratingHandler
import com.waffiq.bazz_movies.core.utils.RatingHelper.setRatingBar
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.utils.Constants.PERSON_MEDIA_TYPE
import com.waffiq.bazz_movies.feature.search.utils.MultiSearchItemMapper.toMediaCastItem
import com.waffiq.bazz_movies.feature.search.utils.MultiSearchItemMapper.toMediaItem
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.getKnownFor
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.profileImageSource
import com.waffiq.bazz_movies.navigation.INavigator

class SearchAdapter(private val navigator: INavigator) :
  PagingDataAdapter<MultiSearchItem, SearchAdapter.ViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding =
      ListItemMediaNoSwipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = getItem(position) ?: return
    if (data.mediaType == PERSON_MEDIA_TYPE) {
      holder.bindPerson(data)
    } else {
      holder.bindMultiSearch(data)
    }

    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(holder.itemView.context, fade_in),
    )
  }

  inner class ViewHolder(private var binding: ListItemMediaNoSwipeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindPerson(data: MultiSearchItem) {
      binding.content.setupPerson(true)
      binding.content.showDataPerson(data)
      binding.item.setOnClickListener {
        navigator.openPersonDetails(itemView.context, data.toMediaCastItem())
      }
    }

    fun bindMultiSearch(data: MultiSearchItem) {
      binding.content.setupPerson(false)
      binding.content.showMediaData(data)
      binding.item.setOnClickListener {
        navigator.openDetails(itemView.context, data.toMediaItem())
      }
    }
  }

  private fun ItemMediaBinding.showDataPerson(data: MultiSearchItem) {
    apply {
      ivPicture.contentDescription = data.validName
      Glide.with(ivPicture)
        .load(data.profileImageSource)
        .placeholder(ic_bazz_placeholder_poster)
        .error(ic_poster_error)
        .transform(CenterCrop())
        .transition(withCrossFade())
        .into(ivPicture)

      tvTitle.text = tvTitle.context.titleHandler(data)
      tvYearReleased.text = data.knownForDepartment
      tvGenre.text = buildString {
        append(tvGenre.context.getString(known_for))
        append(":")
        append("\n")
        append(data.listKnownFor?.let { getKnownFor(it) })
      }
    }
  }

  private fun ItemMediaBinding.showMediaData(data: MultiSearchItem) {
    setImageMedia(data)
    apply {
      tvYearReleased.text = dateFormatterStandard(data.displayDate)
      tvTitle.text = tvTitle.context.titleHandler(data)
      tvGenre.text = tvGenre.context.getGenre(data.listGenreIds)
      tvRating.text = ratingHandler(data.voteAverage.toFloat())
      ratingBar.rating = setRatingBar(data.voteAverage.toFloat())
    }
  }

  private fun ItemMediaBinding.setImageMedia(data: MultiSearchItem) {
    apply {
      ivPicture.contentDescription = titleHandler(data)
      Glide.with(ivPicture)
        .load(data.posterSource)
        .transition(withCrossFade())
        .placeholder(ic_bazz_placeholder_poster)
        .error(ic_poster_error)
        .into(ivPicture)
    }
  }

  private fun ItemMediaBinding.setupPerson(isPerson: Boolean) {
    if (isPerson) tvGenre.maxLines = MAX_LINES else tvGenre.maxLines = 1
    ratingBar.isVisible = !isPerson
    tvRating.isVisible = !isPerson
  }

  companion object {
    const val MAX_LINES = 3
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MultiSearchItem>() {
      override fun areItemsTheSame(oldItem: MultiSearchItem, newItem: MultiSearchItem): Boolean =
        oldItem.id == newItem.id && oldItem.mediaType == newItem.mediaType

      override fun areContentsTheSame(oldItem: MultiSearchItem, newItem: MultiSearchItem): Boolean =
        oldItem.id == newItem.id && oldItem.mediaType == newItem.mediaType
    }
  }
}
