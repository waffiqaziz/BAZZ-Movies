package com.waffiq.bazz_movies.feature.watchlist.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.designsystem.R.color.yellow_700
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_hearth_dark
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_trash
import com.waffiq.bazz_movies.core.designsystem.R.string.added_to_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.removed_from_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.undo
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.UpdateFavoriteParams
import com.waffiq.bazz_movies.core.domain.UpdateWatchlistParams
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.FavoriteAdapterDB
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.BaseViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.FavWatchlistHelper.handlePagingLoadState
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackbarAlreadyUtils
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SwipeCallbackHelper
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.toastShort
import com.waffiq.bazz_movies.core.uihelper.utils.SpannableUtils.buildActionMessage
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.core.utils.DetailDataUtils
import com.waffiq.bazz_movies.core.utils.FlowUtils
import com.waffiq.bazz_movies.core.utils.GeneralHelper
import com.waffiq.bazz_movies.feature.watchlist.databinding.FragmentWatchlistChildBinding
import com.waffiq.bazz_movies.feature.watchlist.ui.viewmodel.WatchlistViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * A base fragment class for displaying and managing favorite items.
 * This class handles both logged-in and guest user states, providing functionality
 * for adding/removing favorites and watchlist items, as well as swipe actions
 * with undo capabilities via Snackbar.
 *
 * @param T The type of data, which [MediaItem].
 */
@Suppress("TooManyFunctions")
abstract class BaseWatchlistFragment<T : Any> : Fragment() {

  @Inject
  lateinit var navigator: INavigator

  @Inject
  lateinit var iSnackbar: ISnackbar

  // Snackbar utils
  protected var snackbarAnchor: Int = 0
  protected var mSnackbar: Snackbar? = null

  // ViewModels
  protected val watchlistViewModel: WatchlistViewModel by viewModels()
  protected val sharedDBViewModel: SharedDBViewModel by viewModels()
  protected val userPreferenceViewModel: UserPreferenceViewModel by viewModels()
  protected val baseViewModel: BaseViewModel by viewModels({ requireParentFragment() })

  // Shared Adapter
  protected lateinit var adapterDB: FavoriteAdapterDB

  // State variables
  protected var isWantToDelete = false
  protected var isUndo = false

  protected abstract val binding: FragmentWatchlistChildBinding

  // Methods to be implemented by subclasses
  protected abstract fun getPagingAdapter(): PagingDataAdapter<T, *>
  protected abstract fun getWatchlistData(token: String): Flow<PagingData<T>>
  protected abstract fun setupPagingAdapterWithFooter()
  protected abstract fun notifyPagingAdapterChanged(position: Int)
  protected abstract fun refreshPagingAdapter()

