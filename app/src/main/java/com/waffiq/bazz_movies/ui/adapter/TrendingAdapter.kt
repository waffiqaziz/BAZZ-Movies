package com.waffiq.bazz_movies.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.remote.response.ResultItem
import com.waffiq.bazz_movies.databinding.ItemTrendingBinding

class TrendingAdapter :
  PagingDataAdapter<ResultItem, TrendingAdapter.ViewHolder>(DIFF_CALLBACK) {


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
        .placeholder(R.drawable.ic_bazz_logo)
        .error(R.drawable.ic_broken_image)
        .into(binding.imgPoster)

      Log.e("Trending Movie Type : ", movie.mediaType)

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