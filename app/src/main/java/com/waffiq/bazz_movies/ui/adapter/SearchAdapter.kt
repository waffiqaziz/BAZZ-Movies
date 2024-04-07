package com.waffiq.bazz_movies.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.R.drawable.ic_backdrop_error
import com.waffiq.bazz_movies.R.drawable.ic_bazz_placeholder_search
import com.waffiq.bazz_movies.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.data.remote.response.tmdb.CastItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultsItemSearch
import com.waffiq.bazz_movies.databinding.ItemResultBinding
import com.waffiq.bazz_movies.ui.activity.detail.DetailMovieActivity
import com.waffiq.bazz_movies.ui.activity.person.PersonActivity
import com.waffiq.bazz_movies.utils.Constants.TMDB_IMG_LINK_BACKDROP_W300
import com.waffiq.bazz_movies.utils.Helper.getKnownFor
import com.waffiq.bazz_movies.utils.Helper.iterateGenre

class SearchAdapter :
  PagingDataAdapter<ResultsItemSearch, SearchAdapter.ViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding =
      ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = getItem(position)
    if (data != null) {
      holder.bind(data)
      holder.itemView.startAnimation(
        AnimationUtils.loadAnimation(
          holder.itemView.context,
          android.R.anim.fade_in
        )
      )
    }
  }

  inner class ViewHolder(private var binding: ItemResultBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: ResultsItemSearch) {

      if (data.mediaType == "person") {
        showDataPerson(binding, data)
        binding.containerResult.setOnClickListener {
          val person = CastItem(
            id = data.id,
            profilePath = data.profilePath,
            name = data.name,
            originalName = data.originalName
          )
          val intent = Intent(it.context, PersonActivity::class.java)
          intent.putExtra(PersonActivity.EXTRA_PERSON, person)
          it.context.startActivity(intent)
        }
      } else { // movie & tv-series
        showDataMovieTv(binding, data)
        binding.containerResult.setOnClickListener {
          val intent = Intent(it.context, DetailMovieActivity::class.java)
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
            genreIds = data.genreIds,
            id = data.id
          )
          intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, resultItem)
          it.context.startActivity(intent)
        }
      }
    }
  }

  private fun showDataPerson(binding: ItemResultBinding, data: ResultsItemSearch) {
    binding.ivPicture.contentDescription =
      data.name ?: data.originalName
    Glide.with(binding.ivPicture)
      .load(
        TMDB_IMG_LINK_BACKDROP_W300 + data.profilePath
      )
      .placeholder(ic_bazz_placeholder_search)
      .error(ic_broken_image)
      .transform(CenterCrop())
      .transition(withCrossFade())
      .into(binding.ivPicture)

    binding.tvTitle.text = data.name
    binding.tvYearReleased.text = data.knownForDepartment
    binding.tvGenre.text = data.knownFor?.let { getKnownFor(it) }
  }

  private fun showDataMovieTv(binding: ItemResultBinding, data: ResultsItemSearch) {
    binding.ivPicture.contentDescription =
      data.name ?: data.title ?: data.originalTitle ?: data.originalName
    Glide.with(binding.ivPicture)
      .load(
        TMDB_IMG_LINK_BACKDROP_W300 + (data.backdropPath ?: data.posterPath)
      )
      .transition(withCrossFade())
      .placeholder(ic_bazz_placeholder_search)
      .error(ic_backdrop_error)
      .into(binding.ivPicture)
    binding.tvYearReleased.text = data.releaseDate
      ?.takeIf { it.isNotBlank() || it.isNotEmpty() }
      ?: data.firstAirDate
        ?.takeIf { it.isNotBlank() || it.isNotEmpty() }
        ?: "N/A"

    binding.tvTitle.text = data.name ?: data.title ?: data.originalTitle ?: data.originalName
    binding.tvGenre.text = if (data.genreIds?.isEmpty() == true) "N/A" else data.genreIds?.let {
      iterateGenre(
        it
      )
    }
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