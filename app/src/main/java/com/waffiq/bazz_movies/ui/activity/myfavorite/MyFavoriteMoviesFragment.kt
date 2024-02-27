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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.color.red_matte
import com.waffiq.bazz_movies.R.color.yellow
import com.waffiq.bazz_movies.R.drawable.ic_trash
import com.waffiq.bazz_movies.R.drawable.ic_bookmark_dark
import com.waffiq.bazz_movies.R.string.added_to_watchlist
import com.waffiq.bazz_movies.R.string.already_watchlist
import com.waffiq.bazz_movies.R.string.undo
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.databinding.FragmentMyFavoriteMoviesBinding
import com.waffiq.bazz_movies.ui.adapter.FavoriteAdapterDB
import com.waffiq.bazz_movies.ui.adapter.FavoriteMovieAdapter
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Event

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class MyFavoriteMoviesFragment : Fragment() {

  private var _binding: FragmentMyFavoriteMoviesBinding? = null
  private val binding get() = _binding!!

  private lateinit var favViewModelMovie: MyFavoriteViewModel
  private lateinit var viewModelAuth: AuthenticationViewModel

  private val adapterDB = FavoriteAdapterDB()
  private val adapterPaging = FavoriteMovieAdapter()

  private var mSnackbar: Snackbar? = null

  // helper
  private var positionIn = 0
  private var isWantToDelete = false

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMyFavoriteMoviesBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val pref = requireContext().dataStore
    val factoryAuth = ViewModelUserFactory.getInstance(pref)
    viewModelAuth = ViewModelProvider(this, factoryAuth)[AuthenticationViewModel::class.java]

    val factory = ViewModelFactory.getInstance(requireContext())
    favViewModelMovie = ViewModelProvider(this, factory)[MyFavoriteViewModel::class.java]

    checkUser()
    return root
  }

  private fun checkUser() {

    // setup recyclerview
    binding.rvFavMovies.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    viewModelAuth.getUser().observe(viewLifecycleOwner) { user ->
      if (user.token != "NaN") { //user login then show data from TMDb
        initActionUserLogin()
        setDataUserLoginProgressBarEmptyView(user.token)
      } else { //guest user then show data from database

        favViewModelMovie.getSnackBarTextInt()
          .observe(viewLifecycleOwner) { showSnackBarUndoGuest(it) }

        initActionGuest()
        setDataGuestUserProgressBarEmptyView()
      }
    }
  }

