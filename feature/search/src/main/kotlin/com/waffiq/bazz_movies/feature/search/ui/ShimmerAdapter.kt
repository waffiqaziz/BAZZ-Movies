package com.waffiq.bazz_movies.feature.search.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.feature.search.databinding.ShimmerItemResultBinding

class ShimmerAdapter(private val itemCount: Int = SHIMMER_DATA) :
  RecyclerView.Adapter<ShimmerAdapter.ShimmerViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
    val view = ShimmerItemResultBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false
    )
    return ShimmerViewHolder(view.root)
  }

  override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {
    /**
     * not used as this adapter only shows as placeholder
     */
  }

  override fun getItemCount(): Int = itemCount

  inner class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

  companion object {
    const val SHIMMER_DATA = 10
  }
}
