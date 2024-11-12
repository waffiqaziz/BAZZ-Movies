package com.waffiq.bazz_movies.feature.home.utils.helpers

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.skydoves.androidveil.VeilRecyclerFrameView
import com.waffiq.bazz_movies.core.movie.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.movie.utils.common.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.movie.utils.helpers.GeneralHelper.initLinearLayoutManagerHorizontal
import com.waffiq.bazz_movies.core.movie.utils.helpers.GeneralHelper.toastShort
import com.waffiq.bazz_movies.core.ui.R.layout.item_poster
import com.waffiq.bazz_movies.core.ui.R.string.data
import com.waffiq.bazz_movies.core.ui.R.string.no_data
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Used as loadStateFlow listener on HomeFragment child (Featured, Movie, TvSeries fragments)
 */
object HomeFragmentHelper {
  fun LifecycleOwner.handleLoadState(
    context: Context,
    adapter: PagingDataAdapter<*, *>,
    recyclerView: RecyclerView,
    textView: TextView,
    noMoviesStringRes: Int,
    region: String,
  ) {
    this.lifecycleScope.launch {
      @OptIn(FlowPreview::class)
      adapter.loadStateFlow.debounce(DEBOUNCE_SHORT).distinctUntilChanged()
        .collectLatest { loadState ->
          if (loadState.source.refresh is LoadState.NotLoading &&
            loadState.append.endOfPaginationReached &&
            adapter.itemCount < 1
          ) {
            context.toastShort(
              context.getString(noMoviesStringRes, Locale("", region).displayCountry)
            )
            recyclerView.isGone = true
            if (!textView.text.contains(context.getString(data))) {
              textView.append(" (${context.getString(no_data)})")
            }
          }
        }
    }
  }

  fun LifecycleOwner.handleLoadState(
    context: Context,
    adapter: PagingDataAdapter<*, *>,
    recyclerView: VeilRecyclerFrameView,
    textView: TextView,
    noMoviesStringRes: Int,
    region: String,
  ) {
    this.lifecycleScope.launch {
      @OptIn(FlowPreview::class)
      adapter.loadStateFlow.debounce(DEBOUNCE_SHORT).distinctUntilChanged()
        .collectLatest { loadState ->
          if (loadState.source.refresh is LoadState.NotLoading &&
            loadState.append.endOfPaginationReached &&
            adapter.itemCount < 1
          ) {
            context.toastShort(
              context.getString(noMoviesStringRes, Locale("", region).displayCountry)
            )
            recyclerView.isGone = true
            if (!textView.text.contains(context.getString(data))) {
              textView.append(" (${context.getString(no_data)})")
            }
          }
        }
    }
  }

  fun setupSwipeRefresh(
    swipeRefresh: SwipeRefreshLayout,
    vararg adapters: PagingDataAdapter<*, *>
  ) {
    swipeRefresh.setOnRefreshListener {
      adapters.forEach { it.refresh() }
      swipeRefresh.isRefreshing = false
    }
  }

  fun setupRetryButton(materialButton: MaterialButton, vararg adapters: PagingDataAdapter<*, *>) {
    materialButton.setOnClickListener {
      adapters.forEach { it.refresh() }
    }
  }

  fun VeilRecyclerFrameView.setupShimmer(context: Context, adapter: PagingDataAdapter<*, *>) {
    this.apply {
      setVeilLayout(layout = item_poster)
      setAdapter(adapter.withLoadStateFooter(footer = LoadingStateAdapter { adapter.retry() }))
      setLayoutManager(initLinearLayoutManagerHorizontal(context))
      addVeiledItems(10)
    }
  }

  fun VeilRecyclerFrameView.detachRecyclerView() {
    val parentViewGroup = this.parent as? ViewGroup
    parentViewGroup?.removeView(this)
  }
}