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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.databinding.FragmentMyWatchlistTvSeriesBinding
import com.waffiq.bazz_movies.ui.adapter.FavoriteAdapterDB
import com.waffiq.bazz_movies.ui.adapter.FavoriteTvAdapter
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory
import com.waffiq.bazz_movies.R.color.red_matte
import com.waffiq.bazz_movies.R.color.yellow
import com.waffiq.bazz_movies.R.drawable.ic_hearth_dark
import com.waffiq.bazz_movies.R.drawable.ic_trash
import com.waffiq.bazz_movies.R.string.added_to_favorite
import com.waffiq.bazz_movies.R.string.already_favorite
import com.waffiq.bazz_movies.R.string.binding_error
import com.waffiq.bazz_movies.R.string.deleted_from_favorite
import com.waffiq.bazz_movies.R.string.undo
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.ui.adapter.FavoriteMovieAdapter
import com.waffiq.bazz_movies.utils.Helper
import com.waffiq.bazz_movies.utils.LocalDatabaseResult

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class MyWatchlistTvSeriesFragment : Fragment() {
  private var _binding: FragmentMyWatchlistTvSeriesBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val adapterPaging = FavoriteTvAdapter()
  private val adapterDB = FavoriteAdapterDB()

  private lateinit var viewModel: MyWatchlistViewModel
  private lateinit var viewModelAuth: AuthenticationViewModel

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
    viewModel = ViewModelProvider(this, factory)[MyWatchlistViewModel::class.java]

    val pref = requireContext().dataStore
    val factoryAuth = ViewModelUserFactory.getInstance(pref)
    this.viewModelAuth = ViewModelProvider(this, factoryAuth)[AuthenticationViewModel::class.java]

    checkUser()
    return root
  }

  private fun checkUser() {
    // setup recyclerview
    binding.rvWatchlistTv.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    viewModelAuth.getUser().observe(viewLifecycleOwner) { user ->
      if (user.token != "NaN") { //user login then show favorite data from TMDB API
        initAction(isLogin = true)
        setDataUserLoginProgressBarEmptyView(user.token)
      } else { //guest user then show favorite data from database
        initAction(isLogin = false)
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
          val fav = (viewHolder as FavoriteMovieAdapter.ViewHolder).data
          val position = viewHolder.absoluteAdapterPosition

          adapterPaging.notifyItemChanged(position)
          binding.rvWatchlistTv.adapter?.notifyItemChanged(position)
          // swipe action
          if (direction == ItemTouchHelper.START) { // swipe left, action add to favorite
            isWantToDelete = false
            fav.id?.let { mediaId ->
              (fav.title ?: fav.originalTitle)?.let { title ->
                postToAddFavoriteTMDB(mediaId, title)
              }
            }
          } else { // swipe right, action to delete
            isWantToDelete = true
            if (fav.id != null && fav.title != null)
              postToRemoveWatchlistTMDB(fav.title, fav.id, position)
            adapterPaging.notifyItemRemoved(position)
          }
        } else {
          val fav = (viewHolder as FavoriteAdapterDB.ViewHolder).data
          val position = viewHolder.bindingAdapterPosition

          // swipe action
          binding.rvWatchlistTv.adapter?.notifyItemChanged(position)
          if (direction == ItemTouchHelper.START) { // swipe left, action add to favorite
            isWantToDelete = false
            performSwipeGuestUser(false, fav)
          } else { // swipe right, action delete
            isWantToDelete = true
            performSwipeGuestUser(true, fav)
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

  private fun performSwipeGuestUser(isWantToDelete: Boolean, fav: FavoriteDB) {
    // check if item is in favorite or not
    if (fav.isFavorite != null) {
      if (isWantToDelete) {
        if (fav.isFavorite) viewModel.updateToRemoveFromWatchlistDB(fav)
        else viewModel.delFromFavoriteDB(fav)
        if (fav.title != null) showSnackBarUndoGuest(fav.title)
      } else { // add to favorite action
        if (fav.isFavorite) showSnackBarNoAction(
          "<b>${fav.title}</b> " + getString(already_favorite)
        )
        else {
          viewModel.updateToFavoriteDB(fav)
          showSnackBarNoAction("<b>${fav.title}</b> " + getString(added_to_favorite))
        }
      }
    }
  }

  private fun postToRemoveWatchlistTMDB(title: String, movieId: Int, position: Int) {
    val watchlistMode = Watchlist(
      mediaType = "tv",
      mediaId = movieId,
      watchlist = false
    )

    viewModelAuth.getUser().observe(viewLifecycleOwner) { user ->
      viewModel.postWatchlist(user, watchlistMode)
    }
    showSnackBarFavUserLogin(title, watchlistMode, position)
  }

  private fun postToAddFavoriteTMDB(mediaId: Int, mediaTitle: String) {
    val favoriteMode = Favorite(
      mediaType = "tv",
      mediaId = mediaId,
      favorite = true
    )

    viewModelAuth.getUser().observe(viewLifecycleOwner) { user ->
      viewModel.getStated(user.token, mediaId)
      viewModel.getStated().observe(viewLifecycleOwner) {
        if (it != null) {
          if (!it.favorite) {
            showSnackBarNoAction("<b>$mediaTitle</b> " + getString(added_to_favorite))
            viewModel.postFavorite(user, favoriteMode)
          } else showSnackBarNoAction("<b>$mediaTitle</b> " + getString(already_favorite))
        }
      }
    }
  }

  private fun showSnackBarUndoGuest(title: String) {
    mSnackbar = Snackbar.make(
      binding.root,
      HtmlCompat.fromHtml(
        "<b>$title</b> " + getString(deleted_from_favorite),
        HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Snackbar.LENGTH_LONG
    ).setAction(getString(undo)) {
      insertDBObserver()
      val fav = viewModel.undoDeleteDB().value?.getContentIfNotHandled() as FavoriteDB
      if (fav.isFavorite != null) {
        if (fav.isFavorite) { // movie is on favorite
          if (isWantToDelete) viewModel.updateToWatchlistDB(fav)
          else viewModel.updateToRemoveFromFavoriteDB(fav)
        } else { // movie is not on watchlist
          if (isWantToDelete) viewModel.insertToDB(fav.copy(isWatchlist = true))
          else viewModel.insertToDB(fav.copy(isFavorite = true))
        }
      }
    }.setAnchorView(binding.guideSnackbar)
    mSnackbar?.show()
  }

  private fun insertDBObserver() {
    viewModel.localDatabaseResult.observe(viewLifecycleOwner) {
      it.getContentIfNotHandled()?.let { result ->
        when (result) {
          is LocalDatabaseResult.Error -> Helper.showToastShort(requireContext(), result.message)
          else -> {}
        }
      }
    }
  }

  private fun showSnackBarFavUserLogin(title: String, fav: Watchlist, position: Int) {
    mSnackbar = Snackbar.make(
      binding.root,
      HtmlCompat.fromHtml(
        "<b>$title</b> " + getString(deleted_from_favorite),
        HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Snackbar.LENGTH_LONG
    ).setAction(getString(undo)) {
      viewModelAuth.getUser().observe(viewLifecycleOwner) { user ->
        viewModel.postWatchlist(user, fav.copy(watchlist = true))
      }
      adapterPaging.notifyItemInserted(position)
    }.setAnchorView(binding.guideSnackbar)
    mSnackbar?.show()
  }

  private fun showSnackBarNoAction(message: String) {
    mSnackbar = Snackbar.make(
      binding.root,
      HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_LEGACY),
      Snackbar.LENGTH_SHORT
    ).setAnchorView(binding.guideSnackbar)
    mSnackbar?.show()
  }

  private fun setDataGuestUserProgressBarEmptyView() {
    binding.rvWatchlistTv.adapter = adapterDB
    binding.rvWatchlistTv.addItemDecoration(
      DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
    )

    viewModel.getWatchlistTvSeriesDB.observe(viewLifecycleOwner) {
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
    binding.rvWatchlistTv.adapter = adapterPaging.withLoadStateFooter(
      footer = LoadingStateAdapter {
        adapterPaging.retry()
      }
    )

    // show or hide view
    adapterPaging.addLoadStateListener { loadState ->
      if (loadState.source.refresh is LoadState.NotLoading
        && loadState.append.endOfPaginationReached
        && adapterPaging.itemCount < 1
      ) {
        // show empty view
        binding.illustrationNoDataView.containerNoData.visibility = View.VISIBLE
      } else {
        //  hide empty view
        binding.illustrationNoDataView.containerNoData.visibility = View.GONE
      }

      binding.progressBar.isVisible =
        loadState.source.refresh is LoadState.Loading // show progressbar
    }

    viewModel.getWatchlistTvSeries(userToken)
      .observe(viewLifecycleOwner) {
        adapterPaging.submitData(lifecycle, it)
      }
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