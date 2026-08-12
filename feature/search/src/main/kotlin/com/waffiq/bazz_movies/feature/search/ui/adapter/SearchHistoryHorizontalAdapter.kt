package com.waffiq.bazz_movies.feature.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.designsystem.databinding.ChipLayoutBinding
import com.waffiq.bazz_movies.core.models.SearchHistory
import com.waffiq.bazz_movies.feature.search.ui.adapter.diffutil.HistoryDiffUtil

class SearchHistoryHorizontalAdapter(private val onItemClick: (String) -> Unit) :
  ListAdapter<SearchHistory, SearchHistoryHorizontalAdapter.ViewHolder>(HistoryDiffUtil()) {

  inner class ViewHolder(private val binding: ChipLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SearchHistory) {
      binding.chip.text = item.query
      binding.chip.setOnClickListener { onItemClick(item.query) }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ChipLayoutBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false,
    )
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position))
  }
}
