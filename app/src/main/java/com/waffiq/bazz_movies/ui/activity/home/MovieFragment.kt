package com.waffiq.bazz_movies.ui.activity.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.databinding.FragmentMovieBinding
import com.waffiq.bazz_movies.ui.activity.more.MoreViewModelUser
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.MovieHomeAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Helper
import com.waffiq.bazz_movies.utils.Helper.getLocation

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class MovieFragment : Fragment() {

  private var _binding: FragmentMovieBinding? = null
  private val binding get() = _binding!!

  private lateinit var homeViewModel: HomeViewModel
  private lateinit var moreViewModelUser: MoreViewModelUser

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMovieBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

    val pref = requireContext().dataStore
    val factory2 = ViewModelUserFactory.getInstance(pref)
    moreViewModelUser = ViewModelProvider(this, factory2)[MoreViewModelUser::class.java]

    showSnackBarNoAction(Helper.checkInternet(requireContext()))
    setRegion()

    return root
  }

  private fun setRegion() {
    // check if user already have region
    moreViewModelUser.getUserRegion().observe(viewLifecycleOwner) { userRegion ->

      // if user didn't have region, then get region from Country API
      if (userRegion.equals("NaN")) {
        moreViewModelUser.getCountryCode()
        moreViewModelUser.countryCode().observe(viewLifecycleOwner) { countryCode ->

          if (countryCode.isNullOrEmpty()) { // if success
            setData(countryCode)
            moreViewModelUser.saveUserRegion(countryCode)
          } else { // if null, then set region using SIM Card and default phone configuration
            val region = getLocation(requireContext())
            setData(region)
            moreViewModelUser.saveUserRegion(region)
          }
        }
      } else setData(userRegion) // user already have region
    }
  }

  private fun setData(region: String){

    binding.rvPopular.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val popularAdapter = MovieHomeAdapter()
    binding.rvPopular.adapter = popularAdapter.withLoadStateFooter(
      footer = LoadingStateAdapter {
        popularAdapter.retry()
      }
    )
    homeViewModel.getPopularMovies().observe(viewLifecycleOwner) {
      popularAdapter.submitData(lifecycle,it)
    }

    binding.rvNowPlaying.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val nowPlayingAdapter = MovieHomeAdapter()
    binding.rvNowPlaying.adapter = nowPlayingAdapter.withLoadStateFooter(
      footer = LoadingStateAdapter {
        nowPlayingAdapter.retry()
      }
    )
    homeViewModel.getPlayingNowMovies(region).observe(viewLifecycleOwner) {
      nowPlayingAdapter.submitData(lifecycle,it)
    }

    binding.rvUpcoming.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val upComingAdapter = MovieHomeAdapter()
    binding.rvUpcoming.adapter = upComingAdapter.withLoadStateFooter(
      footer = LoadingStateAdapter {
        nowPlayingAdapter.retry()
      }
    )
    homeViewModel.getUpcomingMovies(region).observe(viewLifecycleOwner) {
      upComingAdapter.submitData(lifecycle,it)
    }

    binding.rvTopRated.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val topRatedAdapter = MovieHomeAdapter()
    binding.rvTopRated.adapter = topRatedAdapter.withLoadStateFooter(
      footer = LoadingStateAdapter {
        topRatedAdapter.retry()
      }
    )
    homeViewModel.getTopRatedMovies().observe(viewLifecycleOwner) {
      topRatedAdapter.submitData(lifecycle,it)
    }

    binding.swipeRefresh.setOnRefreshListener {
      popularAdapter.refresh()
      topRatedAdapter.refresh()
      popularAdapter.refresh()
      upComingAdapter.refresh()
      showSnackBarNoAction(Helper.checkInternet(requireContext()))
      binding.swipeRefresh.isRefreshing = false
    }
  }

  private fun showSnackBarNoAction(message: String) {
    val snackBar = Snackbar.make(
      activity?.findViewById(android.R.id.content)!!,
      message,
      Snackbar.LENGTH_SHORT
    ).setAnchorView(binding.guideSnackbar)

    val snackbarView = snackBar.view
    snackbarView.setBackgroundColor(
      ContextCompat.getColor(
        requireContext(),
        R.color.red_matte
      )
    )
    if (message.isNotEmpty()) snackBar.show()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}