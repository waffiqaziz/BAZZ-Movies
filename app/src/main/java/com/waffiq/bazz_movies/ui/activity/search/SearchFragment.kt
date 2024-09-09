package com.waffiq.bazz_movies.ui.activity.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.drawable.ic_cross
import com.waffiq.bazz_movies.R.drawable.ic_search
import com.waffiq.bazz_movies.R.id.action_search
import com.waffiq.bazz_movies.R.id.nav_view
import com.waffiq.bazz_movies.R.menu.search_menu
import com.waffiq.bazz_movies.R.string.clear_query
import com.waffiq.bazz_movies.databinding.FragmentSearchBinding
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.SearchAdapter
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelFactory
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.pagingErrorState
import com.waffiq.bazz_movies.utils.helpers.SnackBarManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

  private var _binding: FragmentSearchBinding? = null
  private val binding get() = _binding!!

  private lateinit var searchViewModel: SearchViewModel
  private lateinit var closeButton: View
  private lateinit var searchView: SearchView

  val adapter = SearchAdapter()

  private var mSnackbar: Snackbar? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentSearchBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    searchViewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]

    // setup toolbar as action bar
    (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    (activity as AppCompatActivity).supportActionBar?.title = null
    binding.appBarLayout.setExpanded(true, true)

    setupRecyclerView()
    setupSearchView(searchViewModel)
    return root
  }

  private fun setupRecyclerView() {
    binding.rvSearch.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    binding.rvSearch.itemAnimator = DefaultItemAnimator()
    binding.rvSearch.adapter =
      adapter.withLoadStateFooter(footer = LoadingStateAdapter { adapter.retry() })

    binding.swipeRefresh.setOnRefreshListener {
      adapter.refresh()
      binding.swipeRefresh.isRefreshing = false
    }
  }

  private fun setupSearchView(searchViewModel: SearchViewModel) {
    // show or hide view
    adapter.addLoadStateListener { loadState ->
      when (loadState.source.refresh) {
        is LoadState.Loading -> {
          // Data is loading; keep showing the containerSearch
          binding.progressBar.isVisible = true
          binding.rvSearch.isVisible = true
          binding.illustrationSearchView.containerSearch.isVisible = false
          binding.illustrationSearchNoResultView.containerSearchNoResult.isVisible = false
        }

        is LoadState.NotLoading -> {
          binding.progressBar.isVisible = false
          if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
            // No search results found; show empty view
            binding.rvSearch.isVisible = false
            binding.illustrationSearchNoResultView.containerSearchNoResult.isVisible = true
            binding.illustrationSearchView.containerSearch.isVisible = false
          } else {
            // Data is loaded; show the results and hide the loading view
            binding.rvSearch.isVisible = true
            binding.illustrationSearchView.containerSearch.isVisible = false
            binding.illustrationSearchNoResultView.containerSearchNoResult.isVisible = false
          }
        }

        is LoadState.Error -> {
          // Error occurred; handle error state and hide loading view
          binding.progressBar.isVisible = false
          binding.rvSearch.isVisible = false
          binding.illustrationSearchView.containerSearch.isVisible = false
          pagingErrorState(loadState)?.let {
            mSnackbar = SnackBarManager.snackBarWarning(
              requireContext(),
              binding.root,
              requireActivity().findViewById(nav_view),
              Event(pagingErrorHandling(it.error))
            )
          }
        }
      }
    }

    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        searchViewModel.searchResults.collectLatest { resultItemSearch ->
          adapter.submitData(lifecycle, resultItemSearch)
        }
      }
    }

    //setup searchView
    val menuHost: MenuHost = requireActivity()
    var lastQuery: String? = null
    menuHost.addMenuProvider(object : MenuProvider {
      override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(search_menu, menu)

        searchView = menu.findItem(action_search).actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        customizeSearchView(searchView)

        // search queryTextChange Listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

          override fun onQueryTextSubmit(query: String?): Boolean {
            if (query != null && query != lastQuery) {
              lastQuery = query
              searchViewModel.search(query)
            } else return true
            searchView.clearFocus()
            return true
          }

          override fun onQueryTextChange(query: String?): Boolean {
            return true
          }
        })
      }

      override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
          action_search -> true
          else -> false
        }
      }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
  }

  private fun customizeSearchView(searchView: SearchView) {
    lateinit var backButton: ImageView
    val searchPlate = searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)
    traverseViewHierarchy(searchPlate)

    for (i in 0 until searchView.childCount) {
      val child = searchView.getChildAt(i)
      if (child is ImageView) {
        // Check if the child is the arrow icon
        backButton = child

        // Set custom drawable
        backButton.setImageDrawable(ContextCompat.getDrawable(requireActivity(), ic_search))
        break
      }
    }
  }

  private fun traverseViewHierarchy(view: View) {
    if (view is ViewGroup) {
      for (i in 0 until view.childCount) {
        val child = view.getChildAt(i)
        if (child is ViewGroup) {
          traverseViewHierarchy(child)
        } else if (child is View && child.contentDescription == getString(clear_query)) {
          closeButton = child
          val ivCloseButton = closeButton as ImageView
          ivCloseButton.contentDescription = getString(clear_query)
          ivCloseButton.setImageDrawable(ContextCompat.getDrawable(requireActivity(), ic_cross))
        }
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
    mSnackbar?.dismiss()
  }

  override fun onResume() {
    super.onResume()
    binding.illustrationSearchView.containerSearch.isVisible = true
    binding.appBarLayout.setExpanded(true, true)
  }
}