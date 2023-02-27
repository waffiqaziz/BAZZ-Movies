package com.waffiq.bazz_movies.ui.activity.myfavorite

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.databinding.FragmentMyFavoriteTvSeriesBinding
import com.waffiq.bazz_movies.ui.adapter.FavoriteAdapterDB
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.utils.Event

class MyFavoriteTvSeriesFragment : Fragment() {

  private var _binding: FragmentMyFavoriteTvSeriesBinding? = null
  private val binding get() = _binding!!

  private val adapterDB = FavoriteAdapterDB()

  private lateinit var viewModel: MyFavoriteViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMyFavoriteTvSeriesBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    viewModel = ViewModelProvider(this, factory)[MyFavoriteViewModel::class.java]

    setupSearchView()
    setData()
    initAction()
    return root
  }

  private fun setupSearchView(){
    val menuHost: MenuHost = requireActivity()
    menuHost.addMenuProvider(object : MenuProvider {
      override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search_menu, menu)

        val item = menu.findItem(R.id.action_search)
        val searchView = item?.actionView as SearchView

        // search queryTextChange Listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
          override fun onQueryTextSubmit(query: String?): Boolean { //when user submit data

            if (query != null) {
              viewModel.searchFavorite(query).observe(viewLifecycleOwner){
                adapterDB.setFavorite(it)
              }
            }
            return true
          }

          override fun onQueryTextChange(query: String?): Boolean {
            return true
          }
        })
      }

      override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
          R.id.action_search -> {
            true
          }
          else -> false
        }
      }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
  }

  private fun initAction() {
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
        val fav = (viewHolder as FavoriteAdapterDB.ViewHolder).getFavoriteDB
        viewModel.deleteFavDB(fav)
      }

      // next update
//      override fun onChildDraw(
//        canvas: Canvas,
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        dX: Float,
//        dY: Float,
//        actionState: Int,
//        isCurrentlyActive: Boolean
//      ) {
//        //1. Background color based upon direction swiped
//        when {
//          abs(dX) < width / 3 -> canvas.drawColor(Color.GRAY)
//          dX > width / 3 -> canvas.drawColor(deleteColor)
//          else -> canvas.drawColor(archiveColor)
//        }
//
//        //2. Printing the icons
//        val textMargin = resources.getDimension(R.dimen.text_margin)
//          .roundToInt()
//        deleteIcon.bounds = Rect(
//          textMargin,
//          viewHolder.itemView.top + textMargin + 8.dp,
//          textMargin + deleteIcon.intrinsicWidth,
//          viewHolder.itemView.top + deleteIcon.intrinsicHeight
//            + textMargin + 8.dp
//        )
//        archiveIcon.bounds = Rect(
//          width - textMargin - archiveIcon.intrinsicWidth,
//          viewHolder.itemView.top + textMargin + 8.dp,
//          width - textMargin,
//          viewHolder.itemView.top + archiveIcon.intrinsicHeight
//            + textMargin + 8.dp
//        )
//
//        //3. Drawing icon based upon direction swiped
//        if (dX > 0) deleteIcon.draw(canvas) else archiveIcon.draw(canvas)
//
//        super.onChildDraw(
//          canvas,
//          recyclerView,
//          viewHolder,
//          dX,
//          dY,
//          actionState,
//          isCurrentlyActive
//        )
//      }

    })
    itemTouchHelper.attachToRecyclerView(binding.rvFavTv)

    viewModel.snackBarText.observe(viewLifecycleOwner) {
      showSnackBar(it)
    }
  }

  private fun showSnackBar(eventMessage: Event<Int>) {
    val message = eventMessage.getContentIfNotHandled() ?: return
    Snackbar.make(
      binding.coordinatorLayout,
      getString(message),
      Snackbar.LENGTH_SHORT
    ).setAction("Undo") {
      viewModel.insertToFavoriteDB(viewModel.undo.value?.getContentIfNotHandled() as FavoriteDB)
    }.show()
  }

  private fun setData(){
    //setup recycleView
    binding.rvFavTv.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    binding.rvFavTv.adapter = adapterDB

    viewModel.allFavoriteDB.observe(viewLifecycleOwner){
      adapterDB.setFavorite(it)
      binding.viewEmpty.visibility = if (it.isNotEmpty()) View.GONE else View.VISIBLE
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}