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
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_no_profile_rounded
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.roleName
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.validName
import com.waffiq.bazz_movies.feature.detail.databinding.ItemCreditsPersonBinding
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCrewItem
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ImageHelper.profileImageSource

class CrewAdapter : ListAdapter<MediaCrewItem, CrewAdapter.ViewHolder>(CrewDiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding =
      ItemCreditsPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position))
    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(holder.itemView.context, fade_in),
    )
  }

  class ViewHolder(private val binding: ItemCreditsPersonBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(crew: MediaCrewItem) {
      binding.ivProfile.contentDescription = crew.name

      Glide.with(binding.ivProfile)
        .load(crew.profileImageSource)
        .placeholder(ic_no_profile_rounded)
        .transition(withCrossFade())
        .error(ic_broken_image)
        .into(binding.ivProfile)

      binding.tvName.text = crew.validName
      binding.tvRole.text = crew.department.roleName
    }
  }

  class CrewDiffCallback : DiffUtil.ItemCallback<MediaCrewItem>() {
    override fun areItemsTheSame(oldItem: MediaCrewItem, newItem: MediaCrewItem) =
      oldItem.id == newItem.id && oldItem.creditId == newItem.creditId

    override fun areContentsTheSame(oldItem: MediaCrewItem, newItem: MediaCrewItem) =
      oldItem.id == newItem.id &&
        oldItem.name == newItem.name &&
        oldItem.department == newItem.department
  }
}
