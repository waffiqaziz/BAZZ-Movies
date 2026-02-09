package com.waffiq.bazz_movies.feature.watchlist.ui.delegate

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.designsystem.R.color.yellow_700
import com.waffiq.bazz_movies.core.designsystem.R.string.added_to_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.removed_from_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.undo
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.WatchlistAdapterDB
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackbarAlreadyUtils
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.toastShort
import com.waffiq.bazz_movies.core.uihelper.utils.SpannableUtils.buildActionMessage
import com.waffiq.bazz_movies.feature.watchlist.databinding.FragmentWatchlistChildBinding
import com.waffiq.bazz_movies.navigation.INavigator

/**
 * Handles watchlist list functionality for guest users.
 * Manages local database adapter, snackbar interactions, and DB operations.
 */
class GuestUserDelegate(
  private val fragment: Fragment,
  private val binding: FragmentWatchlistChildBinding,
  private val navigator: INavigator,
  private val sharedDBViewModel: SharedDBViewModel,
  private val mediaType: String,
) {

  private lateinit var adapter: WatchlistAdapterDB
  private var currentSnackbar: Snackbar? = null
  private var isWantToDelete = false

  private val snackbarAnchor = navigator.snackbarAnchor()

  fun setup() {
    setupAdapter()
    setupRefresh()
    setupData()
    observeDbOperations()
  }

  fun cleanup() {
    dismissSnackbar()
  }

  private fun setupAdapter() {
    adapter = WatchlistAdapterDB(
      navigator = navigator,
      onDelete = { favorite, position ->
        isWantToDelete = true
        deleteWatchlist(favorite)
        showUndoSnackbar(favorite.title, position)
      },
      onAddToWatchlist = { favorite, position ->
        isWantToDelete = false
        addToFavorite(favorite, position)
      },
    )
    binding.rvWatchlist.adapter = adapter
  }

  private fun setupRefresh() {
    binding.swipeRefresh.setOnRefreshListener {
      if (adapter.itemCount > 0) {
        adapter.notifyItemRangeChanged(0, adapter.itemCount - 1)
      }
      binding.swipeRefresh.isRefreshing = false
    }
  }

  private fun setupData() {
    getDBWatchlistData().observe(fragment.viewLifecycleOwner) { favorites ->
      adapter.setWatchlist(favorites)
      updateViewVisibility(favorites.isNotEmpty())
    }
  }

  private fun getDBWatchlistData(): LiveData<List<Favorite>> =
    if (mediaType == MOVIE_MEDIA_TYPE) {
      sharedDBViewModel.watchlistMoviesDB
    } else {
      sharedDBViewModel.watchlistTvSeriesDB
    }

  private fun updateViewVisibility(hasData: Boolean) {
    binding.rvWatchlist.visibility = if (hasData) View.VISIBLE else View.GONE
    binding.illustrationNoDataView.root.visibility = if (hasData) View.GONE else View.VISIBLE
    binding.progressBar.visibility = View.GONE
  }

  private fun deleteWatchlist(watchlistItem: Favorite) {
    if (watchlistItem.isWatchlist) {
      sharedDBViewModel.updateToRemoveFromWatchlistDB(watchlistItem)
    } else {
      sharedDBViewModel.delFromFavoriteDB(watchlistItem)
    }
  }

  private fun addToFavorite(watchlistItem: Favorite, position: Int) {
    if (watchlistItem.isWatchlist) {
      showAlreadySnackbar(Event(watchlistItem.title))
    } else {
      sharedDBViewModel.updateToFavoriteDB(watchlistItem)
      showUndoSnackbar(watchlistItem.title, position)
    }
  }

  private fun showUndoSnackbar(title: String, position: Int) {
    dismissSnackbar()

    val message = if (isWantToDelete) {
      buildActionMessage(title, fragment.getString(removed_from_watchlist))
    } else {
      buildActionMessage(title, fragment.getString(added_to_favorite))
    }

    currentSnackbar = Snackbar.make(
      fragment.requireActivity().findViewById(snackbarAnchor),
      message,
      Snackbar.LENGTH_LONG,
    ).apply {
      setAction(fragment.getString(undo)) {
        handleUndo(position)
      }
      setAnchorView(fragment.requireActivity().findViewById(snackbarAnchor))
      setActionTextColor(ContextCompat.getColor(fragment.requireContext(), yellow_700))
      show()
    }
  }

  private fun handleUndo(position: Int) {
    sharedDBViewModel.undoDB.value?.getContentIfNotHandled()?.let { watchlistItem ->
      if (isWantToDelete) {
        restoreWatchlist(watchlistItem, position)
      } else {
        sharedDBViewModel.updateToRemoveFromFavoriteDB(watchlistItem)
      }
    }
  }

  private fun restoreWatchlist(watchlistItem: Favorite, position: Int) {
    if (watchlistItem.isFavorite) {
      sharedDBViewModel.updateToWatchlistDB(watchlistItem)
    } else {
      sharedDBViewModel.insertToDB(watchlistItem.copy(isWatchlist = true))
    }
    binding.rvWatchlist.scrollToPosition(position)
  }

  private fun observeDbOperations() {
    sharedDBViewModel.dbResult.observe(fragment.viewLifecycleOwner) { eventResult ->
      eventResult.getContentIfNotHandled()?.let {
        if (it is DbResult.Error) {
          fragment.requireContext().toastShort(it.errorMessage)
        }
      }
    }
  }

  private fun showAlreadySnackbar(event: Event<String>) {
    currentSnackbar = SnackbarAlreadyUtils.snackBarAlready(
      fragment.requireContext(),
      fragment.requireActivity().findViewById(snackbarAnchor),
      fragment.requireActivity().findViewById(snackbarAnchor),
      event,
      false,
    )
  }

  private fun dismissSnackbar() {
    currentSnackbar?.dismiss()
    currentSnackbar = null
  }
}
