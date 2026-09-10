package com.waffiq.bazz_movies.feature.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.model.SearchHistory
import com.waffiq.bazz_movies.feature.search.databinding.ItemSearchHistoryBinding
import com.waffiq.bazz_movies.feature.search.ui.adapter.diffutil.HistoryDiffUtil

class SearchHistoryAdapter(
  private val onItemClick: (String) -> Unit,
  private val onDeleteClick: (SearchHistory) -> Unit,
) : ListAdapter<SearchHistory, SearchHistoryAdapter.ViewHolder>(HistoryDiffUtil()) {

  inner class ViewHolder(private val binding: ItemSearchHistoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SearchHistory) {
      binding.tvQuery.text = item.query
      binding.root.setOnClickListener { onItemClick(item.query) }
      binding.btnDelete.setOnClickListener { onDeleteClick(item) }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemSearchHistoryBinding.inflate(
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
