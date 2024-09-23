package com.waffiq.bazz_movies.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.R.anim.fade_in
import com.waffiq.bazz_movies.R.anim.fade_out
import com.waffiq.bazz_movies.R.drawable.ic_backdrop_error
import com.waffiq.bazz_movies.R.drawable.ic_bazz_placeholder_search
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.cast_crew.MovieTvCastItemResponse
import com.waffiq.bazz_movies.databinding.ItemResultBinding
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.model.search.ResultsItemSearch
import com.waffiq.bazz_movies.ui.activity.detail.DetailMovieActivity
import com.waffiq.bazz_movies.ui.activity.person.PersonActivity
import com.waffiq.bazz_movies.utils.Helper.getKnownFor
import com.waffiq.bazz_movies.utils.common.Constants.TMDB_IMG_LINK_BACKDROP_W300
import com.waffiq.bazz_movies.utils.common.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.utils.helpers.GenreHelper.getGenreName

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
      when (data.mediaType) {
        "person" -> holder.bindPerson(data)
        else -> holder.bindMovieTv(data)
      }
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
    fun bindPerson(data: ResultsItemSearch) {
      showDataPerson(binding, data)
      binding.containerResult.setOnClickListener {
        val person = MovieTvCastItemResponse(
          id = data.id,
          profilePath = data.profilePath,
          name = data.name,
          originalName = data.originalName
        )
        val intent = Intent(it.context, PersonActivity::class.java)
        intent.putExtra(PersonActivity.EXTRA_PERSON, person)
        val options =
          ActivityOptionsCompat.makeCustomAnimation(
            it.context,
            fade_in,
            fade_out
          )
        ActivityCompat.startActivity(it.context, intent, options.toBundle())
      }
    }

    fun bindMovieTv(data: ResultsItemSearch) {
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
          listGenreIds = data.listGenreIds,
          id = data.id
        )
        intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, resultItem)
        val options =
          ActivityOptionsCompat.makeCustomAnimation(
            it.context,
            fade_in,
            fade_out
          )
        ActivityCompat.startActivity(it.context, intent, options.toBundle())
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

    binding.tvTitle.text = data.name
    binding.tvYearReleased.text = data.knownForDepartment
    binding.tvGenre.text = data.listKnownFor?.let { getKnownFor(it) }
  }

  private fun showDataMovieTv(binding: ItemResultBinding, data: ResultsItemSearch) {
    binding.ivPicture.contentDescription =
      data.name ?: data.title ?: data.originalTitle ?: data.originalName
    setImageMovieTv(binding, data)
    binding.tvYearReleased.text = data.releaseDate
      ?.takeIf { it.isNotBlank() || it.isNotEmpty() }
      ?: data.firstAirDate
        ?.takeIf { it.isNotBlank() || it.isNotEmpty() }
      ?: "N/A"

    binding.tvTitle.text = data.name ?: data.title ?: data.originalTitle ?: data.originalName
    binding.tvGenre.text =
      if (data.listGenreIds?.isEmpty() == true) {
        "N/A"
      } else {
        data.listGenreIds?.let { getGenreName(it) }
      }
  }

  private fun setImageMovieTv(binding: ItemResultBinding, data: ResultsItemSearch) {
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