  protected abstract fun getDBWatchlistData(): LiveData<List<Favorite>>
  protected abstract fun createWatchlistModel(mediaId: Int): UpdateWatchlistParams
  protected abstract fun postToAddFavorite(title: String, mediaId: Int)
  protected abstract fun extractDataFromPagingViewHolder(viewHolder: RecyclerView.ViewHolder): MediaItem

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    adapterDB = FavoriteAdapterDB(navigator)
    snackbarAnchor = navigator.snackbarAnchor()
    checkUser()
  }

  /**
   * Check user login status and setup UI accordingly.
   * If the user is logged in, initialize actions and refresh for logged-in state,
   * and set up data with progress bar and empty view for logged-in user.
   * If the user is not logged in, initialize actions and refresh for guest state,
   * observe database changes, and set up data with progress bar and empty view for guest user
   */
  private fun checkUser() {
    setupRecyclerView()

    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      if (user.token != Constants.NAN) {
        initAction(isLogin = true)
        setupRefresh(isLogin = true)
        setDataUserLoginProgressBarEmptyView(user.token)
      } else {
        initAction(isLogin = false)
        setupRefresh(isLogin = false)
        insertDBObserver()
        setDataGuestUserProgressBarEmptyView()
      }
    }
  }

  /**
   * Setup the RecyclerView with a vertical linear layout manager and default item animator.
   */
  private fun setupRecyclerView() {
    binding.rvWatchlist.layoutManager =
      GeneralHelper.initLinearLayoutManagerVertical(requireContext())
    binding.rvWatchlist.itemAnimator = DefaultItemAnimator()
  }

  /**
   * Initialize swipe actions for the RecyclerView.
   */
  private fun initAction(isLogin: Boolean) {
    val swipeCallback = SwipeCallbackHelper(
      onSwipeLeft = { viewHolder, position ->
        handleSwipeLeft(isLogin, viewHolder, position)
      },
      onSwipeRight = { viewHolder, position ->
        handleSwipeRight(isLogin, viewHolder, position)
      },
      context = requireContext(),
      deleteIconResId = ic_trash,
      actionIconResId = ic_hearth_dark,
    )

    val itemTouchHelper = ItemTouchHelper(swipeCallback)
    itemTouchHelper.attachToRecyclerView(binding.rvWatchlist)
  }

  /**
   * Handle left swipe action on a RecyclerView item to add to favorite
   *
   * @param login Boolean indicating if the user is logged in.
   * @param viewHolder The ViewHolder of the swiped item.
   * @param position The position of the swiped item in the adapter.
   */
  private fun handleSwipeLeft(login: Boolean, viewHolder: RecyclerView.ViewHolder, position: Int) {
    isUndo = false
    if (login) {
      val itemData = extractDataFromPagingViewHolder(viewHolder)
      isWantToDelete = false
      postToAddFavorite(DetailDataUtils.titleHandler(itemData), itemData.id)
      notifyPagingAdapterChanged(position)
    } else {
      val fav = (viewHolder as FavoriteAdapterDB.ViewHolder).data
      isWantToDelete = false
      performSwipeGuestUser(false, fav, position)
      adapterDB.notifyItemChanged(position)
    }
  }

  /**
   * Handle right swipe action on a RecyclerView item to remove from favorites
   *
   * @param login Boolean indicating if the user is logged in.
   * @param viewHolder The ViewHolder of the swiped item.
   * @param position The position of the swiped item in the adapter.
   */
  private fun handleSwipeRight(login: Boolean, viewHolder: RecyclerView.ViewHolder, position: Int) {
    isUndo = false
    if (login) {
      val itemData = extractDataFromPagingViewHolder(viewHolder)
      isWantToDelete = true
      postRemoveWatchlist(DetailDataUtils.titleHandler(itemData), itemData.id)
      notifyPagingAdapterChanged(position)
    } else {
      val fav = (viewHolder as FavoriteAdapterDB.ViewHolder).data
      isWantToDelete = true
      performSwipeGuestUser(true, fav, position)
    }
  }

  /**
   * Setup the swipe-to-refresh functionality.
   * Configures the SwipeRefreshLayout to refresh the data when swiped.
   *
   * @param isLogin Boolean indicating if the user is logged in.
   */
  private fun setupRefresh(isLogin: Boolean) {
    binding.swipeRefresh.setOnRefreshListener {
      if (isLogin) {
        baseViewModel.resetSnackbarShown()
        refreshPagingAdapter()
      } else {
        if (adapterDB.itemCount > 0) {
          adapterDB.notifyItemRangeChanged(0, adapterDB.itemCount - 1)
        }
      }
      binding.swipeRefresh.isRefreshing = false
    }
  }

  // region LOGGED IN USER

  /**
   * Set up data for logged-in users with progress bar and empty view handling.
   * Initializes snackbar handling, paging adapter with footer, and collects and submits data to the adapter.
   *
   * @param userToken The token of the logged-in user.
   */
  private fun setDataUserLoginProgressBarEmptyView(userToken: String) {
    handleSnackbarLoginUser()
    setupPagingAdapterWithFooter()

    binding.illustrationError.btnTryAgain.setOnClickListener {
      baseViewModel.resetSnackbarShown()
      getPagingAdapter().refresh()
    }

    FlowUtils.collectAndSubmitData(this, { getWatchlistData(userToken) }, getPagingAdapter())
  }

  /**
   * Handle snackbar notifications for logged-in users.
   * Observes ViewModel LiveData for snackbar events and displays appropriate messages.
   * Also manages the paging adapter's load state and error handling.
   */
  private fun handleSnackbarLoginUser() {
    watchlistViewModel.snackBarAlready.observe(viewLifecycleOwner) {
      mSnackbar = SnackbarAlreadyUtils.snackBarAlready(
        requireContext(),
        requireActivity().findViewById(snackbarAnchor),
        requireActivity().findViewById(snackbarAnchor),
        it,
        false
      )
    }

    watchlistViewModel.snackBarAdded.observe(viewLifecycleOwner) { event ->
      event.getContentIfNotHandled()?.let {
        if (!isUndo) {
          if (it.isSuccess && isWantToDelete) {
            showSnackBarUserLogin(it.title, it.favoriteModel, it.watchlistModel)
            refreshPagingAdapter()
          } else if (!it.isSuccess) {
            mSnackbar = iSnackbar.showSnackbarWarning(Event(it.title))
          } else {
            showSnackBarUserLogin(it.title, it.favoriteModel, it.watchlistModel)
          }
        } else if (it.isSuccess) {
          refreshPagingAdapter()
        }
      }
    }

    // Handle Paging Load State, show/hide progress bar, empty view, error views, and retry
    viewLifecycleOwner.handlePagingLoadState(
      adapterPaging = getPagingAdapter(),
      loadStateFlow = getPagingAdapter().loadStateFlow,
      recyclerView = binding.rvWatchlist,
      progressBar = binding.progressBar,
      errorView = binding.illustrationError.root,
      emptyView = binding.illustrationNoDataView.root,
      onError = { error ->
        error?.let {
          if (baseViewModel.isSnackbarShown.value == false) {
            mSnackbar = iSnackbar.showSnackbarWarning(error)
            baseViewModel.markSnackbarShown()
          }
        }
      }
    )
  }

  /**
   * Post a request to remove a favorite item from TMDB.
   *
   * @param title The title of the media item to be removed from watchlist.
   * @param mediaId The ID of the media item to be removed from watchlist.
   */
  private fun postRemoveWatchlist(title: String, mediaId: Int) {
    watchlistViewModel.postWatchlist(createWatchlistModel(mediaId).copy(watchlist = false), title)
  }

  /**
   * Show a Snackbar notification for user actions related to favorites and watchlist.
   * Provides an option to undo the action via the Snackbar.
   *
   * @param title title of the media item involved in the action.
   * @param fav [UpdateFavoriteParams]  representing the favorite item.
   * @param wtc [UpdateWatchlistParams]  representing the watchlist item.
   */
  protected fun showSnackBarUserLogin(
    title: String,
    fav: UpdateFavoriteParams?,
    wtc: UpdateWatchlistParams?,
  ) {
    val delete = isWantToDelete && wtc != null
    val addToWatchlist = !isWantToDelete && fav != null
    if (delete || addToWatchlist) {
      mSnackbar = Snackbar.make(
        requireActivity().findViewById(snackbarAnchor),
        if (delete) {
          buildActionMessage(title, getString(removed_from_watchlist))
        } else {
          buildActionMessage(title, getString(added_to_favorite))
        },
        Snackbar.LENGTH_LONG
      ).setAction(getString(undo)) {
        isUndo = true
        if (wtc != null) {
          watchlistViewModel.postWatchlist(wtc.copy(watchlist = true), title)
          isWantToDelete = !isWantToDelete
        } else if (fav != null) {
          watchlistViewModel.postFavorite(fav.copy(favorite = false), title)
        }
      }.setAnchorView(requireActivity().findViewById(snackbarAnchor))
        .setActionTextColor(ContextCompat.getColor(requireContext(), yellow_700))
      mSnackbar?.show()
    }
  }

  // endregion LOGGED IN USER

  // region GUEST USER

  /**
   * Perform swipe action for guest users to add/remove items from favorites or watchlist.
   * Updates the local database and shows an undo Snackbar.
   *
   * @param isWantToDelete Boolean indicating if the action is to delete (true) or add to favorite (false).
   * @param wtc The [Favorite] item being acted upon.
   * @param pos The position of the item in the adapter.
   */
  private fun performSwipeGuestUser(isWantToDelete: Boolean, wtc: Favorite, pos: Int) {
    if (isWantToDelete) {
      if (wtc.isFavorite) {
        sharedDBViewModel.updateToRemoveFromWatchlistDB(wtc)
      } else {
        sharedDBViewModel.delFromFavoriteDB(wtc)
      }
      showSnackBarUndoGuest(wtc.title, pos)
    } else {
      if (wtc.isFavorite) {
        mSnackbar = SnackbarAlreadyUtils.snackBarAlready(
          requireContext(),
          requireActivity().findViewById(snackbarAnchor),
          requireActivity().findViewById(snackbarAnchor),
          Event(wtc.title),
          false
        )
      } else {
        sharedDBViewModel.updateToFavoriteDB(wtc)
        showSnackBarUndoGuest(wtc.title, pos)
      }
    }
  }

  /**
   * Set up data for guest users with progress bar and empty view handling.
   * Observes local database changes and updates the RecyclerView accordingly.
   */
  private fun setDataGuestUserProgressBarEmptyView() {
    binding.rvWatchlist.adapter = adapterDB
    getDBWatchlistData().observe(viewLifecycleOwner) {
      adapterDB.setFavorite(it)
      if (it.isNotEmpty()) {
        binding.rvWatchlist.visibility = View.VISIBLE
        binding.illustrationNoDataView.root.visibility = View.GONE
      } else {
        binding.rvWatchlist.visibility = View.GONE
        binding.illustrationNoDataView.root.visibility = View.VISIBLE
      }
      binding.progressBar.visibility = View.GONE
    }
  }

  /**
   * Show a Snackbar notification for guest user actions related to favorites and watchlist.
   * Provides an option to undo the action via the Snackbar.
   *
   * @param title title of the media item involved in the action.
   * @param pos The position of the item in the adapter.
   */
  private fun showSnackBarUndoGuest(title: String, pos: Int) {
    mSnackbar = Snackbar.make(
      requireActivity().findViewById(snackbarAnchor),
      if (isWantToDelete) {
        buildActionMessage(title, getString(removed_from_watchlist))
      } else {
        buildActionMessage(title, getString(added_to_favorite))
      },
      Snackbar.LENGTH_LONG
    ).setAction(getString(undo)) {
      sharedDBViewModel.undoDB.value?.getContentIfNotHandled()?.let { wtc ->
        if (isWantToDelete) {
          if (wtc.isFavorite) {
            sharedDBViewModel.updateToWatchlistDB(wtc)
          } else {
            sharedDBViewModel.insertToDB(wtc.copy(isWatchlist = true))
          }
          binding.rvWatchlist.scrollToPosition(pos)
        } else {
          sharedDBViewModel.updateToRemoveFromFavoriteDB(wtc)
        }
      }
    }.setAnchorView(requireActivity().findViewById(snackbarAnchor))
      .setActionTextColor(ContextCompat.getColor(requireContext(), yellow_700))
    mSnackbar?.show()
  }

  /**
   * Observe database operation results and display error messages if any.
   * Listens to the ViewModel's LiveData for database results and shows a toast message for errors.
   */
  private fun insertDBObserver() {
    sharedDBViewModel.dbResult.observe(viewLifecycleOwner) { eventResult ->
      eventResult.getContentIfNotHandled().let {
        when (it) {
          is DbResult.Error -> requireContext().toastShort(it.errorMessage)
          else -> {
            /* do nothing */
          }
        }
      }
    }
  }

  // endregion GUEST USER

  // region LIFECYCLE

  override fun onPause() {
    super.onPause()
    mSnackbar?.dismiss()
    mSnackbar = null
  }

  override fun onResume() {
    super.onResume()
    baseViewModel.resetSnackbarShown()
    if (userPreferenceViewModel.getUserPref().value?.token != Constants.NAN) {
      getPagingAdapter().refresh()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    mSnackbar?.dismiss()
    mSnackbar = null
  }

  // endregion LIFECYCLE
}