//  private fun setupSearchView(){
//    val menuHost: MenuHost = requireActivity()
//    menuHost.addMenuProvider(object : MenuProvider {
//      override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//        menuInflater.inflate(R.menu.search_menu, menu)
//
//        val item = menu.findItem(R.id.action_search)
//        val searchView = item?.actionView as SearchView
//
//        // search queryTextChange Listener
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//          override fun onQueryTextSubmit(query: String?): Boolean { //when user submit data
//
//            if (query != null) {
//              viewModel.searchFavorite(query).observe(viewLifecycleOwner){
//                adapter.setFavorite(it)
//              }
//            }
//            return true
//          }
//
//          override fun onQueryTextChange(query: String?): Boolean {
//            return true
//          }
//        })
//      }
//
//      override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//        return when (menuItem.itemId) {
//          R.id.action_search -> {
//            true
//          }
//          else -> false
//        }
//      }
//    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
//  }

  private fun initActionGuest() {
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
        val fav = (viewHolder as FavoriteAdapterDB.ViewHolder).data

        // swipe action
        val position = viewHolder.bindingAdapterPosition
        binding.rvFavMovies.adapter?.notifyItemChanged(position)
        if (direction == ItemTouchHelper.START) { // swipe left, action add to watchlist
          isWantToDelete = false
          checkIsWatchlistDB(false, fav)
        } else { // swipe right, action add delete
          isWantToDelete = true
          checkIsWatchlistDB(true, fav)
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
          val editIcon = ContextCompat.getDrawable(requireContext(), ic_trash)
          val intrinsicWidth = editIcon?.intrinsicWidth
          val intrinsicHeight = editIcon?.intrinsicHeight

          // draw the red background
          background.color = ContextCompat.getColor(requireContext(), red_matte)
          background.setBounds(itemView.left, itemView.top, dX.toInt() + 10, itemView.bottom)
          background.draw(c)

          // calculate position of delete icon
          val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
          val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
          val deleteIconLeft = itemView.left + deleteIconMargin
          val deleteIconRight = deleteIconLeft + intrinsicWidth!!
          val deleteIconBottom = deleteIconTop + intrinsicHeight

          // Draw the delete icon
          editIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
          editIcon.draw(c)
        } else {  // swipe right to add to watchlist
          val watchlistIcon =
            ContextCompat.getDrawable(requireContext(), ic_bookmark_dark)
          val intrinsicWidth = watchlistIcon?.intrinsicWidth
          val intrinsicHeight = watchlistIcon?.intrinsicHeight

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
          val watchlistIconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
          val watchlistIconMargin = (itemHeight - intrinsicHeight) / 2
          val watchlistIconLeft = itemView.right - watchlistIconMargin - intrinsicWidth!!
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

    itemTouchHelper.attachToRecyclerView(binding.rvFavMovies)
  }

  private fun checkIsWatchlistDB(isWantToDelete: Boolean, fav: FavoriteDB) {
    // check if item is in watchlist or not
    favViewModelMovie.isWatchlistDB(fav.mediaId!!)
    favViewModelMovie.isWatchlistDB().observe(viewLifecycleOwner) {
      if (isWantToDelete) {
        if (it) favViewModelMovie.updateToRemoveFromFavoriteDB(fav)
        else favViewModelMovie.delFromFavoriteDB(fav)
      } else { // add to watchlist action
        if (it) {
          showSnackBarNoAction("<b>${fav.title}</b> " + getString(already_watchlist))
        } else {
          favViewModelMovie.updateToWatchlistDB(fav)
          showSnackBarNoAction("<b>${fav.title}</b> " + getString(added_to_watchlist))
        }
      }
    }
  }

  private fun initActionUserLogin() {
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
        val colorDrawable = ColorDrawable()
        colorDrawable.color = ContextCompat.getColor(requireActivity(), red_matte)
        viewHolder.itemView.background = colorDrawable
        return false
      }

      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val fav = (viewHolder as FavoriteMovieAdapter.ViewHolder).data
        val position = viewHolder.absoluteAdapterPosition
        positionIn = position

        adapterPaging.notifyItemChanged(position)
        binding.rvFavMovies.adapter?.notifyItemChanged(position)
        // swipe action
        if (direction == ItemTouchHelper.START) { // swipe left, action add to watchlist
          isWantToDelete = false
          postToAddWatchlistTMDB(fav.id!!, fav.title ?: fav.originalTitle!!)
        } else { // swipe right, action to delete
          isWantToDelete = true
          postToRemoveFavTMDB(fav.id!!)
          adapterPaging.notifyItemRemoved(position)
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
          val editIcon = ContextCompat.getDrawable(requireContext(), ic_trash)
          val intrinsicWidth = editIcon?.intrinsicWidth
          val intrinsicHeight = editIcon?.intrinsicHeight

          // draw the red background
          background.color = ContextCompat.getColor(requireContext(), red_matte)
          background.setBounds(itemView.left, itemView.top, dX.toInt() + 10, itemView.bottom)
          background.draw(c)

          // calculate position of delete icon
          val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
          val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
          val deleteIconLeft = itemView.left + deleteIconMargin
          val deleteIconRight = deleteIconLeft + intrinsicWidth!!
          val deleteIconBottom = deleteIconTop + intrinsicHeight

          // Draw the delete icon
          editIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
          editIcon.draw(c)
        } else {  // swipe right to add to watchlist
          val watchlistIcon =
            ContextCompat.getDrawable(requireContext(), ic_bookmark_dark)
          val intrinsicWidth = watchlistIcon?.intrinsicWidth
          val intrinsicHeight = watchlistIcon?.intrinsicHeight

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
          val watchlistIconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
          val watchlistIconMargin = (itemHeight - intrinsicHeight) / 2
          val watchlistIconLeft = itemView.right - watchlistIconMargin - intrinsicWidth!!
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

    itemTouchHelper.attachToRecyclerView(binding.rvFavMovies)
  }

  private fun postToRemoveFavTMDB(movieId: Int) {
    val favoriteMode = Favorite(
      mediaType = "movie",
      mediaId = movieId,
      favorite = false
    )
    viewModelAuth.getUser().observe(viewLifecycleOwner) { user ->
      favViewModelMovie.postFavoriteToDelete(user, favoriteMode)

      favViewModelMovie.getSnackBarTextInt()
        .observe(viewLifecycleOwner) { showSnackBarFavUserLogin(user, favoriteMode, it) }
    }
  }

  private fun postToAddWatchlistTMDB(mediaId: Int, mediaTitle: String) {
    val watchlistMode = Watchlist(
      mediaType = "movie",
      mediaId = mediaId,
      watchlist = true
    )

    viewModelAuth.getUser().observe(this) { user ->
      favViewModelMovie.getStated(user.token, mediaId)
      favViewModelMovie.getStated().observe(this) {
        it.let {
          if (!it?.watchlist!!) {
            showSnackBarNoAction("<b>$mediaTitle</b> " + getString(added_to_watchlist))
            favViewModelMovie.postWatchlist(user, watchlistMode)
          } else showSnackBarNoAction("<b>$mediaTitle</b> " + getString(already_watchlist))
        }
      }
    }
  }

  private fun showSnackBarUndoGuest(eventMessage: Event<Int>) {
    val message = eventMessage.getContentIfNotHandled() ?: return
    mSnackbar = Snackbar.make(
      binding.root,
      getString(message),
      Snackbar.LENGTH_LONG
    ).setAction(getString(undo)) {
      val fav = favViewModelMovie.undoDeleteDB().value?.getContentIfNotHandled() as FavoriteDB
      favViewModelMovie.isWatchlistDB(fav.mediaId!!)
      favViewModelMovie.isWatchlistDB().observe(viewLifecycleOwner) {
        if (it) { // movie is on watchlist
          if (isWantToDelete)
            favViewModelMovie.updateToFavoriteDB(fav)
          else
            favViewModelMovie.updateToRemoveFromWatchlistDB(fav)
        } else { // movie is not on watchlist
          if (isWantToDelete) {
            fav.isFavorite = true
            favViewModelMovie.insertToDB(fav)
          } else {
            fav.isWatchlist = true
            favViewModelMovie.insertToDB(fav)
          }
        }
      }
    }.setAnchorView(binding.guideSnackbar)
    mSnackbar?.show()
  }

  private fun showSnackBarFavUserLogin(user: UserModel, fav: Favorite, eventMessage: Event<Int>) {
    val message = eventMessage.getContentIfNotHandled() ?: return
    mSnackbar = Snackbar.make(
      binding.root,
      getString(message),
      Snackbar.LENGTH_LONG
    ).setAction(getString(undo)) {
      fav.favorite = true
      favViewModelMovie.postFavorite(user, fav)
      adapterPaging.notifyItemInserted(positionIn)
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

  private fun setDataUserLoginProgressBarEmptyView(userToken: String) {
    binding.rvFavMovies.adapter = adapterPaging.withLoadStateFooter(
      footer = LoadingStateAdapter {
        adapterPaging.retry()
      }
    )

    // show/hide view
    adapterPaging.addLoadStateListener { loadState ->
      if (loadState.source.refresh is LoadState.NotLoading
        && loadState.append.endOfPaginationReached
        && adapterPaging.itemCount < 1
      ) {
        // show empty view
        binding.illustrationNoDataView.containerSearchNoData.visibility = View.VISIBLE
      } else {
        //  hide empty view
        binding.illustrationNoDataView.containerSearchNoData.visibility = View.INVISIBLE
        binding.rvFavMovies.visibility = View.VISIBLE
      }

      binding.progressBar.isVisible =
        loadState.source.refresh is LoadState.Loading // show progressbar
    }

    favViewModelMovie.getFavoriteMovies(userToken)
      .observe(viewLifecycleOwner) {
        adapterPaging.submitData(lifecycle, it)
      }
  }

  private fun setDataGuestUserProgressBarEmptyView() {
    binding.rvFavMovies.adapter = adapterDB
    binding.rvFavMovies.addItemDecoration(
      DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
    )

    favViewModelMovie.getFavoriteMoviesFromDB.observe(viewLifecycleOwner) {
      adapterDB.setFavorite(it)
      if (it.isNotEmpty()) {
        binding.rvFavMovies.visibility = View.VISIBLE
        binding.illustrationNoDataView.containerSearchNoData.visibility = View.INVISIBLE
      } else {
        binding.rvFavMovies.visibility = View.INVISIBLE
        binding.illustrationNoDataView.containerSearchNoData.visibility = View.VISIBLE
      }
      binding.progressBar.visibility = View.INVISIBLE
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