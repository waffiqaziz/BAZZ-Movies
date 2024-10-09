package com.waffiq.bazz_movies.ui.activity.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.MyApplication
import com.waffiq.bazz_movies.R.id.bottom_navigation
import com.waffiq.bazz_movies.R.string.binding_error
import com.waffiq.bazz_movies.databinding.FragmentTvSeriesBinding
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.TvAdapter
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelFactory
import com.waffiq.bazz_movies.utils.Helper.initLinearLayoutManager
import com.waffiq.bazz_movies.utils.common.Constants
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.helpers.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.utils.helpers.HomeFragmentHelper.setupRetryButton
import com.waffiq.bazz_movies.utils.helpers.HomeFragmentHelper.setupSwipeRefresh
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.pagingErrorState
import com.waffiq.bazz_movies.utils.helpers.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.utils.uihelpers.Animation.fadeOut
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class TvSeriesFragment : Fragment() {

  @Inject
  lateinit var factory: ViewModelFactory

  private var _binding: FragmentTvSeriesBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val tvSeriesViewModel: TvSeriesViewModel by viewModels { factory }

  private var mSnackbar: Snackbar? = null

  override fun onAttach(context: Context) {
    super.onAttach(context)
    (requireActivity().application as MyApplication).appComponent.inject(this)
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

    combinedLoadStatesHandle(topRatedAdapter)

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
    setupSwipeRefresh(
      binding.swipeRefresh,
      popularAdapter,
      topRatedAdapter,
      nowPlayingAdapter,
      onTvAdapter
    )

    // Set up retry button
    setupRetryButton(
      binding.illustrationError.btnTryAgain,
      popularAdapter,
      topRatedAdapter,
      nowPlayingAdapter,
      onTvAdapter
    )
  }

  private fun combinedLoadStatesHandle(adapter: TvAdapter) {
    viewLifecycleOwner.lifecycleScope.launch {
      @OptIn(FlowPreview::class)
      adapter.loadStateFlow.debounce(Constants.DEBOUNCE_SHORT).distinctUntilChanged()
        .collectLatest { loadState ->
          when {
            loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
              binding.progressBar.isVisible = true
            }

            loadState.refresh is LoadState.NotLoading &&
              loadState.prepend is LoadState.NotLoading &&
              loadState.append is LoadState.NotLoading -> {
              fadeOut(binding.backgroundDimMovie)
              binding.progressBar.isGone = true
              showView(true)
            }

            loadState.refresh is LoadState.Error -> {
              fadeOut(binding.backgroundDimMovie)
              binding.progressBar.isGone = true
              pagingErrorState(loadState)?.let {
                showView(adapter.itemCount > 0)
                mSnackbar = snackBarWarning(
                  requireActivity().findViewById(bottom_navigation),
                  requireActivity().findViewById(bottom_navigation),
                  Event(pagingErrorHandling(it.error))
                )
              }
            }
          }
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
    mSnackbar = null
    _binding = null
  }
}
