@file:Suppress("BackingPropertyNaming")

package com.waffiq.bazz_movies.feature.search.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageButton
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.withStarted
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.R.id.open_search_view_clear_button
import com.google.android.material.R.id.open_search_view_toolbar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.Genre
import com.waffiq.bazz_movies.core.common.MediaType
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.designsystem.R.color.yellow
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_cross
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_left_icon
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.utils.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.utils.LayoutHelper.initLinearLayoutManagerVertical
import com.waffiq.bazz_movies.core.utils.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.core.utils.PagingLoadStateHelper.pagingErrorState
import com.waffiq.bazz_movies.feature.search.R.id.btn_movie
import com.waffiq.bazz_movies.feature.search.databinding.FragmentSearchBinding
import com.waffiq.bazz_movies.feature.search.ui.adapter.GenreAdapter
import com.waffiq.bazz_movies.feature.search.ui.adapter.GridSpacingItemDecoration
import com.waffiq.bazz_movies.feature.search.ui.adapter.SearchAdapter
import com.waffiq.bazz_movies.feature.search.ui.adapter.SearchHistoryAdapter
import com.waffiq.bazz_movies.feature.search.ui.adapter.ShimmerAdapter
import com.waffiq.bazz_movies.feature.search.ui.viewmodel.SearchViewModel
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.setupRecyclerView
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

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
  private lateinit var searchHistoryAdapter: SearchHistoryAdapter
  private lateinit var genreAdapter: GenreAdapter

  private var lastQuery: String? = null
  private var mSnackbar: Snackbar? = null
  private var lastRefreshErrorMessage: String? = null

  private var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  internal var loadStateFlowProvider: Flow<CombinedLoadStates>? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    searchAdapter = SearchAdapter(navigator)
    shimmerAdapter = ShimmerAdapter()
    searchHistoryAdapter = SearchHistoryAdapter(
      onItemClick = { query ->
        // click history item will run search
        binding.searchView.editText.setText(query)
        binding.searchView.hide()
        if (query != lastQuery) performSearch(query)
        binding.searchBar.setText(query)
      },
      onDeleteClick = { item ->
        searchViewModel.deleteHistory(item)
      },
    )
    genreAdapter = GenreAdapter(navigator)
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

    setupKeyboardScroll()
    setupAction()
    setupToggle()
    setupGenreList(MediaType.MOVIE)
    setupMaterialSearchView()
    adapterLoadStateListener()
    setSearchBarScrollable(false)
    setupSearchHistoryRecyclerView()
    observeSearchHistory()

    // Set up fragment result listener
    setupFragmentResult()

    collectAndSubmitData(this, { searchViewModel.searchResults }, searchAdapter)
  }

  private fun setupAction() {
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
  }

  private fun setupGenreList(mediaType: MediaType) {
    binding.rvGenre.layoutManager = GridLayoutManager(requireContext(), 2)
    if (binding.rvGenre.itemDecorationCount == 0) {
      binding.rvGenre.addItemDecoration(GridSpacingItemDecoration(2, GRID_SPACING))
    }
    binding.rvGenre.adapter = genreAdapter
    genreAdapter.setMediaType(mediaType)
    genreAdapter.submitList(Genre.forMediaType(mediaType))
  }

  private fun setupToggle() {
    binding.toggleMediaType.addOnButtonCheckedListener { _, checkedId, isChecked ->
      if (!isChecked) return@addOnButtonCheckedListener
      val mediaType = if (checkedId == btn_movie) MediaType.MOVIE else MediaType.TV
      setupGenreList(mediaType)
    }
  }

  private fun showShimmer() {
    binding.browseGenreContainer.isVisible = false
    binding.rvSearch.adapter = shimmerAdapter
  }

  private fun setupKeyboardScroll() {
    val rootView = requireActivity().window.decorView.rootView

    globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
      val rect = android.graphics.Rect()
      rootView.getWindowVisibleDisplayFrame(rect)
      val screenHeight = rootView.height
      val keyboardHeight = screenHeight - rect.bottom

      if (keyboardHeight > screenHeight * SOFT_KEYBOARD_PERCENTAGE) {
        binding.rvSearchHistory.setPadding(0, 0, 0, keyboardHeight)
      } else {
        binding.rvSearchHistory.setPadding(0, 0, 0, ADDITION_PADDING)
      }
    }

    rootView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
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

  private fun setupSearchHistoryRecyclerView() {
    binding.rvSearchHistory.layoutManager = LinearLayoutManager(requireContext())
    binding.rvSearchHistory.adapter = searchHistoryAdapter

    binding.btnClearAll.setOnClickListener {
      searchViewModel.deleteAllHistory()
    }
  }

  private fun observeSearchHistory() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        searchViewModel.searchHistory.collect { history ->
          searchHistoryAdapter.submitList(history)

          // show/hide header based on whether there is any history
          binding.historyHeader.isVisible = history.isNotEmpty()
        }
      }
    }
  }

  private fun showActualData() {
    binding.browseGenreContainer.isVisible = false
    binding.swipeRefresh.isVisible = true
    val currentAdapter = binding.rvSearch.adapter
    if (currentAdapter !is ConcatAdapter || !currentAdapter.adapters.contains(searchAdapter)) {
      binding.rvSearch.setupRecyclerView(requireContext(), searchAdapter)
    }
  }

  private fun setupMaterialSearchView() {
    // set navigation icon with custom left icon
    binding.searchView.post {
      val toolbar = ViewCompat.requireViewById<MaterialToolbar>(
        binding.searchView,
        open_search_view_toolbar,
      )
      toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), ic_left_icon)
      toolbar.setNavigationIconTint(ContextCompat.getColor(requireContext(), yellow))
    }

    // set clear icon with custom croll icon
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
    viewLifecycleOwner.lifecycleScope.launch {
      searchAdapter.submitData(PagingData.empty())
    }
    searchAdapter.refresh()
    lastQuery = query
    searchViewModel.search(query)
    binding.swipeRefresh.isVisible = true
    binding.browseGenreContainer.isVisible = false
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun adapterLoadStateListener() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        (loadStateFlowProvider ?: searchAdapter.loadStateFlow)
          .debounce(DEBOUNCE_SHORT.milliseconds)
          .collectLatest { loadState ->
            val currentRefresh = loadState.source.refresh

            if (currentRefresh is LoadState.Error) {
              val errorMessage = currentRefresh.error.message
              if (errorMessage != lastRefreshErrorMessage) {
                lastRefreshErrorMessage = errorMessage
                handleRefreshState(loadState, currentRefresh)
              }
            } else {
              lastRefreshErrorMessage = null
              handleRefreshState(loadState, currentRefresh)
            }
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
    showShimmer()
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
    } else if (!loadState.append.endOfPaginationReached && searchAdapter.itemCount < 1) {
      setSearchBarScrollable(false)
      binding.rvSearch.isVisible = false
      binding.illustrationSearchNoResultView.root.isVisible = false
    } else {
      showActualData()
      setSearchBarScrollable(true)
      binding.rvSearch.isVisible = true
      binding.illustrationSearchNoResultView.root.isVisible = false
    }
  }

  private fun showErrorState(loadState: CombinedLoadStates) {
    lastQuery = null
    showActualData()

    val hasNoItems = searchAdapter.itemCount < 1
    binding.illustrationError.root.isVisible = hasNoItems
    binding.rvSearch.isVisible = !hasNoItems
    binding.illustrationError.progressCircular.isVisible = false
    binding.illustrationError.btnTryAgain.isVisible = true

    pagingErrorState(loadState)?.let {
      mSnackbar = snackbar.showSnackbarWarning(pagingErrorHandling(it.error))
    }
  }

  private fun setupFragmentResult() {
    requireActivity().supportFragmentManager.setFragmentResultListener(
      "open_search_view",
      viewLifecycleOwner,
    ) { _, _ ->
      openSearchView()
    }
    requireActivity().supportFragmentManager.setFragmentResultListener(
      "clear_search_view",
      viewLifecycleOwner,
    ) { _, _ ->
      searchViewModel.clearSearch()
      lastQuery = null
      binding.searchBar.setText("")
    }
  }

  private fun openSearchView() {
    if (!isAdded || isDetached) return

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
    requireActivity().window.decorView.rootView
      .viewTreeObserver
      .removeOnGlobalLayoutListener(globalLayoutListener)
    globalLayoutListener = null
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

  @VisibleForTesting(otherwise = VisibleForTesting.NONE)
  fun setAdapterForTest(adapter: SearchAdapter) {
    this.searchAdapter = adapter
    binding.rvSearch.adapter = adapter
  }

  companion object {
    private const val ADDITION_PADDING = 246
    private const val SOFT_KEYBOARD_PERCENTAGE = 0.15
    private const val GRID_SPACING = 8
  }
}
