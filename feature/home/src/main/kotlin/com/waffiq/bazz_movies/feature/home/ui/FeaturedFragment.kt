@file:Suppress("BackingPropertyNaming")

package com.waffiq.bazz_movies.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
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
import com.waffiq.bazz_movies.feature.home.ui.adapter.MediaAdapter
import com.waffiq.bazz_movies.feature.home.ui.adapter.MediaSource
import com.waffiq.bazz_movies.feature.home.ui.domain.TrendingPeriod
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel
import com.waffiq.bazz_movies.feature.home.utils.helpers.CountryNameHelper.getCountryDisplayName
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.detachRecyclerView
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.handleLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.observeLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRetryButton
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupSwipeRefresh
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FeaturedFragment : Fragment() {

  @Inject
  lateinit var navigator: INavigator

  @Inject
  lateinit var snackbar: ISnackbar

  // Initialize adapters
  private lateinit var adapterTrending: MediaAdapter
  private lateinit var adapterUpcoming: MediaAdapter
  private lateinit var adapterPlayingNow: MediaAdapter

  private var _binding: FragmentFeaturedBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val movieViewModel: MovieViewModel by viewModels()
  private val userPreferenceViewModel: UserPreferenceViewModel by activityViewModels()
  private val regionViewModel: RegionViewModel by viewModels()

  private var mSnackbar: Snackbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    adapterTrending = MediaAdapter(navigator, MediaSource.Trending)
    adapterUpcoming = MediaAdapter(navigator, MediaSource.Typed(MOVIE_MEDIA_TYPE))
    adapterPlayingNow = MediaAdapter(navigator, MediaSource.Typed(MOVIE_MEDIA_TYPE))
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
      listOf(
        binding.rvUpcomingMovieFeatured,
        binding.rvMoviePlayingNowFeatured,
        binding.rvTrending,
      ),
    )

    showShimmer(true)
    setRegion()
    setData()
    setupAdapter()
    showMainPicture()
    refreshHandle()
    moreButtonAction()
  }

  private fun showShimmer(isVisible: Boolean) {
    binding.shimmer.shimmerFeatured.isVisible = isVisible
  }

  private fun setupAdapter() {
    binding.apply {
      rvUpcomingMovieFeatured.setupLoadState(adapterUpcoming)
      rvMoviePlayingNowFeatured.setupLoadState(adapterPlayingNow)
      rvTrending.setupLoadState(adapterTrending)
    }
  }

  private fun refreshHandle() {
    // Set up swipe-to-refresh
    setupSwipeRefresh(
      binding.swipeRefreshFeatured,
      adapterTrending,
      adapterPlayingNow,
      adapterUpcoming,
    )

    // Set up retry button
    setupRetryButton(
      binding.illustrationErrorFeatured,
      adapterTrending,
      adapterPlayingNow,
      adapterUpcoming,
    )
  }

  private fun showMainPicture() {
    Glide.with(requireContext())
      .load("$TMDB_IMG_LINK_BACKDROP_ORIGINAL/bQXAqRx2Fgc46uCVWgoPz5L5Dtr.jpg") // URL movie poster
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

          // if still empty, then get region from device network location
          val region = countryCode.ifEmpty { getLocation(requireContext()) }
          handleLoadState(region)
          userPreferenceViewModel.saveRegionPref(region)
        }
      } else {
        handleLoadState(userRegion)
      }
    }
  }

  private fun setData() {
    viewLifecycleOwner.observeLoadState(
      loadStateFlow = adapterTrending.loadStateFlow,
      onLoading = { if (adapterTrending.itemCount <= 0) showShimmer(true) },
      onSuccess = {
        binding.illustrationErrorFeatured.apply {
          progressCircular.isVisible = false
          btnTryAgain.isVisible = true
        }
        showShimmer(false)
        showView(true)
      },
      onError = { error ->
        binding.illustrationErrorFeatured.apply {
          progressCircular.isVisible = false
          btnTryAgain.isVisible = true
        }
        showShimmer(false)
        showView(adapterTrending.itemCount > 0)
        mSnackbar = snackbar.showSnackbarWarning(error)
      },
    )

    // Observe ViewModel data and submit to adapters
    observeTrendingMovies(adapterTrending)
    collectAndSubmitData(this, { movieViewModel.getUpcomingMovies() }, adapterUpcoming)
    collectAndSubmitData(this, { movieViewModel.getPlayingNowMovies() }, adapterPlayingNow)
  }

  private fun handleLoadState(region: String) {
    viewLifecycleOwner.handleLoadState(
      adapterPlayingNow,
      getString(no_movies_currently_playing, getCountryDisplayName(region)),
      binding.layoutNoPlaying,
      binding.rvMoviePlayingNowFeatured,
      binding.btnMoreMoviePlayingNowFeatured.root,
    )
    viewLifecycleOwner.handleLoadState(
      adapterUpcoming,
      getString(no_upcoming_movies, getCountryDisplayName(region)),
      binding.layoutNoUpcoming,
      binding.rvUpcomingMovieFeatured,
      binding.btnMoreUpcomingMovieFeatured.root,
    )
    viewLifecycleOwner.handleLoadState(
      adapterTrending,
      getString(no_trending, getCountryDisplayName(region)),
      binding.layoutNoTrending,
      binding.rvTrending,
    )
  }

  private fun observeTrendingMovies(adapter: MediaAdapter) {
    // Collect the single trending flow — ViewModel handles switching
    collectAndSubmitData(this, { movieViewModel.trending }, adapter)

    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        movieViewModel.trendingPeriod.collect { period ->
          binding.btnTrendingToday.isChecked = period == TrendingPeriod.TODAY
          binding.btnTrendingThisWeek.isChecked = period == TrendingPeriod.WEEK
        }
      }
    }

    binding.btnTrendingToday.setOnClickListener {
      movieViewModel.setTrendingPeriod(TrendingPeriod.TODAY)
    }
    binding.btnTrendingThisWeek.setOnClickListener {
      movieViewModel.setTrendingPeriod(TrendingPeriod.WEEK)
    }
  }

  private fun showView(isVisible: Boolean) {
    // Toggle visibility based on the flag
    binding.apply {
      imgMainFeatured.isVisible = isVisible
      buttonGroup.isVisible = isVisible
      rvTrending.isVisible = isVisible
      layoutHeaderUpcomingMovieFeatured.isVisible = isVisible
      rvUpcomingMovieFeatured.isVisible = isVisible
      layoutHeaderMoviePlayingNowFeatured.isVisible = isVisible
      rvMoviePlayingNowFeatured.isVisible = isVisible
      illustrationErrorFeatured.root.isVisible = !isVisible
    }
  }

  private fun moreButtonAction() {
    binding.btnMoreTrendingFeatured.button.setOnClickListener {
      navigator.openList(
        requireContext(),
        ListArgs(
          listType = if (movieViewModel.trendingPeriod.value == TrendingPeriod.WEEK) {
            ListType.TRENDING_WEEK
          } else {
            ListType.TRENDING_TODAY
          },
          mediaType = "",
          title = "",
        ),
      )
    }
    binding.btnMoreMoviePlayingNowFeatured.button.setOnClickListener {
      navigator.openList(
        requireContext(),
        ListArgs(listType = ListType.NOW_PLAYING, mediaType = MOVIE_MEDIA_TYPE, title = ""),
      )
    }
    binding.btnMoreUpcomingMovieFeatured.button.setOnClickListener {
      navigator.openList(
        requireContext(),
        ListArgs(listType = ListType.UPCOMING, mediaType = MOVIE_MEDIA_TYPE, title = ""),
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

    adapterUpcoming.removeLoadStateListener { }
    adapterPlayingNow.removeLoadStateListener { }
    adapterTrending.removeLoadStateListener { }

    // Detach RecyclerViews programmatically
    binding.apply {
      rvUpcomingMovieFeatured.detachRecyclerView()
      rvMoviePlayingNowFeatured.detachRecyclerView()
      rvTrending.detachRecyclerView()
    }

    mSnackbar = null
    _binding = null
  }
}
