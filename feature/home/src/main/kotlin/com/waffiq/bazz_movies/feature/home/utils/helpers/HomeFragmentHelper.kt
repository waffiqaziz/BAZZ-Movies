package com.waffiq.bazz_movies.feature.home.utils.helpers

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_VERY_LONG
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.designsystem.R.string.data
import com.waffiq.bazz_movies.core.designsystem.R.string.no_data
import com.waffiq.bazz_movies.core.designsystem.databinding.IllustrationErrorBinding
import com.waffiq.bazz_movies.core.movie.utils.helpers.GeneralHelper.initLinearLayoutManagerHorizontal
import com.waffiq.bazz_movies.core.movie.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.toastShort
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
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

  fun setupRetryButton(
    binding: IllustrationErrorBinding,
    vararg adapters: PagingDataAdapter<*, *>
  ) {
    binding.btnTryAgain.setOnClickListener {
      adapters.forEach { it.refresh() }
      binding.btnTryAgain.isVisible = false
      binding.progressCircular.isVisible = true
    }
  }

  fun RecyclerView.detachRecyclerView() {
    val parentViewGroup = this.parent as? ViewGroup
    parentViewGroup?.removeView(this)
  }

  fun RecyclerView.setupRecyclerView(context: Context, pagingDataAdapter: PagingDataAdapter<*, *>) {
    this.apply {
      itemAnimator = DefaultItemAnimator()
      layoutManager = initLinearLayoutManagerHorizontal(context)
      adapter = pagingDataAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { pagingDataAdapter.retry() }
      )
    }
  }

  @OptIn(FlowPreview::class)
  fun LifecycleOwner.observeLoadState(
    loadStateFlow: Flow<CombinedLoadStates>,
    onLoading: () -> Unit,
    onSuccess: () -> Unit,
    onError: (Event<String>?) -> Unit
  ) {
    lifecycleScope.launch {
      loadStateFlow
        .debounce(DEBOUNCE_VERY_LONG)
        .distinctUntilChanged()
        .collectLatest { loadState ->
          when {
            (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) &&
              loadState.append.endOfPaginationReached -> {
              onLoading()
            }

            loadState.refresh is LoadState.NotLoading &&
              loadState.prepend is LoadState.NotLoading &&
              loadState.append is LoadState.NotLoading -> {
              delay(DEBOUNCE_SHORT)
              onSuccess()
            }

            loadState.refresh is LoadState.Error -> {
              val error = (loadState.refresh as LoadState.Error).error
              onError(Event(pagingErrorHandling(error)))
            }
          }
        }
    }
  }
}
