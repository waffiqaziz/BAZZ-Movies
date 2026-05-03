@file:Suppress("BackingPropertyNaming")

package com.waffiq.bazz_movies.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.designsystem.R.string.no_movies_currently_playing
import com.waffiq.bazz_movies.core.designsystem.R.string.no_upcoming_movies
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnap
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnapGridLayout
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.core.utils.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.feature.home.databinding.FragmentMovieBinding
import com.waffiq.bazz_movies.feature.home.ui.adapter.ItemWIdeAdapter
import com.waffiq.bazz_movies.feature.home.ui.adapter.MediaAdapter
import com.waffiq.bazz_movies.feature.home.ui.adapter.MediaSource
import com.waffiq.bazz_movies.feature.home.ui.shimmer.ShimmerAdapter
import com.waffiq.bazz_movies.feature.home.ui.shimmer.ShimmerItemWideAdapter
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel
import com.waffiq.bazz_movies.feature.home.utils.helpers.CountryNameHelper.getCountryDisplayName
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.detachRecyclerView
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.handleLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.observeLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRecyclerWideItem
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRetryButton
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupSwipeRefresh
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieFragment : Fragment() {

  @Inject
  lateinit var navigator: INavigator

  @Inject
  lateinit var snackbar: ISnackbar

  private lateinit var popularAdapter: ItemWIdeAdapter
  private lateinit var nowPlayingAdapter: MediaAdapter
  private lateinit var upComingAdapter: MediaAdapter
  private lateinit var topRatedAdapter: MediaAdapter
  private lateinit var shimmerAdapter: ShimmerAdapter
  private lateinit var shimmerWideAdapter: ShimmerItemWideAdapter

  private var _binding: FragmentMovieBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val movieViewModel: MovieViewModel by viewModels()
  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()

  private var mSnackbar: Snackbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    popularAdapter = ItemWIdeAdapter(navigator)
    nowPlayingAdapter = MediaAdapter(navigator, MediaSource.Typed(MOVIE_MEDIA_TYPE))
    upComingAdapter = MediaAdapter(navigator, MediaSource.Typed(MOVIE_MEDIA_TYPE))
    topRatedAdapter = MediaAdapter(navigator, MediaSource.Typed(MOVIE_MEDIA_TYPE))
    shimmerAdapter = ShimmerAdapter()
    shimmerWideAdapter = ShimmerItemWideAdapter()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentMovieBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Set up RecyclerViews
    setupRecyclerViewsWithSnap(listOf(binding.rvUpcomingMovie, binding.rvTopRatedMovie))
    setupRecyclerWideItem(binding.rvPopularMovie)
    setupRecyclerViewsWithSnapGridLayout(recyclerViews = listOf(binding.rvMovieAiringToday))

    showShimmer()
    userPreferenceViewModel.getUserRegionPref().observe(viewLifecycleOwner) {
      handleLoadState(it)
    }
    setData()
    moreButtonAction()
  }

  private fun showShimmer() {
    binding.apply {
      if (rvPopularMovie.adapter != shimmerWideAdapter) rvPopularMovie.adapter = shimmerWideAdapter
      if (rvMovieAiringToday.adapter != shimmerAdapter) rvMovieAiringToday.adapter = shimmerAdapter
      if (rvUpcomingMovie.adapter != shimmerAdapter) rvUpcomingMovie.adapter = shimmerAdapter
      if (rvTopRatedMovie.adapter != shimmerAdapter) rvTopRatedMovie.adapter = shimmerAdapter
    }
  }

  private fun showActualData() {
    binding.apply {
      rvPopularMovie.setupLoadState(popularAdapter)
      rvMovieAiringToday.setupLoadState(nowPlayingAdapter)
      rvUpcomingMovie.setupLoadState(upComingAdapter)
      rvTopRatedMovie.setupLoadState(topRatedAdapter)
    }
  }

  private fun setData() {
    refreshHandle()
    viewLifecycleOwner.observeLoadState(
      loadStateFlow = topRatedAdapter.loadStateFlow,
      onLoading = { showShimmer() },
      onSuccess = {
        binding.illustrationErrorMovie.apply {
          progressCircular.isVisible = false
          btnTryAgain.isVisible = true
        }
        showActualData()
        showView(true)
      },
      onError = { error ->
        binding.illustrationErrorMovie.apply {
          progressCircular.isVisible = false
          btnTryAgain.isVisible = true
        }
        showActualData()
        showView(topRatedAdapter.itemCount > 0)
        mSnackbar = snackbar.showSnackbarWarning(error)
      },
    )

    // Observe ViewModel data and submit to adapters
    collectAndSubmitData(this, { movieViewModel.getPopularMovies() }, popularAdapter)
    collectAndSubmitData(this, { movieViewModel.getPlayingNowMovies() }, nowPlayingAdapter)
    collectAndSubmitData(this, { movieViewModel.getUpcomingMovies() }, upComingAdapter)
    collectAndSubmitData(this, { movieViewModel.getTopRatedMovies() }, topRatedAdapter)
  }

  private fun handleLoadState(region: String) {
    viewLifecycleOwner.handleLoadState(
      nowPlayingAdapter,
      getString(no_movies_currently_playing, getCountryDisplayName(region)),
      binding.layoutNoMovieAiringToday,
      binding.rvMovieAiringToday,
      binding.btnMoreMovieAiringToday.root,
    )
    viewLifecycleOwner.handleLoadState(
      upComingAdapter,
      getString(no_upcoming_movies, getCountryDisplayName(region)),
      binding.layoutNoUpcomingMovie,
      binding.rvUpcomingMovie,
      binding.btnMoreUpcomingMovie.root,
    )
  }

  private fun refreshHandle() {
    // Set up swipe-to-refresh
    setupSwipeRefresh(
      binding.swipeRefreshMovie,
      popularAdapter,
      nowPlayingAdapter,
      upComingAdapter,
      topRatedAdapter,
    )

    // Set up retry button
    setupRetryButton(
      binding.illustrationErrorMovie,
      popularAdapter,
      nowPlayingAdapter,
      upComingAdapter,
      topRatedAdapter,
    )
  }

  private fun showView(isVisible: Boolean) {
    // Toggle visibility based on the flag
    binding.apply {
      layoutHeaderPopularMovie.isVisible = isVisible
      rvPopularMovie.isVisible = isVisible
      layoutHeaderMovieAiringToday.isVisible = isVisible
      rvMovieAiringToday.isVisible = isVisible
      layoutHeaderUpcomingMovie.isVisible = isVisible
      rvUpcomingMovie.isVisible = isVisible
      layoutHeaderTopRatedMovie.isVisible = isVisible
      rvTopRatedMovie.isVisible = isVisible
      illustrationErrorMovie.root.isVisible = !isVisible
    }
  }

  private fun moreButtonAction() {
    binding.btnMorePopularMovie.button.setOnClickListener {
      navigator.openList(
        requireContext(),
        ListArgs(listType = ListType.POPULAR, mediaType = MOVIE_MEDIA_TYPE, title = ""),
      )
    }
    binding.btnMoreMovieAiringToday.button.setOnClickListener {
      navigator.openList(
        requireContext(),
        ListArgs(listType = ListType.NOW_PLAYING, mediaType = MOVIE_MEDIA_TYPE, title = ""),
      )
    }
    binding.btnMoreUpcomingMovie.button.setOnClickListener {
      navigator.openList(
        requireContext(),
        ListArgs(listType = ListType.UPCOMING, mediaType = MOVIE_MEDIA_TYPE, title = ""),
      )
    }
    binding.btnMoreTopRatedMovie.button.setOnClickListener {
      navigator.openList(
        requireContext(),
        ListArgs(listType = ListType.TOP_RATED, mediaType = MOVIE_MEDIA_TYPE, title = ""),
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

    popularAdapter.removeLoadStateListener { }
    nowPlayingAdapter.removeLoadStateListener { }
    upComingAdapter.removeLoadStateListener { }
    topRatedAdapter.removeLoadStateListener { }

    // Detach RecyclerViews programmatically
    binding.apply {
      binding.rvPopularMovie.detachRecyclerView()
      binding.rvMovieAiringToday.detachRecyclerView()
      binding.rvUpcomingMovie.detachRecyclerView()
      binding.rvTopRatedMovie.detachRecyclerView()
    }

    mSnackbar = null
    _binding = null
  }
}
