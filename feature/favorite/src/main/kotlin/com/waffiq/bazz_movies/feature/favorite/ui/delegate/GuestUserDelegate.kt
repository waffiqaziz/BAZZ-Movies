package com.waffiq.bazz_movies.feature.favorite.ui.delegate

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ConcatAdapter
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.designsystem.R.color.yellow_700
import com.waffiq.bazz_movies.core.designsystem.R.string.added_to_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.removed_from_favorite
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
 * Handles favorite list functionality for guest users.
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
      config = SwipeConfig.forFavorite(),
      onDelete = { favorite, position ->
        isWantToDelete = true
        deleteFavorite(favorite)
        showUndoSnackbar(favorite.title, position)
      },
      onAddToWatchlist = { favorite, position ->
        isWantToDelete = false
        addToWatchlist(favorite, position)
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
    getDBFavoriteData().observe(fragment.viewLifecycleOwner) { favorites ->
      adapter.submitList(favorites)
      updateViewVisibility(favorites.isNotEmpty())
    }
  }

  fun getDBFavoriteData(): LiveData<List<Favorite>> =
    if (mediaType == MOVIE_MEDIA_TYPE) {
      sharedDBViewModel.favoriteMoviesFromDB
    } else {
      sharedDBViewModel.favoriteTvFromDB
    }

  private fun updateViewVisibility(hasData: Boolean) {
    binding.recyclerView.visibility = if (hasData) View.VISIBLE else View.GONE
    binding.illustrationNoDataView.root.visibility = if (hasData) View.GONE else View.VISIBLE
    binding.progressBar.visibility = View.GONE
  }

  private fun deleteFavorite(favorite: Favorite) {
    if (favorite.isWatchlist) {
      sharedDBViewModel.updateDB(favorite.copy(isFavorite = false))
    } else {
      sharedDBViewModel.deleteFromDB(favorite)
    }
  }

  private fun addToWatchlist(favorite: Favorite, position: Int) {
    if (favorite.isWatchlist) {
      showAlreadySnackbar(Event(favorite.title))
    } else {
      sharedDBViewModel.updateDB(favorite.copy(isWatchlist = true))
      showUndoSnackbar(favorite.title, position)
    }
  }

  private fun showUndoSnackbar(title: String, position: Int) {
    dismissSnackbar()

    val message = if (isWantToDelete) {
      buildActionMessage(title, fragment.getString(removed_from_favorite))
    } else {
      buildActionMessage(title, fragment.getString(added_to_watchlist))
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
    sharedDBViewModel.undoDB.value?.getContentIfNotHandled()?.let { favorite ->
      if (isWantToDelete) {
        restoreFavorite(favorite, position)
      } else {
        sharedDBViewModel.updateDB(favorite.copy(isWatchlist = false))
      }
    }
  }

  private fun restoreFavorite(favorite: Favorite, position: Int) {
    if (favorite.isWatchlist) {
      sharedDBViewModel.updateDB(favorite.copy(isFavorite = true))
    } else {
      sharedDBViewModel.insertToDB(favorite.copy(isFavorite = true))
    }
    binding.recyclerView.scrollToPosition(position)
  }

  private fun observeDbOperations() {
    sharedDBViewModel.dbResult.observe(fragment.viewLifecycleOwner) { eventResult ->
      eventResult.getContentIfNotHandled()?.let {
        if (it is DbResult.Error) {
          context.toastShort(it.errorMessage)
        }
      }
    }
  }

  private fun showAlreadySnackbar(event: Event<String>) {
    currentSnackbar = SnackbarAlreadyUtils.snackBarAlready(
      context,
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
