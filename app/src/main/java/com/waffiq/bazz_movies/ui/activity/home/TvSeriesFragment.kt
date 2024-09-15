package com.waffiq.bazz_movies.ui.activity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.id.nav_view
import com.waffiq.bazz_movies.R.string.binding_error
import com.waffiq.bazz_movies.databinding.FragmentTvSeriesBinding
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.TvAdapter
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelFactory
import com.waffiq.bazz_movies.utils.Helper.animFadeOutLong
import com.waffiq.bazz_movies.utils.Helper.initLinearLayoutManager
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.helpers.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.pagingErrorState
import com.waffiq.bazz_movies.utils.helpers.SnackBarManager.snackBarWarning

class TvSeriesFragment : Fragment() {

  private var _binding: FragmentTvSeriesBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private lateinit var tvSeriesViewModel: TvSeriesViewModel

  private var mSnackbar: Snackbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val factory = ViewModelFactory.getInstance(requireContext())
    tvSeriesViewModel = ViewModelProvider(this, factory)[TvSeriesViewModel::class.java]
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentTvSeriesBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setData()
  }

  private fun setData() {
    // Initialize adapters
    val popularAdapter = TvAdapter()
    val nowPlayingAdapter = TvAdapter()
    val onTvAdapter = TvAdapter()
    val topRatedAdapter = TvAdapter()

    // show loading(progressbar)
    topRatedAdapter.addLoadStateListener {
      combinedLoadStatesHandle(topRatedAdapter, it)
    }

    // Setup RecyclerViews
    binding.apply {
      rvPopular.layoutManager = initLinearLayoutManager(requireContext())
      rvPopular.adapter = popularAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { popularAdapter.retry() }
      )

      rvAiringToday.layoutManager = initLinearLayoutManager(requireContext())
      rvAiringToday.adapter = nowPlayingAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { nowPlayingAdapter.retry() }
      )

      rvOnTv.layoutManager = initLinearLayoutManager(requireContext())
      rvOnTv.adapter = onTvAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { onTvAdapter.retry() }
      )

      rvTopRated.layoutManager = initLinearLayoutManager(requireContext())
      rvTopRated.adapter = topRatedAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { topRatedAdapter.retry() }
      )
    }

    // Observe ViewModel data and submit to adapters
    collectAndSubmitData(this, { tvSeriesViewModel.getPopularTv() }, popularAdapter)
    collectAndSubmitData(this, { tvSeriesViewModel.getAiringTodayTv() }, nowPlayingAdapter)
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
    setupSwipeRefresh(popularAdapter, topRatedAdapter, nowPlayingAdapter, onTvAdapter)

    // Set up retry button
    setupRetryButton(popularAdapter, topRatedAdapter, nowPlayingAdapter, onTvAdapter)
  }

  private fun combinedLoadStatesHandle(adapter: TvAdapter, loadState: CombinedLoadStates) {
    if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
      showLoading(true) // show ProgressBar
    } else {
      showLoading(false) // hide ProgressBar

      pagingErrorState(loadState)?.let {
        if (adapter.itemCount < 1) showView(false)
        mSnackbar = snackBarWarning(
          requireContext(),
          requireActivity().findViewById(nav_view),
          requireActivity().findViewById(nav_view),
          Event(pagingErrorHandling(it.error))
        )
      } ?: run {
        showView(true) // hide the error illustration if no error
      }
    }
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

  private fun setupSwipeRefresh(vararg adapters: PagingDataAdapter<*, *>) {
    binding.swipeRefresh.setOnRefreshListener {
      adapters.forEach { it.refresh() }
      binding.swipeRefresh.isRefreshing = false
    }
  }

  private fun setupRetryButton(vararg adapters: PagingDataAdapter<*, *>) {
    binding.illustrationError.btnTryAgain.setOnClickListener { adapters.forEach { it.refresh() } }
  }

  private fun animationFadeOut() {
    val animation = animFadeOutLong(requireContext())
    binding.backgroundDimMovie.startAnimation(animation)
    binding.progressBar.startAnimation(animation)
    binding.backgroundDimMovie.isGone = true
    binding.progressBar.isGone = true
  }

  private fun showLoading(isLoading: Boolean) {
    if (isLoading) {
      binding.backgroundDimMovie.isVisible = true
      binding.progressBar.isVisible = true
    } else {
      animationFadeOut()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
    mSnackbar?.dismiss()
    mSnackbar = null
  }
}
