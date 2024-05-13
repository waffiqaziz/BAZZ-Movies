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
import com.waffiq.bazz_movies.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItemResponse
import com.waffiq.bazz_movies.databinding.ItemMulmedBinding
import com.waffiq.bazz_movies.ui.activity.detail.DetailMovieActivity
import com.waffiq.bazz_movies.utils.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.utils.Helper.dateFormatter
import com.waffiq.bazz_movies.utils.Helper.iterateGenre
import java.text.DecimalFormat

class FavoriteMovieAdapter :
  PagingDataAdapter<ResultItemResponse, FavoriteMovieAdapter.ViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemMulmedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

  inner class ViewHolder(private var binding: ItemMulmedBinding) :
    RecyclerView.ViewHolder(binding.root) {
    lateinit var data: ResultItemResponse

    fun bind(resultItemResponse: ResultItemResponse) {
      data = resultItemResponse
      binding.ivPicture.contentDescription =
        resultItemResponse.name ?: resultItemResponse.title ?: resultItemResponse.originalTitle ?: resultItemResponse.originalName

      Glide.with(binding.ivPicture)
        .load(TMDB_IMG_LINK_POSTER_W185 + resultItemResponse.posterPath) // URL movie poster
        .placeholder(ic_bazz_placeholder_poster)
        .transform(CenterCrop())
        .transition(withCrossFade())
        .error(ic_poster_error)
        .into(binding.ivPicture)

      binding.tvTitle.text =
        resultItemResponse.name ?: resultItemResponse.title ?: resultItemResponse.originalTitle ?: resultItemResponse.originalName
      binding.tvYearReleased.text = (resultItemResponse.firstAirDate ?: resultItemResponse.releaseDate)?.let {
        dateFormatter(it)
      } ?: "N/A"
      binding.tvGenre.text = resultItemResponse.genreIds?.let { iterateGenre(it) } ?: "N/A"
      binding.ratingBar.rating = (resultItemResponse.voteAverage ?: 0F) / 2

      val df = DecimalFormat("#.#")
      (df.format((resultItemResponse.voteAverage ?: 0F)).toString() + "/10").also {
        binding.tvRating.text = it
      }

      // OnClickListener
      binding.container.setOnClickListener {
        val intent = Intent(it.context, DetailMovieActivity::class.java)
        intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, resultItemResponse.copy(mediaType = "movie"))
        it.context.startActivity(intent)
      }
    }
  }

  companion object {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultItemResponse>() {
      override fun areItemsTheSame(
        oldItem: ResultItemResponse,
        newItem: ResultItemResponse
      ): Boolean {
        return oldItem.id == newItem.id
      }

      override fun areContentsTheSame(
        oldItem: ResultItemResponse,
        newItem: ResultItemResponse
      ): Boolean {
        return oldItem.id == newItem.id
      }
    }
  }
}