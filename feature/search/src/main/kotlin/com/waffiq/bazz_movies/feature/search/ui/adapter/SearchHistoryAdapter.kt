package com.waffiq.bazz_movies.feature.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.models.SearchHistory
import com.waffiq.bazz_movies.feature.search.databinding.ItemSearchHistoryBinding

class SearchHistoryAdapter(
  private val onItemClick: (String) -> Unit,
  private val onDeleteClick: (SearchHistory) -> Unit,
) : ListAdapter<SearchHistory, SearchHistoryAdapter.ViewHolder>(DiffCallback()) {

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

  private class DiffCallback : DiffUtil.ItemCallback<SearchHistory>() {
    override fun areItemsTheSame(old: SearchHistory, new: SearchHistory) = old.query == new.query
    override fun areContentsTheSame(old: SearchHistory, new: SearchHistory) = old == new
  }
}
