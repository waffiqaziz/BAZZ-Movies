package com.waffiq.bazz_movies.ui.activity.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.id.nav_view
import com.waffiq.bazz_movies.R.string.binding_error
import com.waffiq.bazz_movies.R.string.no_movie_currently_playing
import com.waffiq.bazz_movies.R.string.no_upcoming_movie
import com.waffiq.bazz_movies.databinding.FragmentMovieBinding
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.MovieHomeAdapter
import com.waffiq.bazz_movies.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Helper.initLinearLayoutManager
import com.waffiq.bazz_movies.utils.common.Constants
import com.waffiq.bazz_movies.utils.common.Constants.NAN
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.helpers.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.utils.helpers.GetRegionHelper.getLocation
import com.waffiq.bazz_movies.utils.helpers.HomeFragmentHelper.handleLoadState
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.pagingErrorState
import com.waffiq.bazz_movies.utils.helpers.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.utils.uihelpers.Animation.fadeOut
import com.waffiq.bazz_movies.utils.uihelpers.FadeInItemAnimator
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class MovieFragment : Fragment() {

  private var _binding: FragmentMovieBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private lateinit var movieViewModel: MovieViewModel
  private lateinit var regionViewModel: RegionViewModel
  private lateinit var userPreferenceViewModel: UserPreferenceViewModel

  private var mSnackbar: Snackbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val factory = ViewModelFactory.getInstance(requireContext())
    movieViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]

    val pref = requireContext().dataStore
    val factory2 = ViewModelUserFactory.getInstance(pref)
    regionViewModel = ViewModelProvider(this, factory2)[RegionViewModel::class.java]
    userPreferenceViewModel = ViewModelProvider(this, factory2)[UserPreferenceViewModel::class.java]
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
    val popularAdapter = MovieHomeAdapter()
    val nowPlayingAdapter = MovieHomeAdapter()
    val upComingAdapter = MovieHomeAdapter()
    val topRatedAdapter = MovieHomeAdapter()

    combinedLoadStatesHandle(topRatedAdapter)

    // Setup RecyclerViews
    binding.apply {
      rvPopular.layoutManager = initLinearLayoutManager(requireContext())
      rvPopular.adapter = popularAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { popularAdapter.retry() }
      )

      rvNowPlaying.layoutManager = initLinearLayoutManager(requireContext())
      rvNowPlaying.adapter = nowPlayingAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { nowPlayingAdapter.retry() }
      )

      rvUpcoming.layoutManager = initLinearLayoutManager(requireContext())
      rvUpcoming.adapter = upComingAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { nowPlayingAdapter.retry() }
      )

      rvTopRated.layoutManager = initLinearLayoutManager(requireContext())
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
    setupSwipeRefresh(popularAdapter, nowPlayingAdapter, upComingAdapter, topRatedAdapter)

    // Set up retry button
    setupRetryButton(popularAdapter, nowPlayingAdapter, upComingAdapter, topRatedAdapter)
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
                mSnackbar = snackBarWarning(
                  requireActivity().findViewById(nav_view),
                  requireActivity().findViewById(nav_view),
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
      rvNowPlaying.isVisible = isVisible
      tvUpcoming.isVisible = isVisible
      rvUpcoming.isVisible = isVisible
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
    binding.illustrationError.btnTryAgain.setOnClickListener {
      adapters.forEach { it.refresh() }
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
