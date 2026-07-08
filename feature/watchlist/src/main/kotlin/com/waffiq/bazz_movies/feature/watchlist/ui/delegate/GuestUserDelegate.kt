package com.waffiq.bazz_movies.feature.watchlist.ui.delegate

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ConcatAdapter
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.designsystem.R.color.yellow_700
import com.waffiq.bazz_movies.core.designsystem.R.string.added_to_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.removed_from_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.undo
import com.waffiq.bazz_movies.core.favoritewatchlist.databinding.FragmentChildBinding
import com.waffiq.bazz_movies.core.favoritewatchlist.domain.sort.GuestFavoriteSortOption
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.local.MediaLocalAdapter
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.sort.SortChipAdapter
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackbarAlreadyUtils
import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.core.uihelper.dialog.SingleChoiceDialog
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.SwipeConfig
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.toastShort
import com.waffiq.bazz_movies.core.uihelper.utils.SpannableUtils.buildActionMessage
import com.waffiq.bazz_movies.navigation.INavigator

/**
 * Handles watchlist list functionality for guest users.
 * Manages local database adapter, snackbar interactions, and DB operations.
 */
class GuestUserDelegate(
  private val fragment: Fragment,
  private val binding: FragmentChildBinding,
  private val navigator: INavigator,
  private val sharedDBViewModel: SharedDBViewModel,
  private val mediaType: String,
) {

  private lateinit var adapter: MediaLocalAdapter
  private lateinit var headerAdapter: SortChipAdapter
  private var currentSnackbar: Snackbar? = null
  private var isWantToDelete = false
  private var pendingRestorePosition: Int? = null

  private val snackbarAnchor = navigator.snackbarAnchor()
  private val context = fragment.requireContext()

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
    adapter = MediaLocalAdapter(
      navigator = navigator,
      config = SwipeConfig.forWatchlist(),
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
    headerAdapter = SortChipAdapter {
      SingleChoiceDialog.show(
        context = context,
        items = GuestFavoriteSortOption.entries,
        selected = sharedDBViewModel.currentSort.value,
        onSelected = { selectedOption ->
          sharedDBViewModel.updateSort(selectedOption)
          headerAdapter.updateSort(selectedOption.label)
        },
      )
    }
    binding.recyclerView.adapter = ConcatAdapter(
      headerAdapter,
      adapter,
    )
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
      adapter.submitList(favorites) {
        pendingRestorePosition?.let { pos ->
          binding.recyclerView.scrollToPosition(pos + headerAdapter.itemCount)
          pendingRestorePosition = null // prevent multiple scrollToPosition
        }
      }
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
    binding.recyclerView.visibility = if (hasData) View.VISIBLE else View.GONE
    binding.illustrationNoDataView.root.visibility = if (hasData) View.GONE else View.VISIBLE
    binding.progressBar.visibility = View.GONE
  }

  private fun deleteWatchlist(watchlistItem: Favorite) {
    if (watchlistItem.isFavorite) {
      sharedDBViewModel.updateDB(watchlistItem.copy(isWatchlist = false))
    } else {
      sharedDBViewModel.deleteFromDB(watchlistItem)
    }
  }

  private fun addToFavorite(watchlistItem: Favorite, position: Int) {
    if (watchlistItem.isFavorite) {
      showAlreadySnackbar(Event(watchlistItem.title))
    } else {
      sharedDBViewModel.updateDB(watchlistItem.copy(isFavorite = true))
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
      anchorView = fragment.requireActivity().findViewById(snackbarAnchor)
      setActionTextColor(context.getColor(yellow_700))
      show()
    }
  }

  private fun handleUndo(position: Int) {
    sharedDBViewModel.undoDB.value?.getContentIfNotHandled()?.let { watchlistItem ->
      if (isWantToDelete) {
        restoreWatchlist(watchlistItem, position)
      } else {
        // undo add to favorite
        sharedDBViewModel.updateDB(watchlistItem.copy(isFavorite = false))
      }
    }
  }

  private fun restoreWatchlist(watchlistItem: Favorite, position: Int) {
    // undo delete from watchlist
    if (watchlistItem.isFavorite) {
      sharedDBViewModel.updateDB(watchlistItem.copy(isWatchlist = true))
    } else {
      sharedDBViewModel.insertToDB(watchlistItem.copy(isWatchlist = true))
    }
    pendingRestorePosition = position
  }

  private fun observeDbOperations() {
    sharedDBViewModel.dbResult.observe(fragment.viewLifecycleOwner) { eventResult ->
      eventResult.getContentIfNotHandled()?.let {
        if (it is DbResult.Error) context.toastShort(it.errorMessage)
      }
    }
  }

  private fun showAlreadySnackbar(event: Event<String>) {
    currentSnackbar = SnackbarAlreadyUtils.snackBarAlready(
      context,
      fragment.requireActivity().findViewById(snackbarAnchor),
      fragment.requireActivity().findViewById(snackbarAnchor),
      event,
      true,
    )
  }

  private fun dismissSnackbar() {
    currentSnackbar?.dismiss()
    currentSnackbar = null
  }
}
