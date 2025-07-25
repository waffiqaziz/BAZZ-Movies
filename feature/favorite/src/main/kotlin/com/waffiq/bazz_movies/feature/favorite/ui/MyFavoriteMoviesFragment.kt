package com.waffiq.bazz_movies.feature.favorite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.designsystem.R.color.yellow
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bookmark_dark
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_trash
import com.waffiq.bazz_movies.core.designsystem.R.string.added_to_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.designsystem.R.string.removed_from_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.undo
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.FavoriteAdapterDB
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.FavoriteMovieAdapter
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.BaseViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.FavWatchlistHelper.handlePagingLoadState
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackbarAlreadyUtils.snackBarAlready
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SwipeCallbackHelper
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.toastShort
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.core.utils.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.utils.GeneralHelper.initLinearLayoutManagerVertical
import com.waffiq.bazz_movies.feature.favorite.databinding.FragmentMyFavoriteMoviesBinding
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyFavoriteMoviesFragment : Fragment() {

  @Inject
  lateinit var navigator: INavigator

  @Inject
  lateinit var iSnackbar: ISnackbar

  private var snackbarAnchor: Int = 0

  private var _binding: FragmentMyFavoriteMoviesBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val viewModelFav: MyFavoriteViewModel by viewModels()
  private val viewModelDBFav: SharedDBViewModel by viewModels()
  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()
  private val baseViewModel: BaseViewModel by viewModels({ requireParentFragment() })

  private lateinit var adapterDB: FavoriteAdapterDB
  private lateinit var adapterPaging: FavoriteMovieAdapter

  private var mSnackbar: Snackbar? = null

  // helper
  private var isWantToDelete = false
  private var isUndo = false

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentMyFavoriteMoviesBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    adapterDB = FavoriteAdapterDB(navigator)
    adapterPaging = FavoriteMovieAdapter(navigator)

    snackbarAnchor = navigator.snackbarAnchor()
    checkUser()
  }

  private fun checkUser() {
    // setup recyclerview
    binding.rvFavMovies.layoutManager = initLinearLayoutManagerVertical(requireContext())
    binding.rvFavMovies.itemAnimator = DefaultItemAnimator()

    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      if (user.token != NAN) { // user login then show data from TMDb
        initAction(isLogin = true)
        setupRefresh(isLogin = true)
        setDataUserLoginProgressBarEmptyView(user.token)
      } else { // guest user then show data from database
        initAction(isLogin = false)
        setupRefresh(isLogin = false)
        insertDBObserver()
        setDataGuestUserProgressBarEmptyView()
      }
    }
  }

  private fun initAction(isLogin: Boolean) {
    val swipeCallback = SwipeCallbackHelper(
      isLogin = isLogin,
      onSwipeLeft = { login, viewHolder, position ->
        isUndo = false
        if (login) {
          val fav = (viewHolder as FavoriteMovieAdapter.ViewHolder).data
          isWantToDelete = false
          postToAddWatchlistTMDB(titleHandler(fav), fav.id)
          adapterPaging.notifyItemChanged(position)
        } else {
          val fav = (viewHolder as FavoriteAdapterDB.ViewHolder).data
          isWantToDelete = false
          performSwipeGuestUser(false, fav, position)
          adapterDB.notifyItemChanged(position)
        }
      },
      onSwipeRight = { login, viewHolder, position ->
        isUndo = false
        if (login) {
          val fav = (viewHolder as FavoriteMovieAdapter.ViewHolder).data
          isWantToDelete = true
          postToRemoveFavTMDB(titleHandler(fav), fav.id)
          adapterPaging.notifyItemChanged(position)
        } else {
          val fav = (viewHolder as FavoriteAdapterDB.ViewHolder).data
          isWantToDelete = true
          performSwipeGuestUser(true, fav, position)
        }
      },
      context = requireContext(),
      deleteIconResId = ic_trash,
      actionIconResId = ic_bookmark_dark,
    )

    val itemTouchHelper = ItemTouchHelper(swipeCallback)
    itemTouchHelper.attachToRecyclerView(binding.rvFavMovies)
  }

  private fun setupRefresh(isLogin: Boolean) {
    binding.swipeRefresh.setOnRefreshListener {
      if (isLogin) {
        baseViewModel.resetSnackbarShown()
        adapterPagingRefresh()
      } else {
        if (adapterDB.itemCount > 0) {
          adapterDB.notifyItemRangeChanged(0, adapterDB.itemCount - 1)
        }
      }

      binding.swipeRefresh.isRefreshing = false
    }
  }

  // region LOG-IN USER
  private fun setDataUserLoginProgressBarEmptyView(userToken: String) {
    handleSnackbarLoginUser()

    binding.rvFavMovies.adapter = adapterPaging.withLoadStateFooter(
      footer = LoadingStateAdapter { adapterPaging.retry() }
    )

    binding.illustrationError.btnTryAgain.setOnClickListener {
      baseViewModel.resetSnackbarShown()
      adapterPaging.refresh()
    }
    collectAndSubmitData(this, { viewModelFav.favoriteMovies(userToken) }, adapterPaging)
  }

  private fun handleSnackbarLoginUser() {
    viewModelFav.snackBarAlready.observe(viewLifecycleOwner) {
      mSnackbar = snackBarAlready(
        requireContext(),
        requireActivity().findViewById(snackbarAnchor),
        requireActivity().findViewById(snackbarAnchor),
        it,
        false
      )
    }

    viewModelFav.snackBarAdded.observe(viewLifecycleOwner) { event ->
      event.getContentIfNotHandled()?.let {
        if (!isUndo) {
          if (it.isSuccess && isWantToDelete) { // remove item success
            showSnackBarUserLogin(it.title, it.favoriteModel, it.watchlistModel)
            adapterPagingRefresh()
          } else if (!it.isSuccess) { // an error happen
            mSnackbar = iSnackbar.showSnackbarWarning(Event(it.title))
          } else {
            // add to watchlist success
            showSnackBarUserLogin(it.title, it.favoriteModel, it.watchlistModel)
          }
        } else if (it.isSuccess) adapterPagingRefresh() // refresh when undo remove item triggered
      }
    }

    viewLifecycleOwner.handlePagingLoadState(
      adapterPaging = adapterPaging,
      loadStateFlow = adapterPaging.loadStateFlow,
      recyclerView = binding.rvFavMovies,
      progressBar = binding.progressBar,
      errorView = binding.illustrationError.root,
      emptyView = binding.illustrationNoDataView.containerNoData,
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

  private fun postToRemoveFavTMDB(title: String, movieId: Int) {
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      viewModelFav.postFavorite(
        user.token,
        user.userId,
        FavoriteModel(
          mediaType = MOVIE_MEDIA_TYPE,
          mediaId = movieId,
          favorite = false
        ),
        title
      )
    }
  }

  private fun postToAddWatchlistTMDB(title: String, movieId: Int) {
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      viewModelFav.checkMovieStatedThenPostWatchlist(user, movieId, title)
    }
  }

  private fun showSnackBarUserLogin(
    title: String,
    fav: FavoriteModel?,
    wtc: WatchlistModel?,
  ) {
    val delete = isWantToDelete && fav != null
    val addToWatchlist = !isWantToDelete && wtc != null
    if (delete || addToWatchlist) {
      mSnackbar = Snackbar.make(
        requireActivity().findViewById(snackbarAnchor),
        HtmlCompat.fromHtml(
          "<b>$title</b> " +
            if (delete) getString(removed_from_favorite) else getString(added_to_watchlist),
          HtmlCompat.FROM_HTML_MODE_LEGACY
        ),
        Snackbar.LENGTH_LONG
      ).setAction(getString(undo)) {
        isUndo = true
        if (fav != null) { // undo remove from favorite
          userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
            viewModelFav.postFavorite(user.token, user.userId, fav.copy(favorite = true), title)
          }
          isWantToDelete = !isWantToDelete // to prevent show same snackbar when undo is triggered
        } else if (wtc != null) { // undo add to watchlist
          userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
            viewModelFav.postWatchlist(user.token, user.userId, wtc.copy(watchlist = false), title)
          }
        }
      }.setAnchorView(requireActivity().findViewById(snackbarAnchor))
        .setActionTextColor(ContextCompat.getColor(requireContext(), yellow))
      mSnackbar?.show()
    }
  }

  private fun adapterPagingRefresh() {
    adapterPaging.retry()
    adapterPaging.refresh()
  }
  // endregion LOG-IN USER

  // region GUEST USER
  private fun performSwipeGuestUser(isWantToDelete: Boolean, fav: Favorite, pos: Int) {
    if (isWantToDelete) { // delete from favorite
      if (fav.isWatchlist) {
        viewModelDBFav.updateToRemoveFromFavoriteDB(fav)
      } else {
        viewModelDBFav.delFromFavoriteDB(fav)
      }
      showSnackBarUndoGuest(fav.title, pos)
    } else { // add to watchlist action
      if (fav.isWatchlist) {
        mSnackbar = snackBarAlready(
          requireContext(),
          requireActivity().findViewById(snackbarAnchor),
          requireActivity().findViewById(snackbarAnchor),
          Event(fav.title),
          false
        )
      } else {
        viewModelDBFav.updateToWatchlistDB(fav)
        showSnackBarUndoGuest(fav.title, pos)
      }
    }
  }

  private fun setDataGuestUserProgressBarEmptyView() {
    binding.rvFavMovies.adapter = adapterDB
    viewModelDBFav.favoriteMoviesFromDB.observe(viewLifecycleOwner) {
      adapterDB.setFavorite(it)
      if (it.isNotEmpty()) {
        binding.rvFavMovies.visibility = View.VISIBLE
        binding.illustrationNoDataView.containerNoData.visibility = View.GONE
      } else {
        binding.rvFavMovies.visibility = View.GONE
        binding.illustrationNoDataView.containerNoData.visibility = View.VISIBLE
      }
      binding.progressBar.visibility = View.GONE
    }
  }

  private fun showSnackBarUndoGuest(title: String, pos: Int) {
    mSnackbar = Snackbar.make(
      requireActivity().findViewById(snackbarAnchor),
      HtmlCompat.fromHtml(
        "<b>$title</b> " +
          if (isWantToDelete) getString(removed_from_favorite) else getString(added_to_watchlist),
        HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Snackbar.LENGTH_LONG
    ).setAction(getString(undo)) {
      val fav = viewModelDBFav.undoDB.value?.getContentIfNotHandled() as Favorite
      if (isWantToDelete) { // undo remove from favorite
        if (fav.isWatchlist) {
          viewModelDBFav.updateToFavoriteDB(fav)
        } else {
          viewModelDBFav.insertToDB(fav.copy(isFavorite = true))
        }
        binding.rvFavMovies.scrollToPosition(pos)
      } else { // undo add to watchlist
        viewModelDBFav.updateToRemoveFromWatchlistDB(fav)
      }
    }.setAnchorView(requireActivity().findViewById(snackbarAnchor))
      .setActionTextColor(ContextCompat.getColor(requireContext(), yellow))
    mSnackbar?.show()
  }

  private fun insertDBObserver() {
    viewModelDBFav.dbResult.observe(viewLifecycleOwner) { eventResult ->
      eventResult.getContentIfNotHandled().let {
        when (it) {
          is DbResult.Error -> requireContext().toastShort(it.errorMessage)
          else -> {}
        }
      }
    }
  }
  // endregion GUEST USER

  override fun onPause() {
    super.onPause()
    mSnackbar?.dismiss()
    mSnackbar = null
  }

  override fun onResume() {
    super.onResume()
    baseViewModel.resetSnackbarShown()
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      if (user.token != NAN) {
        adapterPaging.refresh()
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    mSnackbar?.dismiss()
    mSnackbar = null
    _binding = null
  }
}
