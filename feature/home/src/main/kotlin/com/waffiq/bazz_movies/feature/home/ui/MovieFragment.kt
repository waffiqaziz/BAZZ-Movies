package com.waffiq.bazz_movies.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.designsystem.R.string.no_movie_currently_playing
import com.waffiq.bazz_movies.core.designsystem.R.string.no_upcoming_movie
import com.waffiq.bazz_movies.core.movie.utils.helpers.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.movie.utils.helpers.GeneralHelper.initLinearLayoutManagerHorizontal
import com.waffiq.bazz_movies.core.movie.utils.helpers.GetRegionHelper.getLocation
import com.waffiq.bazz_movies.core.movie.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.core.uihelper.utils.UIController
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.home.databinding.FragmentMovieBinding
import com.waffiq.bazz_movies.feature.home.ui.adapter.MovieHomeAdapter
import com.waffiq.bazz_movies.feature.home.ui.shimmer.ShimmerAdapter
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.detachRecyclerView
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.handleLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.observeLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRecyclerView
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRetryButton
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupSwipeRefresh
import com.waffiq.bazz_movies.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
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
  private lateinit var shimmerAdapter: ShimmerAdapter

  private var _binding: FragmentMovieBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val movieViewModel: MovieViewModel by viewModels()
  private val regionViewModel: RegionViewModel by viewModels()
  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()

  private var mSnackbar: Snackbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    popularAdapter = MovieHomeAdapter(navigator)
    nowPlayingAdapter = MovieHomeAdapter(navigator)
    upComingAdapter = MovieHomeAdapter(navigator)
    topRatedAdapter = MovieHomeAdapter(navigator)
    shimmerAdapter = ShimmerAdapter()
  }

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

    // Set up RecyclerViews with shimmer
    binding.apply {
      rvPopular.layoutManager = initLinearLayoutManagerHorizontal(requireContext())
      rvNowPlaying.layoutManager = initLinearLayoutManagerHorizontal(requireContext())
      rvUpcoming.layoutManager = initLinearLayoutManagerHorizontal(requireContext())
      rvTopRated.layoutManager = initLinearLayoutManagerHorizontal(requireContext())
    }

    showShimmer()
    showData()
  }

  private fun showShimmer() {
    binding.apply {
      rvPopular.adapter = shimmerAdapter
      rvNowPlaying.adapter = shimmerAdapter
      rvUpcoming.adapter = shimmerAdapter
      rvTopRated.adapter = shimmerAdapter
    }
  }

  private fun showActualData() {
    binding.apply {
      rvPopular.setupRecyclerView(requireContext(), popularAdapter)
      rvNowPlaying.setupRecyclerView(requireContext(), nowPlayingAdapter)
      rvUpcoming.setupRecyclerView(requireContext(), upComingAdapter)
      rvTopRated.setupRecyclerView(requireContext(), topRatedAdapter)
    }
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
    observeLoadState(
      lifecycleOwner = viewLifecycleOwner,
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
        pagingErrorHandling(error).let {
          showActualData()
          showView(topRatedAdapter.itemCount > 0)
          mSnackbar = uiController?.showSnackbarWarning(Event(it))
        }
      }
    )

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
      binding.illustrationError,
      popularAdapter,
      nowPlayingAdapter,
      upComingAdapter,
      topRatedAdapter
    )
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
