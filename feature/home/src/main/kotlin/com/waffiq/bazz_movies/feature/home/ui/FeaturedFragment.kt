@file:Suppress("BackingPropertyNaming")

package com.waffiq.bazz_movies.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_BACKDROP_ORIGINAL
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_search
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.designsystem.R.string.no_movies_currently_playing
import com.waffiq.bazz_movies.core.designsystem.R.string.no_trending
import com.waffiq.bazz_movies.core.designsystem.R.string.no_upcoming_movies
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnap
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.core.utils.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.utils.GetRegionHelper.getLocation
import com.waffiq.bazz_movies.feature.home.databinding.FragmentFeaturedBinding
import com.waffiq.bazz_movies.feature.home.ui.adapter.MovieHomeAdapter
import com.waffiq.bazz_movies.feature.home.ui.adapter.TrendingAdapter
import com.waffiq.bazz_movies.feature.home.ui.shimmer.ShimmerAdapter
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel
import com.waffiq.bazz_movies.feature.home.utils.helpers.FlowJobHelper.collectAndSubmitDataJob
import com.waffiq.bazz_movies.feature.home.utils.helpers.Helper.getCountryDisplayName
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.detachRecyclerView
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.handleLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.observeLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRetryButton
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupSwipeRefresh
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import javax.inject.Inject

@AndroidEntryPoint
class FeaturedFragment : Fragment() {

  @Inject
  lateinit var navigator: INavigator

  @Inject
  lateinit var snackbar: ISnackbar

  // Initialize adapters
  private lateinit var adapterTrending: TrendingAdapter
  private lateinit var adapterUpcoming: MovieHomeAdapter
  private lateinit var adapterPlayingNow: MovieHomeAdapter
  private lateinit var shimmerAdapter: ShimmerAdapter

  private var _binding: FragmentFeaturedBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val movieViewModel: MovieViewModel by viewModels()
  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()
  private val regionViewModel: RegionViewModel by viewModels()

  private var mSnackbar: Snackbar? = null
  private var currentJob: Job? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    adapterTrending = TrendingAdapter(navigator)
    adapterUpcoming = MovieHomeAdapter(navigator)
    adapterPlayingNow = MovieHomeAdapter(navigator)
    shimmerAdapter = ShimmerAdapter()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentFeaturedBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupRecyclerViewsWithSnap(
      listOf(binding.rvUpcoming, binding.rvPlayingNow, binding.rvTrending),
    )

    showShimmer()
    setRegion()
    showMainPicture()
  }

  private fun showShimmer() {
    binding.apply {
      if (rvUpcoming.adapter != shimmerAdapter) rvUpcoming.adapter = shimmerAdapter
      if (rvPlayingNow.adapter != shimmerAdapter) rvPlayingNow.adapter = shimmerAdapter
      if (rvTrending.adapter != shimmerAdapter) rvTrending.adapter = shimmerAdapter
    }
  }

  private fun showActualData() {
    binding.apply {
      if (rvUpcoming.adapter != adapterUpcoming) rvUpcoming.setupLoadState(adapterUpcoming)
      if (rvPlayingNow.adapter != adapterPlayingNow) rvPlayingNow.setupLoadState(adapterPlayingNow)
      if (rvTrending.adapter != adapterTrending) rvTrending.setupLoadState(adapterTrending)
    }
  }

  private fun showMainPicture() {
    Glide.with(requireContext())
      .load(TMDB_IMG_LINK_BACKDROP_ORIGINAL + "bQXAqRx2Fgc46uCVWgoPz5L5Dtr.jpg") // URL movie poster
      .placeholder(ic_bazz_placeholder_search)
      .transition(withCrossFade())
      .error(ic_broken_image)
      .into(binding.imgMainFeatured)
  }

  private fun setRegion() {
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
    viewLifecycleOwner.observeLoadState(
      loadStateFlow = adapterTrending.loadStateFlow,
      onLoading = { if (adapterTrending.itemCount <= 0) showShimmer() },
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
          showView(adapterTrending.itemCount > 0)
          mSnackbar = snackbar.showSnackbarWarning(error)
        }
      },
    )

    // Observe ViewModel data and submit to adapters
    observeTrendingMovies(region, adapterTrending)
    collectAndSubmitData(this, { movieViewModel.getUpcomingMovies(region) }, adapterUpcoming)
    collectAndSubmitData(this, { movieViewModel.getPlayingNowMovies(region) }, adapterPlayingNow)

    // Handle LoadState for RecyclerViews
    viewLifecycleOwner.handleLoadState(
      adapterPlayingNow,
      binding.rvPlayingNow,
      getString(no_movies_currently_playing, getCountryDisplayName(region)),
      binding.layoutNoPlaying,
    )
    viewLifecycleOwner.handleLoadState(
      adapterUpcoming,
      binding.rvUpcoming,
      getString(no_upcoming_movies, getCountryDisplayName(region)),
      binding.layoutNoUpcoming,
    )
    viewLifecycleOwner.handleLoadState(
      adapterTrending,
      binding.rvTrending,
      getString(no_trending, getCountryDisplayName(region)),
      binding.layoutNoUpcoming,
    )

    // Set up swipe-to-refresh
    setupSwipeRefresh(
      binding.swipeRefresh,
      adapterTrending,
      adapterPlayingNow,
      adapterUpcoming,
    )
    binding.illustrationError

    // Set up retry button
    setupRetryButton(
      binding.illustrationError,
      adapterTrending,
      adapterPlayingNow,
      adapterUpcoming,
    )
  }

  private fun observeTrendingMovies(region: String, adapter: TrendingAdapter) {
    collectAndSubmitData(this, { movieViewModel.getTrendingWeek(region) }, adapter)
    binding.btnToday.setOnClickListener {
      binding.btnToday.isChecked = true
      binding.btnWeek.isChecked = false
      currentJob?.cancel() // Cancel the previous job if it exists
      currentJob =
        collectAndSubmitDataJob(this, { movieViewModel.getTrendingDay(region) }, adapter)
    }
    binding.btnWeek.setOnClickListener {
      binding.btnToday.isChecked = false
      binding.btnWeek.isChecked = true
      currentJob?.cancel() // Cancel the previous job if it exists
      currentJob =
        collectAndSubmitDataJob(this, { movieViewModel.getTrendingWeek(region) }, adapter)
    }
  }

  private fun showView(isVisible: Boolean) {
    // Toggle visibility based on the flag
    binding.apply {
      imgMainFeatured.isVisible = isVisible
      tvTrending.isVisible = isVisible
      buttonGroup.isVisible = isVisible
      rvTrending.isVisible = isVisible
      tvUpcomingMovie.isVisible = isVisible
      rvUpcoming.isVisible = isVisible
      rvPlayingNow.isVisible = isVisible
      tvPlayingNow.isVisible = isVisible
      illustrationError.root.isVisible = !isVisible
    }
  }

  override fun onPause() {
    super.onPause()
    mSnackbar?.dismiss()
    currentJob?.cancel()
  }

  override fun onStop() {
    super.onStop()
    mSnackbar?.dismiss()
  }

  override fun onDestroyView() {
    super.onDestroyView()

    adapterUpcoming.removeLoadStateListener { }
    adapterPlayingNow.removeLoadStateListener { }
    adapterTrending.removeLoadStateListener { }

    // Detach RecyclerViews programmatically
    binding.apply {
      rvUpcoming.detachRecyclerView()
      rvPlayingNow.detachRecyclerView()
      rvTrending.detachRecyclerView()
    }

    mSnackbar = null
    Glide.get(requireContext()).clearMemory()
    _binding = null
  }
}
