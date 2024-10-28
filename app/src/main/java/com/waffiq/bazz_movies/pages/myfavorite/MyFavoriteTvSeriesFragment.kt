package com.waffiq.bazz_movies.pages.myfavorite

import android.R.anim.fade_in
import android.R.anim.fade_out
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.drawable.ic_bookmark_dark
import com.waffiq.bazz_movies.R.drawable.ic_trash
import com.waffiq.bazz_movies.R.id.bottom_navigation
import com.waffiq.bazz_movies.core.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.core.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.core.domain.model.Favorite
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.navigation.DetailNavigator
import com.waffiq.bazz_movies.core.ui.adapter.FavoriteAdapterDB
import com.waffiq.bazz_movies.core.ui.adapter.FavoriteTvAdapter
import com.waffiq.bazz_movies.core.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.utils.common.Constants.NAN
import com.waffiq.bazz_movies.core.utils.common.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.utils.common.Event
import com.waffiq.bazz_movies.core.utils.helpers.FavWatchlistHelper.handlePagingLoadState
import com.waffiq.bazz_movies.core.utils.helpers.FavWatchlistHelper.snackBarAlreadyWatchlist
import com.waffiq.bazz_movies.core.utils.helpers.FavWatchlistHelper.titleHandler
import com.waffiq.bazz_movies.core.utils.helpers.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.toastShort
import com.waffiq.bazz_movies.core.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.core.utils.helpers.uihelpers.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.core.utils.helpers.uihelpers.SwipeCallbackHelper
import com.waffiq.bazz_movies.core.utils.result.DbResult
import com.waffiq.bazz_movies.core_ui.R.color.red_matte
import com.waffiq.bazz_movies.core_ui.R.color.yellow
import com.waffiq.bazz_movies.core_ui.R.string.added_to_watchlist
import com.waffiq.bazz_movies.core_ui.R.string.binding_error
import com.waffiq.bazz_movies.core_ui.R.string.deleted_from_favorite
import com.waffiq.bazz_movies.core_ui.R.string.undo
import com.waffiq.bazz_movies.databinding.FragmentMyFavoriteTvSeriesBinding
import com.waffiq.bazz_movies.feature_detail.ui.DetailMovieActivity
import com.waffiq.bazz_movies.feature_detail.ui.DetailMovieActivity.Companion.EXTRA_MOVIE
import com.waffiq.bazz_movies.viewmodel.BaseViewModel
import com.waffiq.bazz_movies.viewmodel.UserPreferenceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyFavoriteTvSeriesFragment : Fragment(), DetailNavigator {

  private var _binding: FragmentMyFavoriteTvSeriesBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val adapterPaging = FavoriteTvAdapter(this)
  private val adapterDB = FavoriteAdapterDB(this)

  private val viewModelFav: MyFavoriteViewModel by viewModels()
  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()
  private val baseViewModel: BaseViewModel by viewModels()

  private var mSnackbar: Snackbar? = null

  // helper
  private var isWantToDelete = false
  private var isUndo = false

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMyFavoriteTvSeriesBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    checkUser()
  }

  private fun checkUser() {
    // setup recyclerview
    binding.rvFavTv.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    binding.rvFavTv.itemAnimator = DefaultItemAnimator()

    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      if (user.token != NAN) { // user login then show data from TMDB
        initAction(isLogin = true)
        setupRefresh(true)
        setDataUserLoginProgressBarEmptyView(user.token)
      } else { // guest user then show data from database
        initAction(isLogin = false)
        setDataGuestUserProgressBarEmptyView()
        setupRefresh(false)
      }
    }
  }

  private fun initAction(isLogin: Boolean) {
    val swipeCallback = SwipeCallbackHelper(
      isLogin = isLogin,
      onSwipeLeft = { login, viewHolder, position ->
        isUndo = false
        if (login) {
          val fav = (viewHolder as FavoriteTvAdapter.ViewHolder).data
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
          val fav = (viewHolder as FavoriteTvAdapter.ViewHolder).data
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
      deleteColor = red_matte,
      actionColor = yellow
    )

    val itemTouchHelper = ItemTouchHelper(swipeCallback)
    itemTouchHelper.attachToRecyclerView(binding.rvFavTv)
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

    binding.rvFavTv.adapter = adapterPaging.withLoadStateFooter(
      footer = LoadingStateAdapter { adapterPaging.retry() }
    )

    binding.illustrationError.btnTryAgain.setOnClickListener {
      baseViewModel.resetSnackbarShown()
      adapterPaging.refresh()
    }
    collectAndSubmitData(this, { viewModelFav.favoriteTvSeries(userToken) }, adapterPaging)
  }

  private fun handleSnackbarLoginUser() {
    viewModelFav.snackBarAlready.observe(viewLifecycleOwner) {
      mSnackbar =
        snackBarAlreadyWatchlist(
          requireContext(),
          requireActivity().findViewById(bottom_navigation),
          requireActivity().findViewById(bottom_navigation),
          it
        )
    }

    viewModelFav.snackBarAdded.observe(viewLifecycleOwner) { event ->
      event.getContentIfNotHandled()?.let {
        if (!isUndo) {
          if (it.isSuccess && isWantToDelete) { // remove item success
            showSnackBarUserLogin(it.title, it.favoritePostModel, it.watchlistPostModel)
            adapterPagingRefresh()
          } else if (!it.isSuccess) {
            mSnackbar = snackBarWarning(
              requireActivity().findViewById(bottom_navigation),
              requireActivity().findViewById(bottom_navigation),
              Event(it.title)
            )
          } else {
            // add to watchlist success
            showSnackBarUserLogin(it.title, it.favoritePostModel, it.watchlistPostModel)
          }
        } else if (it.isSuccess) adapterPagingRefresh() // refresh when undo remove item triggered
      }
    }

    handlePagingLoadState(
      adapterPaging = adapterPaging,
      loadStateFlow = adapterPaging.loadStateFlow,
      recyclerView = binding.rvFavTv,
      progressBar = binding.progressBar,
      errorView = binding.illustrationError.root,
      emptyView = binding.illustrationNoDataView.containerNoData,
      lifecycleOwner = viewLifecycleOwner,
      onError = { error ->
        error?.let {
          if (baseViewModel.isSnackbarShown.value == false) {
            mSnackbar = snackBarWarning(
              requireActivity().findViewById(bottom_navigation),
              requireActivity().findViewById(bottom_navigation),
              pagingErrorHandling(it)
            )
            baseViewModel.markSnackbarShown()
          }
        }
      }
    )
  }

  private fun postToRemoveFavTMDB(title: String, tvId: Int) {
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      viewModelFav.postFavorite(
        user.token,
        user.userId,
        FavoritePostModel(
          mediaType = TV_MEDIA_TYPE,
          mediaId = tvId,
          favorite = false
        ),
        title
      )
    }
  }

  private fun postToAddWatchlistTMDB(title: String, tvId: Int) {
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      viewModelFav.checkStatedThenPostWatchlist(TV_MEDIA_TYPE, user, tvId, title)
    }
  }

  private fun showSnackBarUserLogin(
    title: String,
    fav: FavoritePostModel?,
    wtc: WatchlistPostModel?
  ) {
    val delete = isWantToDelete && fav != null
    val addToWatchlist = !isWantToDelete && wtc != null
    if (delete || addToWatchlist) {
      mSnackbar = Snackbar.make(
        requireActivity().findViewById(bottom_navigation),
        HtmlCompat.fromHtml(
          "<b>$title</b> " +
            if (delete) getString(deleted_from_favorite) else getString(added_to_watchlist),
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
      }.setAnchorView(requireActivity().findViewById(bottom_navigation))
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
        viewModelFav.updateToRemoveFromFavoriteDB(fav)
      } else {
        viewModelFav.delFromFavoriteDB(fav)
      }
      showSnackBarUndoGuest(fav.title, pos)
    } else { // add to watchlist action
      if (fav.isWatchlist) {
        mSnackbar =
          snackBarAlreadyWatchlist(
            requireContext(),
            requireActivity().findViewById(bottom_navigation),
            requireActivity().findViewById(bottom_navigation),
            Event(fav.title)
          )
      } else {
        viewModelFav.updateToWatchlistDB(fav)
        showSnackBarUndoGuest(fav.title, pos)
      }
    }
  }

  private fun setDataGuestUserProgressBarEmptyView() {
    binding.rvFavTv.adapter = adapterDB
    viewModelFav.favoriteTvFromDB.observe(viewLifecycleOwner) {
      adapterDB.setFavorite(it)
      if (it.isNotEmpty()) {
        binding.rvFavTv.visibility = View.VISIBLE
        binding.illustrationNoDataView.containerNoData.visibility = View.GONE
      } else {
        binding.rvFavTv.visibility = View.GONE
        binding.illustrationNoDataView.containerNoData.visibility = View.VISIBLE
      }
      binding.progressBar.visibility = View.GONE
    }
  }

  private fun showSnackBarUndoGuest(title: String, pos: Int) {
    mSnackbar = Snackbar.make(
      requireActivity().findViewById(bottom_navigation),
      HtmlCompat.fromHtml(
        "<b>$title</b> " +
          if (isWantToDelete) getString(deleted_from_favorite) else getString(added_to_watchlist),
        HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Snackbar.LENGTH_LONG
    ).setAction(getString(undo)) {
      insertDBObserver()
      val fav = viewModelFav.undoDB.value?.getContentIfNotHandled() as Favorite
      if (isWantToDelete) { // undo remove from favorite
        if (fav.isWatchlist) {
          viewModelFav.updateToFavoriteDB(fav)
        } else {
          viewModelFav.insertToDB(fav.copy(isFavorite = true))
        }
        binding.rvFavTv.scrollToPosition(pos)
      } else { // undo add to watchlist
        viewModelFav.updateToRemoveFromWatchlistDB(fav)
      }
    }.setAnchorView(requireActivity().findViewById(bottom_navigation))
      .setActionTextColor(ContextCompat.getColor(requireContext(), yellow))
    mSnackbar?.show()
  }

  private fun insertDBObserver() {
    viewModelFav.dbResult.observe(viewLifecycleOwner) { eventResult ->
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
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      if (user.token != NAN) {
        baseViewModel.resetSnackbarShown()
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

  override fun openDetails(resultItem: ResultItem) {
    val intent = Intent(requireContext(), DetailMovieActivity::class.java)
    intent.putExtra(EXTRA_MOVIE, resultItem)
    val options =
      ActivityOptionsCompat.makeCustomAnimation(requireContext(), fade_in, fade_out)
    ActivityCompat.startActivity(requireContext(), intent, options.toBundle())
  }
}
