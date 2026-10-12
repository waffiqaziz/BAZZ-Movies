package com.waffiq.bazz_movies.feature.list.ui

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.common.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.Constants.TMDB_IMG_LINK_BACKDROP_W780
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_logo
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.core.designsystem.R.string.airing_this_week
import com.waffiq.bazz_movies.core.designsystem.R.string.airing_today
import com.waffiq.bazz_movies.core.designsystem.R.string.all_time
import com.waffiq.bazz_movies.core.designsystem.R.string.anime
import com.waffiq.bazz_movies.core.designsystem.R.string.asian
import com.waffiq.bazz_movies.core.designsystem.R.string.costume_drama
import com.waffiq.bazz_movies.core.designsystem.R.string.donghua
import com.waffiq.bazz_movies.core.designsystem.R.string.now_playing
import com.waffiq.bazz_movies.core.designsystem.R.string.popular
import com.waffiq.bazz_movies.core.designsystem.R.string.reality_show
import com.waffiq.bazz_movies.core.designsystem.R.string.recommendation
import com.waffiq.bazz_movies.core.designsystem.R.string.romance_drama
import com.waffiq.bazz_movies.core.designsystem.R.string.this_season
import com.waffiq.bazz_movies.core.designsystem.R.string.this_week
import com.waffiq.bazz_movies.core.designsystem.R.string.today
import com.waffiq.bazz_movies.core.designsystem.R.string.top_rated
import com.waffiq.bazz_movies.core.designsystem.R.string.trending
import com.waffiq.bazz_movies.core.designsystem.R.string.upcoming
import com.waffiq.bazz_movies.core.designsystem.R.string.view_options
import com.waffiq.bazz_movies.core.uihelper.mapper.UIStateMapper.toUiState
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.uihelper.state.isLoading
import com.waffiq.bazz_movies.core.uihelper.utils.InsetHelper.setupWindowInsets
import com.waffiq.bazz_movies.core.utils.FlowUtils.load
import com.waffiq.bazz_movies.core.utils.GenreHelper.toStringRes
import com.waffiq.bazz_movies.feature.list.databinding.ActivityListBinding
import com.waffiq.bazz_movies.feature.list.domain.model.ListViewMode
import com.waffiq.bazz_movies.feature.list.ui.adapter.ListAdapter
import com.waffiq.bazz_movies.feature.list.ui.viewmodel.ListViewModel
import com.waffiq.bazz_movies.feature.list.utils.BackdropHelper.getBackdrop
import com.waffiq.bazz_movies.feature.list.utils.Helper.capitaliseEachWord
import com.waffiq.bazz_movies.feature.list.utils.RecyclerViewLayoutHelper.restoreInstanceState
import com.waffiq.bazz_movies.feature.list.utils.RecyclerViewLayoutHelper.saveInstanceState
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType
import com.waffiq.bazz_movies.navigation.MediaSource
import com.waffiq.bazz_movies.navigation.extractParcelableExtraFromIntent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@Suppress("TooManyFunctions")
@AndroidEntryPoint
class ListActivity : AppCompatActivity() {

  @Inject
  lateinit var navigator: INavigator

  private lateinit var binding: ActivityListBinding
  private lateinit var adapter: ListAdapter

  private val viewModel: ListViewModel by viewModels()

