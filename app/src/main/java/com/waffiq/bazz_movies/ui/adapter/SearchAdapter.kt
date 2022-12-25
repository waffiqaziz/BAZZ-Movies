package com.waffiq.bazz_movies.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.local.model.Search
import com.waffiq.bazz_movies.data.remote.response.ResultItem
import com.waffiq.bazz_movies.databinding.ItemSearchResultBinding
import com.waffiq.bazz_movies.ui.activity.detail.DetailMovieActivity
import com.waffiq.bazz_movies.utils.Helper.iterateGenre

class SearchAdapter :
  PagingDataAdapter<Search, SearchAdapter.ViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding =
      ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = getItem(position)
    if (data != null) {
      holder.bind(data)
    }
  }

  inner class ViewHolder(private var binding: ItemSearchResultBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: Search) {
      Glide.with(binding.ivPoster)
        .load(
          "http://image.tmdb.org/t/p/w300/" + (data.backdropPath ?: data.posterPath)
        ) // URL movie poster
        .placeholder(R.drawable.ic_bazz_logo)
        .error(R.drawable.ic_broken_image)
        .into(binding.ivPoster)
      binding.tvYearReleased.text = data.releaseDate ?: data.firstAirDate
      binding.tvTitle.text = data.title ?: data.originalTitle ?: data.originalName
      binding.tvGenre.text = iterateGenre(data.genreIds)
      data.posterPath
      
      // image OnClickListener
      binding.ivPoster.setOnClickListener {
        val intent = Intent(it.context, DetailMovieActivity::class.java)
        val resultItem = ResultItem(
          posterPath = data.posterPath,
          firstAirDate = data.firstAirDate,
          releaseDate = data.releaseDate,
          overview = data.overview,
          title = data.title,
          originalTitle = data.originalTitle,
          originalName = data.originalName,
          genreIds = data.genreIds,
          id = data.id
        )
        intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, resultItem)
        it.context.startActivity(intent)
      }
    }
  }

  companion object {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Search>() {
      override fun areItemsTheSame(
        oldItem: Search,
        newItem: Search
      ): Boolean {
        return oldItem.id == newItem.id
      }

      override fun areContentsTheSame(
        oldItem: Search,
        newItem: Search
      ): Boolean {
        return oldItem == newItem
      }
    }
  }
}