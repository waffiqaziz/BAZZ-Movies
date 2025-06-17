package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_ORIGINAL
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.feature.detail.databinding.ItemWatchProviderBinding
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.Provider

@Suppress("ForbiddenComment")
class WatchProvidersAdapter :
  RecyclerView.Adapter<WatchProvidersAdapter.ViewHolder>() {

  private val providerList = ArrayList<Provider>()

  fun setProviders(newList: List<Provider>) {
    val diffCallback = DiffCallback(this.providerList, newList)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    this.providerList.clear()
    this.providerList.addAll(newList)
    diffResult.dispatchUpdatesTo(this)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemWatchProviderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(providerList[position])
    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(holder.itemView.context, fade_in)
    )
  }

  override fun getItemCount(): Int = providerList.size

  inner class ViewHolder(private val binding: ItemWatchProviderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(provider: Provider) {
      binding.ivPictureProvider.contentDescription = provider.providerName

      Glide.with(binding.ivPictureProvider)
        .load(
          if (!provider.logoPath.isNullOrEmpty()) {
            TMDB_IMG_LINK_ORIGINAL + provider.logoPath
          } else {
            ic_broken_image
          }
        )
        .placeholder(ic_broken_image)
        .transition(withCrossFade())
        .error(ic_broken_image)
        .into(binding.ivPictureProvider)

      binding.container.setOnClickListener {
        // TODO: handle click (open link to watch providers)
      }
    }
  }

  inner class DiffCallback(
    private val oldList: List<Provider>,
    private val newList: List<Provider>
  ) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      return oldList[oldItemPosition].providerId == newList[newItemPosition].providerId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      return oldList[oldItemPosition] == newList[newItemPosition]
    }
  }
}
