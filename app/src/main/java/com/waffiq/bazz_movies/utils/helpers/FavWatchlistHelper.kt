package com.waffiq.bazz_movies.utils.helpers

import android.app.Activity
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
import com.waffiq.bazz_movies.ui.viewmodel.BaseViewModel
import com.waffiq.bazz_movies.utils.common.Constants.DEBOUNCE_TIME
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.pagingErrorState
import com.waffiq.bazz_movies.utils.helpers.SnackBarManager.snackBarWarning
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
    val twoWeeksFromNow = LocalDate.now().plusWeeks(2)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return twoWeeksFromNow.format(formatter)
  }

  fun handlePagingLoadState(
    adapterPaging: PagingDataAdapter<*, *>,
    loadStateFlow: Flow<CombinedLoadStates>,
    recyclerView: RecyclerView,
    progressBar: ProgressBar,
    errorView: View,
    emptyView: View,
    viewModel: BaseViewModel,
    context: Context,
    activity: Activity,
    navViewId: Int,
    lifecycleOwner: LifecycleOwner,
    snackbar: Snackbar? = null
  ): Snackbar? {
    var updatedSnackbar = snackbar
    lifecycleOwner.lifecycleScope.launch {
      @OptIn(FlowPreview::class)
      loadStateFlow.debounce(DEBOUNCE_TIME).distinctUntilChanged().collectLatest { loadState ->
        when {
          loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
            updatedSnackbar?.dismiss()
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
            pagingErrorState(loadState)?.let {
              if (viewModel.isSnackbarShown.value == false) {
                updatedSnackbar = snackBarWarning(
                  context,
                  activity.findViewById(navViewId),
                  activity.findViewById(navViewId),
                  pagingErrorHandling(it.error)
                )
                viewModel.markSnackbarShown()
              }
            }
          }
          loadState.append.endOfPaginationReached && adapterPaging.itemCount < 1 -> {
            progressBar.isVisible = false
            recyclerView.isVisible = false
            errorView.isVisible = false
            emptyView.isVisible = true
            updatedSnackbar?.dismiss()
          }
          else -> {
            progressBar.isVisible = false
            recyclerView.isVisible = true
            errorView.isVisible = false
            emptyView.isVisible = false
            updatedSnackbar?.dismiss()
          }
        }
      }
    }
    return updatedSnackbar
  }
}
