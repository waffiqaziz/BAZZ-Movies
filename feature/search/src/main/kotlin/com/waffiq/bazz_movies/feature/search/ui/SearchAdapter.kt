package com.waffiq.bazz_movies.feature.search.ui

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_search
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemResultBinding
import com.waffiq.bazz_movies.core.domain.MediaCastItem
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.dateOf
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.imageSource
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.validName
import com.waffiq.bazz_movies.core.utils.GenreHelper.getGenre
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.utils.Constants.PERSON_MEDIA_TYPE
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.getKnownFor
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.profileImageSource
import com.waffiq.bazz_movies.navigation.INavigator

class SearchAdapter(private val navigator: INavigator) :
  PagingDataAdapter<MultiSearchItem, SearchAdapter.ViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding =
      ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

  inner class ViewHolder(private var binding: ItemResultBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindPerson(data: MultiSearchItem) {
      showDataPerson(binding, data)
      binding.containerResult.setOnClickListener {
        val person = MediaCastItem(
          id = data.id,
          profilePath = data.profilePath,
          name = data.name,
          originalName = data.originalName,
        )
        navigator.openPersonDetails(itemView.context, person)
      }
    }

    fun bindMultiSearch(data: MultiSearchItem) {
      showMediaData(binding, data, itemView)
      binding.containerResult.setOnClickListener {
        val mediaItem = MediaItem(
          posterPath = data.posterPath,
          backdropPath = data.backdropPath,
          firstAirDate = data.firstAirDate,
          releaseDate = data.releaseDate,
          overview = data.overview,
          title = data.title,
          name = data.name,
          originalTitle = data.originalTitle,
          originalName = data.originalName,
          mediaType = data.mediaType,
          listGenreIds = data.listGenreIds,
          id = data.id,
        )
        navigator.openDetails(itemView.context, mediaItem)
      }
    }
  }

  private fun showDataPerson(binding: ItemResultBinding, data: MultiSearchItem) {
    binding.ivPicture.contentDescription = data.validName
    Glide.with(binding.ivPicture)
      .load(data.profileImageSource)
      .placeholder(ic_bazz_placeholder_search)
      .error(ic_backdrop_error)
      .transform(CenterCrop())
      .transition(withCrossFade())
      .into(binding.ivPicture)

    binding.apply {
      tvTitle.text = binding.tvTitle.context.titleHandler(data)
      tvYearReleased.text = data.knownForDepartment
      tvGenre.text = data.listKnownFor?.let { getKnownFor(it) }
    }
  }

  private fun showMediaData(
    binding: ItemResultBinding,
    data: MultiSearchItem,
    view: View,
  ) {
    setImageMedia(binding, data)
    binding.apply {
      tvYearReleased.text = view.context.dateOf(data)
      tvTitle.text = view.context.titleHandler(data)
      tvGenre.text = view.context.getGenre(data.listGenreIds)
    }
  }

  private fun setImageMedia(binding: ItemResultBinding, data: MultiSearchItem) {
    binding.ivPicture.contentDescription = titleHandler(data)
    Glide.with(binding.ivPicture)
      .load(data.imageSource)
      .transition(withCrossFade())
      .placeholder(ic_bazz_placeholder_search)
      .error(ic_backdrop_error)
      .into(binding.ivPicture)
  }

  companion object {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MultiSearchItem>() {
      override fun areItemsTheSame(oldItem: MultiSearchItem, newItem: MultiSearchItem): Boolean =
        oldItem.id == newItem.id && oldItem.mediaType == newItem.mediaType

      override fun areContentsTheSame(oldItem: MultiSearchItem, newItem: MultiSearchItem): Boolean =
        oldItem.id == newItem.id && oldItem.mediaType == newItem.mediaType
    }
  }
}
