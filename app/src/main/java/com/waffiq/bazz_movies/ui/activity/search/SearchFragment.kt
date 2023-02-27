package com.waffiq.bazz_movies.ui.activity.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.databinding.FragmentSearchBinding
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.SearchAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory

class SearchFragment : Fragment() {

  private var _binding: FragmentSearchBinding? = null
  private val binding get() = _binding!!

  private lateinit var searchViewModel: SearchViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentSearchBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    searchViewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]

    setupAll(searchViewModel)
    return root
  }

  private fun setupAll(searchViewModel: SearchViewModel) {

    //setup recycleView
    binding.rvSearch.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    val adapter = SearchAdapter()

    binding.rvSearch.adapter = adapter.withLoadStateFooter(
      footer = LoadingStateAdapter {
        adapter.retry()
      }
    )

    //show/hide view
    adapter.addLoadStateListener { loadState ->
      if (loadState.source.refresh is LoadState.NotLoading
        && loadState.append.endOfPaginationReached
        && adapter.itemCount < 1
      ) {
        /// show empty view
        binding.illustrationSearchNoResultView.container.isVisible = true
        binding.illustrationSearchView.container.isVisible = false
      } else {
        ///  hide empty view
        binding.illustrationSearchView.container.isVisible = false
        binding.illustrationSearchNoResultView.container.isVisible = false
      }
    }

    //setup searchView
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
              searchViewModel.search(query).observe(viewLifecycleOwner) {
                adapter.submitData(lifecycle, it)
                adapter.addLoadStateListener{
                  binding.progressBar.isVisible = it.source.refresh is LoadState.Loading
                }
              }
            }
            return true
          }

          override fun onQueryTextChange(query: String?): Boolean {
//            Log.d("onQueryTextChange", "query: $query")
            return true
          }
        })

        //Expand Collapse listener
//        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
//          override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
//            Toast.makeText(context, "Action Collapse", Toast.LENGTH_SHORT).show()
//            return true
//          }
//
//          override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
//            Toast.makeText(context, "Action Expand", Toast.LENGTH_SHORT).show()
//            return true
//          }
//        })
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


  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}