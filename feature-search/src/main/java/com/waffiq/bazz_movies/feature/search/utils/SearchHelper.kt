package com.waffiq.bazz_movies.feature.search.utils

import android.content.Context
import androidx.paging.PagingDataAdapter
import com.skydoves.androidveil.VeilRecyclerFrameView
import com.waffiq.bazz_movies.core.ui.R.layout.item_result_shimmer
import com.waffiq.bazz_movies.core.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.initLinearLayoutManagerVertical

object SearchHelper {

  fun VeilRecyclerFrameView.setupShimmer(context: Context, adapter: PagingDataAdapter<*, *>) {
    this.run {
      setVeilLayout(layout = item_result_shimmer)
      setAdapter(adapter.withLoadStateFooter(footer = LoadingStateAdapter { adapter.retry() }))
      setLayoutManager(initLinearLayoutManagerVertical(context))
      addVeiledItems(10)
    }
  }
}