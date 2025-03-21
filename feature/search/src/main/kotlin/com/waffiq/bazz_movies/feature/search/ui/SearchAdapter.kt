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
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_BACKDROP_W300
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_search
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemResultBinding
import com.waffiq.bazz_movies.core.domain.MovieTvCastItem
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreIdsToJoinName
import com.waffiq.bazz_movies.feature.search.domain.model.ResultsItemSearch
import com.waffiq.bazz_movies.feature.search.utils.Constants.PERSON_MEDIA_TYPE
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.getKnownFor
import com.waffiq.bazz_movies.navigation.INavigator

class SearchAdapter(private val navigator: INavigator) :
  PagingDataAdapter<ResultsItemSearch, SearchAdapter.ViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding =
      ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = getItem(position) ?: return
    when (data.mediaType) {
      PERSON_MEDIA_TYPE -> holder.bindPerson(data)
      else -> holder.bindMovieTv(data)
    }
    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(holder.itemView.context, fade_in)
    )
  }

  inner class ViewHolder(private var binding: ItemResultBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindPerson(data: ResultsItemSearch) {
      showDataPerson(binding, data)
      binding.containerResult.setOnClickListener {
        val person = MovieTvCastItem(
          id = data.id,
          profilePath = data.profilePath,
          name = data.name,
          originalName = data.originalName
        )
        navigator.openPersonDetails(itemView.context, person)
      }
    }

    fun bindMovieTv(data: ResultsItemSearch) {
      showDataMovieTv(binding, data, itemView)
      binding.containerResult.setOnClickListener {
        val resultItem = ResultItem(
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
          id = data.id
        )
        navigator.openDetails(itemView.context, resultItem)
      }
    }
  }

  private fun showDataPerson(binding: ItemResultBinding, data: ResultsItemSearch) {
    binding.ivPicture.contentDescription =
      data.name ?: data.originalName
    Glide.with(binding.ivPicture)
      .load(
        if (!data.profilePath.isNullOrEmpty()) {
          TMDB_IMG_LINK_POSTER_W185 + data.profilePath
        } else {
          ic_backdrop_error
        }
      )
      .placeholder(ic_bazz_placeholder_search)
      .error(ic_backdrop_error)
      .transform(CenterCrop())
      .transition(withCrossFade())
      .into(binding.ivPicture)

    binding.tvTitle.text = data.name ?: data.originalName
    binding.tvYearReleased.text = data.knownForDepartment
    binding.tvGenre.text = data.listKnownFor?.let { getKnownFor(it) }
  }

  private fun showDataMovieTv(binding: ItemResultBinding, data: ResultsItemSearch, view: View) {
    setImageMovieTv(binding, data)
    binding.tvYearReleased.text = when {
      !data.releaseDate.isNullOrEmpty() -> data.releaseDate
      !data.firstAirDate.isNullOrEmpty() -> data.firstAirDate
      else -> view.context.getString(not_available)
    }

    binding.tvTitle.text = data.name ?: data.title ?: data.originalTitle ?: data.originalName
    showGenreMovie(binding, data, view)
  }

  private fun showGenreMovie(binding: ItemResultBinding, data: ResultsItemSearch, view: View) {
    binding.tvGenre.text =
      if (data.listGenreIds?.isEmpty() == true) {
        view.context.getString(not_available)
      } else {
        data.listGenreIds?.let { transformListGenreIdsToJoinName(it) }
      }
  }

  private fun setImageMovieTv(binding: ItemResultBinding, data: ResultsItemSearch) {
    binding.ivPicture.contentDescription =
      data.name ?: data.title ?: data.originalTitle ?: data.originalName
    Glide.with(binding.ivPicture)
      .load(
        if (!data.backdropPath.isNullOrEmpty()) {
          TMDB_IMG_LINK_BACKDROP_W300 + data.backdropPath
        } else if (!data.posterPath.isNullOrEmpty()) {
          TMDB_IMG_LINK_BACKDROP_W300 + data.posterPath
        } else {
          ic_backdrop_error
        }
      )
      .transition(withCrossFade())
      .placeholder(ic_bazz_placeholder_search)
      .error(ic_backdrop_error)
      .into(binding.ivPicture)
  }

  companion object {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultsItemSearch>() {
      override fun areItemsTheSame(
        oldItem: ResultsItemSearch,
        newItem: ResultsItemSearch
      ): Boolean {
        return oldItem.id == newItem.id && oldItem.mediaType == newItem.mediaType
      }

      override fun areContentsTheSame(
        oldItem: ResultsItemSearch,
        newItem: ResultsItemSearch
      ): Boolean {
        return oldItem.id == newItem.id && oldItem.mediaType == newItem.mediaType
      }
    }
  }
}
