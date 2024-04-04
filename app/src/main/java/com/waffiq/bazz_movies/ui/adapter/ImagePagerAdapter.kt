package com.waffiq.bazz_movies.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.R.id.imageViewSlider
import com.waffiq.bazz_movies.R.layout.item_image_slider
import com.waffiq.bazz_movies.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.R.drawable.ic_broken_image

class ImagePagerAdapter(private val images: List<String>) :
  RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(item_image_slider, parent, false)
    return ImageViewHolder(view)
  }

  override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
    val imageUrl = images[position]
    Glide.with(holder.imageView.context)
      .load(imageUrl)
      .placeholder(ic_bazz_placeholder_poster)
      .transition(withCrossFade())
      .error(ic_broken_image)
      .into(holder.imageView)
  }

  override fun getItemCount(): Int {
    return images.size
  }

  class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageView: ImageView = itemView.findViewById(imageViewSlider)
  }
}