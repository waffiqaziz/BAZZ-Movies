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
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.designsystem.R.color.yellow
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_cross
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_search
import com.waffiq.bazz_movies.core.movie.utils.helpers.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.movie.utils.helpers.GeneralHelper.initLinearLayoutManagerVertical
import com.waffiq.bazz_movies.core.movie.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.core.movie.utils.helpers.PagingLoadStateHelper.pagingErrorState
import com.waffiq.bazz_movies.core.uihelper.ISnackbar
import com.waffiq.bazz_movies.feature.search.R.id.action_search
import com.waffiq.bazz_movies.feature.search.R.menu.search_menu
import com.waffiq.bazz_movies.feature.search.databinding.FragmentSearchBinding
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.setupRecyclerView
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

  @Inject
  lateinit var navigator: INavigator

  @Inject
  lateinit var snackbar: ISnackbar

  private var _binding: FragmentSearchBinding? = null
  private val binding get() = _binding!!

  private val searchViewModel: SearchViewModel by viewModels()
  private lateinit var searchAdapter: SearchAdapter
  private lateinit var shimmerAdapter: ShimmerAdapter

  private var lastQuery: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    searchAdapter = SearchAdapter(navigator)
    shimmerAdapter = ShimmerAdapter()
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
    (activity as AppCompatActivity).setSupportActionBar(binding.toolbarLayout.toolbar)
    (activity as AppCompatActivity).supportActionBar?.title = null
    binding.appBarLayout.setExpanded(true, true)

    binding.rvSearch.layoutManager = initLinearLayoutManagerVertical(requireContext())

    binding.illustrationError.btnTryAgain.setOnClickListener {
      searchAdapter.refresh()
      binding.illustrationError.btnTryAgain.isVisible = false
      binding.illustrationError.progressCircular.isVisible = true
      showShimmer()
    }
    binding.swipeRefresh.setOnRefreshListener {
      searchAdapter.refresh()
      binding.swipeRefresh.isRefreshing = false
    }

    adapterLoadStateListener()
    setupSearchView()
    collectAndSubmitData(this, { searchViewModel.searchResults }, searchAdapter)
  }

  private fun showShimmer() {
    binding.rvSearch.adapter = shimmerAdapter
  }

  private fun showActualData() {
    val currentAdapter = binding.rvSearch.adapter
    if (currentAdapter !is ConcatAdapter || !currentAdapter.adapters.contains(searchAdapter)) {
      binding.rvSearch.setupRecyclerView(requireContext(), searchAdapter)
    }
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
                binding.illustrationSearchView.root.isVisible = false
                showShimmer()
              } else {
                return true
              }
              searchView.clearFocus()
              return false
            }

            override fun onQueryTextChange(query: String?): Boolean = true
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

  private fun adapterLoadStateListener() {
    lifecycleScope.launch {
      searchAdapter.loadStateFlow.collect { loadState ->
        when (loadState.source.refresh) {
          is LoadState.Loading -> {
            // Data is loading; keep showing the containerSearch
            binding.illustrationSearchView.root.isVisible = false
            binding.illustrationError.root.isVisible = false
            binding.illustrationSearchNoResultView.root.isVisible = false
            binding.rvSearch.isVisible = true
          }

          is LoadState.NotLoading -> {
            binding.illustrationError.root.isVisible = false
            binding.illustrationError.btnTryAgain.isVisible = false
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
              showActualData()
              binding.rvSearch.isVisible = true
              binding.illustrationSearchView.root.isVisible = false
              binding.illustrationSearchNoResultView.root.isVisible = false
            }
          }

          is LoadState.Error -> {
            // Error occurred; handle error state
            lastQuery = null
            showActualData()
            if (searchAdapter.itemCount < 1) {
              binding.illustrationError.root.isVisible = true
              binding.rvSearch.isVisible = false
            } else {
              binding.illustrationError.root.isVisible = false
              binding.rvSearch.isVisible = true
            }
            binding.illustrationSearchView.root.isVisible = false
            binding.illustrationError.progressCircular.isVisible = false
            binding.illustrationError.btnTryAgain.isVisible = true

            // show snackbar
            pagingErrorState(loadState)?.let {
              snackbar.showSnackbarWarning(Event(pagingErrorHandling(it.error)))
            }
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
    searchAdapter.refresh()
    binding.appBarLayout.setExpanded(true)
    searchViewModel.setExpandSearchView(false)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    searchViewModel.setExpandSearchView(false) // reset expand search view
    lastQuery = null
    _binding = null
  }
}
