package com.waffiq.bazz_movies.feature.home.ui.shimmer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.feature.home.databinding.ShimmerItemWideBinding

class ShimmerItemWideAdapter(private val itemCount: Int = SHIMMER_DATA) :
  RecyclerView.Adapter<ShimmerItemWideAdapter.ShimmerViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
    val view = ShimmerItemWideBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false,
    )
    return ShimmerViewHolder(view.root)
  }

  override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {
    /* not used as this adapter only shows as placeholder */
  }

  override fun getItemCount(): Int = itemCount

  class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

  companion object {
    const val SHIMMER_DATA = 10
  }
}
