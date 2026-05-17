@file:Suppress("BackingPropertyNaming")

package com.waffiq.bazz_movies.feature.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.designsystem.R.string.no_popular_series
import com.waffiq.bazz_movies.core.designsystem.R.string.no_series_airing_this_week
import com.waffiq.bazz_movies.core.designsystem.R.string.no_series_airing_today
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnap
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnapGridLayout
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.core.utils.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.feature.home.databinding.FragmentTvSeriesBinding
import com.waffiq.bazz_movies.feature.home.ui.adapter.ItemWIdeAdapter
import com.waffiq.bazz_movies.feature.home.ui.adapter.MediaAdapter
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.TvSeriesViewModel
import com.waffiq.bazz_movies.feature.home.utils.helpers.CountryNameHelper.getCountryDisplayName
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.detachRecyclerView
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.handleLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRecyclerWideItem
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRetryButton
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupSwipeRefresh
import com.waffiq.bazz_movies.navigation.ListType
import com.waffiq.bazz_movies.navigation.MediaSource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvSeriesFragment : BaseHomeFragment() {

  private lateinit var popularAdapter: ItemWIdeAdapter
  private lateinit var airingTodayAdapter: MediaAdapter
  private lateinit var airingThisWeekAdapter: MediaAdapter
  private lateinit var topRatedAdapter: MediaAdapter

  private var _binding: FragmentTvSeriesBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val userPreferenceViewModel: UserPreferenceViewModel by activityViewModels()
  private val tvSeriesViewModel: TvSeriesViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    popularAdapter = ItemWIdeAdapter(navigator)
    airingTodayAdapter = MediaAdapter(navigator, MediaSource.Typed(TV_MEDIA_TYPE))
    airingThisWeekAdapter = MediaAdapter(navigator, MediaSource.Typed(TV_MEDIA_TYPE))
    topRatedAdapter = MediaAdapter(navigator, MediaSource.Typed(TV_MEDIA_TYPE))
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentTvSeriesBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    showShimmer(true)
    setData()
    setupAdapter()
    userPreferenceViewModel.getUserRegionPref().observe(viewLifecycleOwner) {
      handleLoadState(it)
    }
    refreshHandle()
    moreButtonAction()
  }

  override fun showShimmer(isVisible: Boolean) {
    binding.shimmer.shimmerTvSeries.isVisible = isVisible
  }

  private fun setupAdapter() {
    // Set up RecyclerViews
    setupRecyclerViewsWithSnap(listOf(binding.rvTvSeriesAiringToday, binding.rvTopRatedTvSeries))
    setupRecyclerWideItem(binding.rvPopularTvSeries)
    setupRecyclerViewsWithSnapGridLayout(recyclerViews = listOf(binding.rvTvSeriesAiringThisWeek))

    binding.apply {
      rvPopularTvSeries.setupLoadState(popularAdapter)
      rvTvSeriesAiringToday.setupLoadState(airingTodayAdapter)
      rvTvSeriesAiringThisWeek.setupLoadState(airingThisWeekAdapter)
      rvTopRatedTvSeries.setupLoadState(topRatedAdapter)
    }
  }

  private fun setData() {
    observePrimaryLoadState(
      topRatedAdapter.loadStateFlow,
      { topRatedAdapter.itemCount },
      binding.illustrationErrorTvSeries,
    )

    // Observe ViewModel data and submit to adapters
    collectAndSubmitData(this, { tvSeriesViewModel.getPopularTv() }, popularAdapter)
    collectAndSubmitData(this, { tvSeriesViewModel.getAiringTodayTv() }, airingTodayAdapter)
    collectAndSubmitData(this, { tvSeriesViewModel.getAiringThisWeekTv() }, airingThisWeekAdapter)
    collectAndSubmitData(this, { tvSeriesViewModel.getTopRatedTv() }, topRatedAdapter)
  }

  private fun handleLoadState(region: String) {
    viewLifecycleOwner.handleLoadState(
      popularAdapter,
      getString(no_popular_series, getCountryDisplayName(region)),
      binding.layoutNoPopularTvSeries,
      binding.rvPopularTvSeries,
    )
    viewLifecycleOwner.handleLoadState(
      airingTodayAdapter,
      getString(no_series_airing_today, getCountryDisplayName(region)),
      binding.layoutNoTvSeriesAiringToday,
      binding.rvTvSeriesAiringToday,
    )
    viewLifecycleOwner.handleLoadState(
      airingThisWeekAdapter,
      getString(no_series_airing_this_week, getCountryDisplayName(region)),
      binding.layoutNoTvSeriesAiringThisWeek,
      binding.rvTvSeriesAiringThisWeek,
    )
  }

  private fun refreshHandle() {
    // Set up swipe-to-refresh
    setupSwipeRefresh(
      binding.swipeRefreshTvSeries,
      popularAdapter,
      topRatedAdapter,
      airingTodayAdapter,
      airingThisWeekAdapter,
    )

    // Set up retry button
    setupRetryButton(
      binding.illustrationErrorTvSeries,
      popularAdapter,
      topRatedAdapter,
      airingTodayAdapter,
      airingThisWeekAdapter,
    )
  }

  override fun showView(isVisible: Boolean) {
    // Toggle visibility based on the flag
    binding.apply {
      layoutHeaderPopularTvSeries.isVisible = isVisible
      rvPopularTvSeries.isVisible = isVisible
      layoutHeaderTvSeriesAiringToday.isVisible = isVisible
      rvTvSeriesAiringToday.isVisible = isVisible
      layoutHeaderTvSeriesAiringThisWeek.isVisible = isVisible
      rvTvSeriesAiringThisWeek.isVisible = isVisible
      layoutHeaderTopRatedTvSeries.isVisible = isVisible
      rvTopRatedTvSeries.isVisible = isVisible
      illustrationErrorTvSeries.root.isVisible = !isVisible
    }
  }

  private fun moreButtonAction() {
    binding.btnMorePopularTvSeries.button.setOnClickListener {
      openList(ListType.POPULAR, TV_MEDIA_TYPE)
    }
    binding.btnMoreTvSeriesAiringToday.button.setOnClickListener {
      openList(ListType.NOW_PLAYING, TV_MEDIA_TYPE)
    }
    binding.btnMoreTvSeriesAiringThisWeek.button.setOnClickListener {
      openList(ListType.AIRING_THIS_WEEK, TV_MEDIA_TYPE)
    }
    binding.btnMoreTopRatedTvSeries.button.setOnClickListener {
      openList(ListType.TOP_RATED, TV_MEDIA_TYPE)
    }
  }

  override fun onClearBinding() {
    popularAdapter.removeLoadStateListener { }
    airingTodayAdapter.removeLoadStateListener { }
    airingThisWeekAdapter.removeLoadStateListener { }
    topRatedAdapter.removeLoadStateListener { }

    // Detach RecyclerViews programmatically
    binding.apply {
      rvPopularTvSeries.detachRecyclerView()
      rvTvSeriesAiringToday.detachRecyclerView()
      rvTvSeriesAiringThisWeek.detachRecyclerView()
      rvTopRatedTvSeries.detachRecyclerView()
    }

    _binding = null
  }
}
