package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.feature.detail.databinding.ChipGenreBinding
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywordsItem

class KeywordsAdapter :
  ListAdapter<MediaKeywordsItem, KeywordsAdapter.ViewHolder>(CastDiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ChipGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class ViewHolder(private var binding: ChipGenreBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: MediaKeywordsItem) {
      binding.chip.text = data.name
    }
  }

  class CastDiffCallback : DiffUtil.ItemCallback<MediaKeywordsItem>() {
    override fun areItemsTheSame(oldItem: MediaKeywordsItem, newItem: MediaKeywordsItem): Boolean =
      oldItem.id == newItem.id

    override fun areContentsTheSame(
      oldItem: MediaKeywordsItem,
      newItem: MediaKeywordsItem,
    ): Boolean = oldItem.id == newItem.id
  }
}
