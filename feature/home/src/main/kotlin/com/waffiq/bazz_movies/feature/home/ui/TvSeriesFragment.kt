package com.waffiq.bazz_movies.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.movie.utils.helpers.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.uihelper.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnap
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.home.databinding.FragmentTvSeriesBinding
import com.waffiq.bazz_movies.feature.home.ui.adapter.TvAdapter
import com.waffiq.bazz_movies.feature.home.ui.shimmer.ShimmerAdapter
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.TvSeriesViewModel
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.detachRecyclerView
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.observeLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRetryButton
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupSwipeRefresh
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TvSeriesFragment : Fragment() {

  @Inject
  lateinit var navigator: INavigator

  @Inject
  lateinit var snackbar: ISnackbar

  private lateinit var popularAdapter: TvAdapter
  private lateinit var nowPlayingAdapter: TvAdapter
  private lateinit var onTvAdapter: TvAdapter
  private lateinit var topRatedAdapter: TvAdapter
  private lateinit var shimmerAdapter: ShimmerAdapter

  private var _binding: FragmentTvSeriesBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()
  private val tvSeriesViewModel: TvSeriesViewModel by viewModels()

  private var mSnackbar: Snackbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    popularAdapter = TvAdapter(navigator)
    nowPlayingAdapter = TvAdapter(navigator)
    onTvAdapter = TvAdapter(navigator)
    topRatedAdapter = TvAdapter(navigator)
    shimmerAdapter = ShimmerAdapter()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentTvSeriesBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onStart() {
    super.onStart()

    // Set up RecyclerViews
    setupRecyclerViewsWithSnap(
      listOf(binding.rvPopular, binding.rvAiringToday, binding.rvOnTv, binding.rvTopRated)
    )
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    showShimmer()

    userPreferenceViewModel.getUserRegionPref().observe(viewLifecycleOwner) { setData(it) }
  }

  private fun showShimmer() {
    binding.apply {
      if (rvPopular.adapter != shimmerAdapter) rvPopular.adapter = shimmerAdapter
      if (rvAiringToday.adapter != shimmerAdapter) rvAiringToday.adapter = shimmerAdapter
      if (rvOnTv.adapter != shimmerAdapter) rvOnTv.adapter = shimmerAdapter
      if (rvTopRated.adapter != shimmerAdapter) rvTopRated.adapter = shimmerAdapter
    }
  }

  private fun showActualData() {
    binding.apply {
      if (rvPopular.adapter != popularAdapter) rvPopular.setupLoadState(popularAdapter)
      if (rvAiringToday.adapter != nowPlayingAdapter) rvAiringToday.setupLoadState(nowPlayingAdapter)
      if (rvOnTv.adapter != onTvAdapter) rvOnTv.setupLoadState(onTvAdapter)
      if (rvTopRated.adapter != topRatedAdapter) rvTopRated.setupLoadState(topRatedAdapter)
    }
  }

  private fun setData(region: String) {
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
      }
    )

    // Observe ViewModel data and submit to adapters
    collectAndSubmitData(this, { tvSeriesViewModel.getPopularTv(region) }, popularAdapter)
    collectAndSubmitData(this, { tvSeriesViewModel.getAiringTodayTv(region) }, nowPlayingAdapter)
    collectAndSubmitData(this, { tvSeriesViewModel.getOnTv() }, onTvAdapter)
    collectAndSubmitData(this, { tvSeriesViewModel.getTopRatedTv() }, topRatedAdapter)

    // refresh whe swipe down
    binding.swipeRefresh.setOnRefreshListener {
      popularAdapter.refresh()
      topRatedAdapter.refresh()
      nowPlayingAdapter.refresh()
      onTvAdapter.refresh()
      binding.swipeRefresh.isRefreshing = false
    }

    // Set up swipe-to-refresh
    setupSwipeRefresh(
      binding.swipeRefresh,
      popularAdapter,
      topRatedAdapter,
      nowPlayingAdapter,
      onTvAdapter
    )

    // Set up retry button
    setupRetryButton(
      binding.illustrationError,
      popularAdapter,
      topRatedAdapter,
      nowPlayingAdapter,
      onTvAdapter
    )
  }

  private fun showView(isVisible: Boolean) {
    // Toggle visibility based on the flag
    binding.apply {
      tvPopular.isVisible = isVisible
      rvPopular.isVisible = isVisible
      tvAiringToday.isVisible = isVisible
      rvAiringToday.isVisible = isVisible
      tvOnTv.isVisible = isVisible
      rvOnTv.isVisible = isVisible
      tvTopRated.isVisible = isVisible
      rvTopRated.isVisible = isVisible
      illustrationError.root.isVisible = !isVisible
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
    nowPlayingAdapter.removeLoadStateListener { }
    onTvAdapter.removeLoadStateListener { }
    topRatedAdapter.removeLoadStateListener { }

    // Detach RecyclerViews programmatically
    binding.apply {
      rvPopular.detachRecyclerView()
      rvAiringToday.detachRecyclerView()
      rvOnTv.detachRecyclerView()
      rvTopRated.detachRecyclerView()
    }

    mSnackbar = null
    _binding = null
  }
}
