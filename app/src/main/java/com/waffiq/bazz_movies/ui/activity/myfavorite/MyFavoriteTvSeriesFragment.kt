package com.waffiq.bazz_movies.ui.activity.myfavorite

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
import com.waffiq.bazz_movies.R.drawable.ic_bookmark_dark
import com.waffiq.bazz_movies.R.drawable.ic_trash
import com.waffiq.bazz_movies.R.string.added_to_watchlist
import com.waffiq.bazz_movies.R.string.already_watchlist
import com.waffiq.bazz_movies.R.string.binding_error
import com.waffiq.bazz_movies.R.string.deleted_from_favorite
import com.waffiq.bazz_movies.R.string.undo
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.remote.Favorite
import com.waffiq.bazz_movies.data.remote.Watchlist
import com.waffiq.bazz_movies.databinding.FragmentMyFavoriteTvSeriesBinding
import com.waffiq.bazz_movies.ui.adapter.FavoriteAdapterDB
import com.waffiq.bazz_movies.ui.adapter.FavoriteTvAdapter
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.Helper.combinedLoadStatesHandle2
import com.waffiq.bazz_movies.utils.Helper.showToastShort
import com.waffiq.bazz_movies.utils.LocalResult

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class MyFavoriteTvSeriesFragment : Fragment() {

  private var _binding: FragmentMyFavoriteTvSeriesBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val adapterPaging = FavoriteTvAdapter()
  private val adapterDB = FavoriteAdapterDB()

  private lateinit var viewModelFav: MyFavoriteViewModel
  private lateinit var viewModelAuth: AuthenticationViewModel

  private var mSnackbar: Snackbar? = null

  // helper
  private var isWantToDelete = false

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMyFavoriteTvSeriesBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val pref = requireContext().dataStore
    val factoryAuth = ViewModelUserFactory.getInstance(pref)
    this.viewModelAuth =
      ViewModelProvider(this, factoryAuth)[AuthenticationViewModel::class.java]

    val factory = ViewModelFactory.getInstance(requireContext())
    viewModelFav =
      ViewModelProvider(this, factory)[MyFavoriteViewModel::class.java]

    checkUser()
    return root
  }

  private fun checkUser() {
    //setup recyclerview
    binding.rvFavTv.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    viewModelAuth.getUserPref().observe(viewLifecycleOwner) { user ->
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
          if (direction == ItemTouchHelper.START) { // swipe left, action add to watchlist
            isWantToDelete = false
            if (fav.id != null && fav.name != null){
              postToAddWatchlistTMDB(fav.name, fav.id, position)
              adapterPaging.notifyItemChanged(position)
            }
          } else { // swipe right, action to delete
            isWantToDelete = true
            if (fav.id != null && fav.name != null){
              postToRemoveFavTMDB(fav.name, fav.id, position)
              adapterPaging.notifyItemRemoved(position)
            }
          }
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
            ContextCompat.getDrawable(requireContext(), ic_bookmark_dark) ?: error("No Icon Found")
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

    itemTouchHelper.attachToRecyclerView(binding.rvFavTv)
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

  private fun performSwipeGuestUser(isWantToDelete: Boolean, fav: FavoriteDB, pos: Int) {
    if (fav.isWatchlist != null) {
      if (isWantToDelete) { // delete from favorite
        if (fav.isWatchlist) viewModelFav.updateToRemoveFromFavoriteDB(fav)
        else viewModelFav.delFromFavoriteDB(fav)
        fav.title?.let { showSnackBarUndoGuest(it, pos) }
      } else { // add to watchlist action
        if (fav.isWatchlist) showSnackBarAlready(Event(fav.title.toString()))
        else {
          viewModelFav.updateToWatchlistDB(fav)
          fav.title?.let { showSnackBarUndoGuest(it, pos) }
        }
      }
    }
  }

  private fun setDataGuestUserProgressBarEmptyView() {
    binding.rvFavTv.adapter = adapterDB
    viewModelFav.favoriteTvFromDB.observe(viewLifecycleOwner) {
      adapterDB.setFavorite(it)
      if (it.isNotEmpty()) {
        binding.rvFavTv.visibility = View.VISIBLE
        binding.illustrationNoDataView.containerNoData.visibility = View.INVISIBLE
      } else {
        binding.rvFavTv.visibility = View.INVISIBLE
        binding.illustrationNoDataView.containerNoData.visibility = View.VISIBLE
      }
      binding.progressBar.visibility = View.INVISIBLE
    }
  }

  private fun setDataUserLoginProgressBarEmptyView(userToken: String) {
    viewModelFav.snackBarAlready.observe(viewLifecycleOwner) { showSnackBarAlready(it) }
    viewModelFav.snackBarAdded.observe(viewLifecycleOwner) { event ->
      event.getContentIfNotHandled()?.let {
        showSnackBarUserLogin(it.title, it.favorite, it.watchlist, it.position)
      }
    }

    adapterPaging.addLoadStateListener {
      // error handle
      combinedLoadStatesHandle2(it)?.let { errorMessage -> showSnackBarWarning(Event(errorMessage)) }

      // show/hide view
      if (it.source.refresh is LoadState.NotLoading
        && it.append.endOfPaginationReached
        && adapterPaging.itemCount < 1
      ) { // show empty view
        binding.illustrationNoDataView.containerNoData.isVisible = true
      } else { // hide empty view
        binding.illustrationNoDataView.containerNoData.visibility = View.INVISIBLE
        binding.rvFavTv.visibility = View.VISIBLE
      }

      binding.progressBar.isVisible =
        it.source.refresh is LoadState.Loading // show progressbar
    }
    binding.rvFavTv.adapter = adapterPaging.withLoadStateFooter(
      footer = LoadingStateAdapter { adapterPaging.retry() }
    )

    viewModelFav.getFavoriteTvSeries(userToken)
      .observe(viewLifecycleOwner) {
        adapterPaging.submitData(lifecycle, it)
      }
  }

  private fun postToRemoveFavTMDB(title: String, tvId: Int, pos: Int) {
    val favoriteMode = Favorite(
      mediaType = "tv",
      mediaId = tvId,
      favorite = false
    )

    viewModelAuth.getUserPref().observe(viewLifecycleOwner) { user ->
      viewModelFav.postFavorite(user, favoriteMode, title, pos)
    }
    showSnackBarUserLogin(title, favoriteMode, null, pos)
  }

  private fun postToAddWatchlistTMDB(title: String, tvId: Int, position: Int) {
    viewModelAuth.getUserPref().observe(viewLifecycleOwner) { user ->
      viewModelFav.getStatedTv(user.token, tvId, title)
    }
    viewModelFav.stated.observe(viewLifecycleOwner) {
      if (it != null) {
        if (!it.watchlist) {
          val watchlistMode = Watchlist(
            mediaType = "tv",
            mediaId = tvId,
            watchlist = true
          )
          viewModelAuth.getUserPref().observe(viewLifecycleOwner) { user ->
            viewModelFav.postWatchlist(user, watchlistMode, title, position)
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
          if (isWantToDelete) getString(deleted_from_favorite) else getString(added_to_watchlist),
        HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Snackbar.LENGTH_LONG
    ).setAction(getString(undo)) {
      insertDBObserver()
      val fav = viewModelFav.undoDB.value?.getContentIfNotHandled() as FavoriteDB
      if (fav.isWatchlist != null) {
        if (isWantToDelete) { // undo remove from favorite
          if (fav.isWatchlist) viewModelFav.updateToFavoriteDB(fav)
          else viewModelFav.insertToDB(fav.copy(isFavorite = true))
          binding.rvFavTv.scrollToPosition(pos)
        } else { // undo add to watchlist
          viewModelFav.updateToRemoveFromWatchlistDB(fav)
        }
      }
    }.setAnchorView(binding.guideSnackbar)
    mSnackbar?.show()
  }

  private fun insertDBObserver() {
    viewModelFav.localResult.observe(viewLifecycleOwner) {
      it.getContentIfNotHandled()?.let { result ->
        when (result) {
          is LocalResult.Error -> showToastShort(requireActivity(), result.message)
          else -> {}
        }
      }
    }
  }

  private fun showSnackBarUserLogin(title: String, fav: Favorite?, wtc: Watchlist?, pos: Int) {
    if (isWantToDelete && fav != null || !isWantToDelete && wtc != null) {
      mSnackbar = Snackbar.make(
        binding.root,
        HtmlCompat.fromHtml(
          "<b>$title</b> " +
            if (isWantToDelete && fav != null) getString(deleted_from_favorite)
            else if (!isWantToDelete && wtc != null) getString(added_to_watchlist) else {
            },
          HtmlCompat.FROM_HTML_MODE_LEGACY
        ),
        Snackbar.LENGTH_LONG
      ).setAction(getString(undo)) {
        if (fav != null) { // undo remove from favorite
          viewModelAuth.getUserPref().observe(viewLifecycleOwner) { user ->
            viewModelFav.postFavorite(user, fav.copy(favorite = true), title, pos)
          }
          isWantToDelete = !isWantToDelete // to prevent show same snackbar when undo is triggered
          adapterPaging.notifyItemInserted(pos)
          binding.rvFavTv.scrollToPosition(pos)
        } else if (wtc != null) { // undo add to watchlist
          viewModelAuth.getUserPref().observe(viewLifecycleOwner) { user ->
            viewModelFav.postWatchlist(user, wtc.copy(watchlist = false), title, pos)
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
        "<b>${result}</b> " + getString(already_watchlist), HtmlCompat.FROM_HTML_MODE_LEGACY
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