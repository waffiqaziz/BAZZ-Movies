package com.waffiq.bazz_movies.ui.activity.mywatchlist

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
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
import com.waffiq.bazz_movies.R.string.already_favorite
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
import com.waffiq.bazz_movies.utils.Helper.combinedLoadStatesHandle2
import com.waffiq.bazz_movies.utils.Helper.showToastShort
import com.waffiq.bazz_movies.utils.LocalResult
import com.waffiq.bazz_movies.utils.common.Event

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
      private val clearPaint =
        Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

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
          val fav = (viewHolder as FavoriteTvAdapter.ViewHolder).data
          val position = viewHolder.absoluteAdapterPosition

          // swipe action
          if (direction == ItemTouchHelper.START) { // swipe left, action add to favorite
            isWantToDelete = false
            if (fav.id != null && fav.name != null) {
              postToAddFavoriteTMDB(fav.name, fav.id, position)
              adapterPaging.notifyItemChanged(position)
            }
          } else { // swipe right, action to delete
            isWantToDelete = true
            if (fav.id != null && fav.name != null) {
              postToRemoveWatchlistTMDB(fav.name, fav.id, position)
              adapterPaging.notifyItemRemoved(position)
            }
          }
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
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
          clearCanvas(
            c,
            itemView.right + dX,
            itemView.top.toFloat(),
            itemView.right.toFloat(),
            itemView.bottom.toFloat()
          )
          super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
          return
        }

        if (dX > 0) { // swipe left to delete item
          val editIcon =
            ContextCompat.getDrawable(requireContext(), ic_trash) ?: error("No icon Found")
          val intrinsicWidth = editIcon.intrinsicWidth
          val intrinsicHeight = editIcon.intrinsicHeight

          // draw the red background
          background.color = ContextCompat.getColor(requireContext(), red_matte)
          background.setBounds(itemView.left, itemView.top, dX.toInt() + 10, itemView.bottom)
          background.draw(c)

          // calculate position of delete icon
          val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
          val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
          val deleteIconLeft = itemView.left + deleteIconMargin
          val deleteIconRight = deleteIconLeft + intrinsicWidth
          val deleteIconBottom = deleteIconTop + intrinsicHeight

          // Draw the delete icon
          editIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
          editIcon.draw(c)
        } else {  // swipe right to add to watchlist
          val watchlistIcon =
            ContextCompat.getDrawable(requireContext(), ic_hearth_dark) ?: error("No Icon Found")
          val intrinsicWidth = watchlistIcon.intrinsicWidth
          val intrinsicHeight = watchlistIcon.intrinsicHeight

          // draw the delete background
          background.color = ContextCompat.getColor(requireContext(), yellow)
          background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
          )
          background.draw(c)

          // calculate position of delete icon
          val watchlistIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
          val watchlistIconMargin = (itemHeight - intrinsicHeight) / 2
          val watchlistIconLeft = itemView.right - watchlistIconMargin - intrinsicWidth
          val watchlistIconRight = watchlistIconLeft + intrinsicWidth
          val watchlistIconBottom = watchlistIconTop + intrinsicHeight

          // draw the delete icon
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

      private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
      }
    })

    itemTouchHelper.attachToRecyclerView(binding.rvWatchlistTv)
  }

  private fun setupRefresh(isLogin: Boolean) {
    binding.swipeRefresh.setOnRefreshListener {
      if (isLogin) adapterPaging.refresh()
      else {
        adapterDB.notifyDataSetChanged()
      }

      binding.swipeRefresh.isRefreshing = false
    }
  }

  private fun performSwipeGuestUser(isWantToDelete: Boolean, fav: Favorite, pos: Int) {
    if (isWantToDelete) {
      if (fav.isFavorite) viewModel.updateToRemoveFromWatchlistDB(fav)
      else viewModel.delFromFavoriteDB(fav)
      showSnackBarUndoGuest(fav.title, pos)
    } else { // add to favorite action
      if (fav.isFavorite) showSnackBarAlready(Event(fav.title))
      else {
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
        binding.illustrationNoDataView.containerNoData.visibility = View.INVISIBLE
      } else {
        binding.rvWatchlistTv.visibility = View.INVISIBLE
        binding.illustrationNoDataView.containerNoData.visibility = View.VISIBLE
      }
      binding.progressBar.visibility = View.INVISIBLE
    }
  }

  private fun setDataUserLoginProgressBarEmptyView(userToken: String) {
    viewModel.snackBarAlready.observe(viewLifecycleOwner) { showSnackBarAlready(it) }
    viewModel.snackBarAdded.observe(viewLifecycleOwner) { event ->
      event.getContentIfNotHandled()?.let {
        showSnackBarUserLogin(it.title, it.favoritePostModel, it.watchlistPostModel, it.position)
      }
    }
    adapterPaging.addLoadStateListener {
      // error handle
      combinedLoadStatesHandle2(it)?.let { errorMessage -> showSnackBarWarning(Event(errorMessage)) }

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

  private fun postToRemoveWatchlistTMDB(title: String, movieId: Int, position: Int) {
    val watchlistPostModelMode = WatchlistPostModel(
      mediaType = "tv",
      mediaId = movieId,
      watchlist = false
    )

    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      viewModel.postWatchlist(user, watchlistPostModelMode, title, position)
    }
    showSnackBarUserLogin(title, null, watchlistPostModelMode, position)
  }

  private fun postToAddFavoriteTMDB(title: String, tvId: Int, position: Int) {
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      viewModel.getStatedTv(user.token, tvId, title)
    }
    viewModel.stated.observe(viewLifecycleOwner) {
      if (it != null) {
        if (!it.favorite) {
          val favoritePostModelMode = FavoritePostModel(
            mediaType = "tv",
            mediaId = tvId,
            favorite = true
          )
          userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
            viewModel.postFavorite(user, favoritePostModelMode, title, position)
          }
        } else {
          /* handled by snackbarAlready.observe */
        }
      }
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
    viewModel.localResult.observe(viewLifecycleOwner) {
      it.getContentIfNotHandled()?.let { result ->
        when (result) {
          is LocalResult.Error -> showToastShort(requireContext(), result.message)
          else -> {}
        }
      }
    }
  }

  private fun showSnackBarUserLogin(
    title: String,
    fav: FavoritePostModel?,
    wtc: WatchlistPostModel?,
    pos: Int
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
        if (wtc != null) { // undo remove from watchlist
          userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
            viewModel.postWatchlist(user, wtc.copy(watchlist = true), title, pos)
          }
          isWantToDelete = !isWantToDelete // to prevent show same snackbar when undo is triggered
          adapterPaging.notifyItemInserted(pos)
          binding.rvWatchlistTv.scrollToPosition(pos)
        } else if (fav != null) { // undo add to favorite
          userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
            viewModel.postFavorite(user, fav.copy(favorite = false), title, pos)
          }
        }
      }.setAnchorView(binding.guideSnackbar)
      mSnackbar?.show()
    }
  }

  private fun showSnackBarAlready(eventMessage: Event<String>) {
    val result = eventMessage.getContentIfNotHandled() ?: return
    mSnackbar = Snackbar.make(
      binding.root,
      HtmlCompat.fromHtml(
        "<b>${result}</b> " + getString(already_favorite), HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Snackbar.LENGTH_SHORT
    ).setAnchorView(binding.guideSnackbar)
    mSnackbar?.show()
  }

  private fun showSnackBarWarning(eventMessage: Event<String>) {
    val message = eventMessage.getContentIfNotHandled() ?: return
    mSnackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
      .setAnchorView(binding.guideSnackbar)

    val snackbarView = mSnackbar?.view
    snackbarView?.setBackgroundColor(ContextCompat.getColor(requireContext(), red_matte))
    if (message.isNotEmpty()) mSnackbar?.show()
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