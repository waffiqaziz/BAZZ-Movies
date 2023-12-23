package com.waffiq.bazz_movies.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.databinding.ItemTrendingBinding
import com.waffiq.bazz_movies.ui.activity.detail.DetailMovieActivity

class MovieHomeAdapter :
  PagingDataAdapter<ResultItem, MovieHomeAdapter.ViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemTrendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = getItem(position)
    if (data != null) {
      holder.bind(data)
    }
  }

  inner class ViewHolder(private var binding: ItemTrendingBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: ResultItem) {
      Glide.with(binding.imgPoster)
        .load("http://image.tmdb.org/t/p/w200/" + movie.posterPath) // URL movie poster
        .placeholder(R.drawable.ic_bazz_placeholder_poster)
        .transform(CenterCrop())
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(R.drawable.ic_broken_image)
        .into(binding.imgPoster)

      // image OnClickListener
      binding.imgPoster.setOnClickListener {
        val intent = Intent(it.context, DetailMovieActivity::class.java)
        movie.mediaType = "movie"
        intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movie)
        it.context.startActivity(intent)
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