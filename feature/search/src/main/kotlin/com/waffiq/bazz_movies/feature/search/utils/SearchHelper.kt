package com.waffiq.bazz_movies.feature.search.utils

import android.content.Context
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.movie.utils.helpers.GeneralHelper.initLinearLayoutManagerVertical
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.feature.search.domain.model.KnownForItem

object SearchHelper {

  fun RecyclerView.setupRecyclerView(context: Context, pagingAdapter: PagingDataAdapter<*, *>) {
    this.apply {
      layoutManager = initLinearLayoutManagerVertical(context)
      adapter = pagingAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { pagingAdapter.retry() }
      )
    }
  }

  fun getKnownFor(knownForItemResponse: List<KnownForItem>): String {
    var temp = ""
    knownForItemResponse.forEach { temp = temp + it.title + ", " }
    temp = temp.dropLast(2)
    return temp
  }
}
