package com.waffiq.bazz_movies.pages.home

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
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.drawable.ic_bazz_placeholder_search
import com.waffiq.bazz_movies.R.id.bottom_navigation
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.navigation.DetailNavigator
import com.waffiq.bazz_movies.core.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.ui.adapter.MovieHomeAdapter
import com.waffiq.bazz_movies.core.ui.adapter.TrendingAdapter
import com.waffiq.bazz_movies.core.utils.common.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.utils.common.Constants.NAN
import com.waffiq.bazz_movies.core.utils.common.Constants.TMDB_IMG_LINK_BACKDROP_W780
import com.waffiq.bazz_movies.core.utils.common.Event
import com.waffiq.bazz_movies.core.utils.helpers.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.utils.helpers.FlowUtils.collectAndSubmitDataJob
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.initLinearLayoutManager
import com.waffiq.bazz_movies.core.utils.helpers.GetRegionHelper.getLocation
import com.waffiq.bazz_movies.core.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.core.utils.helpers.PagingLoadStateHelper.pagingErrorState
import com.waffiq.bazz_movies.core.utils.helpers.home.HomeFragmentHelper.handleLoadState
import com.waffiq.bazz_movies.core.utils.helpers.home.HomeFragmentHelper.setupRetryButton
import com.waffiq.bazz_movies.core.utils.helpers.home.HomeFragmentHelper.setupSwipeRefresh
import com.waffiq.bazz_movies.core.utils.helpers.uihelpers.Animation.fadeOut
import com.waffiq.bazz_movies.core.utils.helpers.uihelpers.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.core_ui.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.core_ui.R.string.binding_error
import com.waffiq.bazz_movies.core_ui.R.string.no_movie_currently_playing
import com.waffiq.bazz_movies.core_ui.R.string.no_upcoming_movie
import com.waffiq.bazz_movies.databinding.FragmentFeaturedBinding
import com.waffiq.bazz_movies.pages.detail.DetailMovieActivity
import com.waffiq.bazz_movies.pages.detail.DetailMovieActivity.Companion.EXTRA_MOVIE
import com.waffiq.bazz_movies.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.viewmodel.UserPreferenceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeaturedFragment : Fragment(), DetailNavigator {

  private var _binding: FragmentFeaturedBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val movieViewModel: MovieViewModel by viewModels()
  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()
  private val regionViewModel: RegionViewModel by viewModels()

  private var mSnackbar: Snackbar? = null
  private var currentJob: Job? = null

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
    // Initialize adapters
    val adapterTrending = TrendingAdapter(this)
    val adapterUpcoming = MovieHomeAdapter(this)
    val adapterPlayingNow = MovieHomeAdapter(this)

    combinedLoadStatesHandle(adapterTrending)

    // Setup RecyclerViews
    binding.apply {
      rvTrending.itemAnimator = DefaultItemAnimator()
      rvTrending.layoutManager = initLinearLayoutManager(requireContext())
      rvTrending.adapter = adapterTrending.withLoadStateFooter(
        footer = LoadingStateAdapter { adapterTrending.retry() }
      )

      rvUpcoming.itemAnimator = DefaultItemAnimator()
      rvUpcoming.layoutManager = initLinearLayoutManager(requireContext())
      rvUpcoming.adapter = adapterUpcoming.withLoadStateFooter(
        footer = LoadingStateAdapter { adapterUpcoming.retry() }
      )

      rvPlayingNow.itemAnimator = DefaultItemAnimator()
      rvPlayingNow.layoutManager = initLinearLayoutManager(requireContext())
      rvPlayingNow.adapter = adapterPlayingNow.withLoadStateFooter(
        footer = LoadingStateAdapter { adapterPlayingNow.retry() }
      )
    }

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
    mSnackbar = null
    Glide.get(requireContext()).clearMemory()
    _binding = null
  }

  override fun openDetails(resultItem: ResultItem) {
    val intent = Intent(requireContext(), DetailMovieActivity::class.java)
    intent.putExtra(EXTRA_MOVIE, resultItem)
    val options =
      ActivityOptionsCompat.makeCustomAnimation(requireContext(), fade_in, fade_out)
    ActivityCompat.startActivity(requireContext(), intent, options.toBundle())
  }
}
