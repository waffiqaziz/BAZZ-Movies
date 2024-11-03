package com.waffiq.bazz_movies.feature.home.ui

import android.R.anim.fade_in
import android.R.anim.fade_out
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.navigation.DetailNavigator
import com.waffiq.bazz_movies.core.ui.R.string.binding_error
import com.waffiq.bazz_movies.core.ui.R.string.no_movie_currently_playing
import com.waffiq.bazz_movies.core.ui.R.string.no_upcoming_movie
import com.waffiq.bazz_movies.core.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.ui.adapter.MovieHomeAdapter
import com.waffiq.bazz_movies.core.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.core.utils.common.Constants
import com.waffiq.bazz_movies.core.utils.common.Constants.NAN
import com.waffiq.bazz_movies.core.utils.common.Event
import com.waffiq.bazz_movies.core.utils.helpers.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.initLinearLayoutManagerHorizontal
import com.waffiq.bazz_movies.feature.detail.utils.helpers.GetRegionHelper.getLocation
import com.waffiq.bazz_movies.core.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.core.utils.helpers.PagingLoadStateHelper.pagingErrorState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.handleLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRetryButton
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupSwipeRefresh
import com.waffiq.bazz_movies.core.utils.helpers.uihelpers.Animation.fadeOut
import com.waffiq.bazz_movies.feature.home.utils.uihelpers.FadeInItemAnimator
import com.waffiq.bazz_movies.core.utils.helpers.uihelpers.UIController
import com.waffiq.bazz_movies.feature.home.databinding.FragmentMovieBinding
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieFragment : Fragment(), DetailNavigator {

  private var uiController: UIController? = null
    get() = activity as? UIController

  private var _binding: FragmentMovieBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val movieViewModel: MovieViewModel by viewModels()
  private val regionViewModel: RegionViewModel by viewModels()
  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()

  private var mSnackbar: Snackbar? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMovieBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    showData()
  }

  private fun showData() {
    // check if user already have region
    userPreferenceViewModel.getUserRegionPref().observe(viewLifecycleOwner) { userRegion ->

      // if user didn't have region, then get region from Country API
      if (userRegion == NAN) {
        regionViewModel.getCountryCode()
        regionViewModel.countryCode.observe(viewLifecycleOwner) { countryCode ->

          if (countryCode.isNotEmpty()) { // if success
            setData(countryCode)
            userPreferenceViewModel.saveRegionPref(countryCode)
          } else { // if null, then set region using SIM Card or default phone configuration
            val region = getLocation(requireContext())
            setData(region)
            userPreferenceViewModel.saveRegionPref(region)
          }
        }
      } else {
        setData(userRegion) // user already have region
      }
    }
  }

  private fun setData(region: String) {
    // Initialize adapters
    val popularAdapter = MovieHomeAdapter(this)
    val nowPlayingAdapter = MovieHomeAdapter(this)
    val upComingAdapter = MovieHomeAdapter(this)
    val topRatedAdapter = MovieHomeAdapter(this)

    combinedLoadStatesHandle(topRatedAdapter)

    // Setup RecyclerViews
    binding.apply {
      rvPopular.layoutManager = initLinearLayoutManagerHorizontal(requireContext())
      rvPopular.adapter = popularAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { popularAdapter.retry() }
      )

      rvNowPlaying.layoutManager = initLinearLayoutManagerHorizontal(requireContext())
      rvNowPlaying.adapter = nowPlayingAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { nowPlayingAdapter.retry() }
      )

      rvUpcoming.layoutManager = initLinearLayoutManagerHorizontal(requireContext())
      rvUpcoming.adapter = upComingAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { nowPlayingAdapter.retry() }
      )

      rvTopRated.layoutManager = initLinearLayoutManagerHorizontal(requireContext())
      rvTopRated.adapter = topRatedAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { topRatedAdapter.retry() }
      )

      rvPopular.itemAnimator = FadeInItemAnimator()
      rvNowPlaying.itemAnimator = FadeInItemAnimator()
      rvUpcoming.itemAnimator = FadeInItemAnimator()
      rvTopRated.itemAnimator = FadeInItemAnimator()
    }

    // Observe ViewModel data and submit to adapters
    collectAndSubmitData(this, { movieViewModel.getPopularMovies() }, popularAdapter)
    collectAndSubmitData(this, { movieViewModel.getPlayingNowMovies(region) }, nowPlayingAdapter)
    collectAndSubmitData(this, { movieViewModel.getUpcomingMovies(region) }, upComingAdapter)
    collectAndSubmitData(this, { movieViewModel.getTopRatedMovies() }, topRatedAdapter)

    // Handle LoadState for RecyclerViews
    viewLifecycleOwner.handleLoadState(
      requireContext(),
      nowPlayingAdapter,
      binding.rvNowPlaying,
      binding.tvAiringToday,
      no_movie_currently_playing,
      region
    )
    viewLifecycleOwner.handleLoadState(
      requireContext(),
      upComingAdapter,
      binding.rvUpcoming,
      binding.tvUpcoming,
      no_upcoming_movie,
      region
    )

    // Set up swipe-to-refresh
    setupSwipeRefresh(
      binding.swipeRefresh,
      popularAdapter,
      nowPlayingAdapter,
      upComingAdapter,
      topRatedAdapter
    )

    // Set up retry button
    setupRetryButton(
      binding.illustrationError.btnTryAgain,
      popularAdapter,
      nowPlayingAdapter,
      upComingAdapter,
      topRatedAdapter
    )
  }

  private fun combinedLoadStatesHandle(adapter: MovieHomeAdapter) {
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
                mSnackbar = uiController?.showSnackbar(Event(pagingErrorHandling(it.error)))
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
      rvNowPlaying.isVisible = isVisible
      tvUpcoming.isVisible = isVisible
      rvUpcoming.isVisible = isVisible
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

  override fun openDetails(resultItem: ResultItem) {
    val intent = Intent(
      requireContext(),
      com.waffiq.bazz_movies.feature.detail.ui.DetailMovieActivity::class.java
    )
    intent.putExtra(
      com.waffiq.bazz_movies.feature.detail.ui.DetailMovieActivity.Companion.EXTRA_MOVIE,
      resultItem
    )
    val options =
      ActivityOptionsCompat.makeCustomAnimation(requireContext(), fade_in, fade_out)
    ActivityCompat.startActivity(requireContext(), intent, options.toBundle())
  }
}
