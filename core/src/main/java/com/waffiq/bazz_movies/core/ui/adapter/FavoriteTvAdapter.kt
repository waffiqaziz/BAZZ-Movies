package com.waffiq.bazz_movies.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core_ui.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core_ui.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core_ui.databinding.ItemMulmedBinding
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.navigation.DetailNavigator
import com.waffiq.bazz_movies.core.utils.common.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.dateFormatterStandard
import com.waffiq.bazz_movies.core.utils.helpers.GenreHelper.getGenreName
import java.text.DecimalFormat

class FavoriteTvAdapter(private val detailNavigator: DetailNavigator) :
  PagingDataAdapter<ResultItem, FavoriteTvAdapter.ViewHolder>(DIFF_CALLBACK) {

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
    lateinit var data: ResultItem

    fun bind(resultItem: ResultItem) {
      data = resultItem
      binding.ivPicture.contentDescription =
        resultItem.name ?: resultItem.title ?: resultItem.originalTitle ?: resultItem.originalName

      Glide.with(binding.ivPicture)
        .load(
          if (!resultItem.posterPath.isNullOrEmpty()) {
            TMDB_IMG_LINK_POSTER_W185 + resultItem.posterPath
          } else {
            ic_poster_error
          }
        )
        .placeholder(ic_bazz_placeholder_poster)
        .transform(CenterCrop())
        .transition(withCrossFade())
        .error(ic_poster_error)
        .into(binding.ivPicture)

      binding.tvTitle.text =
        resultItem.name ?: resultItem.title ?: resultItem.originalTitle ?: resultItem.originalName
      binding.tvYearReleased.text = (resultItem.firstAirDate ?: resultItem.releaseDate)?.let {
        dateFormatterStandard(it)
      } ?: "N/A"
      binding.tvGenre.text = resultItem.listGenreIds?.let { getGenreName(it) } ?: "N/A"
      binding.ratingBar.rating = (resultItem.voteAverage ?: 0F) / 2

      val df = DecimalFormat("#.#")
      (df.format((resultItem.voteAverage ?: 0F)).toString() + "/10").also {
        binding.tvRating.text = it
      }

      // OnClickListener
      binding.container.setOnClickListener {
        detailNavigator.openDetails(resultItem.copy(mediaType = "tv"))
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
        return oldItem.id == newItem.id
      }
    }
  }
}