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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_BACKDROP_W780
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_search
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.designsystem.R.string.no_movie_currently_playing
import com.waffiq.bazz_movies.core.designsystem.R.string.no_upcoming_movie
import com.waffiq.bazz_movies.core.movie.utils.helpers.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.movie.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.core.movie.utils.helpers.PagingLoadStateHelper.pagingErrorState
import com.waffiq.bazz_movies.core.uihelper.utils.UIController
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.detail.utils.helpers.GetRegionHelper.getLocation
import com.waffiq.bazz_movies.feature.home.databinding.FragmentFeaturedBinding
import com.waffiq.bazz_movies.feature.home.ui.adapter.MovieHomeAdapter
import com.waffiq.bazz_movies.feature.home.ui.adapter.TrendingAdapter
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel
import com.waffiq.bazz_movies.feature.home.utils.helpers.FlowJobHelper.collectAndSubmitDataJob
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.detachRecyclerView
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.handleLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRetryButton
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupShimmer
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupSwipeRefresh
import com.waffiq.bazz_movies.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FeaturedFragment : Fragment() {

  @Inject
  lateinit var navigator: Navigator

  private var uiController: UIController? = null
    get() = activity as? UIController

  // Initialize adapters
  private lateinit var adapterTrending: TrendingAdapter
  private lateinit var adapterUpcoming: MovieHomeAdapter
  private lateinit var adapterPlayingNow: MovieHomeAdapter

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
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentFeaturedBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Set up RecyclerViews with shimmer
    binding.apply {
      rvUpcoming.setupShimmer(requireContext(), adapterUpcoming)
      rvPlayingNow.setupShimmer(requireContext(), adapterPlayingNow)
      rvTrending.setupShimmer(requireContext(), adapterTrending)
    }

    setRegion()
    showMainPicture()
  }

  private fun showMainPicture() {
    Glide.with(requireContext())
      .load(TMDB_IMG_LINK_BACKDROP_W780 + "bQXAqRx2Fgc46uCVWgoPz5L5Dtr.jpg") // URL movie poster
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
    combinedLoadStatesHandle(adapterTrending)

    // Observe ViewModel data and submit to adapters
    observeTrendingMovies(region, adapterTrending)
    collectAndSubmitData(this, { movieViewModel.getUpcomingMovies(region) }, adapterUpcoming)
    collectAndSubmitData(this, { movieViewModel.getPlayingNowMovies(region) }, adapterPlayingNow)

    // Handle LoadState for RecyclerViews
    viewLifecycleOwner.handleLoadState(
      requireContext(),
      adapterPlayingNow,
      binding.rvPlayingNow,
      binding.tvPlayingNow,
      no_movie_currently_playing,
      region
    )
    viewLifecycleOwner.handleLoadState(
      requireContext(),
      adapterUpcoming,
      binding.rvUpcoming,
      binding.tvUpcomingMovie,
      no_upcoming_movie,
      region
    )

    // Set up swipe-to-refresh
    setupSwipeRefresh(
      binding.swipeRefresh,
      adapterTrending,
      adapterPlayingNow,
      adapterUpcoming
    )

    // Set up retry button
    setupRetryButton(
      binding.illustrationError.btnTryAgain,
      adapterTrending,
      adapterPlayingNow,
      adapterUpcoming
    )
  }

  private fun observeTrendingMovies(region: String, adapter: TrendingAdapter) {
    collectAndSubmitData(this, { movieViewModel.getTrendingWeek(region) }, adapter)
    binding.rbToday.setOnClickListener {
      currentJob?.cancel() // Cancel the previous job if it exists
      currentJob =
        collectAndSubmitDataJob(this, { movieViewModel.getTrendingDay(region) }, adapter)
    }
    binding.rbThisWeek.setOnClickListener {
      currentJob?.cancel() // Cancel the previous job if it exists
      currentJob =
        collectAndSubmitDataJob(this, { movieViewModel.getTrendingWeek(region) }, adapter)
    }
  }

  private fun combinedLoadStatesHandle(adapter: TrendingAdapter) {
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
      imgMainFeatured.isVisible = isVisible
      tvTrending.isVisible = isVisible
      toggle.isVisible = isVisible
      rvTrending.isVisible = isVisible
      tvUpcomingMovie.isVisible = isVisible
      rvUpcoming.isVisible = isVisible
      rvPlayingNow.isVisible = isVisible
      tvPlayingNow.isVisible = isVisible
      illustrationError.root.isVisible = !isVisible
    }
  }

  private fun isUnveil(isUnveil: Boolean) {
    if (isUnveil) {
      binding.apply {
        rvUpcoming.unVeil()
        rvTrending.unVeil()
        rvPlayingNow.unVeil()
      }
    } else {
      binding.apply {
        rvUpcoming.veil()
        rvTrending.veil()
        rvPlayingNow.veil()
      }
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
