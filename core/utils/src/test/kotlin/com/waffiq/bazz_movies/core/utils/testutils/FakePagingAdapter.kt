package com.waffiq.bazz_movies.core.utils.testutils

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers

class FakePagingAdapter :
  PagingDataAdapter<String, RecyclerView.ViewHolder>(
    diffCallback = object : DiffUtil.ItemCallback<String>() {
      override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
      override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    },
    mainDispatcher = Dispatchers.Main,
    workerDispatcher = Dispatchers.Default,
  ) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
    throw UnsupportedOperationException()
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int): Unit =
    throw UnsupportedOperationException()
}
