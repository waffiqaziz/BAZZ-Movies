package com.waffiq.bazz_movies.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.movie.utils.common.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.movie.utils.common.Event
import com.waffiq.bazz_movies.core.movie.utils.helpers.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.movie.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.core.movie.utils.helpers.PagingLoadStateHelper.pagingErrorState
import com.waffiq.bazz_movies.core.movie.utils.helpers.uihelpers.UIController
import com.waffiq.bazz_movies.core.ui.R.string.binding_error
import com.waffiq.bazz_movies.feature.home.databinding.FragmentTvSeriesBinding
import com.waffiq.bazz_movies.feature.home.ui.adapter.TvAdapter
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.TvSeriesViewModel
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.detachRecyclerView
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRetryButton
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupShimmer
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupSwipeRefresh
import com.waffiq.bazz_movies.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TvSeriesFragment : Fragment() {

  @Inject
  lateinit var navigator: Navigator

  private var uiController: UIController? = null
    get() = activity as? UIController

  private lateinit var popularAdapter: TvAdapter
  private lateinit var nowPlayingAdapter: TvAdapter
  private lateinit var onTvAdapter: TvAdapter
  private lateinit var topRatedAdapter: TvAdapter

  private var _binding: FragmentTvSeriesBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val tvSeriesViewModel: TvSeriesViewModel by viewModels()

  private var mSnackbar: Snackbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    popularAdapter = TvAdapter(navigator)
    nowPlayingAdapter = TvAdapter(navigator)
    onTvAdapter = TvAdapter(navigator)
    topRatedAdapter = TvAdapter(navigator)
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

    // Set up RecyclerViews with shimmer
    binding.apply {
      rvPopular.setupShimmer(requireContext(), popularAdapter)
      rvAiringToday.setupShimmer(requireContext(), nowPlayingAdapter)
      rvOnTv.setupShimmer(requireContext(), onTvAdapter)
      rvTopRated.setupShimmer(requireContext(), topRatedAdapter)
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setData()
  }

  private fun setData() {
    combinedLoadStatesHandle(topRatedAdapter)

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
      adapter.loadStateFlow.debounce(DEBOUNCE_SHORT).distinctUntilChanged()
        .collectLatest { loadState ->
          when {
            (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) &&
              loadState.append.endOfPaginationReached -> {
              isUnveil(false)
            }

            loadState.refresh is LoadState.NotLoading &&
              loadState.prepend is LoadState.NotLoading &&
              loadState.append is LoadState.NotLoading -> {
              isUnveil(true)
              showView(true)
            }

            loadState.refresh is LoadState.Error -> {
              isUnveil(true)
              pagingErrorState(loadState)?.let {
                showView(adapter.itemCount > 0)
                mSnackbar = uiController?.showSnackbarWarning(Event(pagingErrorHandling(it.error)))
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

  private fun isUnveil(isUnveil: Boolean) {
    if (isUnveil) {
      binding.apply {
        rvPopular.unVeil()
        rvAiringToday.unVeil()
        rvOnTv.unVeil()
        rvTopRated.unVeil()
      }
    } else {
      binding.apply {
        rvPopular.veil()
        rvAiringToday.veil()
        rvOnTv.veil()
        rvTopRated.veil()
      }
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
