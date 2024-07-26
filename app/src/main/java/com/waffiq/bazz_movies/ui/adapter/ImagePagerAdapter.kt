package com.waffiq.bazz_movies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.databinding.ItemImageSliderBinding

class ImagePagerAdapter(private val images: List<String>) :
  RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
    val binding =
      ItemImageSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ImageViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
    val imageUrl = images[position]
    holder.bind(imageUrl)
    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(
        holder.itemView.context,
        android.R.anim.fade_in
      )
    )
  }

  override fun getItemCount(): Int {
    return images.size
  }

  inner class ImageViewHolder(private var binding: ItemImageSliderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: String) {
      Glide.with(binding.imageViewSlider.context)
        .load(data)
        .placeholder(ic_bazz_placeholder_poster)
        .transition(withCrossFade())
        .error(ic_poster_error)
        .into(binding.imageViewSlider)
    }
  }
}