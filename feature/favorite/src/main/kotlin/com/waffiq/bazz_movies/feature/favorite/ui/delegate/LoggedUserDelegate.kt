package com.waffiq.bazz_movies.feature.favorite.ui.delegate

import android.text.SpannableString
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.designsystem.R.color.yellow_700
import com.waffiq.bazz_movies.core.designsystem.R.string.added_to_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.removed_from_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.undo
import com.waffiq.bazz_movies.core.favoritewatchlist.databinding.FragmentChildBinding
import com.waffiq.bazz_movies.core.favoritewatchlist.domain.sort.LoggedFavoriteSortOption
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging.MediaPagingAdapter
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.sort.SortChipAdapter
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.BaseViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.FavWatchlistHelper.handlePagingLoadState
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackbarAlreadyUtils.snackBarAlready
import com.waffiq.bazz_movies.core.models.FavoriteParams
import com.waffiq.bazz_movies.core.models.WatchlistParams
import com.waffiq.bazz_movies.core.uihelper.dialog.SingleChoiceDialog
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.SwipeConfig
import com.waffiq.bazz_movies.core.uihelper.utils.SpannableUtils.buildActionMessage
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.core.utils.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.utils.FlowUtils.collectFlow
import com.waffiq.bazz_movies.feature.favorite.ui.viewmodel.FavoriteViewModel
import com.waffiq.bazz_movies.navigation.INavigator

/**
 * Handles favorite list functionality for logged-in users.
 * Manages paging adapter, snackbar interactions, and API calls.
 */
