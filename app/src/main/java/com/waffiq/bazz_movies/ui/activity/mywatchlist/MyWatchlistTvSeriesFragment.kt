package com.waffiq.bazz_movies.ui.activity.mywatchlist

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.color.red_matte
import com.waffiq.bazz_movies.R.color.yellow
import com.waffiq.bazz_movies.R.drawable.ic_hearth_dark
import com.waffiq.bazz_movies.R.drawable.ic_trash
import com.waffiq.bazz_movies.R.string.added_to_favorite
import com.waffiq.bazz_movies.R.string.binding_error
import com.waffiq.bazz_movies.R.string.deleted_from_watchlist
import com.waffiq.bazz_movies.R.string.undo
import com.waffiq.bazz_movies.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.databinding.FragmentMyWatchlistTvSeriesBinding
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
import com.waffiq.bazz_movies.utils.helpers.FavWatchlistHelper.snackBarWarning
import com.waffiq.bazz_movies.utils.helpers.FavWatchlistHelper.titleHandler
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.combinedLoadStatesHandle2
import com.waffiq.bazz_movies.utils.result_state.DbResult

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class MyWatchlistTvSeriesFragment : Fragment() {

  private var _binding: FragmentMyWatchlistTvSeriesBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val adapterPaging = FavoriteTvAdapter()
  private val adapterDB = FavoriteAdapterDB()

  private lateinit var viewModel: MyWatchlistViewModel
  private lateinit var userPreferenceViewModel: UserPreferenceViewModel

  private var mSnackbar: Snackbar? = null

  // helper
  private var isWantToDelete = false
  private var isUndo = false

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMyWatchlistTvSeriesBinding.inflate(inflater, container, false)
    val root = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    viewModel = ViewModelProvider(this.requireActivity(), factory)[MyWatchlistViewModel::class.java]

    val pref = requireContext().dataStore
    val factoryUser = ViewModelUserFactory.getInstance(pref)
    this.userPreferenceViewModel =
      ViewModelProvider(this.requireActivity(), factoryUser)[UserPreferenceViewModel::class.java]

    checkUser()
    return root
  }

  private fun checkUser() {
    // setup recyclerview
    binding.rvWatchlistTv.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      if (user.token != "NaN") { //user login then show favorite data from TMDB API
        initAction(isLogin = true)
        setupRefresh(true)
        setDataUserLoginProgressBarEmptyView(user.token)
      } else { //guest user then show favorite data from database
        initAction(isLogin = false)
        setupRefresh(false)
        setDataGuestUserProgressBarEmptyView()
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
          if (direction == ItemTouchHelper.START) { // swipe left, action add to favorite
            isWantToDelete = false
            postToAddFavoriteTMDB(titleHandler(fav), fav.id)
          } else { // swipe right, action to delete
            isWantToDelete = true
            postToRemoveWatchlistTMDB(titleHandler(fav), fav.id)
          }
          adapterPaging.notifyItemChanged(position)
        } else {
          val fav = (viewHolder as FavoriteAdapterDB.ViewHolder).data
          val position = viewHolder.bindingAdapterPosition

          // swipe action
          if (direction == ItemTouchHelper.START) { // swipe left, action add to favorite
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
        } else if (dX < 0) { // swipe left to add to favorite
          val favoriteIcon =
            ContextCompat.getDrawable(requireContext(), ic_hearth_dark) ?: error("No Icon Found")
          val intrinsicWidth = favoriteIcon.intrinsicWidth
          val intrinsicHeight = favoriteIcon.intrinsicHeight

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

          favoriteIcon.setBounds(
            watchlistIconLeft,
            watchlistIconTop,
            watchlistIconRight,
            watchlistIconBottom
          )
          favoriteIcon.draw(c)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
      }

      override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return SWIPE_THRESHOLD
      }
    })

    itemTouchHelper.attachToRecyclerView(binding.rvWatchlistTv)
  }

  private fun setupRefresh(isLogin: Boolean) {
    binding.swipeRefresh.setOnRefreshListener {
      if (isLogin) adapterPaging.refresh()
      else adapterDB.notifyDataSetChanged()

      binding.swipeRefresh.isRefreshing = false
    }
  }

  private fun performSwipeGuestUser(isWantToDelete: Boolean, fav: Favorite, pos: Int) {
    if (isWantToDelete) {
      if (fav.isFavorite) viewModel.updateToRemoveFromWatchlistDB(fav)
      else viewModel.delFromFavoriteDB(fav)
      showSnackBarUndoGuest(fav.title, pos)
    } else { // add to favorite action
      if (fav.isFavorite) {
        mSnackbar =
          snackBarAlreadyFavorite(
            requireActivity(),
            binding.root,
            binding.guideSnackbar,
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

  private fun setDataUserLoginProgressBarEmptyView(userToken: String) {
    viewModel.snackBarAlready.observe(viewLifecycleOwner) {
      mSnackbar =
        snackBarAlreadyFavorite(requireActivity(), binding.root, binding.guideSnackbar, it)
    }
    viewModel.snackBarAdded.observe(viewLifecycleOwner) { event ->
      event.getContentIfNotHandled()?.let {
        if (!isUndo) {
          if (it.isSuccess && isWantToDelete) {// success to remove item
            showSnackBarUserLogin(it.title, it.favoritePostModel, it.watchlistPostModel)
            adapterPagingRefresh()
          } else if (!it.isSuccess)
            mSnackbar = snackBarWarning(
              requireActivity(),
              binding.root,
              binding.guideSnackbar,
              Event(it.title)
            )
          else // add to favorite success
            showSnackBarUserLogin(it.title, it.favoritePostModel, it.watchlistPostModel)
        } else if (it.isSuccess) adapterPagingRefresh() // refresh when undo remove item triggered
      }

    }
    adapterPaging.addLoadStateListener {
      mSnackbar?.dismiss()
      mSnackbar = snackBarWarning(
        requireContext(),
        binding.root,
        binding.guideSnackbar,
        Event(combinedLoadStatesHandle2(it))
      )

      // show or hide view
      if (it.source.refresh is LoadState.NotLoading
        && it.append.endOfPaginationReached
        && adapterPaging.itemCount < 1
      ) { // show empty view
        binding.illustrationNoDataView.containerNoData.visibility = View.VISIBLE
      } else { // hide empty view
        binding.illustrationNoDataView.containerNoData.visibility = View.GONE
      }

      binding.progressBar.isVisible =
        it.source.refresh is LoadState.Loading // show progressbar
    }
    binding.rvWatchlistTv.adapter = adapterPaging.withLoadStateFooter(
      footer = LoadingStateAdapter {
        adapterPaging.retry()
      }
    )

    viewModel.watchlistTvSeries(userToken)
      .observe(viewLifecycleOwner) {
        adapterPaging.submitData(lifecycle, it)
      }
  }

  private fun postToRemoveWatchlistTMDB(title: String, movieId: Int) {
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      viewModel.postWatchlist(
        user.token, user.userId,
        WatchlistPostModel(
          mediaType = "tv",
          mediaId = movieId,
          watchlist = false
        ), title
      )
    }
  }

  private fun postToAddFavoriteTMDB(title: String, tvId: Int) {
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      viewModel.checkStatedThenPostFavorite("tv", user, tvId, title)
    }
  }

  private fun showSnackBarUndoGuest(title: String, pos: Int) {
    mSnackbar = Snackbar.make(
      binding.root,
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
        if (fav.isFavorite) viewModel.updateToWatchlistDB(fav)
        else viewModel.insertToDB(fav.copy(isWatchlist = true))
        binding.rvWatchlistTv.scrollToPosition(pos)
      } else { // undo add to watchlist
        viewModel.updateToRemoveFromFavoriteDB(fav)
      }
    }.setAnchorView(binding.guideSnackbar)
    mSnackbar?.show()
  }

  private fun insertDBObserver() {
    viewModel.dbResult.observe(viewLifecycleOwner) { eventResult ->
      eventResult.getContentIfNotHandled().let {
        when (it) {
          is DbResult.Error -> showToastShort(requireContext(), it.errorMessage)
          else -> {}
        }
      }
    }
  }

  private fun showSnackBarUserLogin(
    title: String,
    fav: FavoritePostModel?,
    wtc: WatchlistPostModel?
  ) {
    if (isWantToDelete && wtc != null || !isWantToDelete && fav != null) {
      mSnackbar = Snackbar.make(
        binding.root,
        HtmlCompat.fromHtml(
          "<b>$title</b> " +
            if (isWantToDelete && wtc != null) getString(deleted_from_watchlist)
            else if (!isWantToDelete && fav != null) getString(added_to_favorite) else {
            },
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
      }.setAnchorView(binding.guideSnackbar)
      mSnackbar?.show()
    }
  }

  private fun adapterPagingRefresh() {
    adapterPaging.retry()
    adapterPaging.refresh()
  }

  override fun onResume() {
    super.onResume()
    adapterPaging.refresh()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
    mSnackbar?.dismiss()
  }
}