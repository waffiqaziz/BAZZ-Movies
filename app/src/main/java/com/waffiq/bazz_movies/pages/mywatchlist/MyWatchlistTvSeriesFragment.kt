package com.waffiq.bazz_movies.pages.mywatchlist

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
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.drawable.ic_hearth_dark
import com.waffiq.bazz_movies.R.drawable.ic_trash
import com.waffiq.bazz_movies.R.id.bottom_navigation
import com.waffiq.bazz_movies.core.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.core.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.core.domain.model.Favorite
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.navigation.DetailNavigator
import com.waffiq.bazz_movies.core.ui.R.color.red_matte
import com.waffiq.bazz_movies.core.ui.R.color.yellow
import com.waffiq.bazz_movies.core.ui.R.string.added_to_favorite
import com.waffiq.bazz_movies.core.ui.R.string.binding_error
import com.waffiq.bazz_movies.core.ui.R.string.deleted_from_watchlist
import com.waffiq.bazz_movies.core.ui.R.string.undo
import com.waffiq.bazz_movies.core.ui.adapter.FavoriteAdapterDB
import com.waffiq.bazz_movies.core.ui.adapter.FavoriteTvAdapter
import com.waffiq.bazz_movies.core.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.core.utils.common.Constants.NAN
import com.waffiq.bazz_movies.core.utils.common.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.utils.common.Event
import com.waffiq.bazz_movies.core.utils.helpers.FavWatchlistHelper.handlePagingLoadState
import com.waffiq.bazz_movies.core.utils.helpers.FavWatchlistHelper.snackBarAlreadyFavorite
import com.waffiq.bazz_movies.core.utils.helpers.FavWatchlistHelper.titleHandler
import com.waffiq.bazz_movies.core.utils.helpers.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.initLinearLayoutManagerVertical
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.toastShort
import com.waffiq.bazz_movies.core.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.core.utils.helpers.uihelpers.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.core.utils.helpers.uihelpers.SwipeCallbackHelper
import com.waffiq.bazz_movies.core.utils.result.DbResult
import com.waffiq.bazz_movies.databinding.FragmentMyWatchlistTvSeriesBinding
import com.waffiq.bazz_movies.feature.detail.ui.DetailMovieActivity
import com.waffiq.bazz_movies.viewmodel.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyWatchlistTvSeriesFragment : Fragment(), DetailNavigator {

  private var _binding: FragmentMyWatchlistTvSeriesBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val adapterPaging = FavoriteTvAdapter(this)
  private val adapterDB = FavoriteAdapterDB(this)

  private val viewModel: MyWatchlistViewModel by viewModels()
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
    _binding = FragmentMyWatchlistTvSeriesBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    checkUser()
  }

  private fun checkUser() {
    // setup recyclerview
    binding.rvWatchlistTv.layoutManager = initLinearLayoutManagerVertical(requireContext())
    binding.rvWatchlistTv.itemAnimator = DefaultItemAnimator()

    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      if (user.token != NAN) { // user login then show favorite data from TMDB API
        initAction(isLogin = true)
        setupRefresh(true)
        setDataUserLoginProgressBarEmptyView(user.token)
      } else { // guest user then show favorite data from database
        initAction(isLogin = false)
        setupRefresh(false)
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
          val fav = (viewHolder as FavoriteTvAdapter.ViewHolder).data
          isWantToDelete = false
          postToAddFavoriteTMDB(titleHandler(fav), fav.id)
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
          postToRemoveWatchlistTMDB(titleHandler(fav), fav.id)
          adapterPaging.notifyItemChanged(position)
        } else {
          val fav = (viewHolder as FavoriteAdapterDB.ViewHolder).data
          isWantToDelete = true
          performSwipeGuestUser(true, fav, position)
        }
      },
      context = requireContext(),
      deleteIconResId = ic_trash,
      actionIconResId = ic_hearth_dark,
      deleteColor = red_matte,
      actionColor = yellow
    )

    val itemTouchHelper = ItemTouchHelper(swipeCallback)
    itemTouchHelper.attachToRecyclerView(binding.rvWatchlistTv)
  }

  private fun setupRefresh(isLogin: Boolean) {
    binding.swipeRefresh.setOnRefreshListener {
      if (isLogin) {
        baseViewModel.resetSnackbarShown()
        adapterPaging.refresh()
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
    handleSnackbarUserLogin()

    binding.rvWatchlistTv.adapter = adapterPaging.withLoadStateFooter(
      footer = LoadingStateAdapter { adapterPaging.retry() }
    )

    binding.illustrationError.btnTryAgain.setOnClickListener {
      baseViewModel.resetSnackbarShown()
      adapterPaging.refresh()
    }
    collectAndSubmitData(this, { viewModel.watchlistTvSeries(userToken) }, adapterPaging)
  }

  private fun handleSnackbarUserLogin() {
    viewModel.snackBarAlready.observe(viewLifecycleOwner) {
      mSnackbar =
        snackBarAlreadyFavorite(
          requireContext(),
          requireActivity().findViewById(bottom_navigation),
          requireActivity().findViewById(bottom_navigation),
          it
        )
    }

    viewModel.snackBarAdded.observe(viewLifecycleOwner) { event ->
      event.getContentIfNotHandled()?.let {
        if (!isUndo) {
          if (it.isSuccess && isWantToDelete) { // success to remove item
            showSnackBarUserLogin(it.title, it.favoritePostModel, it.watchlistPostModel)
            adapterPagingRefresh()
          } else if (!it.isSuccess) {
            mSnackbar = snackBarWarning(
              requireActivity().findViewById(bottom_navigation),
              requireActivity().findViewById(bottom_navigation),
              Event(it.title)
            )
          } else {
            // add to favorite success
            showSnackBarUserLogin(it.title, it.favoritePostModel, it.watchlistPostModel)
          }
        } else if (it.isSuccess) adapterPagingRefresh() // refresh when undo remove item triggered
      }
    }

    handlePagingLoadState(
      adapterPaging = adapterPaging,
      loadStateFlow = adapterPaging.loadStateFlow,
      recyclerView = binding.rvWatchlistTv,
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

  private fun postToRemoveWatchlistTMDB(title: String, tvId: Int) {
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      viewModel.postWatchlist(
        user.token,
        user.userId,
        WatchlistPostModel(
          mediaType = TV_MEDIA_TYPE,
          mediaId = tvId,
          watchlist = false
        ),
        title
      )
    }
  }

  private fun postToAddFavoriteTMDB(title: String, tvId: Int) {
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      viewModel.checkStatedThenPostFavorite(TV_MEDIA_TYPE, user, tvId, title)
    }
  }

  private fun showSnackBarUserLogin(
    title: String,
    fav: FavoritePostModel?,
    wtc: WatchlistPostModel?
  ) {
    val delete = isWantToDelete && wtc != null
    val addToFavorite = !isWantToDelete && fav != null
    if (delete || addToFavorite) {
      mSnackbar = Snackbar.make(
        requireActivity().findViewById(bottom_navigation),
        HtmlCompat.fromHtml(
          "<b>$title</b> " +
            if (delete) getString(deleted_from_watchlist) else getString(added_to_favorite),
          HtmlCompat.FROM_HTML_MODE_LEGACY
        ),
        Snackbar.LENGTH_LONG
      ).setAction(getString(undo)) {
        isUndo = true
        if (wtc != null) { // undo remove from watchlist
          userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
            viewModel.postWatchlist(user.token, user.userId, wtc.copy(watchlist = true), title)
          }
          isWantToDelete = !isWantToDelete // to prevent show same snackbar when undo is triggered
        } else if (fav != null) { // undo add to favorite
          userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
            viewModel.postFavorite(user.token, user.userId, fav.copy(favorite = false), title)
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
    if (isWantToDelete) {
      if (fav.isFavorite) {
        viewModel.updateToRemoveFromWatchlistDB(fav)
      } else {
        viewModel.delFromFavoriteDB(fav)
      }
      showSnackBarUndoGuest(fav.title, pos)
    } else { // add to favorite action
      if (fav.isFavorite) {
        mSnackbar =
          snackBarAlreadyFavorite(
            requireContext(),
            requireActivity().findViewById(bottom_navigation),
            requireActivity().findViewById(bottom_navigation),
            Event(fav.title)
          )
      } else {
        viewModel.updateToFavoriteDB(fav)
        showSnackBarUndoGuest(fav.title, pos)
      }
    }
  }

  private fun setDataGuestUserProgressBarEmptyView() {
    binding.rvWatchlistTv.adapter = adapterDB
    viewModel.watchlistTvSeriesDB.observe(viewLifecycleOwner) {
      adapterDB.setFavorite(it)
      if (it.isNotEmpty()) {
        binding.rvWatchlistTv.visibility = View.VISIBLE
        binding.illustrationNoDataView.containerNoData.visibility = View.GONE
      } else {
        binding.rvWatchlistTv.visibility = View.GONE
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
          if (isWantToDelete) getString(deleted_from_watchlist) else getString(added_to_favorite),
        HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Snackbar.LENGTH_LONG
    ).setAction(getString(undo)) {
      insertDBObserver()
      val fav = viewModel.undoDB.value?.getContentIfNotHandled() as Favorite
      if (isWantToDelete) { // undo remove from favorite
        if (fav.isFavorite) {
          viewModel.updateToWatchlistDB(fav)
        } else {
          viewModel.insertToDB(fav.copy(isWatchlist = true))
        }
        binding.rvWatchlistTv.scrollToPosition(pos)
      } else { // undo add to watchlist
        viewModel.updateToRemoveFromFavoriteDB(fav)
      }
    }.setAnchorView(requireActivity().findViewById(bottom_navigation))
      .setActionTextColor(ContextCompat.getColor(requireContext(), yellow))
    mSnackbar?.show()
  }

  private fun insertDBObserver() {
    viewModel.dbResult.observe(viewLifecycleOwner) { eventResult ->
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

  override fun openDetails(resultItem: ResultItem) {
    val intent = Intent(requireContext(), DetailMovieActivity::class.java)
    intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, resultItem)
    val options =
      ActivityOptionsCompat.makeCustomAnimation(requireContext(), fade_in, fade_out)
    ActivityCompat.startActivity(requireContext(), intent, options.toBundle())
  }
}
