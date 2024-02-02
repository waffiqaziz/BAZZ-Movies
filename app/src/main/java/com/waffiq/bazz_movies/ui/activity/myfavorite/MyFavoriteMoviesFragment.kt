package com.waffiq.bazz_movies.ui.activity.myfavorite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.databinding.FragmentMyFavoriteMoviesBinding
import com.waffiq.bazz_movies.ui.adapter.FavoriteAdapterDB
import com.waffiq.bazz_movies.ui.adapter.FavoriteMovieAdapter
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.Helper.showToastShort

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class MyFavoriteMoviesFragment : Fragment() {

  private var _binding: FragmentMyFavoriteMoviesBinding? = null
  private val binding get() = _binding!!

  private lateinit var favViewModelMovie: MyFavoriteViewModel
  private lateinit var viewModelAuth: AuthenticationViewModel

  private val adapterDB = FavoriteAdapterDB()
  private val adapterPaging = FavoriteMovieAdapter()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMyFavoriteMoviesBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val pref = requireContext().dataStore
    val factoryAuth = ViewModelUserFactory.getInstance(pref)
    this.viewModelAuth = ViewModelProvider(this, factoryAuth)[AuthenticationViewModel::class.java]

    val factory = ViewModelFactory.getInstance(requireContext())
    favViewModelMovie = ViewModelProvider(this, factory)[MyFavoriteViewModel::class.java]

    checkUser()
    return root
  }

  private fun checkUser() {
    //setup recyclerview
    binding.rvFavMovies.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    viewModelAuth.getUser().observe(viewLifecycleOwner) { user ->
      if (user.token != "NaN") { //user login then show data from TMDB
        initActionUserLogin()
        setDataUserLogin(user.token)
      } else { //guest user then show data from database
        favViewModelMovie.getSnackBarTextInt()
          .observe(viewLifecycleOwner) { showSnackBarGuest(it) }
        initActionGuest()
        setDataGuestUser()
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
        checkIsInWatchlist(fav)

        val position = viewHolder.bindingAdapterPosition

        if (direction == ItemTouchHelper.LEFT) {
          showToastShort(requireContext(), position.toString())
          binding.rvFavMovies.adapter?.notifyItemChanged(position)
        } else {
          showToastShort(requireContext(), position.toString())
          binding.rvFavMovies.adapter?.notifyItemChanged(position)
        }
      }

      override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
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
          val editIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_trash)
          val intrinsicWidth = editIcon?.intrinsicWidth
          val intrinsicHeight = editIcon?.intrinsicHeight

          // Draw the red background
          background.color = ContextCompat.getColor(requireContext(), R.color.red_matte)
          background.setBounds(itemView.left, itemView.top, dX.toInt() + 10, itemView.bottom)
          background.draw(c)

          // Calculate position of delete icon
          val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
          val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
          val deleteIconLeft = itemView.left + deleteIconMargin
          val deleteIconRight = deleteIconLeft + intrinsicWidth!!
          val deleteIconBottom = deleteIconTop + intrinsicHeight

          // Draw the delete icon
          editIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
          editIcon.draw(c)
        } else {  // swipe right to add to watchlist
          val watchlistIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_bookmark_dark)
          val intrinsicWidth = watchlistIcon?.intrinsicWidth
          val intrinsicHeight = watchlistIcon?.intrinsicHeight

          // Draw the delete background
          background.color = ContextCompat.getColor(requireContext(), R.color.yellow)
          background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
          )
          background.draw(c)

          // Calculate position of delete icon
          val watchlistIconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
          val watchlistIconMargin = (itemHeight - intrinsicHeight) / 2
          val watchlistIconLeft = itemView.right - watchlistIconMargin - intrinsicWidth!!
          val watchlistIconRight = watchlistIconLeft + intrinsicWidth
          val watchlistIconBottom = watchlistIconTop + intrinsicHeight

          // Draw the delete icon
          watchlistIcon.setBounds(watchlistIconLeft, watchlistIconTop, watchlistIconRight, watchlistIconBottom)
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

  private fun checkIsInWatchlist(fav: FavoriteDB) {
    favViewModelMovie.isWatchlistDB(fav.mediaId!!)
    favViewModelMovie.isWatchlistDB().observe(this) {
      if (it) {
        Log.e("KKKK", it.toString())
//        favViewModelMovie.deleteFavDB(fav)
        favViewModelMovie.updateToRemoveFromFavoriteDB(fav)
      } else {
        Log.e("KKKKN", it.toString())
        favViewModelMovie.deleteFavDB(fav)
      }
    }
  }

  private fun initActionUserLogin() {
    val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
      override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
      ): Int {
        return makeMovementFlags(0, ItemTouchHelper.RIGHT)
      }

      override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
      ): Boolean {
        val colorDrawable = ColorDrawable()
        colorDrawable.color = ContextCompat.getColor(requireActivity(), R.color.red_matte)
        viewHolder.itemView.background = colorDrawable
        return false
      }

      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val fav = (viewHolder as FavoriteMovieAdapter.ViewHolder).data
        postFavDataToTMDB(fav.id!!)
        showToastShort(requireContext(), getString(R.string.deleted_from_favorite))
      }
    })

    itemTouchHelper.attachToRecyclerView(binding.rvFavMovies)
  }

  private fun postFavDataToTMDB(movieId: Int) {
    val favoriteMode = Favorite(
      "movie",
      movieId,
      false
    )
    viewModelAuth.getUser().observe(this) { user ->
      favViewModelMovie.postFavorite(user, favoriteMode)

      favViewModelMovie.getSnackBarTextInt()
        .observe(viewLifecycleOwner) { showSnackBarUserLogin(user, favoriteMode, it) }
    }
  }

  private fun showSnackBarGuest(eventMessage: Event<Int>) {
    val message = eventMessage.getContentIfNotHandled() ?: return
    Snackbar.make(
      binding.coordinatorLayout,
      getString(message),
      Snackbar.LENGTH_SHORT
    ).setAction("Undo") {
      val fav = favViewModelMovie.undoDeleteDB().value?.getContentIfNotHandled() as FavoriteDB
      fav.isFavorite = true
      favViewModelMovie.insertToDB(fav)
    }.show()
  }

  private fun showSnackBarUserLogin(user: UserModel, fav: Favorite, eventMessage: Event<Int>) {
    val message = eventMessage.getContentIfNotHandled() ?: return
    Snackbar.make(
      binding.coordinatorLayout,
      getString(message),
      Snackbar.LENGTH_SHORT
    ).setAction("Undo") {
      favViewModelMovie.postFavorite(user, fav)
    }.show()
  }

  private fun setDataUserLogin(userToken: String) {
    binding.rvFavMovies.adapter = adapterPaging.withLoadStateFooter(
      footer = LoadingStateAdapter {
        adapterPaging.retry()
      }
    )

    //show/hide view
    adapterPaging.addLoadStateListener { loadState ->
      if (loadState.source.refresh is LoadState.NotLoading
        && loadState.append.endOfPaginationReached
        && adapterPaging.itemCount < 1
      ) {
        /// show empty view
        binding.viewEmpty.isVisible = true
      } else {
        ///  hide empty view
      }

      binding.progressBar.isVisible =
        loadState.source.refresh is LoadState.Loading //show progressbar
    }

    favViewModelMovie.getFavoriteMovies(userToken)
      .observe(viewLifecycleOwner) {
        adapterPaging.submitData(lifecycle, it)
      }
  }

  private fun setDataGuestUser() {
    binding.rvFavMovies.adapter = adapterDB

    favViewModelMovie.getFavoriteMoviesFromDB.observe(viewLifecycleOwner) {
      adapterDB.setFavorite(it)
      binding.viewEmpty.visibility = if (it.isNotEmpty()) View.INVISIBLE else View.VISIBLE
//      binding.progressBar.visibility = if (it.isNotEmpty()) View.INVISIBLE else View.VISIBLE
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}