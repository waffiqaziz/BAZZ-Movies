package com.waffiq.bazz_movies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.local.model.Movie
import com.waffiq.bazz_movies.databinding.ItemListMovieBinding
import com.waffiq.bazz_movies.utils.Helper.iterateGenre

class MovieAdapter :
  PagingDataAdapter<Movie, MovieAdapter.ViewHolder>(DIFF_CALLBACK) {


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemListMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = getItem(position)
    if (data != null) {
      holder.bind(data)
    }
  }

  inner class ViewHolder(private var binding: ItemListMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
      with(binding) {
        Glide.with(imgItemImage)
          .load("http://image.tmdb.org/t/p/w200/" + movie.posterPath) // URL movie poster
          .placeholder(R.mipmap.ic_launcher)
          .override(30, 30)
          .error(R.drawable.ic_broken_image)
          .into(imgItemImage)
        tvName.text = movie.title

        tvDescription.text = iterateGenre(movie.genreIds)
        tvReleasedAt.text = movie.releaseDate
      }
    }
  }


  companion object {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
      override fun areItemsTheSame(
        oldItem: Movie,
        newItem: Movie
      ): Boolean {
        return oldItem.id == newItem.id
      }

      override fun areContentsTheSame(
        oldItem: Movie,
        newItem: Movie
      ): Boolean {
        return oldItem == newItem
      }
    }
  }
}