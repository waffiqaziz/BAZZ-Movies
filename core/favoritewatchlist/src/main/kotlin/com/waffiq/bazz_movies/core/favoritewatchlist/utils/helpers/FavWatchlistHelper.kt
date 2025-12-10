package com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers

import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.utils.PagingLoadStateHelper.pagingErrorHandling
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.text.DecimalFormat

/**
 * A utility object that provides helper functions for handling UI interactions
 * and managing load states during paging.
 */
object FavWatchlistHelper {

  /**
   * Handle rating and shows with format `rate/10`
   * For example: 10/10 or 8.5/10
   *
   * @param voteAverage is user rating
   */
  fun ratingHandler(voteAverage: Float?): String =
    DecimalFormat("#.#").format((voteAverage ?: 0F)).toString() + "/10"

  /**
   * Handles the paging load state for a PagingDataAdapter.
   * Based on the load state, it shows or hides the progress bar, recycler view, error view, and empty view.
   * The function listens to changes in the load state and updates the UI accordingly.
   *
   * - If the data is loading, it shows the progress bar and the recycler view.
   * - If an error occurs during loading, it hides the progress bar and shows the error view.
   * - If the end of the pagination is reached and there are no items, it shows the empty view.
   * - Otherwise, it hides the progress bar and shows the recycler view (real data).
   *
   * @param adapterPaging The `PagingDataAdapter` that handles the data.
   * @param loadStateFlow The flow of load states to monitor.
   * @param recyclerView The `RecyclerView` that displays the data.
   * @param progressBar The `ProgressBar` that shows when data is loading.
   * @param errorView The view that shows when an error occurs.
   * @param emptyView The view that shows when no data is available.
   * @param onError A callback function to handle errors, passing the error message inside an `Event`.
   */
  fun LifecycleOwner.handlePagingLoadState(
    adapterPaging: PagingDataAdapter<*, *>,
    loadStateFlow: Flow<CombinedLoadStates>,
    recyclerView: RecyclerView,
    progressBar: ProgressBar,
    errorView: View,
    emptyView: View,
    onError: (Event<String>?) -> Unit, // A callback for when there’s an error
  ) {
    lifecycleScope.launch {
      @OptIn(FlowPreview::class)
      loadStateFlow.debounce(DEBOUNCE_SHORT).distinctUntilChanged().collectLatest { loadState ->
        when {
          loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
            progressBar.isVisible = true
            recyclerView.isVisible = true
            errorView.isVisible = false
            emptyView.isVisible = false
          }

          loadState.refresh is LoadState.Error -> {
            progressBar.isVisible = false
            recyclerView.isVisible = adapterPaging.itemCount > 0
            errorView.isVisible = adapterPaging.itemCount <= 0
            emptyView.isVisible = false

            // trigger the error callback
            val error = (loadState.refresh as LoadState.Error).error
            onError(Event(pagingErrorHandling(error)))
          }

          loadState.append.endOfPaginationReached && adapterPaging.itemCount < 1 -> {
            progressBar.isVisible = false
            recyclerView.isVisible = false
            errorView.isVisible = false
            emptyView.isVisible = true
          }

          else -> {
            progressBar.isVisible = false
            recyclerView.isVisible = true
            errorView.isVisible = false
            emptyView.isVisible = false
          }
        }
      }
    }
  }

  /**
   * Collects an [Outcome] flow in `viewModelScope` and triggers callbacks based on
   * the emission state.
   *
   * - `onSuccess` is invoked when the flow emits [Outcome.Success].
   * - `onError` is invoked when an [Outcome.Error] occurs.
   * - `onLoading` is optionally invoked while processing is in progress.
   *
   * @param flow the stream of [Outcome] values to collect.
   * @param onSuccess callback for successful results.
   * @param onError callback for error messages.
   * @param onLoading optional callback for loading state.
   */
  fun <T> ViewModel.launchAndHandleOutcome(
    flow: Flow<Outcome<T>>,
    onSuccess: (T) -> Unit,
    onError: (String) -> Unit,
    onLoading: (() -> Unit)? = null
  ) {
    viewModelScope.launch {
      flow.collect { outcome ->
        when (outcome) {
          is Outcome.Success -> onSuccess(outcome.data)
          is Outcome.Error -> onError(outcome.message)
          is Outcome.Loading -> onLoading?.invoke()
        }
      }
    }
  }
}
