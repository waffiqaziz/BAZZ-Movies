package com.waffiq.bazz_movies.feature.search.testutils

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback

object HelperTest {
  class TestListCallback : ListUpdateCallback {
    override fun onChanged(position: Int, count: Int, payload: Any?) { /* unused */
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) { /* unused */
    }

    override fun onInserted(position: Int, count: Int) { /* unused */
    }

    override fun onRemoved(position: Int, count: Int) { /* unused */
    }
  }

  class TestDiffCallback<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
      return oldItem == newItem
    }

    override fun areContentsTheSame(
      oldItem: T & Any,
      newItem: T & Any
    ): Boolean {
      return oldItem == newItem
    }
  }
}