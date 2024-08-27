package com.waffiq.bazz_movies.ui.activity.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper.getMainLooper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.id.nav_view
import com.waffiq.bazz_movies.R.string.binding_error
import com.waffiq.bazz_movies.databinding.FragmentMovieBinding
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.MovieHomeAdapter
import com.waffiq.bazz_movies.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.FadeInItemAnimator
import com.waffiq.bazz_movies.utils.Helper.animFadeOutLong
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.helpers.GetRegionHelper.getLocation
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.utils.helpers.SnackBarManager

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class MovieFragment : Fragment() {

  private var _binding: FragmentMovieBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private lateinit var movieViewModel: MovieViewModel
  private lateinit var regionViewModel: RegionViewModel
  private lateinit var userPreferenceViewModel: UserPreferenceViewModel

  private var mSnackbar: Snackbar? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMovieBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    movieViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]

    val pref = requireContext().dataStore
    val factory2 = ViewModelUserFactory.getInstance(pref)
    regionViewModel = ViewModelProvider(this, factory2)[RegionViewModel::class.java]
    userPreferenceViewModel = ViewModelProvider(this, factory2)[UserPreferenceViewModel::class.java]

    setRegion()

    return root
  }

  private fun setRegion() {
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
      } else setData(userRegion) // user already have region
    }
  }

  private fun setData(region: String) {
    // setup adapter
    val popularAdapter = MovieHomeAdapter()
    val nowPlayingAdapter = MovieHomeAdapter()
    val upComingAdapter = MovieHomeAdapter()
    val topRatedAdapter = MovieHomeAdapter()
    topRatedAdapter.addLoadStateListener { combinedLoadStatesHandle(it) } // show loading(progressbar)

    // setup recyclerview
    binding.apply {
      rvPopular.layoutManager =
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      rvPopular.adapter = popularAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { popularAdapter.retry() }
      )

      rvNowPlaying.layoutManager =
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      rvNowPlaying.adapter = nowPlayingAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { nowPlayingAdapter.retry() }
      )

      rvUpcoming.layoutManager =
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      rvUpcoming.adapter = upComingAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { nowPlayingAdapter.retry() }
      )

      rvTopRated.layoutManager =
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      binding.rvTopRated.adapter = topRatedAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { topRatedAdapter.retry() }
      )

      rvPopular.itemAnimator = FadeInItemAnimator()
      rvNowPlaying.itemAnimator = FadeInItemAnimator()
      rvUpcoming.itemAnimator = FadeInItemAnimator()
      rvTopRated.itemAnimator = FadeInItemAnimator()
    }


    movieViewModel.getPopularMovies().observe(viewLifecycleOwner) {
      popularAdapter.submitData(lifecycle, it)
    }
    movieViewModel.getPlayingNowMovies(region).observe(viewLifecycleOwner) {
      nowPlayingAdapter.submitData(lifecycle, it)
    }
    movieViewModel.getUpcomingMovies(region).observe(viewLifecycleOwner) {
      upComingAdapter.submitData(lifecycle, it)
    }
    movieViewModel.getTopRatedMovies().observe(viewLifecycleOwner) {
      topRatedAdapter.submitData(lifecycle, it)
    }

    binding.swipeRefresh.setOnRefreshListener {
      popularAdapter.refresh()
      topRatedAdapter.refresh()
      popularAdapter.refresh()
      upComingAdapter.refresh()
      binding.swipeRefresh.isRefreshing = false
    }
  }

  private fun combinedLoadStatesHandle(loadState: CombinedLoadStates) {
    if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading)
      showLoading(true) // show ProgressBar
    else {
      showLoading(false) // hide ProgressBar

      val errorState = when { // If theres an error, show a toast
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        else -> null
      }
      errorState?.let {
        mSnackbar = SnackBarManager.snackBarWarning(
          requireContext(),
          binding.root,
          requireActivity().findViewById(nav_view),
          Event(pagingErrorHandling(it.error))
        )
      }
    }
  }

  private fun animationFadeOut() {
    val animation = animFadeOutLong(requireContext())
    binding.backgroundDimMovie.startAnimation(animation)
    binding.progressBar.startAnimation(animation)

    Handler(getMainLooper()).post {
      binding.backgroundDimMovie.visibility = View.GONE
      binding.progressBar.visibility = View.GONE
    }
  }

  private fun showLoading(isLoading: Boolean) {
    if (isLoading) {
      binding.backgroundDimMovie.visibility = View.VISIBLE
      binding.progressBar.visibility = View.VISIBLE
    } else animationFadeOut()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
    mSnackbar?.dismiss()
  }
}