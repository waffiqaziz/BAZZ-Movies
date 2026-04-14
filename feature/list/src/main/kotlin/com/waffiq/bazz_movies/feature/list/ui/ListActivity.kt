package com.waffiq.bazz_movies.feature.list.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_BACKDROP_W780
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_logo
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_grid
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_list
import com.waffiq.bazz_movies.core.designsystem.R.string.airing_this_week
import com.waffiq.bazz_movies.core.designsystem.R.string.airing_today
import com.waffiq.bazz_movies.core.designsystem.R.string.now_playing
import com.waffiq.bazz_movies.core.designsystem.R.string.popular
import com.waffiq.bazz_movies.core.designsystem.R.string.recommendation
import com.waffiq.bazz_movies.core.designsystem.R.string.toggle_grid_layout
import com.waffiq.bazz_movies.core.designsystem.R.string.toggle_list_layout
import com.waffiq.bazz_movies.core.designsystem.R.string.top_rated
import com.waffiq.bazz_movies.core.designsystem.R.string.upcoming
import com.waffiq.bazz_movies.core.uihelper.mappers.UIStateMapper.toUiState
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.uihelper.state.isLoading
import com.waffiq.bazz_movies.core.utils.FlowUtils.load
import com.waffiq.bazz_movies.core.utils.GenreHelper.getGenreName
import com.waffiq.bazz_movies.feature.list.databinding.ActivityListBinding
import com.waffiq.bazz_movies.feature.list.ui.adapter.ListAdapter
import com.waffiq.bazz_movies.feature.list.ui.viewmodel.ListViewModel
import com.waffiq.bazz_movies.feature.list.utils.BackdropHelper.getBackdrop
import com.waffiq.bazz_movies.feature.list.utils.Helper.capitaliseEachWord
import com.waffiq.bazz_movies.feature.list.utils.ParcelableHelper.extractArgsItemFromIntent
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ListActivity : AppCompatActivity() {

  @Inject
  lateinit var navigator: INavigator

  private lateinit var binding: ActivityListBinding

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  lateinit var adapter: ListAdapter

  private val viewModel: ListViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
    )
    binding = ActivityListBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // get data extra
    val args = extractDataFromIntent() ?: run {
      finish()
      return
    }

    buttonAction()
    setupRecyclerView()
    setupList(args)
    observeLoadState()
  }

  private fun extractDataFromIntent(): ListArgs? = extractArgsItemFromIntent(intent)

  private var shouldUpdateBackdropFromItems = false

  private fun setupList(args: ListArgs) {
    adapter.setMediaType(args.mediaType)
    binding.toolbar.subtitle = args.mediaType.uppercase()

    shouldUpdateBackdropFromItems = args.listType.shouldUpdateBackdrop()

    handleListType(args)
  }

  private val handlers: Map<ListType, (ListArgs) -> Unit> = mapOf(
    ListType.BY_GENRE to ::showListBasedGenre,
    ListType.BY_KEYWORD to ::showListBasedKeywords,
    ListType.NOW_PLAYING to ::showNowPlaying,
    ListType.POPULAR to ::showPopular,
    ListType.TOP_RATED to ::showTopRated,
    ListType.UPCOMING to { showUpcomingMovies() },
    ListType.AIRING_THIS_WEEK to { showTvAiringThisWeek() },
    ListType.RECOMMENDATION to ::showRecommendation,
  )

  private fun handleListType(args: ListArgs) {
    handlers.getValue(args.listType).invoke(args)
  }

  private fun observeLoadState() {
    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        adapter.loadStateFlow
          .distinctUntilChangedBy { it.refresh }
          .collect { loadStates ->
            handleRefreshState(loadStates.toUiState())
            if (shouldUpdateBackdropFromItems) loadStateChanged()
          }
      }
    }
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  internal fun handleRefreshState(state: UIState<Unit>) {
    binding.loadingIndicator.isVisible = state.isLoading
    binding.rvList.isVisible = state is UIState.Success
    binding.illustrationError.root.isVisible = state is UIState.Error
    binding.illustrationError.progressCircular.isVisible = false
    binding.illustrationError.btnTryAgain.isVisible = state is UIState.Error
  }

  private fun showListBasedGenre(args: ListArgs) {
    showBackdrop(getBackdrop(args.mediaType, args.id))
    binding.toolbar.title = getGenreName(args.id)
    load(viewModel.getByGenre(args.mediaType, args.id.toString()), adapter)
  }

  private fun showListBasedKeywords(args: ListArgs) {
    setToolbarTitle(args.title.capitaliseEachWord())
    load(viewModel.getByKeyword(args.mediaType, args.id.toString()), adapter)
  }

  private fun showUpcomingMovies() {
    setToolbarTitle(upcoming)
    load(viewModel.getUpcomingMovies(), adapter)
  }

  private fun showTvAiringThisWeek() {
    setToolbarTitle(airing_this_week)
    load(viewModel.getAiringThisWeekTv(), adapter)
  }

  private fun showNowPlaying(args: ListArgs) {
    setToolbarTitle(
      if (args.mediaType == MOVIE_MEDIA_TYPE) getString(now_playing) else getString(airing_today),
    )
    load(viewModel.getNowPlaying(args.mediaType), adapter)
  }

  private fun showTopRated(args: ListArgs) {
    setToolbarTitle(top_rated)
    load(viewModel.getTopRated(args.mediaType), adapter)
  }

  private fun showPopular(args: ListArgs) {
    setToolbarTitle(popular)
    load(viewModel.getPopular(args.mediaType), adapter)
  }

  private fun showRecommendation(args: ListArgs) {
    setToolbarTitle(args.title)
    binding.toolbar.subtitle = getString(recommendation)
    showBackdrop(args.backdrop)
    load(viewModel.getRecommendation(args.mediaType, args.id), adapter)
  }

  private fun setToolbarTitle(@StringRes res: Int) {
    binding.toolbar.title = getString(res)
  }

  private fun setToolbarTitle(text: String) {
    binding.toolbar.title = text
  }

  @VisibleForTesting
  internal fun loadStateChanged() {
    if (adapter.itemCount >= 1) {
      showBackdrop(adapter.snapshot().items.first().backdropPath)
    }
  }

  private fun showBackdrop(backdrop: String?) {
    Glide.with(binding.ivPicture)
      .load(TMDB_IMG_LINK_BACKDROP_W780 + backdrop)
      .placeholder(ic_bazz_logo)
      .transition(withCrossFade())
      .error(ic_broken_image)
      .into(binding.ivPicture)
  }

  private fun setupRecyclerView() {
    adapter = ListAdapter(navigator)
    binding.rvList.layoutManager = GridLayoutManager(
      this,
      calculateSpanCount(),
      GridLayoutManager.VERTICAL,
      false,
    )
    binding.rvList.adapter = adapter
  }

  private fun buttonAction() {
    binding.btnClose.setOnClickListener { finish() }
    binding.swipeRefresh.setOnRefreshListener {
      adapter.refresh()
      binding.swipeRefresh.isRefreshing = false
    }
    binding.illustrationError.btnTryAgain.setOnClickListener {
      adapter.refresh()
      binding.swipeRefresh.isRefreshing = false
    }
    binding.btnToggleLayout.setOnClickListener { toggleLayout() }
  }

  private fun toggleLayout() {
    val isGrid = !adapter.isGridMode()

    binding.btnToggleLayout.contentDescription = getString(
      if (isGrid) toggle_list_layout else toggle_grid_layout,
    )

    // save scroll state from the outgoing LayoutManager
    val savedState = binding.rvList.layoutManager!!.onSaveInstanceState()
    binding.rvList.recycledViewPool.clear() // clear stale holders

    // update the layout manager
    adapter.setGridMode(isGrid)
    binding.rvList.layoutManager = if (isGrid) {
      GridLayoutManager(this, calculateSpanCount(), GridLayoutManager.VERTICAL, false)
    } else {
      LinearLayoutManager(this)
    }

    // set state into the incoming LayoutManager
    binding.rvList.layoutManager!!.onRestoreInstanceState(savedState)

    // swap icon
    binding.btnToggleLayout.setIconResource(if (isGrid) ic_grid else ic_list)
  }

  private fun calculateSpanCount(): Int {
    val displayMetrics = resources.displayMetrics
    val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
    val columnWidthDp = COLUMN_WIDTH
    return (screenWidthDp / columnWidthDp).toInt().coerceAtLeast(2)
  }

  companion object {
    const val COLUMN_WIDTH = 120f
    const val EXTRA_LIST = "LIST"
  }
}
