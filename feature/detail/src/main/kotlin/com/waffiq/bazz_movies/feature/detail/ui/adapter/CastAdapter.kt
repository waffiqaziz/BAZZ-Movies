package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_no_profile_rounded
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemCastBinding
import com.waffiq.bazz_movies.core.domain.MediaCastItem
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.nameHandler
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.roleName
import com.waffiq.bazz_movies.feature.detail.databinding.ItemCreditsPersonBinding
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ImageHelper.profileImageSource
import com.waffiq.bazz_movies.navigation.INavigator

class CastAdapter(private val navigator: INavigator) :
  ListAdapter<MediaCastItem, RecyclerView.ViewHolder>(CastDiffCallback()) {

  private var isVerticalMode = false

  fun setVerticalMode(isVertical: Boolean) {
    if (isVerticalMode != isVertical) {
      isVerticalMode = isVertical
      notifyItemRangeChanged(0, itemCount)
    }
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun isVerticalMode() = isVerticalMode

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
    if (viewType == VIEW_TYPE_VERTICAL) {
      VerticalViewHolder(
        ItemCreditsPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false),
      )
    } else {
      HorizontalViewHolder(
        ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false),
      )
    }

  override fun getItemViewType(position: Int): Int =
    if (isVerticalMode) VIEW_TYPE_VERTICAL else VIEW_TYPE_HORIZONTAL

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, fade_in))
    (holder as BaseViewHolder).bind(getItem(position))
  }

  inner class HorizontalViewHolder(private var binding: ItemCastBinding) :
    BaseViewHolder(binding.root) {

    override fun bind(cast: MediaCastItem) {
      binding.imgCastPhoto.contentDescription = cast.name

      Glide.with(binding.imgCastPhoto)
        .load(cast.profileImageSource)
        .placeholder(ic_no_profile_rounded)
        .transition(withCrossFade())
        .error(ic_broken_image)
        .into(binding.imgCastPhoto)

      binding.tvCastName.text = nameHandler(cast)
      binding.tvCastCharacter.text = cast.character?.takeIf { it.isNotBlank() } ?: "TBA"

      binding.container.setOnClickListener {
        navigator.openPersonDetails(itemView.context, cast)
      }
    }
  }

  inner class VerticalViewHolder(private var binding: ItemCreditsPersonBinding) :
    BaseViewHolder(binding.root) {

    override fun bind(cast: MediaCastItem) {
      binding.ivProfile.contentDescription = cast.name

      Glide.with(binding.ivProfile)
        .load(cast.profileImageSource)
        .placeholder(ic_no_profile_rounded)
        .transition(withCrossFade())
        .error(ic_broken_image)
        .into(binding.ivProfile)

      binding.tvName.text = nameHandler(cast)
      binding.tvRole.text = cast.character.roleName

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

  companion object {
    const val VIEW_TYPE_HORIZONTAL = 0 // used on cast list inside detail page
    const val VIEW_TYPE_VERTICAL = 1 // used on credits list inside bottom sheet
  }
}
