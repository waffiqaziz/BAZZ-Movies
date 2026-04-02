@file:Suppress("BackingPropertyNaming")

package com.waffiq.bazz_movies.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.designsystem.R.string.no_popular_series
import com.waffiq.bazz_movies.core.designsystem.R.string.no_series_airing_this_week
import com.waffiq.bazz_movies.core.designsystem.R.string.no_series_airing_today
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnap
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnapGridLayout
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.core.utils.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.feature.home.databinding.FragmentTvSeriesBinding
import com.waffiq.bazz_movies.feature.home.ui.adapter.ItemWIdeAdapter
import com.waffiq.bazz_movies.feature.home.ui.adapter.TvAdapter
import com.waffiq.bazz_movies.feature.home.ui.shimmer.ShimmerAdapter
import com.waffiq.bazz_movies.feature.home.ui.shimmer.ShimmerItemWideAdapter
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.TvSeriesViewModel
import com.waffiq.bazz_movies.feature.home.utils.helpers.Helper.getCountryDisplayName
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.detachRecyclerView
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.handleLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.observeLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRecyclerWideItem
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRetryButton
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupSwipeRefresh
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TvSeriesFragment : Fragment() {

  @Inject
  lateinit var navigator: INavigator

  @Inject
  lateinit var snackbar: ISnackbar

  private lateinit var popularAdapter: ItemWIdeAdapter
  private lateinit var airingTodayAdapter: TvAdapter
  private lateinit var airingThisWeekAdapter: TvAdapter
  private lateinit var topRatedAdapter: TvAdapter
  private lateinit var shimmerAdapter: ShimmerAdapter
  private lateinit var shimmerWideAdapter: ShimmerItemWideAdapter

  private var _binding: FragmentTvSeriesBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()
  private val tvSeriesViewModel: TvSeriesViewModel by viewModels()

  private var mSnackbar: Snackbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    popularAdapter = ItemWIdeAdapter(navigator)
    airingTodayAdapter = TvAdapter(navigator)
    airingThisWeekAdapter = TvAdapter(navigator)
    topRatedAdapter = TvAdapter(navigator)
    shimmerAdapter = ShimmerAdapter()
    shimmerWideAdapter = ShimmerItemWideAdapter()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentTvSeriesBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onStart() {
    super.onStart()

    // Set up RecyclerViews
    setupRecyclerViewsWithSnap(listOf(binding.rvAiringToday, binding.rvTopRated))
    setupRecyclerWideItem(binding.rvPopular)
    setupRecyclerViewsWithSnapGridLayout(recyclerViews = listOf(binding.rvAiringThisWeek))
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    showShimmer()
    setData()
    userPreferenceViewModel.getUserRegionPref().observe(viewLifecycleOwner) {
      handleLoadState(it)
    }
    moreButtonAction()
  }

  private fun showShimmer() {
    binding.apply {
      if (rvPopular.adapter != shimmerWideAdapter) rvPopular.adapter = shimmerWideAdapter
      if (rvAiringToday.adapter != shimmerAdapter) rvAiringToday.adapter = shimmerAdapter
      if (rvAiringThisWeek.adapter != shimmerAdapter) rvAiringThisWeek.adapter = shimmerAdapter
      if (rvTopRated.adapter != shimmerAdapter) rvTopRated.adapter = shimmerAdapter
    }
  }

  private fun showActualData() {
    binding.apply {
      if (rvPopular.adapter != popularAdapter) rvPopular.setupLoadState(popularAdapter)
      if (rvAiringToday.adapter != airingTodayAdapter) {
        rvAiringToday.setupLoadState(
          airingTodayAdapter,
        )
      }
      if (rvAiringThisWeek.adapter != airingThisWeekAdapter) {
        rvAiringThisWeek.setupLoadState(
          airingThisWeekAdapter,
        )
      }
      if (rvTopRated.adapter != topRatedAdapter) rvTopRated.setupLoadState(topRatedAdapter)
    }
  }

  private fun setData() {
    refreshHandle()
    viewLifecycleOwner.observeLoadState(
      loadStateFlow = topRatedAdapter.loadStateFlow,
      onLoading = { showShimmer() },
      onSuccess = {
        binding.illustrationError.apply {
          progressCircular.isVisible = false
          btnTryAgain.isVisible = true
        }
        showActualData()
        showView(true)
      },
      onError = { error ->
        binding.illustrationError.apply {
          progressCircular.isVisible = false
          btnTryAgain.isVisible = true
        }
        error?.let {
          showActualData()
          showView(topRatedAdapter.itemCount > 0)
          mSnackbar = snackbar.showSnackbarWarning(error)
        }
      },
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
      binding.layoutNoPopular,
      binding.rvPopular,
    )
    viewLifecycleOwner.handleLoadState(
      airingTodayAdapter,
      getString(no_series_airing_today, getCountryDisplayName(region)),
      binding.layoutNoAiringToday,
      binding.rvAiringToday,
    )
    viewLifecycleOwner.handleLoadState(
      airingThisWeekAdapter,
      getString(no_series_airing_this_week, getCountryDisplayName(region)),
      binding.layoutNoAiringThisWeek,
      binding.rvAiringThisWeek,
    )
  }

  private fun refreshHandle() {
    // refresh whe swipe down
    binding.swipeRefresh.setOnRefreshListener {
      popularAdapter.refresh()
      topRatedAdapter.refresh()
      airingTodayAdapter.refresh()
      airingThisWeekAdapter.refresh()
      binding.swipeRefresh.isRefreshing = false
    }

    // Set up swipe-to-refresh
    setupSwipeRefresh(
      binding.swipeRefresh,
      popularAdapter,
      topRatedAdapter,
      airingTodayAdapter,
      airingThisWeekAdapter,
    )

    // Set up retry button
    setupRetryButton(
      binding.illustrationError,
      popularAdapter,
      topRatedAdapter,
      airingTodayAdapter,
      airingThisWeekAdapter,
    )
  }

  private fun showView(isVisible: Boolean) {
    // Toggle visibility based on the flag
    binding.apply {
      tvPopular.isVisible = isVisible
      rvPopular.isVisible = isVisible
      tvAiringToday.isVisible = isVisible
      rvAiringToday.isVisible = isVisible
      tvAiringThisWeek.isVisible = isVisible
      rvAiringThisWeek.isVisible = isVisible
      tvTopRated.isVisible = isVisible
      rvTopRated.isVisible = isVisible
      illustrationError.root.isVisible = !isVisible
    }
  }

  private fun moreButtonAction() {
    binding.btnMorePopular.button.setOnClickListener {
      navigator.openList(
        requireContext(),
        ListArgs(listType = ListType.POPULAR, mediaType = TV_MEDIA_TYPE, title = ""),
      )
    }
    binding.btnMoreAiringToday.button.setOnClickListener {
      navigator.openList(
        requireContext(),
        ListArgs(listType = ListType.NOW_PLAYING, mediaType = TV_MEDIA_TYPE, title = ""),
      )
    }
    binding.btnMoreAiringThisWeek.button.setOnClickListener {
      navigator.openList(
        requireContext(),
        ListArgs(listType = ListType.AIRING_THIS_WEEK, mediaType = TV_MEDIA_TYPE, title = ""),
      )
    }
    binding.btnMoreTopRated.button.setOnClickListener {
      navigator.openList(
        requireContext(),
        ListArgs(listType = ListType.TOP_RATED, mediaType = TV_MEDIA_TYPE, title = ""),
      )
    }
  }

  override fun onPause() {
    super.onPause()
    mSnackbar?.dismiss()
  }

  override fun onStop() {
    super.onStop()
    mSnackbar?.dismiss()
  }

  override fun onDestroyView() {
    super.onDestroyView()

    popularAdapter.removeLoadStateListener { }
    airingTodayAdapter.removeLoadStateListener { }
    airingThisWeekAdapter.removeLoadStateListener { }
    topRatedAdapter.removeLoadStateListener { }

    // Detach RecyclerViews programmatically
    binding.apply {
      rvPopular.detachRecyclerView()
      rvAiringToday.detachRecyclerView()
      rvAiringThisWeek.detachRecyclerView()
      rvTopRated.detachRecyclerView()
    }

    mSnackbar = null
    _binding = null
  }
}
