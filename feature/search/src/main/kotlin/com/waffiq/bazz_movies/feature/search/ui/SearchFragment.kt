@file:Suppress("BackingPropertyNaming")

package com.waffiq.bazz_movies.feature.search.ui

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import com.google.android.material.R.id.open_search_view_clear_button
import com.google.android.material.R.id.open_search_view_toolbar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.designsystem.R.color.yellow
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_cross
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.utils.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.utils.GeneralHelper.initLinearLayoutManagerVertical
import com.waffiq.bazz_movies.core.utils.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.core.utils.PagingLoadStateHelper.pagingErrorState
import com.waffiq.bazz_movies.feature.search.databinding.FragmentSearchBinding
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.setupRecyclerView
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("TooManyFunctions")
@AndroidEntryPoint
class SearchFragment : Fragment() {

  @Inject
  lateinit var navigator: INavigator

  @Inject
  lateinit var snackbar: ISnackbar

  private var _binding: FragmentSearchBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val searchViewModel: SearchViewModel by viewModels()
  private lateinit var searchAdapter: SearchAdapter
  private lateinit var shimmerAdapter: ShimmerAdapter

  private var lastQuery: String? = null
  private var mSnackbar: Snackbar? = null
  private var lastRefreshErrorMessage: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    searchAdapter = SearchAdapter(navigator)
    shimmerAdapter = ShimmerAdapter()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentSearchBinding.inflate(inflater, container, false)
    binding.searchView.hide()
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.searchView.hide()
    binding.rvSearch.layoutManager = initLinearLayoutManagerVertical(requireContext())

    binding.illustrationError.btnTryAgain.setOnClickListener {
      lastRefreshErrorMessage = null
      searchAdapter.refresh()
      binding.illustrationError.btnTryAgain.isVisible = false
      binding.illustrationError.progressCircular.isVisible = true
      showShimmer()
    }

    binding.swipeRefresh.setOnRefreshListener {
      searchAdapter.refresh()
      binding.swipeRefresh.isRefreshing = false
    }

    setupMaterialSearchView()
    adapterLoadStateListener()
    setSearchBarScrollable(false)

    // Set up fragment result listener
    requireActivity().supportFragmentManager.setFragmentResultListener(
      "open_search_view",
      viewLifecycleOwner,
    ) { _, _ ->
      openSearchView()
    }

