package com.waffiq.bazz_movies.feature.search.ui

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
import androidx.appcompat.R.id.search_button
import androidx.appcompat.R.id.search_close_btn
import androidx.appcompat.R.id.search_src_text
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.domain.model.search.ResultsItemSearch
import com.waffiq.bazz_movies.core.ui.R.color.yellow
import com.waffiq.bazz_movies.core.ui.R.drawable.ic_cross
import com.waffiq.bazz_movies.core.ui.R.drawable.ic_search
import com.waffiq.bazz_movies.core.utils.common.Event
import com.waffiq.bazz_movies.core.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.core.utils.helpers.PagingLoadStateHelper.pagingErrorState
import com.waffiq.bazz_movies.core.utils.helpers.uihelpers.UIController
import com.waffiq.bazz_movies.feature.search.R.id.action_search
import com.waffiq.bazz_movies.feature.search.R.menu.search_menu
import com.waffiq.bazz_movies.feature.search.databinding.FragmentSearchBinding
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.setupShimmer
import com.waffiq.bazz_movies.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

  @Inject
  lateinit var navigator: Navigator

  private var uiController: UIController? = null
    get() = activity as? UIController

  private var _binding: FragmentSearchBinding? = null
  private val binding get() = _binding!!

  private val searchViewModel: SearchViewModel by viewModels()
  private lateinit var searchAdapter: SearchAdapter

  private var lastQuery: String? = null

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
    (activity as AppCompatActivity).setSupportActionBar(binding.toolbarLayout.toolbar)
    (activity as AppCompatActivity).supportActionBar?.title = null
    binding.appBarLayout.setExpanded(true, true)

    searchAdapter = SearchAdapter(navigator)
    binding.rvSearch.setupShimmer(requireContext(), searchAdapter)

    binding.illustrationError.btnTryAgain.setOnClickListener {
      searchAdapter.refresh()
    }
    binding.swipeRefresh.setOnRefreshListener {
      searchAdapter.refresh()
      binding.swipeRefresh.isRefreshing = false
    }

    adapterLoadStateListener()
    setupSearchView()
    observeSearchResult()
  }

  private fun setupSearchView() {
    requireActivity().addMenuProvider(
      object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
          menuInflater.inflate(search_menu, menu)
          val searchView = menu.findItem(action_search).actionView as SearchView
          searchView.maxWidth = Int.MAX_VALUE

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            searchView.findViewById<EditText>(search_src_text)
              .textCursorDrawable?.setTint(ContextCompat.getColor(requireContext(), yellow))
          }
          searchView.findViewById<ImageView>(search_close_btn)
            .setImageResource(ic_cross)
          searchView.findViewById<ImageView>(search_button)
            .setImageResource(ic_search)

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
                lifecycleScope.launch {
                  searchAdapter.submitData(PagingData.empty())
                }
                searchAdapter.refresh()
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
          searchAdapter.submitData(lifecycle, resultItemSearch)
        }
      }
    }
  }

  private fun adapterLoadStateListener() {
    searchAdapter.addLoadStateListener { loadState ->
      when (loadState.source.refresh) {
        is LoadState.Loading -> {
          // Data is loading; keep showing the containerSearch
          isUnveil(false)
          binding.illustrationSearchView.root.isVisible = false
          binding.illustrationError.root.isVisible = false
          binding.illustrationSearchNoResultView.root.isVisible = false
          binding.rvSearch.isVisible = true
        }

        is LoadState.NotLoading -> {
          isUnveil(true)
          binding.illustrationError.root.isVisible = false
          if (loadState.append.endOfPaginationReached && searchAdapter.itemCount < 1) {
            // No results found; displaying empty view instead.
            binding.rvSearch.isVisible = false
            binding.illustrationSearchNoResultView.root.isVisible = true
            binding.illustrationSearchView.root.isVisible = false
          } else if (!loadState.append.endOfPaginationReached && searchAdapter.itemCount < 1) {
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
          lastQuery = null
          isUnveil(true)
          if (searchAdapter.itemCount < 1) {
            binding.illustrationError.root.isVisible = true
            binding.rvSearch.isVisible = false
          } else {
            binding.illustrationError.root.isVisible = false
            binding.rvSearch.isVisible = true
          }
          binding.illustrationSearchView.root.isVisible = false

          // show snackbar
          pagingErrorState(loadState)?.let {
            uiController?.showSnackbar(Event(pagingErrorHandling(it.error)))
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

  fun isUnveil(isUnveil: Boolean) {
    if (isUnveil) {
      binding.rvSearch.unVeil()
    } else {
      binding.rvSearch.veil()
    }
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    if (newConfig.keyboardHidden == Configuration.KEYBOARDHIDDEN_YES) {
      binding.appBarLayout.setExpanded(true)
    }
  }

  override fun onResume() {
    super.onResume()
    searchAdapter.refresh()
    binding.appBarLayout.setExpanded(true)
    searchViewModel.setExpandSearchView(false)
  }

  override fun onDetach() {
    super.onDetach()
    uiController = null
  }

  override fun onDestroyView() {
    super.onDestroyView()
    searchViewModel.setExpandSearchView(false) // reset expand search view
    lastQuery = null
    _binding = null
  }
}
