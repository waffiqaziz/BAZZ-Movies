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
 * A utility object that provides helper functions for managing favorite and watchlist items,
 * as well as handling UI interactions like displaying snackbars and managing load states during paging.
 */
object FavWatchlistHelper {

  /**
   * Returns the title for a given `ResultItem`. The function checks the following properties in order:
   * - `name`
   * - `title`
   * - `originalTitle`
   *
   * If none of those properties are available, it defaults to "Item".
   *
   * @param item The `ResultItem` whose title is to be determined.
   * @return The title of the item, or "Item" if no title is found.
   */
  fun titleHandler(item: ResultItem): String {
    return item.name ?: item.title ?: item.originalTitle ?: "Item"
  }

  /**
   * Displays a Snackbar indicating that an item is already in the watchlist.
   * The message is styled with bold text and a predefined message that includes the item's name.
   *
   * @param context The context in which the Snackbar is displayed.
   * @param view The view to associate with the Snackbar.
   * @param viewGuide A view to anchor the Snackbar to.
   * @param eventMessage The message event to be displayed in the Snackbar.
   * @return The Snackbar instance, or null if no content is available to display.
   */
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

  /**
   * Displays a Snackbar indicating that an item is already marked as a favorite.
   * The message is styled with bold text and a predefined message that includes the item's name.
   *
   * @param context The context in which the Snackbar is displayed.
   * @param view The view to associate with the Snackbar.
   * @param viewGuide A view to anchor the Snackbar to.
   * @param eventMessage The message event to be displayed in the Snackbar.
   * @return The Snackbar instance, or null if no content is available to display.
   */
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
