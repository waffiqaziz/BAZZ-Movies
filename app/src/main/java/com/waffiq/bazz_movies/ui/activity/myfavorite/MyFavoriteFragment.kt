package com.waffiq.bazz_movies.ui.activity.myfavorite

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.widget.SearchView
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
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.databinding.FragmentMyFavoriteBinding
import com.waffiq.bazz_movies.ui.adapter.FavoriteAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.utils.Event

class MyFavoriteFragment : Fragment() {

  private var _binding: FragmentMyFavoriteBinding? = null
  private val binding get() = _binding!!

  private lateinit var viewModel : MyFavoriteViewModel
  private val adapter = FavoriteAdapter()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMyFavoriteBinding.inflate(inflater, container, false)
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
                adapter.setFavorite(it)
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
        return false
      }

      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val fav = (viewHolder as FavoriteAdapter.ViewHolder).getFavorite
        viewModel.deleteFav(fav)
      }

    })
    itemTouchHelper.attachToRecyclerView(binding.rvFav)

    viewModel.snackbarText.observe(viewLifecycleOwner) {
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
      viewModel.insertToFavorite(viewModel.undo.value?.getContentIfNotHandled() as Favorite)
    }.show()
  }

  private fun setData(){
    //setup recycleView
    binding.rvFav.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    binding.rvFav.adapter = adapter

    viewModel.allFavorite.observe(viewLifecycleOwner){
      adapter.setFavorite(it)
      binding.viewEmpty.visibility = if (it.isNotEmpty()) GONE else VISIBLE
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}