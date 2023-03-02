package com.waffiq.bazz_movies.ui.activity.myfavorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.waffiq.bazz_movies.databinding.FragmentMyFavoriteMoviesBinding
import com.waffiq.bazz_movies.ui.adapter.FavoriteAdapterDB
import com.waffiq.bazz_movies.ui.adapter.FavoriteMovieAdapter
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class MyFavoriteMoviesFragment : Fragment() {

  private var _binding: FragmentMyFavoriteMoviesBinding? = null
  private val binding get() = _binding!!

  private lateinit var viewModelMovie: MyFavoriteViewModel
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
    viewModelMovie = ViewModelProvider(this, factory)[MyFavoriteViewModel::class.java]

    checkUser()
    return root
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
//
//  private fun initAction() {
//    val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
//      override fun getMovementFlags(
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder
//      ): Int {
//        return makeMovementFlags(0, ItemTouchHelper.RIGHT)
//      }
//
//      override fun onMove(
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        target: RecyclerView.ViewHolder
//      ): Boolean {
//        val colorDrawable = ColorDrawable()
//        colorDrawable.color = ContextCompat.getColor(requireActivity(), R.color.red_matte)
//        viewHolder.itemView.background = colorDrawable
//        return false
//      }
//
//      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//        val fav = (viewHolder as FavoriteAdapterDB.ViewHolder).getFavoriteDB
//        viewModel.deleteFavDB(fav)
//      }
//
//      // next update
////      override fun onChildDraw(
////        canvas: Canvas,
////        recyclerView: RecyclerView,
////        viewHolder: RecyclerView.ViewHolder,
////        dX: Float,
////        dY: Float,
////        actionState: Int,
////        isCurrentlyActive: Boolean
////      ) {
////        //1. Background color based upon direction swiped
////        when {
////          abs(dX) < width / 3 -> canvas.drawColor(Color.GRAY)
////          dX > width / 3 -> canvas.drawColor(deleteColor)
////          else -> canvas.drawColor(archiveColor)
////        }
////
////        //2. Printing the icons
////        val textMargin = resources.getDimension(R.dimen.text_margin)
////          .roundToInt()
////        deleteIcon.bounds = Rect(
////          textMargin,
////          viewHolder.itemView.top + textMargin + 8.dp,
////          textMargin + deleteIcon.intrinsicWidth,
////          viewHolder.itemView.top + deleteIcon.intrinsicHeight
////            + textMargin + 8.dp
////        )
////        archiveIcon.bounds = Rect(
////          width - textMargin - archiveIcon.intrinsicWidth,
////          viewHolder.itemView.top + textMargin + 8.dp,
////          width - textMargin,
////          viewHolder.itemView.top + archiveIcon.intrinsicHeight
////            + textMargin + 8.dp
////        )
////
////        //3. Drawing icon based upon direction swiped
////        if (dX > 0) deleteIcon.draw(canvas) else archiveIcon.draw(canvas)
////
////        super.onChildDraw(
////          canvas,
////          recyclerView,
////          viewHolder,
////          dX,
////          dY,
////          actionState,
////          isCurrentlyActive
////        )
////      }
//
//    })
//    itemTouchHelper.attachToRecyclerView(binding.rvFavMovies)
//
//    viewModel.snackBarText.observe(viewLifecycleOwner) {
//      showSnackBar(it)
//    }
//  }
//
//  private fun showSnackBar(eventMessage: Event<Int>) {
//    val message = eventMessage.getContentIfNotHandled() ?: return
//    Snackbar.make(
//      binding.coordinatorLayout,
//      getString(message),
//      Snackbar.LENGTH_SHORT
//    ).setAction("Undo") {
//      viewModel.insertToFavoriteDB(viewModel.undo.value?.getContentIfNotHandled() as FavoriteDB)
//    }.show()
//  }

  private fun checkUser() {
    //setup recyclerview
    binding.rvFavMovies.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    viewModelAuth.getUser().observe(viewLifecycleOwner) { user ->
      if (user.token != "NaN") { //user login then show data from TMDB
        setDataUserLogin(user.token)
      } else { //guest user then show data from database
        setDataGuestUser()
      }
    }
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

    viewModelMovie.getFavoriteMovies(userToken)
      .observe(viewLifecycleOwner) {
        adapterPaging.submitData(lifecycle, it)
      }
  }

  private fun setDataGuestUser() {
    binding.rvFavMovies.adapter = adapterDB

    viewModelMovie.getFavoriteMoviesFromDB.observe(viewLifecycleOwner) {
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