class LoggedUserDelegate(
  private val fragment: Fragment,
  private val binding: FragmentChildBinding,
  private val favoriteViewModel: FavoriteViewModel,
  private val baseViewModel: BaseViewModel,
  private val mediaType: String,
  private val navigator: INavigator,
  private val iSnackbar: ISnackbar,
) {

  private lateinit var adapter: MediaPagingAdapter
  private lateinit var headerAdapter: SortChipAdapter
  private var currentSnackbar: Snackbar? = null
  private var isWantToDelete = false
  private var isUndo = false

  private val snackbarAnchor = navigator.snackbarAnchor()

  fun setup() {
    setupAdapter()
    setupRefresh()
    setupData()
    observeSnackbarEvents()
  }

  fun refresh() {
    if (::adapter.isInitialized) {
      adapter.refresh()
    }
  }

  fun cleanup() {
    dismissSnackbar()
  }

  private fun setupAdapter() {
    adapter = MediaPagingAdapter(
      navigator = navigator,
      mediaType = mediaType,
      config = SwipeConfig.forFavorite(),
      onDelete = { mediaItem ->
        isUndo = false
        isWantToDelete = true
        postRemoveFavorite(titleHandler(mediaItem), mediaItem.id)
      },
      { mediaItem ->
        isUndo = false
        isWantToDelete = false
        postToAddWatchlist(titleHandler(mediaItem), mediaItem.id)
      },
    )
    headerAdapter = SortChipAdapter {
      SingleChoiceDialog.show(
        context = fragment.requireContext(),
        items = LoggedFavoriteSortOption.entries,
        selected = favoriteViewModel.currentSort.value,
        onSelected = { selectedOption ->
          favoriteViewModel.updateSort(selectedOption)
          headerAdapter.updateSort(selectedOption.label)
        },
      )
    }
    binding.recyclerView.adapter = ConcatAdapter(
      headerAdapter,
      adapter.withLoadStateFooter(
        footer = LoadingStateAdapter { adapter.retry() },
      ),
    )
  }

  private fun postToAddWatchlist(title: String, mediaId: Int) {
    if (mediaType == MOVIE_MEDIA_TYPE) {
      favoriteViewModel.addMovieToWatchlist(mediaId, title)
    } else {
      favoriteViewModel.addTvToWatchlist(mediaId, title)
    }
  }

  private fun setupRefresh() {
    binding.swipeRefresh.setOnRefreshListener {
      baseViewModel.resetSnackbarShown()
      adapter.refresh()
      binding.swipeRefresh.isRefreshing = false
    }
  }

  private fun setupData() {
    binding.illustrationError.btnTryAgain.setOnClickListener {
      baseViewModel.resetSnackbarShown()
      adapter.refresh()
    }

    collectAndSubmitData(
      fragment = fragment,
      flowProvider = { favoriteViewModel.getFavoriteData(mediaType) },
      adapter = adapter,
    )

    handleLoadState()
  }

  private fun observeSnackbarEvents() {
    favoriteViewModel.snackBarAlready.observe(fragment.viewLifecycleOwner) {
      showAlreadySnackbar(it)
    }

    fragment.requireActivity().collectFlow(favoriteViewModel.snackBarAdded, false) {
      handleSnackbarData(it)
    }
  }

  private fun handleSnackbarData(data: SnackBarUserLoginData) {
    when {
      isUndo -> {
        if (data.isSuccess) adapter.refresh()
      }

      data.isSuccess && isWantToDelete -> {
        showUndoSnackbar(data.title, data.favoriteModel, data.watchlistModel)
        adapter.refresh()
      }

      !data.isSuccess -> {
        showWarningSnackbar(Event(data.title))
      }

      else -> {
        showUndoSnackbar(data.title, data.favoriteModel, data.watchlistModel)
      }
    }
  }

  private fun handleLoadState() {
    fragment.viewLifecycleOwner.handlePagingLoadState(
      adapterPaging = adapter,
      loadStateFlow = adapter.loadStateFlow,
      binding = binding,
      onError = { error ->
        if (baseViewModel.isSnackbarShown.value == false) {
          showWarningSnackbar(error)
          baseViewModel.markSnackbarShown()
        }
      },
    )
  }

  private fun postRemoveFavorite(title: String, mediaId: Int) {
    favoriteViewModel.postFavorite(
      FavoriteParams(
        mediaType = mediaType,
        mediaId = mediaId,
        favorite = false,
      ).copy(favorite = false),
      title,
    )
  }

  private fun showUndoSnackbar(
    title: String,
    fav: FavoriteParams?,
    wtc: WatchlistParams?,
  ) {
    if (isWantToDelete) {
      showFavoriteUndoSnackbar(title, fav ?: return)
    } else {
      showWatchlistUndoSnackbar(title, wtc ?: return)
    }
  }

  private fun showFavoriteUndoSnackbar(title: String, fav: FavoriteParams) {
    val message = buildActionMessage(title, fragment.getString(removed_from_favorite))
    makeUndoSnackbar(message) {
      isUndo = true
      favoriteViewModel.postFavorite(fav.copy(favorite = true), title)
      isWantToDelete = false // cancel delete, then update flag as false
    }
  }

  private fun showWatchlistUndoSnackbar(title: String, wtc: WatchlistParams) {
    val message = buildActionMessage(title, fragment.getString(added_to_watchlist))
    makeUndoSnackbar(message) {
      isUndo = true
      favoriteViewModel.postWatchlist(wtc.copy(watchlist = false), title)
    }
  }

  private fun makeUndoSnackbar(message: SpannableString, onUndo: () -> Unit) {
    currentSnackbar = Snackbar.make(
      fragment.requireActivity().findViewById(snackbarAnchor),
      message,
      Snackbar.LENGTH_LONG,
    ).apply {
      setAction(fragment.getString(undo)) { onUndo() }
      anchorView = fragment.requireActivity().findViewById(snackbarAnchor)
      setActionTextColor(ContextCompat.getColor(fragment.requireContext(), yellow_700))
      show()
    }
  }

  private fun showAlreadySnackbar(event: Event<String>) {
    currentSnackbar = snackBarAlready(
      fragment.requireContext(),
      fragment.requireActivity().findViewById(snackbarAnchor),
      fragment.requireActivity().findViewById(snackbarAnchor),
      event,
      false,
    )
  }

  private fun showWarningSnackbar(event: Event<String>) {
    currentSnackbar = iSnackbar.showSnackbarWarning(event)
  }

  private fun dismissSnackbar() {
    currentSnackbar?.dismiss()
    currentSnackbar = null
  }
}
