package com.waffiq.bazz_movies.feature.watchlist.ui.delegate

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.paging.PagingData
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.designsystem.R.color.yellow_700
import com.waffiq.bazz_movies.core.designsystem.R.string.added_to_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.removed_from_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.undo
import com.waffiq.bazz_movies.core.domain.FavoriteParams
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.WatchlistParams
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging.WatchlistPagingAdapter
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.BaseViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.FavWatchlistHelper.handlePagingLoadState
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackbarAlreadyUtils
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.uihelper.utils.SpannableUtils.buildActionMessage
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.core.utils.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.feature.watchlist.databinding.FragmentWatchlistChildBinding
import com.waffiq.bazz_movies.feature.watchlist.ui.viewmodel.WatchlistViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import kotlinx.coroutines.flow.Flow

/**
 * Handles watchlist list functionality for logged-in users.
 * Manages paging adapter, snackbar interactions, and API calls.
 */
class LoggedUserDelegate(
  private val fragment: Fragment,
  private val binding: FragmentWatchlistChildBinding,
  private val watchlistViewModel: WatchlistViewModel,
  private val baseViewModel: BaseViewModel,
  private val mediaType: String,
  private val navigator: INavigator,
  private val iSnackbar: ISnackbar,
) {

  private lateinit var adapter: WatchlistPagingAdapter
  private var currentSnackbar: Snackbar? = null
  private var isWantToDelete = false
  private var isUndo = false

  private val snackbarAnchor = navigator.snackbarAnchor()

  fun setup(token: String) {
    setupAdapter()
    setupRefresh()
    setupData(token)
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

  fun getWatchlistData(token: String): Flow<PagingData<MediaItem>> =
    if (mediaType == MOVIE_MEDIA_TYPE) {
      watchlistViewModel.watchlistMovies(token)
    } else {
      watchlistViewModel.watchlistTvSeries(token)
    }

  private fun setupAdapter() {
    adapter = WatchlistPagingAdapter(navigator, mediaType, onDelete = { mediaItem, position ->
      isWantToDelete = true
      postRemoveWatchlist(titleHandler(mediaItem), mediaItem.id)
      adapter.notifyItemChanged(position)
    }, onAddToWatchlist = { mediaItem, position ->
      isWantToDelete = false
      postToAddFavorite(titleHandler(mediaItem), mediaItem.id)
      adapter.notifyItemChanged(position)
    })
    binding.rvWatchlist.adapter = adapter.withLoadStateFooter(
      footer = LoadingStateAdapter { adapter.retry() },
    )
  }

  private fun postToAddFavorite(title: String, mediaId: Int) {
    if (mediaType == MOVIE_MEDIA_TYPE) {
      watchlistViewModel.addMovieToFavorite(mediaId, title)
    } else {
      watchlistViewModel.addTvToFavorite(mediaId, title)
    }
  }

  private fun setupRefresh() {
    binding.swipeRefresh.setOnRefreshListener {
      baseViewModel.resetSnackbarShown()
      adapter.refresh()
      binding.swipeRefresh.isRefreshing = false
    }
  }

  private fun setupData(token: String) {
    binding.illustrationError.btnTryAgain.setOnClickListener {
      baseViewModel.resetSnackbarShown()
      adapter.refresh()
    }

    collectAndSubmitData(
      fragment = fragment,
      flowProvider = { getWatchlistData(token) },
      adapter = adapter,
    )

    handleLoadState()
  }

  private fun observeSnackbarEvents() {
    watchlistViewModel.snackBarAlready.observe(fragment.viewLifecycleOwner) {
      showAlreadySnackbar(it)
    }

    watchlistViewModel.snackBarAdded.observe(fragment.viewLifecycleOwner) { event ->
      event.getContentIfNotHandled()?.let { data ->
        handleSnackbarData(data)
      }
    }
  }

  private fun handleSnackbarData(data: SnackBarUserLoginData) {
    when {
      !isUndo && data.isSuccess && isWantToDelete -> {
        showUndoSnackbar(data.title, data.favoriteModel, data.watchlistModel)
        adapter.refresh()
      }

      !isUndo && !data.isSuccess -> {
        showWarningSnackbar(Event(data.title))
      }

      !isUndo -> {
        showUndoSnackbar(data.title, data.favoriteModel, data.watchlistModel)
      }

      isUndo && data.isSuccess -> {
        adapter.refresh()
      }
    }
  }

  private fun handleLoadState() {
    fragment.viewLifecycleOwner.handlePagingLoadState(
      adapterPaging = adapter,
      loadStateFlow = adapter.loadStateFlow,
      recyclerView = binding.rvWatchlist,
      progressBar = binding.progressBar,
      errorView = binding.illustrationError.root,
      emptyView = binding.illustrationNoDataView.root,
      onError = { error ->
        error?.let {
          if (baseViewModel.isSnackbarShown.value == false) {
            showWarningSnackbar(it)
            baseViewModel.markSnackbarShown()
          }
        }
      },
    )
  }

  private fun postRemoveWatchlist(title: String, mediaId: Int) {
    watchlistViewModel.postWatchlist(
      WatchlistParams(
        mediaType = mediaType,
        mediaId = mediaId,
        watchlist = false,
      ).copy(watchlist = false),
      title,
    )
  }

  private fun showUndoSnackbar(
    title: String,
    fav: FavoriteParams?,
    wtc: WatchlistParams?,
  ) {
    val isDelete = isWantToDelete && fav != null
    val isAddToWatchlist = !isWantToDelete && wtc != null

    if (!isDelete && !isAddToWatchlist) return

    val message = if (isDelete) {
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
        isUndo = true
        when {
          wtc != null -> {
            watchlistViewModel.postWatchlist(wtc.copy(watchlist = true), title)
            isWantToDelete = !isWantToDelete
          }

          fav != null -> {
            watchlistViewModel.postFavorite(fav.copy(favorite = false), title)
          }
        }
      }
      setAnchorView(fragment.requireActivity().findViewById(snackbarAnchor))
      setActionTextColor(ContextCompat.getColor(fragment.requireContext(), yellow_700))
      show()
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

  private fun showWarningSnackbar(event: Event<String>) {
    currentSnackbar = iSnackbar.showSnackbarWarning(event)
  }

  private fun dismissSnackbar() {
    currentSnackbar?.dismiss()
    currentSnackbar = null
  }
}
