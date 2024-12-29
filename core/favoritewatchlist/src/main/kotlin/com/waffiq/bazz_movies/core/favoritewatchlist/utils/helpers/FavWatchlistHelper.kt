package com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.designsystem.R.string.already_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.already_watchlist
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.utils.PagingLoadStateHelper.pagingErrorHandling
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * Used for watchlist and favorite fragments
 */
object FavWatchlistHelper {
  fun titleHandler(item: ResultItem): String {
    return item.name ?: item.title ?: item.originalTitle ?: "Item"
  }

  fun snackBarAlreadyWatchlist(
    context: Context,
    view: View,
    viewGuide: View,
    eventMessage: Event<String>
  ): Snackbar? {
    val result = eventMessage.getContentIfNotHandled() ?: return null
    val mSnackbar = Snackbar.make(
      view,
      HtmlCompat.fromHtml(
        "<b>$result</b> " + ContextCompat.getString(context, already_watchlist),
        HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Snackbar.LENGTH_SHORT
    ).setAnchorView(viewGuide)
    mSnackbar.show()
    return mSnackbar
  }

  fun snackBarAlreadyFavorite(
    context: Context,
    view: View,
    viewGuide: View,
    eventMessage: Event<String>
  ): Snackbar? {
    val result = eventMessage.getContentIfNotHandled() ?: return null
    val mSnackbar = Snackbar.make(
      view,
      HtmlCompat.fromHtml(
        "<b>$result</b> " + ContextCompat.getString(context, already_favorite),
        HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Snackbar.LENGTH_SHORT
    ).setAnchorView(viewGuide)
    mSnackbar.show()
    return mSnackbar
  }

  fun LifecycleOwner.handlePagingLoadState(
    adapterPaging: PagingDataAdapter<*, *>,
    loadStateFlow: Flow<CombinedLoadStates>,
    recyclerView: RecyclerView,
    progressBar: ProgressBar,
    errorView: View,
    emptyView: View,
    onError: (Event<String>?) -> Unit // A callback for when there’s an error
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

            // Trigger the error callback
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
}
