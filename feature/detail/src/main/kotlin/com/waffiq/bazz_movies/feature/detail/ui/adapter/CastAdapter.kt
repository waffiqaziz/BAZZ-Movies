package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_BACKDROP_W300
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_no_profile_rounded
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemCastBinding
import com.waffiq.bazz_movies.core.domain.MediaCastItem
import com.waffiq.bazz_movies.navigation.INavigator

class CastAdapter(private val navigator: INavigator) :
  ListAdapter<MediaCastItem, CastAdapter.ViewHolder>(CastDiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position))
    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(holder.itemView.context, fade_in),
    )
  }

  inner class ViewHolder(private var binding: ItemCastBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(cast: MediaCastItem) {
      binding.imgCastPhoto.contentDescription = cast.name

      Glide.with(binding.imgCastPhoto)
        .load(
          if (!cast.profilePath.isNullOrEmpty()) {
            TMDB_IMG_LINK_BACKDROP_W300 + cast.profilePath
          } else {
            ic_no_profile_rounded
          },
        )
        .placeholder(ic_no_profile_rounded)
        .transition(withCrossFade())
        .error(ic_broken_image)
        .into(binding.imgCastPhoto)

      binding.tvCastName.text = cast.name ?: cast.originalName
      binding.tvCastCharacter.text = cast.character?.takeIf { it.isNotBlank() } ?: "TBA"

      binding.container.setOnClickListener {
        navigator.openPersonDetails(itemView.context, cast)
      }
    }
  }

  class CastDiffCallback : DiffUtil.ItemCallback<MediaCastItem>() {
    override fun areItemsTheSame(oldItem: MediaCastItem, newItem: MediaCastItem): Boolean =
      oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MediaCastItem, newItem: MediaCastItem): Boolean =
      oldItem.id == newItem.id &&
        oldItem.name == newItem.name &&
        oldItem.character == newItem.character
  }
}
