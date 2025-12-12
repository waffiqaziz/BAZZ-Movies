package com.waffiq.bazz_movies.core.test

import android.annotation.SuppressLint
import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.Dispatchers

object PagingDataHelperTest {
  class TestListCallback : ListUpdateCallback {
    override fun onChanged(position: Int, count: Int, payload: Any?) {
      /* unused */
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
      /* unused */
    }

    override fun onInserted(position: Int, count: Int) {
      /* unused */
    }

    override fun onRemoved(position: Int, count: Int) {
      /* unused */
    }
  }

  class TestDiffCallback<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean =
      oldItem == newItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean =
      oldItem == newItem
  }

  /**
   * Create an AsyncPagingDataDiffer for testing purposes.
   *
   * @return An instance of AsyncPagingDataDiffer with a test diff callback and list callback.
   */
  fun <T : Any> differ(): AsyncPagingDataDiffer<T> {
    return AsyncPagingDataDiffer(
      diffCallback = TestDiffCallback(),
      updateCallback = TestListCallback(),
      workerDispatcher = Dispatchers.Main
    )
  }
}
