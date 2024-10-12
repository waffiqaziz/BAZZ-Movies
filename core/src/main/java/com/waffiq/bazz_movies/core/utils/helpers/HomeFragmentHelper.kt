package com.waffiq.bazz_movies.core.utils.helpers

import android.content.Context
import android.widget.TextView
import androidx.core.view.isGone
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.waffiq.bazz_movies.core_ui.R.string.data
import com.waffiq.bazz_movies.core_ui.R.string.no_data
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.toastShort
import com.waffiq.bazz_movies.core.utils.common.Constants.DEBOUNCE_SHORT
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
}