    collectAndSubmitData(this, { searchViewModel.searchResults }, searchAdapter)
  }

  private fun showShimmer() {
    binding.rvSearch.adapter = shimmerAdapter
  }

  private fun setSearchBarScrollable(scrollable: Boolean) {
    val params = binding.searchBar.layoutParams as AppBarLayout.LayoutParams
    params.scrollFlags = if (scrollable) {
      AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
        AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or
        AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
    } else {
      0 // no scroll flags = AppBar stays pinned
    }
    binding.searchBar.layoutParams = params
    if (!scrollable) {
      binding.appBarLayout.setExpanded(true, true)
    }
  }

  private fun showActualData() {
    val currentAdapter = binding.rvSearch.adapter
    if (currentAdapter !is ConcatAdapter || !currentAdapter.adapters.contains(searchAdapter)) {
      binding.rvSearch.setupRecyclerView(requireContext(), searchAdapter)
    }
  }

  private fun setupMaterialSearchView() {
    // Change back button and close button on search view
    val toolbar = ViewCompat.requireViewById<MaterialToolbar>(
      binding.searchView,
      open_search_view_toolbar,
    )
    toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), ic_cross)
    toolbar.setNavigationIconTint(ContextCompat.getColor(requireContext(), yellow))

    val clearButton = ViewCompat.requireViewById<ImageButton>(
      binding.searchView,
      open_search_view_clear_button,
    )
    clearButton.setImageResource(ic_cross)
    clearButton.imageTintList = ColorStateList.valueOf(
      ContextCompat.getColor(requireContext(), yellow),
    )

    // Setup SearchView text change listener
    binding.searchView.editText.setOnEditorActionListener { textView, _, _ ->
      val query = textView.text.toString()
      if (query.isNotEmpty() && query != lastQuery) {
        performSearch(query)
        binding.searchBar.setText(query)
      }
      binding.searchView.hide()
      false
    }
  }

  private fun performSearch(query: String) {
    lifecycleScope.launch {
      searchAdapter.submitData(PagingData.empty())
    }
    searchAdapter.refresh()
    lastQuery = query
    searchViewModel.search(query)
    binding.illustrationSearchView.root.isVisible = false
    showShimmer()
  }

  private fun adapterLoadStateListener() {
    lifecycleScope.launch {
      searchAdapter.loadStateFlow
        .debounce(DEBOUNCE_SHORT)
        .collectLatest { loadState ->
          val currentRefresh = loadState.source.refresh
          val errorMessage = (currentRefresh as? LoadState.Error)?.error?.message
          val isNewError =
            currentRefresh is LoadState.Error && errorMessage != lastRefreshErrorMessage

          if (currentRefresh !is LoadState.Error || isNewError) {
            lastRefreshErrorMessage = errorMessage
            handleRefreshState(loadState, currentRefresh)
          }
        }
    }
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  internal fun handleRefreshState(loadState: CombinedLoadStates, refreshState: LoadState) {
    when (refreshState) {
      is LoadState.Loading -> showLoadingState()
      is LoadState.NotLoading -> showNotLoadingState(loadState)
      is LoadState.Error -> showErrorState(loadState)
    }
  }

  private fun showLoadingState() {
    binding.illustrationSearchView.root.isVisible = false
    binding.illustrationError.root.isVisible = false
    binding.illustrationSearchNoResultView.root.isVisible = false
    binding.rvSearch.isVisible = true
  }

  private fun showNotLoadingState(loadState: CombinedLoadStates) {
    binding.illustrationError.root.isVisible = false
    binding.illustrationError.btnTryAgain.isVisible = false

    if (loadState.append.endOfPaginationReached && searchAdapter.itemCount < 1) {
      setSearchBarScrollable(false)
      binding.rvSearch.isVisible = false
      binding.illustrationSearchNoResultView.root.isVisible = true
      binding.illustrationSearchView.root.isVisible = false
    } else if (!loadState.append.endOfPaginationReached && searchAdapter.itemCount < 1) {
      setSearchBarScrollable(false)
      binding.rvSearch.isVisible = false
      binding.illustrationSearchView.root.isVisible = true
      binding.illustrationSearchNoResultView.root.isVisible = false
    } else {
      showActualData()
      setSearchBarScrollable(true)
      binding.rvSearch.isVisible = true
      binding.illustrationSearchView.root.isVisible = false
      binding.illustrationSearchNoResultView.root.isVisible = false
    }
  }

  private fun showErrorState(loadState: CombinedLoadStates) {
    lastQuery = null
    showActualData()

    val hasNoItems = searchAdapter.itemCount < 1
    binding.illustrationError.root.isVisible = hasNoItems
    binding.rvSearch.isVisible = !hasNoItems
    binding.illustrationSearchView.root.isVisible = false
    binding.illustrationError.progressCircular.isVisible = false
    binding.illustrationError.btnTryAgain.isVisible = true

    pagingErrorState(loadState)?.let {
      mSnackbar = snackbar.showSnackbarWarning(Event(pagingErrorHandling(it.error)))
    }
  }

  fun openSearchView() {
    if (!isAdded || isDetached || view == null) return

    @Suppress("TooGenericExceptionCaught")
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.withStarted {
        try {
          binding.searchView.hide()
          binding.searchView.show()

          // request focus and show keyboard
          binding.searchView.requestFocus()
          WindowCompat.getInsetsController(requireActivity().window, binding.searchView)
        } catch (e: IllegalStateException) {
          Log.w("SearchFragment", "Illegal state while opening search view.", e)
        }
      }
    }
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    if (newConfig.keyboardHidden == Configuration.KEYBOARDHIDDEN_YES) {
      onKeyboardHidden()
    }
  }

  override fun onPause() {
    super.onPause()
    mSnackbar?.dismiss()
    mSnackbar = null
  }

  override fun onViewStateRestored(savedInstanceState: Bundle?) {
    super.onViewStateRestored(savedInstanceState)
    binding.searchView.hide()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    mSnackbar = null
    lastQuery = null
    _binding = null
  }

  override fun onStart() {
    super.onStart()
    binding.searchView.hide()
  }

  override fun onResume() {
    super.onResume()
    binding.searchView.hide()
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun onKeyboardHidden() {
    binding.appBarLayout.setExpanded(true)
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun setAdapterForTest(adapter: SearchAdapter) {
    this.searchAdapter = adapter
    binding.rvSearch.adapter = adapter
  }
}
