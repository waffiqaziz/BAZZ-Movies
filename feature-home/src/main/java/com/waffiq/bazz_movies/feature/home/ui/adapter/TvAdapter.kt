package com.waffiq.bazz_movies.feature.home.ui.adapter

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.model.ResultItem
import com.waffiq.bazz_movies.core.model.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.movie.utils.common.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.ui.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.ui.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.core.ui.databinding.ItemPosterBinding
import com.waffiq.bazz_movies.navigation.Navigator

class TvAdapter(private val navigator: Navigator) :
  PagingDataAdapter<ResultItem, TvAdapter.ViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemPosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = getItem(position)
    if (data != null) {
      holder.bind(data)
      holder.itemView.startAnimation(
        AnimationUtils.loadAnimation(holder.itemView.context, fade_in)
      )
    }
  }

  inner class ViewHolder(private var binding: ItemPosterBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(tv: ResultItem) {
      binding.imgPoster.contentDescription =
        tv.name ?: tv.title ?: tv.originalTitle ?: tv.originalName

      Glide.with(binding.imgPoster)
        .load(
          if (!tv.posterPath.isNullOrEmpty()) {
            TMDB_IMG_LINK_POSTER_W185 + tv.posterPath
          } else {
            ic_broken_image
          }
        )
        .placeholder(ic_bazz_placeholder_poster)
        .transform(CenterCrop())
        .transition(withCrossFade())
        .error(ic_broken_image)
        .into(binding.imgPoster)

      // image OnClickListener
      binding.imgPoster.setOnClickListener {
        navigator.openDetails(itemView.context, tv.copy(mediaType = TV_MEDIA_TYPE))
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
