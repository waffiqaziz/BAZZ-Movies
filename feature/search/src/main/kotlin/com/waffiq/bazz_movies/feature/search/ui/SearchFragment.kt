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
import androidx.annotation.OptIn
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.paging.CombinedLoadStates
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.R.id.open_search_view_clear_button
import com.google.android.material.R.id.open_search_view_toolbar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.Genre
import com.waffiq.bazz_movies.core.common.MediaType
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_LONG
import com.waffiq.bazz_movies.core.designsystem.R.color.yellow
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_cross
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_left_icon
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.utils.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.utils.FlowUtils.launchOnStarted
import com.waffiq.bazz_movies.core.utils.LayoutHelper.initLinearLayoutManagerVertical
import com.waffiq.bazz_movies.core.utils.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.feature.search.R.id.action_filter
import com.waffiq.bazz_movies.feature.search.R.id.btn_movie
import com.waffiq.bazz_movies.feature.search.databinding.FragmentSearchBinding
import com.waffiq.bazz_movies.feature.search.ui.SearchFilterBottomSheet.Companion.REQUEST_FILTER_RESULT
import com.waffiq.bazz_movies.feature.search.ui.SearchFilterBottomSheet.Companion.RESULT_SELECTED
import com.waffiq.bazz_movies.feature.search.ui.adapter.GenreAdapter
import com.waffiq.bazz_movies.feature.search.ui.adapter.GridSpacingItemDecoration
import com.waffiq.bazz_movies.feature.search.ui.adapter.SearchAdapter
import com.waffiq.bazz_movies.feature.search.ui.adapter.SearchHistoryAdapter
import com.waffiq.bazz_movies.feature.search.ui.adapter.SearchHistoryHorizontalAdapter
import com.waffiq.bazz_movies.feature.search.ui.viewmodel.SearchViewModel
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.setupRecyclerView
import com.waffiq.bazz_movies.feature.search.utils.SearchLoadStateMapper
import com.waffiq.bazz_movies.feature.search.utils.toMediaTypeSet
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@Suppress("TooManyFunctions")
@OptIn(ExperimentalBadgeUtils::class)
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
  private lateinit var searchHistoryAdapter: SearchHistoryAdapter
  private lateinit var adapterRow1: SearchHistoryHorizontalAdapter
  private lateinit var adapterRow2: SearchHistoryHorizontalAdapter
  private var isSyncingScroll = false
  private lateinit var genreAdapter: GenreAdapter

  private var mSnackbar: Snackbar? = null
  private var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

  private var filterBadge: BadgeDrawable? = null

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  internal var loadStateFlowProvider: Flow<CombinedLoadStates>? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    searchAdapter = SearchAdapter(navigator)
    searchHistoryAdapter = SearchHistoryAdapter(
      onItemClick = ::onHistoryItemClicked,
      onDeleteClick = searchViewModel::deleteHistory,
    )
    adapterRow1 = SearchHistoryHorizontalAdapter(::onHistoryItemClicked)
    adapterRow2 = SearchHistoryHorizontalAdapter(::onHistoryItemClicked)

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

    setupKeyboardInsetHandling()
    setupAction()
    setupToggle()
    setupGenreList()
    setupMaterialSearchView()
    setSearchBarScrollable(false)
    setupSearchHistoryRecyclerView()

    observeSearchHistory()
    observeScreenState()
    setupFragmentResult()

    setupFilterMenu()
    observeSelectedFilters()

    collectAndSubmitData(this, { searchViewModel.searchResults }, searchAdapter)
  }

  // region search input

  private fun onHistoryItemClicked(query: String) {
    binding.searchView.editText.setText(query)
    binding.searchView.hide()
    binding.searchBar.setText(query)
    searchViewModel.search(query)
  }

  private fun setupMaterialSearchView() {
    binding.searchView.post {
      val toolbar = ViewCompat.requireViewById<MaterialToolbar>(
        binding.searchView,
        open_search_view_toolbar,
      )
      toolbar.navigationContentDescription = "Back"
      toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), ic_left_icon)
      toolbar.setNavigationIconTint(ContextCompat.getColor(requireContext(), yellow))
    }

    val clearButton = ViewCompat.requireViewById<ImageButton>(
      binding.searchView,
      open_search_view_clear_button,
    )
    clearButton.setImageResource(ic_cross)
    clearButton.imageTintList = ColorStateList.valueOf(
      ContextCompat.getColor(requireContext(), yellow),
    )

    binding.searchView.editText.setOnEditorActionListener { textView, _, _ ->
      val query = textView.text.toString()
      if (query.isNotEmpty()) {
        searchViewModel.search(query)
        binding.searchBar.setText(query)
      }
      binding.searchView.hide()
      true
    }
  }
  // endregion

  // region genre browsing
  private fun setupGenreList() {
    binding.rvGenre.layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
    if (binding.rvGenre.itemDecorationCount == 0) {
      binding.rvGenre.addItemDecoration(GridSpacingItemDecoration(SPAN_COUNT, GRID_SPACING))
    }
    binding.rvGenre.adapter = genreAdapter

    updateGenreList(MediaType.MOVIE)
  }

  private fun updateGenreList(mediaType: MediaType) {
    genreAdapter.setMediaType(mediaType)
    genreAdapter.submitList(Genre.forMediaType(mediaType))
  }

  private fun setupToggle() {
    binding.toggleMediaType.addOnButtonCheckedListener { _, checkedId, isChecked ->
      if (!isChecked) return@addOnButtonCheckedListener
      val mediaType = if (checkedId == btn_movie) MediaType.MOVIE else MediaType.TV
      updateGenreList(mediaType)
    }
  }
  // endregion

  // region search history
  private fun setupSearchHistoryRecyclerView() {
    binding.rvSearchHistory.layoutManager = initLinearLayoutManagerVertical(requireContext())
    binding.rvSearchHistory.adapter = searchHistoryAdapter
    binding.btnClearAll.setOnClickListener { searchViewModel.deleteAllHistory() }

    binding.rvSearchHistoryRow1.apply {
      layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      adapter = adapterRow1
    }

    binding.rvSearchHistoryRow2.apply {
      layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      adapter = adapterRow2
    }

    syncScroll(binding.rvSearchHistoryRow1, binding.rvSearchHistoryRow2)
    syncScroll(binding.rvSearchHistoryRow2, binding.rvSearchHistoryRow1)
  }

  private fun syncScroll(source: RecyclerView, target: RecyclerView) {
    source.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(
        recyclerView: RecyclerView,
        dx: Int,
        dy: Int,
      ) {
        if (isSyncingScroll) return
        isSyncingScroll = true
        target.scrollBy(dx, 0)
        isSyncingScroll = false
      }
    })
  }

  private fun observeSearchHistory() {
    launchOnStarted {
      searchViewModel.searchHistory.collect { history ->
        val show = history.isNotEmpty()

        searchHistoryAdapter.submitList(history)
        binding.historyHeader.isVisible = show

        // horizontal history
        val row1 = history.filterIndexed { index, _ -> index % 2 == 0 }
        val row2 = history.filterIndexed { index, _ -> index % 2 == 1 }

        adapterRow1.submitList(row1)
        adapterRow2.submitList(row2)
        binding.tvLastSearchHeader.isVisible = show
        binding.layoutSearchHistory.isVisible = show
      }
    }
  }
  // endregion

  // region screen state rendering

  // Combines adapter's paging load state with loadStateFlowProvider into one [SearchScreenState].
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  internal fun observeScreenState() {
    launchOnStarted {
      val loadStates = (loadStateFlowProvider ?: searchAdapter.loadStateFlow)
        .debounce(DEBOUNCE_LONG.milliseconds)

      combine(loadStates, searchViewModel.currentQuery) { loadState, query ->
        SearchLoadStateMapper.map(
          loadState = loadState,
          itemCount = searchAdapter.itemCount,
          hasActiveQuery = query != null,
        )
      }.collectLatest(::render)
    }
  }

  private fun render(state: SearchScreenState) {
    when (state) {
      SearchScreenState.Browse -> renderBrowse()
      SearchScreenState.Loading -> renderLoading()
      SearchScreenState.FetchingMore -> renderFetchingMore()
      SearchScreenState.Content -> renderContent()
      SearchScreenState.NoResults -> renderNoResults()
      is SearchScreenState.Error -> renderError(state.cause)
    }
  }

  // on progress, refresh already finished, and nothing to show yet
  private fun renderFetchingMore() {
    binding.browseGenreContainer.isVisible = false
    binding.rvSearch.isVisible = true
    binding.shimmer.root.isVisible = true
    binding.illustrationError.root.isVisible = false
    binding.illustrationSearchNoResultView.root.isVisible = false
    setSearchBarScrollable(false)
  }

  private fun renderBrowse() {
    binding.shimmer.root.isVisible = false
    binding.browseGenreContainer.isVisible = true
    binding.swipeRefresh.isVisible = false
    binding.illustrationError.root.isVisible = false
    binding.illustrationSearchNoResultView.root.isVisible = false
    setSearchBarScrollable(false)
  }

  private fun renderLoading() {
    binding.browseGenreContainer.isVisible = false
    binding.swipeRefresh.isVisible = true
    binding.rvSearch.isVisible = true
    binding.shimmer.root.isVisible = true

    binding.illustrationError.root.isVisible = false
    binding.illustrationSearchNoResultView.root.isVisible = false
  }

  private fun renderContent() {
    binding.rvSearch.setupRecyclerView(requireContext(), searchAdapter)
    binding.shimmer.root.isVisible = false
    binding.browseGenreContainer.isVisible = false
    binding.swipeRefresh.isVisible = true
    binding.illustrationError.root.isVisible = false
    binding.illustrationSearchNoResultView.root.isVisible = false
    binding.rvSearch.isVisible = true
    setSearchBarScrollable(true)
  }

  private fun renderNoResults() {
    binding.rvSearch.isVisible = false
    binding.shimmer.root.isVisible = false
    binding.browseGenreContainer.isVisible = false
    binding.illustrationError.root.isVisible = false
    binding.illustrationSearchNoResultView.root.isVisible = true
    setSearchBarScrollable(false)
  }

  private fun renderError(cause: Throwable) {
    val hasNoItems = searchAdapter.itemCount < 1
    binding.illustrationError.root.isVisible = hasNoItems
    binding.rvSearch.isVisible = !hasNoItems
    binding.browseGenreContainer.isVisible = false
    binding.illustrationError.progressCircular.isVisible = false
    binding.illustrationError.btnTryAgain.isVisible = true
    mSnackbar = snackbar.showSnackbarWarning(pagingErrorHandling(cause))
  }
  // endregion

  // region misc setup

  private fun setupAction() {
    binding.illustrationError.btnTryAgain.setOnClickListener {
      searchAdapter.refresh()
      binding.illustrationError.btnTryAgain.isVisible = false
      binding.illustrationError.progressCircular.isVisible = true
      binding.shimmer.root.isVisible = true
    }

    binding.swipeRefresh.setOnRefreshListener {
      searchAdapter.refresh()
      binding.swipeRefresh.isRefreshing = false
    }
  }

  // add bottom padding for recycler view history search when keyboard is shows up
  private fun setupKeyboardInsetHandling() {
    val rootView = requireActivity().window.decorView.rootView
    globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
      val rect = android.graphics.Rect()
      rootView.getWindowVisibleDisplayFrame(rect)
      val screenHeight = rootView.height
      val keyboardHeight = screenHeight - rect.bottom

      if (keyboardHeight > screenHeight * SOFT_KEYBOARD_PERCENTAGE) {
        binding.rvSearchHistory.setPadding(0, 0, 0, keyboardHeight)
      } else {
        binding.rvSearchHistory.setPadding(0, 0, 0, DEFAULT_HISTORY_BOTTOM_PADDING)
      }
    }

    rootView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
  }

  private fun setSearchBarScrollable(scrollable: Boolean) {
    val params = binding.toolbar.layoutParams as AppBarLayout.LayoutParams
    params.scrollFlags = if (scrollable) {
      AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
        AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or
        AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
    } else {
      0 // no scroll flags = AppBar stays pinned
    }
    binding.toolbar.layoutParams = params
    if (!scrollable) {
      binding.appBarLayout.setExpanded(true, true)
    }
  }

  private fun setupFragmentResult() {
    // setup search view result
    requireActivity().supportFragmentManager.setFragmentResultListener(
      REQUEST_OPEN_SEARCH_VIEW,
      viewLifecycleOwner,
    ) { _, _ -> openSearchView() }

    // setup clear search result
    requireActivity().supportFragmentManager.setFragmentResultListener(
      REQUEST_CLEAR_SEARCH_VIEW,
      viewLifecycleOwner,
    ) { _, _ ->
      searchViewModel.clearSearch()
      binding.searchBar.setText("")
    }

    // setup filter result
    childFragmentManager.setFragmentResultListener(
      REQUEST_FILTER_RESULT,
      viewLifecycleOwner,
    ) { _, bundle ->
      val selected = bundle.getStringArrayList(RESULT_SELECTED).toMediaTypeSet()
      searchViewModel.setFilters(selected)
    }
  }

  private fun setupFilterMenu() {
    binding.searchBar.setOnMenuItemClickListener {
      showFilterBottomSheet()
      true
    }
  }

  private fun showFilterBottomSheet() {
    SearchFilterBottomSheet.newInstance(searchViewModel.selectedFilters.value)
      .show(childFragmentManager, SearchFilterBottomSheet::class.java.simpleName)
  }

  private fun observeSelectedFilters() {
    launchOnStarted {
      searchViewModel.selectedFilters.collect { filters ->
        val count = (filters - MediaType.MULTI).size
        updateFilterBadge(count)
      }
    }
  }

  private fun updateFilterBadge(count: Int) {
    if (count > 0) {
      val badge = filterBadge ?: BadgeDrawable.create(requireContext()).also { filterBadge = it }
      badge.number = count
      badge.isVisible = true
      BadgeUtils.attachBadgeDrawable(badge, binding.searchBar, action_filter)
    } else {
      filterBadge?.isVisible = false
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
            .show(WindowInsetsCompat.Type.ime())
        } catch (e: IllegalStateException) {
          Log.w(TAG, "Illegal state while opening search view.", e)
        }
      }
    }
  }
  // endregion

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
    filterBadge?.let { BadgeUtils.detachBadgeDrawable(it, binding.searchBar, action_filter) }
    mSnackbar = null
    requireActivity().window.decorView.rootView
      .viewTreeObserver
      .removeOnGlobalLayoutListener(globalLayoutListener)
    globalLayoutListener = null
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

  private companion object {
    const val TAG = "SearchFragment"
    const val REQUEST_OPEN_SEARCH_VIEW = "open_search_view"
    const val REQUEST_CLEAR_SEARCH_VIEW = "clear_search_view"
    const val DEFAULT_HISTORY_BOTTOM_PADDING = 246
    const val GRID_SPACING = 8
    const val SOFT_KEYBOARD_PERCENTAGE = 0.15
    const val SPAN_COUNT = 2
  }
}
