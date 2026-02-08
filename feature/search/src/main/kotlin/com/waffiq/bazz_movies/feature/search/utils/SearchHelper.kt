package com.waffiq.bazz_movies.feature.search.utils

import android.content.Context
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.utils.GeneralHelper.initLinearLayoutManagerVertical
import com.waffiq.bazz_movies.feature.search.domain.model.KnownForItem

object SearchHelper {

  fun RecyclerView.setupRecyclerView(context: Context, pagingAdapter: PagingDataAdapter<*, *>) {
    this.apply {
      layoutManager = initLinearLayoutManagerVertical(context)
      adapter = pagingAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { pagingAdapter.retry() },
      )
    }
  }

  fun getKnownFor(knownForItem: List<KnownForItem>): String {
    var temp = ""
    knownForItem.forEach { item ->
      (item.title ?: item.name ?: item.originalName)
        ?.let { temp += "$it, " } // Use title, fallback to name inline
    }
    return if (temp.isNotEmpty()) temp.dropLast(2) else temp // Remove the trailing ", " if any
  }
}
