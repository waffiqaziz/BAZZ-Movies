package com.waffiq.bazz_movies.utils.helpers

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
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.utils.common.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.utils.common.Event
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
        "<b>$result</b> " + ContextCompat.getString(context, R.string.already_watchlist),
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
        "<b>$result</b> " + ContextCompat.getString(context, R.string.already_favorite),
        HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Snackbar.LENGTH_SHORT
    ).setAnchorView(viewGuide)
    mSnackbar.show()
    return mSnackbar
  }

  fun getDateTwoWeeksFromToday(): String {
    return LocalDate.now().plusWeeks(2) // get date two weeks from now
      .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) // formatter
  }

  fun handlePagingLoadState(
    adapterPaging: PagingDataAdapter<*, *>,
    loadStateFlow: Flow<CombinedLoadStates>,
    recyclerView: RecyclerView,
    progressBar: ProgressBar,
    errorView: View,
    emptyView: View,
    lifecycleOwner: LifecycleOwner,
    onError: (Throwable?) -> Unit // A callback for when there’s an error
  ) {
    lifecycleOwner.lifecycleScope.launch {
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
            val error = (loadState.refresh as? LoadState.Error)?.error
            onError(error)
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
