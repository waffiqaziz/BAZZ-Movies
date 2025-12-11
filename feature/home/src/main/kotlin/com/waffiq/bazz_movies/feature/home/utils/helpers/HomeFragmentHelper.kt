package com.waffiq.bazz_movies.feature.home.utils.helpers

import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_VERY_LONG
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.designsystem.databinding.IllustrationErrorBinding
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.uihelper.utils.CustomSnapHelper
import com.waffiq.bazz_movies.core.utils.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.feature.home.databinding.NoFoundLayoutBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * Used as loadStateFlow listener on HomeFragment child (Featured, Movie, TvSeries fragments)
 */
object HomeFragmentHelper {
  fun LifecycleOwner.handleLoadState(
    adapter: PagingDataAdapter<*, *>,
    recyclerView: RecyclerView,
    message: String,
    view: NoFoundLayoutBinding,
  ) {
    this.lifecycleScope.launch {
      adapter.loadStateFlow.debounce(DEBOUNCE_SHORT)
        .distinctUntilChanged().collectLatest { loadState ->
          if (
            loadState.source.refresh is LoadState.NotLoading &&
            loadState.append.endOfPaginationReached &&
            adapter.itemCount < 1
          ) {
            view.root.isVisible = true
            view.tvMessage.text = message
            recyclerView.isGone = true
          }
        }
    }
  }

  fun setupSwipeRefresh(
    swipeRefresh: SwipeRefreshLayout,
    vararg adapters: PagingDataAdapter<*, *>,
  ) {
    swipeRefresh.setOnRefreshListener {
      adapters.forEach { it.refresh() }
      swipeRefresh.isRefreshing = false
    }
  }

  fun setupRetryButton(
    binding: IllustrationErrorBinding,
    vararg adapters: PagingDataAdapter<*, *>,
  ) {
    binding.btnTryAgain.setOnClickListener {
      adapters.forEach { pagingDataAdapter -> pagingDataAdapter.refresh() }
      binding.btnTryAgain.isVisible = false
      binding.progressCircular.isVisible = true
    }
  }

  fun RecyclerView.detachRecyclerView() {
    val parentViewGroup = this.parent as? ViewGroup
    parentViewGroup?.removeView(this)
  }

  fun RecyclerView.setupLoadState(pagingDataAdapter: PagingDataAdapter<*, *>) {
    this.apply {
      itemAnimator = DefaultItemAnimator()
      adapter = pagingDataAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter {
          pagingDataAdapter.retry()
        }
      )
    }
  }

  fun LifecycleOwner.observeLoadState(
    loadStateFlow: Flow<CombinedLoadStates>,
    onLoading: () -> Unit,
    onSuccess: () -> Unit,
    onError: (Event<String>?) -> Unit,
  ) {
    lifecycleScope.launch {
      loadStateFlow.debounce(DEBOUNCE_VERY_LONG).distinctUntilChanged().collectLatest { loadState ->
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

  fun setupRecyclerWideItem(
    recyclerView: RecyclerView,
    layoutManager: LinearLayoutManager? = null,
  ) {
    // Safely attach SnapHelper
    if (recyclerView.onFlingListener == null) {
      recyclerView.layoutManager = layoutManager ?: LinearLayoutManager(
        recyclerView.context, LinearLayoutManager.HORIZONTAL, false
      )
      CustomSnapHelper(offsetPx = -15).attachToRecyclerView(recyclerView)
    }
  }
}
