package com.waffiq.bazz_movies.feature.favorite.ui.delegate

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.designsystem.R.color.yellow_700
import com.waffiq.bazz_movies.core.designsystem.R.string.added_to_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.removed_from_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.undo
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.local.FavoriteAdapterDB
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackbarAlreadyUtils
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.toastShort
import com.waffiq.bazz_movies.core.uihelper.utils.SpannableUtils.buildActionMessage
import com.waffiq.bazz_movies.feature.favorite.databinding.FragmentFavoriteChildBinding
import com.waffiq.bazz_movies.navigation.INavigator

/**
 * Handles favorite list functionality for guest users.
 * Manages local database adapter, snackbar interactions, and DB operations.
 */
class GuestUserDelegate(
  private val fragment: Fragment,
  private val binding: FragmentFavoriteChildBinding,
  private val navigator: INavigator,
  private val sharedDBViewModel: SharedDBViewModel,
  private val mediaType: String,
) {

  private lateinit var adapter: FavoriteAdapterDB
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
    adapter = FavoriteAdapterDB(
      navigator = navigator,
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
    binding.rvFavorite.adapter = adapter
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
    binding.rvFavorite.visibility = if (hasData) View.VISIBLE else View.GONE
    binding.illustrationNoDataView.root.visibility = if (hasData) View.GONE else View.VISIBLE
    binding.progressBar.visibility = View.GONE
  }

  private fun deleteFavorite(favorite: Favorite) {
    if (favorite.isWatchlist) {
      sharedDBViewModel.updateToRemoveFromFavoriteDB(favorite)
    } else {
      sharedDBViewModel.delFromFavoriteDB(favorite)
    }
  }

  private fun addToWatchlist(favorite: Favorite, position: Int) {
    if (favorite.isWatchlist) {
      showAlreadySnackbar(Event(favorite.title))
    } else {
      sharedDBViewModel.updateToWatchlistDB(favorite)
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
      setAnchorView(fragment.requireActivity().findViewById(snackbarAnchor))
      setActionTextColor(ContextCompat.getColor(fragment.requireContext(), yellow_700))
      show()
    }
  }

  private fun handleUndo(position: Int) {
    sharedDBViewModel.undoDB.value?.getContentIfNotHandled()?.let { favorite ->
      if (isWantToDelete) {
        restoreFavorite(favorite, position)
      } else {
        sharedDBViewModel.updateToRemoveFromWatchlistDB(favorite)
      }
    }
  }

  private fun restoreFavorite(favorite: Favorite, position: Int) {
    if (favorite.isWatchlist) {
      sharedDBViewModel.updateToFavoriteDB(favorite)
    } else {
      sharedDBViewModel.insertToDB(favorite.copy(isFavorite = true))
    }
    binding.rvFavorite.scrollToPosition(position)
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
