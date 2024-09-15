package com.waffiq.bazz_movies.ui.activity.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper.getMainLooper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.R.id.nav_view
import com.waffiq.bazz_movies.R.string.binding_error
import com.waffiq.bazz_movies.databinding.FragmentMovieBinding
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.MovieHomeAdapter
import com.waffiq.bazz_movies.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.uihelpers.FadeInItemAnimator
import com.waffiq.bazz_movies.utils.Helper
import com.waffiq.bazz_movies.utils.Helper.animFadeOutLong
import com.waffiq.bazz_movies.utils.Helper.initLinearLayoutManager
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.helpers.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.utils.helpers.GetRegionHelper.getLocation
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.pagingErrorState
import com.waffiq.bazz_movies.utils.helpers.SnackBarManager.snackBarWarning
import java.util.Locale

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
      if (userRegion.equals("NaN")) {
        regionViewModel.getCountryCode()
        regionViewModel.countryCode.observe(viewLifecycleOwner) { countryCode ->

          if (countryCode.isNotEmpty()) { // if success
            setData(countryCode)
            userPreferenceViewModel.saveRegionPref(countryCode)
          } else { // if null, then set region using SIM Card and default phone configuration
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

    // show loading(progressbar)
    topRatedAdapter.addLoadStateListener {
      combinedLoadStatesHandle(topRatedAdapter, it)
    }

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
    handleLoadState(
      nowPlayingAdapter,
      binding.rvNowPlaying,
      binding.tvAiringToday,
      R.string.no_movie_currently_playing,
      region
    )
    handleLoadState(
      upComingAdapter,
      binding.rvUpcoming,
      binding.tvUpcoming,
      R.string.no_upcoming_movie,
      region
    )

    // Set up swipe-to-refresh
    setupSwipeRefresh(popularAdapter, nowPlayingAdapter, upComingAdapter, topRatedAdapter)

    // Set up retry button
    setupRetryButton(popularAdapter, nowPlayingAdapter, upComingAdapter, topRatedAdapter)
  }

  private fun combinedLoadStatesHandle(adapter: MovieHomeAdapter, loadState: CombinedLoadStates) {
    if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
      showLoading(true) // show ProgressBar
    } else {
      showLoading(false) // hide ProgressBar

      pagingErrorState(loadState)?.let {
        if (adapter.itemCount < 1) showView(false)
        mSnackbar = snackBarWarning(
          requireContext(),
          requireActivity().findViewById(nav_view),
          requireActivity().findViewById(nav_view),
          Event(pagingErrorHandling(it.error))
        )
      } ?: run {
        showView(true) // hide the error illustration if no error
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

  private fun handleLoadState(
    adapter: MovieHomeAdapter,
    recyclerView: RecyclerView,
    textView: TextView,
    noMoviesStringRes: Int,
    region: String
  ) {
    // check if there any upcoming movie on selected region, if not show info on toast and edit text
    adapter.addLoadStateListener { loadState ->
      if (loadState.source.refresh is LoadState.NotLoading &&
        loadState.append.endOfPaginationReached &&
        adapter.itemCount < 1
      ) {
        Helper.showToastShort(
          requireContext(),
          getString(noMoviesStringRes, Locale("", region).displayCountry)
        )
        recyclerView.isGone = true
        if (!textView.text.contains(getString(R.string.data))) {
          textView.append(" (${getString(R.string.no_data)})")
        }
      }
    }
  }

  private fun setupSwipeRefresh(vararg adapters: PagingDataAdapter<*, *>) {
    binding.swipeRefresh.setOnRefreshListener {
      adapters.forEach { it.refresh() }
      binding.swipeRefresh.isRefreshing = false
    }
  }

  private fun setupRetryButton(vararg adapters: PagingDataAdapter<*, *>) {
    binding.illustrationError.btnTryAgain.setOnClickListener { adapters.forEach { it.refresh() } }
  }

  private fun animationFadeOut() {
    val animation = animFadeOutLong(requireContext())
    binding.backgroundDimMovie.startAnimation(animation)
    binding.progressBar.startAnimation(animation)

    Handler(getMainLooper()).post {
      binding.backgroundDimMovie.isGone = true
      binding.progressBar.isGone = true
    }
  }

  private fun showLoading(isLoading: Boolean) {
    if (isLoading) {
      binding.backgroundDimMovie.isVisible = true
      binding.progressBar.isVisible = true
    } else {
      animationFadeOut()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
    mSnackbar?.dismiss()
  }
}
