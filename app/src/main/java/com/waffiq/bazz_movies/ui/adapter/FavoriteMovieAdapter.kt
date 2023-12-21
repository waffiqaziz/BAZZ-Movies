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
import com.waffiq.bazz_movies.databinding.ItemMulmedBinding
import com.waffiq.bazz_movies.ui.activity.detail.DetailMovieActivity
import com.waffiq.bazz_movies.utils.Helper
import java.text.DecimalFormat

class FavoriteMovieAdapter :
  PagingDataAdapter<ResultItem, FavoriteMovieAdapter.ViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemMulmedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = getItem(position)
    if (data != null) {
      holder.bind(data)
    }
  }

  inner class ViewHolder(private var binding: ItemMulmedBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(resultItem: ResultItem) {
      Glide.with(binding.ivPicture)
        .load("http://image.tmdb.org/t/p/w342/" + resultItem.posterPath) // URL movie poster
        .placeholder(R.drawable.ic_bazz_placeholder_poster)
        .transform(CenterCrop())
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(R.drawable.ic_broken_image)
        .into(binding.ivPicture)

      binding.tvTitle.text = resultItem.name ?: resultItem.title ?: resultItem.originalTitle ?: resultItem.originalName
      binding.tvYearReleased.text = resultItem.firstAirDate ?: resultItem.releaseDate
      binding.tvGenre.text = resultItem.genreIds?.let { Helper.iterateGenre(it) }
      binding.ratingBar.rating = (resultItem.voteAverage ?: 0F) / 2

      val df = DecimalFormat("#.#")
      (df.format((resultItem.voteAverage ?: 0F)).toString() + "/10").also { binding.tvRating.text = it }

      // OnClickListener
      binding.container.setOnClickListener {
        val intent = Intent(it.context, DetailMovieActivity::class.java)
        resultItem.mediaType = "movie"
        intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, resultItem)
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