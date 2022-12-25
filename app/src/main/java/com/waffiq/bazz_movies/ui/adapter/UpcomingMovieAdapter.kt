package com.waffiq.bazz_movies.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.local.model.Movie
import com.waffiq.bazz_movies.databinding.ItemUpcomingBinding

class UpcomingMovieAdapter :
  PagingDataAdapter<Movie, UpcomingMovieAdapter.ViewHolder>(DIFF_CALLBACK) {


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = getItem(position)
    if (data != null) {
      holder.bind(data)
    }
  }

  inner class ViewHolder(private var binding: ItemUpcomingBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
      Glide.with(binding.imgPoster)
        .load("http://image.tmdb.org/t/p/w200/" + movie.posterPath) // URL movie poster
        .placeholder(R.drawable.ic_bazz_logo)
        .error(R.drawable.ic_broken_image)
        .into(binding.imgPoster)

      Log.e("Upcoming Movie : ", movie.title)

      // image OnClickListener
//        imgItemImage.setOnClickListener {
//          val optionsCompat: ActivityOptionsCompat =
//            ActivityOptionsCompat.makeSceneTransitionAnimation(
//              itemView.context as Activity,
//              Pair(imgItemImage, "image"),
//              Pair(tvName, "name"),
//              Pair(tvCreatedTime, "created"),
//              Pair(tvDescription, "description"),
//            )
//
//          val intent = Intent(it.context, DetailStoryActivity::class.java)
//          intent.putExtra(DetailStoryActivity.EXTRA_STORY, movie)
//          it.context.startActivity(intent, optionsCompat.toBundle())
//        }
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