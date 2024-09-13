package com.waffiq.bazz_movies.ui.activity.myfavorite

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.color.red_matte
import com.waffiq.bazz_movies.R.color.yellow
import com.waffiq.bazz_movies.R.drawable.ic_bookmark_dark
import com.waffiq.bazz_movies.R.drawable.ic_trash
import com.waffiq.bazz_movies.R.id.nav_view
import com.waffiq.bazz_movies.R.string.added_to_watchlist
import com.waffiq.bazz_movies.R.string.binding_error
import com.waffiq.bazz_movies.R.string.deleted_from_favorite
import com.waffiq.bazz_movies.R.string.undo
import com.waffiq.bazz_movies.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.databinding.FragmentMyFavoriteTvSeriesBinding
import com.waffiq.bazz_movies.domain.model.Favorite
import com.waffiq.bazz_movies.ui.adapter.FavoriteAdapterDB
import com.waffiq.bazz_movies.ui.adapter.FavoriteTvAdapter
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Helper.showToastShort
import com.waffiq.bazz_movies.utils.common.Constants.SWIPE_THRESHOLD
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.helpers.FavWatchlistHelper.snackBarAlreadyFavorite
import com.waffiq.bazz_movies.utils.helpers.FavWatchlistHelper.snackBarAlreadyWatchlist
import com.waffiq.bazz_movies.utils.helpers.FavWatchlistHelper.titleHandler
import com.waffiq.bazz_movies.utils.helpers.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.pagingErrorState
import com.waffiq.bazz_movies.utils.helpers.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.utils.result_state.DbResult

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class MyFavoriteTvSeriesFragment : Fragment() {

  private var _binding: FragmentMyFavoriteTvSeriesBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val adapterPaging = FavoriteTvAdapter()
  private val adapterDB = FavoriteAdapterDB()

  private lateinit var viewModelFav: MyFavoriteViewModel
  private lateinit var userPreferenceViewModel: UserPreferenceViewModel

  private var mSnackbar: Snackbar? = null

  // helper
  private var isWantToDelete = false
  private var isUndo = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val pref = requireContext().dataStore
    val factoryAuth = ViewModelUserFactory.getInstance(pref)
    userPreferenceViewModel =
      ViewModelProvider(this, factoryAuth)[UserPreferenceViewModel::class.java]

    val factory = ViewModelFactory.getInstance(requireContext())
    viewModelFav = ViewModelProvider(this, factory)[MyFavoriteViewModel::class.java]
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
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
    //setup recyclerview
    binding.rvFavTv.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    binding.rvFavTv.itemAnimator = DefaultItemAnimator()

    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      if (user.token != "NaN") { // user login then show data from TMDB
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
    val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {

      override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
      ): Int {
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(0, swipeFlags)
      }

      override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
      ): Boolean {
        return false
      }

      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (isLogin) {
          isUndo = false
          val fav = (viewHolder as FavoriteTvAdapter.ViewHolder).data
          val position = viewHolder.absoluteAdapterPosition

          // swipe action
          if (direction == ItemTouchHelper.START) { // swipe left, action add to watchlist
            isWantToDelete = false
            postToAddWatchlistTMDB(titleHandler(fav), fav.id)
          } else { // swipe right, action to delete
            isWantToDelete = true
            postToRemoveFavTMDB(titleHandler(fav), fav.id)
          }
          adapterPaging.notifyItemChanged(position)
        } else {
          val fav = (viewHolder as FavoriteAdapterDB.ViewHolder).data
          val position = viewHolder.bindingAdapterPosition

          // swipe action
          if (direction == ItemTouchHelper.START) { // swipe left, action add to watchlist
            isWantToDelete = false
            performSwipeGuestUser(false, fav, position)
            adapterDB.notifyItemChanged(position)
          } else { // swipe right, action delete
            isWantToDelete = true
            performSwipeGuestUser(true, fav, position)
          }
        }
      }

      override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
      ) {
        val background = ColorDrawable()
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        if (dX > 0) { // swipe right to delete item
          val deleteIcon =
            ContextCompat.getDrawable(requireContext(), ic_trash) ?: error("No icon Found")
          val intrinsicWidth = deleteIcon.intrinsicWidth
          val intrinsicHeight = deleteIcon.intrinsicHeight

          background.color = ContextCompat.getColor(requireContext(), red_matte)
          background.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
          background.draw(c)

          val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
          val deleteIconLeft = itemView.left + (itemHeight - intrinsicHeight) / 2
          val deleteIconRight = deleteIconLeft + intrinsicWidth
          val deleteIconBottom = deleteIconTop + intrinsicHeight

          deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
          deleteIcon.draw(c)
        } else if (dX < 0) { // swipe left to add to watchlist
          val watchlistIcon =
            ContextCompat.getDrawable(requireContext(), ic_bookmark_dark) ?: error("No Icon Found")
          val intrinsicWidth = watchlistIcon.intrinsicWidth
          val intrinsicHeight = watchlistIcon.intrinsicHeight

          background.color = ContextCompat.getColor(requireContext(), yellow)
          background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
          )
          background.draw(c)

          val watchlistIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
          val watchlistIconLeft =
            itemView.right - (itemHeight - intrinsicHeight) / 2 - intrinsicWidth
          val watchlistIconRight = watchlistIconLeft + intrinsicWidth
          val watchlistIconBottom = watchlistIconTop + intrinsicHeight

          watchlistIcon.setBounds(
            watchlistIconLeft,
            watchlistIconTop,
            watchlistIconRight,
            watchlistIconBottom
          )
          watchlistIcon.draw(c)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
      }

      override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return SWIPE_THRESHOLD
      }
    })

    itemTouchHelper.attachToRecyclerView(binding.rvFavTv)
  }

  private fun setupRefresh(isLogin: Boolean) {
    binding.swipeRefresh.setOnRefreshListener {
      if (isLogin) {
        viewModelFav.clearSnackBar()
        adapterPagingRefresh()
      } else adapterDB.notifyDataSetChanged()

      binding.swipeRefresh.isRefreshing = false
    }
  }

  // region LOG-IN USER
  private fun setDataUserLoginProgressBarEmptyView(userToken: String) {
    viewModelFav.snackBarAlready.observe(viewLifecycleOwner) {
      mSnackbar =
        snackBarAlreadyWatchlist(
          requireContext(),
          requireActivity().findViewById(nav_view),
          requireActivity().findViewById(nav_view),
          it
        )
    }
    viewModelFav.snackBarAdded.observe(viewLifecycleOwner) { event ->
      event.getContentIfNotHandled()?.let {
        if (!isUndo) {
          if (it.isSuccess && isWantToDelete) {// remove item success
            showSnackBarUserLogin(it.title, it.favoritePostModel, it.watchlistPostModel)
            adapterPagingRefresh()
          } else if (!it.isSuccess)  // an error happen
            mSnackbar = snackBarWarning(
              requireContext(),
              requireActivity().findViewById(nav_view),
              requireActivity().findViewById(nav_view),
              Event(it.title)
            )
          else // add to watchlist success
            showSnackBarUserLogin(it.title, it.favoritePostModel, it.watchlistPostModel)
        } else if (it.isSuccess) adapterPagingRefresh() // refresh when undo remove item triggered
      }
    }
    adapterPaging.addLoadStateListener { loadState ->
      pagingErrorState(loadState)?.let {
        if (viewModelFav.isSnackbarShown.value == false) {
          mSnackbar = snackBarWarning(
            requireContext(),
            requireActivity().findViewById(nav_view),
            requireActivity().findViewById(nav_view),
            Event(pagingErrorHandling(it.error))
          )
          viewModelFav.markSnackbarShown()
        }
      }

      // show/hide view
      if (loadState.source.refresh is LoadState.NotLoading
        && loadState.append.endOfPaginationReached
        && adapterPaging.itemCount < 1
      ) { // show empty view
        binding.illustrationNoDataView.containerNoData.isVisible = true
      } else { // hide empty view
        binding.illustrationNoDataView.containerNoData.visibility = View.GONE
      }

      binding.progressBar.isVisible =
        loadState.source.refresh is LoadState.Loading // show progressbar
    }
    binding.rvFavTv.adapter = adapterPaging.withLoadStateFooter(
      footer = LoadingStateAdapter { adapterPaging.retry() }
    )

    collectAndSubmitData(this, { viewModelFav.favoriteTvSeries(userToken) }, adapterPaging)
  }

  private fun postToRemoveFavTMDB(title: String, tvId: Int) {
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      viewModelFav.postFavorite(
        user.token, user.userId,
        FavoritePostModel(
          mediaType = "tv",
          mediaId = tvId,
          favorite = false
        ), title
      )
    }
  }

  private fun postToAddWatchlistTMDB(title: String, tvId: Int) {
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      viewModelFav.checkStatedThenPostWatchlist("tv", user, tvId, title)
    }
  }

  private fun showSnackBarUserLogin(
    title: String,
    fav: FavoritePostModel?,
    wtc: WatchlistPostModel?
  ) {
    if (isWantToDelete && fav != null || !isWantToDelete && wtc != null) {
      mSnackbar = Snackbar.make(
        requireActivity().findViewById(nav_view),
        HtmlCompat.fromHtml(
          "<b>$title</b> " +
            if (isWantToDelete && fav != null) getString(deleted_from_favorite)
            else if (!isWantToDelete && wtc != null) getString(added_to_watchlist) else {
            },
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
      }.setAnchorView(requireActivity().findViewById(nav_view))
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
      if (fav.isWatchlist) viewModelFav.updateToRemoveFromFavoriteDB(fav)
      else viewModelFav.delFromFavoriteDB(fav)
      showSnackBarUndoGuest(fav.title, pos)
    } else { // add to watchlist action
      if (fav.isWatchlist) {
        mSnackbar =
          snackBarAlreadyFavorite(
            requireContext(),
            requireActivity().findViewById(nav_view),
            requireActivity().findViewById(nav_view),
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
      requireActivity().findViewById(nav_view),
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
        if (fav.isWatchlist) viewModelFav.updateToFavoriteDB(fav)
        else viewModelFav.insertToDB(fav.copy(isFavorite = true))
        binding.rvFavTv.scrollToPosition(pos)
      } else { // undo add to watchlist
        viewModelFav.updateToRemoveFromWatchlistDB(fav)
      }
    }.setAnchorView(requireActivity().findViewById(nav_view))
      .setActionTextColor(ContextCompat.getColor(requireContext(), yellow))
    mSnackbar?.show()
  }

  private fun insertDBObserver() {
    viewModelFav.dbResult.observe(viewLifecycleOwner) { eventResult ->
      eventResult.getContentIfNotHandled().let {
        when (it) {
          is DbResult.Error -> showToastShort(requireContext(), it.errorMessage)
          else -> {}
        }
      }
    }
  }
  // endregion GUEST USER

  override fun onResume() {
    super.onResume()
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      if (user.token != "NaN") {
        viewModelFav.clearSnackBar()
        adapterPaging.refresh()
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
    mSnackbar?.dismiss()
  }
}