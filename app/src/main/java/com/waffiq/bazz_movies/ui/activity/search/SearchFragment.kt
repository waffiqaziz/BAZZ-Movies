package com.waffiq.bazz_movies.ui.activity.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
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

  private val adapter = SearchAdapter()

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

    setupView()
    observeSearchResult()
    adapterLoadStateListener()
    return root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupSearchView()
  }

  private fun setupView() {
    binding.rvSearch.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    binding.rvSearch.itemAnimator = DefaultItemAnimator()
    binding.rvSearch.adapter =
      adapter.withLoadStateFooter(footer = LoadingStateAdapter { adapter.retry() })

    binding.illustrationError.btnTryAgain.setOnClickListener { adapter.refresh() }
    binding.swipeRefresh.setOnRefreshListener {
      adapter.refresh()
      binding.swipeRefresh.isRefreshing = false
    }
  }

  private fun setupSearchView() {
    var lastQuery: String? = null
    requireActivity().addMenuProvider(object : MenuProvider {
      override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(search_menu, menu)
        searchView = menu.findItem(action_search).actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        customizeSearchView(searchView)

        searchViewModel.firstTime.observe(viewLifecycleOwner) {
          if (it) {
            menu.findItem(action_search).expandActionView()
          } else {
//            menu.findItem(action_search).expandActionView()
//            searchView.isFocusable = false
//            searchView.isIconified  =  false
//            searchView.clearFocus()
//            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(searchView.windowToken, 0)

            // Set soft input mode to prevent the keyboard from appearing
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

            // Expand the SearchView
            menu.findItem(action_search).expandActionView()
            searchView.isFocusable = false
            searchView.isIconified = false
            searchView.clearFocus()
          }
        }

        // search queryTextChange Listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
          override fun onQueryTextSubmit(query: String?): Boolean {
            if (query != null && query != lastQuery) {
              lastQuery = query
              searchViewModel.search(query)
              searchViewModel.setFirstTIme(false)
            } else return true
            searchView.clearFocus()
            return false
          }

          override fun onQueryTextChange(query: String?): Boolean {
            return true
          }
        })

        // Restore query if available
        searchViewModel.query.observe(viewLifecycleOwner) {
          searchView.setQuery(it, false)
        }
      }

      override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
          action_search -> true
          else -> false
        }
      }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
  }

  private fun observeSearchResult() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        searchViewModel.searchResults.collectLatest { resultItemSearch ->
          adapter.submitData(lifecycle, resultItemSearch)
        }
      }
    }
  }

  private fun adapterLoadStateListener() {
    adapter.addLoadStateListener { loadState ->
      Log.d("LoadState", "Load state triggered: ${loadState.source.refresh}")
      when (loadState.source.refresh) {
        is LoadState.Loading -> {
          // Data is loading; keep showing the containerSearch
          binding.progressBar.isVisible = true
          binding.rvSearch.isVisible = false
          binding.illustrationSearchView.root.isVisible = false
          binding.illustrationSearchNoResultView.root.isVisible = false
          binding.illustrationError.root.isVisible = false
        }

        is LoadState.NotLoading -> {
          binding.progressBar.isVisible = false
          binding.illustrationSearchView.root.isVisible = false
          binding.illustrationError.root.isVisible = false
          if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
            // No search results found; show empty view
            binding.rvSearch.isVisible = false
            binding.illustrationSearchNoResultView.root.isVisible = true
          } else {
            // Data is loaded; show the results and hide the loading view
            binding.rvSearch.isVisible = true
            binding.illustrationSearchNoResultView.root.isVisible = false
          }
        }

        is LoadState.Error -> {
          // Error occurred; handle error state and hide loading view
          binding.progressBar.isVisible = false
          binding.rvSearch.isVisible = false
          binding.illustrationSearchView.root.isVisible = false
          binding.illustrationError.root.isVisible = true
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
    adapter.addLoadStateListener { loadState ->
      when (loadState.source.refresh) {
        is LoadState.Loading -> {}
        is LoadState.Error -> {
          if (adapter.itemCount <= 0) {
            binding.illustrationError.root.isVisible = true
            binding.illustrationSearchView.root.isVisible = false
            binding.rvSearch.isVisible = false
          }
        }

        is LoadState.NotLoading -> {
          if (adapter.itemCount <= 0) {
            binding.illustrationError.root.isVisible = false
            binding.illustrationSearchView.root.isVisible = true
            binding.rvSearch.isVisible = false
          }
        }
      }
    }
  }
}