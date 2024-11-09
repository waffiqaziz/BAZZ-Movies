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
import com.waffiq.bazz_movies.core.ui.R.string.binding_error
import com.waffiq.bazz_movies.core.ui.R.string.no_movie_currently_playing
import com.waffiq.bazz_movies.core.ui.R.string.no_upcoming_movie
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.core.utils.common.Constants
import com.waffiq.bazz_movies.core.utils.common.Constants.NAN
import com.waffiq.bazz_movies.core.utils.common.Event
import com.waffiq.bazz_movies.core.utils.helpers.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.core.utils.helpers.PagingLoadStateHelper.pagingErrorState
import com.waffiq.bazz_movies.core.utils.helpers.uihelpers.UIController
import com.waffiq.bazz_movies.feature.detail.utils.helpers.GetRegionHelper.getLocation
import com.waffiq.bazz_movies.feature.home.databinding.FragmentMovieBinding
import com.waffiq.bazz_movies.feature.home.ui.adapter.MovieHomeAdapter
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.detachRecyclerView
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.handleLoadState
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
class MovieFragment : Fragment() {

  @Inject
  lateinit var navigator: Navigator

  private var uiController: UIController? = null
    get() = activity as? UIController

  private lateinit var popularAdapter: MovieHomeAdapter
  private lateinit var nowPlayingAdapter: MovieHomeAdapter
  private lateinit var upComingAdapter: MovieHomeAdapter
  private lateinit var topRatedAdapter: MovieHomeAdapter

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

    popularAdapter = MovieHomeAdapter(navigator)
    nowPlayingAdapter = MovieHomeAdapter(navigator)
    upComingAdapter = MovieHomeAdapter(navigator)
    topRatedAdapter = MovieHomeAdapter(navigator)

    // Set up RecyclerViews with shimmer
    binding.apply {
      rvPopular.setupShimmer(requireContext(), popularAdapter)
      rvNowPlaying.setupShimmer(requireContext(), nowPlayingAdapter)
      rvUpcoming.setupShimmer(requireContext(), upComingAdapter)
      rvTopRated.setupShimmer(requireContext(), topRatedAdapter)
    }

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
    combinedLoadStatesHandle(topRatedAdapter)

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

  private fun isUnveil(isUnveil: Boolean) {
    if (isUnveil) {
      binding.apply {
        rvPopular.unVeil()
        rvNowPlaying.unVeil()
        rvUpcoming.unVeil()
        rvTopRated.unVeil()
      }
    } else {
      binding.apply {
        rvPopular.veil()
        rvNowPlaying.veil()
        rvUpcoming.veil()
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
    upComingAdapter.removeLoadStateListener { }
    topRatedAdapter.removeLoadStateListener { }

    // Detach RecyclerViews programmatically
    binding.apply {
      binding.rvPopular.detachRecyclerView()
      binding.rvNowPlaying.detachRecyclerView()
      binding.rvUpcoming.detachRecyclerView()
      binding.rvTopRated.detachRecyclerView()
    }

    mSnackbar = null
    _binding = null
  }
}
