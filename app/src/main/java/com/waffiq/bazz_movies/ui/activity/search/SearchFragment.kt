package com.waffiq.bazz_movies.ui.activity.search

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
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
import com.waffiq.bazz_movies.R.color.yellow
import com.waffiq.bazz_movies.R.drawable.ic_cross
import com.waffiq.bazz_movies.R.id.action_search
import com.waffiq.bazz_movies.R.id.nav_view
import com.waffiq.bazz_movies.R.menu.search_menu
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
  private val adapter = SearchAdapter()

  private var mSnackbar: Snackbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val factory = ViewModelFactory.getInstance(requireContext())
    searchViewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentSearchBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // setup toolbar as action bar
    (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    (activity as AppCompatActivity).supportActionBar?.title = null
    binding.appBarLayout.setExpanded(true, true)

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

    adapterLoadStateListener()
    setupSearchView()
    observeSearchResult()
  }

  private fun setupSearchView() {
    var lastQuery: String? = null
    requireActivity().addMenuProvider(
      object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
          menuInflater.inflate(search_menu, menu)
          val searchView = menu.findItem(action_search).actionView as SearchView
          searchView.maxWidth = Int.MAX_VALUE
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
              .textCursorDrawable?.setTint(ContextCompat.getColor(requireContext(), yellow))
          }
          searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
            .setImageResource(ic_cross)

          searchViewModel.expandSearchView.observe(viewLifecycleOwner) {
            if (it) {
              searchView.isVisible = true
              menu.findItem(action_search).expandActionView()
              searchView.isFocusable = false
              searchView.isIconified = false
            }
          }

          // search queryTextChange Listener
          searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
              if (query != null && query != lastQuery) {
                lastQuery = query
                searchViewModel.search(query)
              } else {
                return true
              }
              searchView.clearFocus()
              return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
              return true
            }
          })

          // Restore query if available
          searchViewModel.query.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) searchView.setQuery(it, false)
          }
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
          return when (menuItem.itemId) {
            action_search -> true
            else -> false
          }
        }
      },
      viewLifecycleOwner,
      Lifecycle.State.RESUMED
    )
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
          binding.illustrationError.root.isVisible = false
          if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
            // No results found; displaying empty view instead.
            binding.rvSearch.isVisible = false
            binding.illustrationSearchNoResultView.root.isVisible = true
            binding.illustrationSearchView.root.isVisible = false
          } else if (!loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
            // No search operation; show illustration search
            binding.rvSearch.isVisible = false
            binding.illustrationSearchView.root.isVisible = true
            binding.illustrationSearchNoResultView.root.isVisible = false
          } else {
            // Data is loaded; show the results
            binding.rvSearch.isVisible = true
            binding.illustrationSearchView.root.isVisible = false
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
              requireActivity().findViewById(nav_view),
              requireActivity().findViewById(nav_view),
              Event(pagingErrorHandling(it.error))
            )
          }
        }
      }
    }
  }

  // trigger via bottom navigation
  fun openSearchView() {
    (requireActivity() as AppCompatActivity).supportActionBar?.show()
    binding.appBarLayout.setExpanded(true)
    searchViewModel.setExpandSearchView(true)
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    if (newConfig.keyboardHidden == Configuration.KEYBOARDHIDDEN_YES) {
      binding.appBarLayout.setExpanded(true)
    }
  }

  override fun onResume() {
    super.onResume()
    binding.appBarLayout.setExpanded(true)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    searchViewModel.setExpandSearchView(false) // reset expand search view
    mSnackbar?.dismiss()
    mSnackbar = null
    (activity as? AppCompatActivity)?.setSupportActionBar(null)
    _binding = null
  }
}