  private var viewMode = ListViewMode.TWO_COLUMNS

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
    )
    binding = ActivityListBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.root.setupWindowInsets()

    // get data extra
    val args = extractDataFromIntent() ?: run {
      finish()
      return
    }
    viewMode = restoreViewMode(savedInstanceState?.getString(STATE_VIEW_MODE))

    buttonAction()
    setupRecyclerView(args.mediaType)
    setupList(args)
    observeLoadState()
    observeViewModeResult()
    adjustAdapterPadding()
  }

  private fun extractDataFromIntent(): ListArgs? =
    extractParcelableExtraFromIntent<ListArgs>(intent, EXTRA_LIST)

  private var shouldUpdateBackdropFromItems = false

  private fun setupList(args: ListArgs) {
    binding.toolbar.subtitle = args.mediaType.typeName.uppercase()

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
    ListType.TRENDING_TODAY to ::showTrendingToday,
    ListType.TRENDING_WEEK to ::showTrendingThisWeek,
    ListType.ANIME_ALL_TIME to ::showAnimeAllTime,
    ListType.ANIME_THIS_SEASON to ::showAnimeThisSeason,
    ListType.COSTUME_DRAMA to { showCostumeDrama() },
    ListType.DONGHUA to { showDonghua() },
    ListType.ROMANCE_DRAMA to { showRomanceDrama() },
    ListType.REALITY_SHOW to { showRealityShow() },
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

  // region SHOW BASED LIST TYPE
  private fun showListBasedGenre(args: ListArgs) {
    showBackdrop(getBackdrop(args.mediaType.typeName, args.id))
    binding.toolbar.title = getString(args.id.toStringRes())
    load(viewModel.getByGenre(args.mediaType.typeName, args.id.toString()), adapter)
  }

  private fun showListBasedKeywords(args: ListArgs) {
    setToolbarTitle(args.title.capitaliseEachWord())
    load(viewModel.getByKeyword(args.mediaType.typeName, args.id.toString()), adapter)
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
      if (args.mediaType.typeName == MOVIE_MEDIA_TYPE) {
        getString(now_playing)
      } else {
        getString(airing_today)
      },
    )
    load(viewModel.getNowPlaying(args.mediaType.typeName), adapter)
  }

  private fun showTopRated(args: ListArgs) {
    setToolbarTitle(top_rated)
    load(viewModel.getTopRated(args.mediaType.typeName), adapter)
  }

  private fun showPopular(args: ListArgs) {
    setToolbarTitle(popular)
    load(viewModel.getPopular(args.mediaType.typeName), adapter)
  }

  private fun showRecommendation(args: ListArgs) {
    setToolbarTitle(args.title)
    setToolbarSubTitle(recommendation)
    showBackdrop(args.backdrop)
    load(viewModel.getRecommendation(args.mediaType.typeName, args.id), adapter)
  }

  private fun showTrendingToday(args: ListArgs) {
    setToolbarTitle(trending)
    setToolbarSubTitle(today)
    load(viewModel.getTrending(args.listType), adapter)
  }

  private fun showTrendingThisWeek(args: ListArgs) {
    setToolbarTitle(trending)
    setToolbarSubTitle(this_week)
    load(viewModel.getTrending(args.listType), adapter)
  }

  private fun showAnimeAllTime(args: ListArgs) {
    setToolbarTitle(anime)
    setToolbarSubTitle(all_time)
    load(viewModel.getAnime(args.listType), adapter)
  }

  private fun showAnimeThisSeason(args: ListArgs) {
    setToolbarTitle(anime)
    setToolbarSubTitle(this_season)
    load(viewModel.getAnime(args.listType), adapter)
  }

  private fun showCostumeDrama() {
    setToolbarTitle(asian)
    setToolbarSubTitle(costume_drama)
    load(viewModel.getCostumeDrama(), adapter)
  }

  private fun showRomanceDrama() {
    setToolbarTitle(asian)
    setToolbarSubTitle(romance_drama)
    load(viewModel.getAsianRomance(), adapter)
  }

  private fun showRealityShow() {
    setToolbarTitle(asian)
    setToolbarSubTitle(reality_show)
    load(viewModel.getRealityShow(), adapter)
  }

  private fun showDonghua() {
    setToolbarTitle(asian)
    setToolbarSubTitle(donghua)
    load(viewModel.getDonghua(), adapter)
  }
  // endregion SHOW BASED LIST TYPE

  private fun setToolbarTitle(@StringRes res: Int) {
    binding.toolbar.title = getString(res)
  }

  private fun setToolbarTitle(text: String) {
    binding.toolbar.title = text
  }

  private fun setToolbarSubTitle(@StringRes res: Int) {
    binding.toolbar.subtitle = getString(res)
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

  private fun setupRecyclerView(mediaSource: MediaSource) {
    adapter = ListAdapter(navigator, mediaSource).also { it.setViewMode(viewMode) }
    binding.rvList.layoutManager = createLayoutManager(viewMode)
    binding.rvList.adapter = adapter
    binding.btnToggleLayout.setIconResource(viewMode.icon)
    binding.btnToggleLayout.contentDescription = getString(view_options)
  }

  private fun createLayoutManager(mode: ListViewMode): RecyclerView.LayoutManager =
    if (mode.isDetailed) {
      LinearLayoutManager(this)
    } else {
      GridLayoutManager(this, mode.spanCount, GridLayoutManager.VERTICAL, false)
    }

  private fun observeViewModeResult() {
    supportFragmentManager.setFragmentResultListener(
      ViewModeBottomSheet.REQUEST_KEY,
      this,
    ) { _, bundle ->
      bundle.getString(ViewModeBottomSheet.RESULT_MODE)
        ?.let { applyViewMode(ListViewMode.valueOf(it)) }
    }
  }

  private fun applyViewMode(mode: ListViewMode) {
    if (mode == viewMode) return
    val typeChanged = viewMode.isDetailed != mode.isDetailed
    viewMode = mode

    if (typeChanged) {
      // grid to detailed, swap the layout manager and keep scroll position
      val savedState = binding.rvList.saveInstanceState
      binding.rvList.recycledViewPool.clear()
      adapter.setViewMode(mode)
      binding.rvList.layoutManager = createLayoutManager(mode)
      binding.rvList.restoreInstanceState(savedState)
    } else {
      // grid 2 to 3 columns, only changes the span count
      adapter.setViewMode(mode)
      (binding.rvList.layoutManager as GridLayoutManager).spanCount = mode.spanCount
    }

    binding.btnToggleLayout.setIconResource(mode.icon)
    adjustAdapterPadding()
  }

  private fun adjustAdapterPadding() {
    if (viewMode.isDetailed) {
      binding.rvList.updatePadding(0.dp, 6.dp, 0.dp, 6.dp)
    } else {
      binding.rvList.updatePadding(8.dp, 6.dp, 8.dp, 6.dp)
    }
  }

  private fun buttonAction() {
    binding.btnBack.setOnClickListener { finish() }
    binding.swipeRefresh.setOnRefreshListener {
      adapter.refresh()
      binding.swipeRefresh.isRefreshing = false
    }
    binding.illustrationError.btnTryAgain.setOnClickListener {
      adapter.refresh()
      binding.swipeRefresh.isRefreshing = false
    }
    binding.btnToggleLayout.setOnClickListener {
      ViewModeBottomSheet.newInstance(viewMode)
        .show(supportFragmentManager, ViewModeBottomSheet.TAG)
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString(STATE_VIEW_MODE, viewMode.name)
  }

  val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

  companion object {
    const val EXTRA_LIST = "LIST"

    const val STATE_VIEW_MODE = "STATE_VIEW_MODE"

    internal fun restoreViewMode(saved: String?): ListViewMode =
      ListViewMode.entries.firstOrNull { it.name == saved } ?: ListViewMode.TWO_COLUMNS
  }
}
