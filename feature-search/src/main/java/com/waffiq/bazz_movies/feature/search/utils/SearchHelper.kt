package com.waffiq.bazz_movies.feature.search.utils

import android.content.Context
import androidx.paging.PagingDataAdapter
import com.skydoves.androidveil.VeilRecyclerFrameView
import com.waffiq.bazz_movies.core.movie.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.movie.utils.helpers.GeneralHelper.initLinearLayoutManagerVertical
import com.waffiq.bazz_movies.core.ui.R.layout.item_result_shimmer
import com.waffiq.bazz_movies.feature.search.domain.model.KnownForItem

object SearchHelper {

  fun VeilRecyclerFrameView.setupShimmer(context: Context, adapter: PagingDataAdapter<*, *>) {
    this.run {
      setVeilLayout(layout = item_result_shimmer)
      setAdapter(adapter.withLoadStateFooter(footer = LoadingStateAdapter { adapter.retry() }))
      setLayoutManager(initLinearLayoutManagerVertical(context))
      addVeiledItems(10)
    }
  }

  fun getKnownFor(knownForItemResponse: List<KnownForItem>): String {
    var temp = ""
    knownForItemResponse.forEach { temp = temp + it.title + ", " }
    temp = temp.dropLast(2)
    return temp
  }
}