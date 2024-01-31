package com.waffiq.bazz_movies.ui.activity.home

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.databinding.FragmentFeaturedBinding
import com.waffiq.bazz_movies.ui.activity.more.MoreViewModelUser
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.MovieHomeAdapter
import com.waffiq.bazz_movies.ui.adapter.TrendingAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Constants.TMDB_IMG_LINK_BACKDROP_W780
import com.waffiq.bazz_movies.utils.Helper
import com.waffiq.bazz_movies.utils.Helper.getLocation
import com.waffiq.bazz_movies.utils.Helper.showToastShort
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class FeaturedFragment : Fragment() {

  private var _binding: FragmentFeaturedBinding? = null
  private val binding get() = _binding!!

  private lateinit var homeViewModel: HomeViewModel
  private lateinit var moreViewModelUser: MoreViewModelUser

  @RequiresApi(Build.VERSION_CODES.S)
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentFeaturedBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

    val pref = requireContext().dataStore
    val factory2 = ViewModelUserFactory.getInstance(pref)
    moreViewModelUser = ViewModelProvider(this, factory2)[MoreViewModelUser::class.java]

    setRegion()
    setMoveNowPlaying()
    return root
  }

  private fun setMoveNowPlaying() {
//    val data = viewModel.getFirstMovieNowPlaying()
//    viewModel.getFirstMovieNowPlaying().observe(viewLifecycleOwner) {
//      Log.e("Cek Link : ", it[0].backdropPath)
//    }

    // show main picture
    Glide.with(binding.imgMainFeatured)
      //.load("http://image.tmdb.org/t/p/w500/" + data.backdropPath) // URL movie poster
      .load(TMDB_IMG_LINK_BACKDROP_W780 + "bQXAqRx2Fgc46uCVWgoPz5L5Dtr.jpg") // URL movie poster
      .placeholder(R.mipmap.ic_launcher)
      .error(R.drawable.ic_broken_image)
      .into(binding.imgMainFeatured)
  }

  private fun setRegion() {
    // check if user already have region
    moreViewModelUser.getUserRegion().observe(viewLifecycleOwner) { userRegion ->

      // if user didn't have region, then get region from Country API
      if (userRegion.equals("NaN")) {
        moreViewModelUser.getCountryCode()
        moreViewModelUser.countryCode().observe(viewLifecycleOwner) { countryCode ->

          if (countryCode.isNotEmpty()) { // if success
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

  private fun setData(region: String) {
    binding.rvTrending.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    binding.rvUpcoming.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    binding.rvPlayingNow.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val adapterTrending = TrendingAdapter()
    val adapterUpcoming = MovieHomeAdapter()
    val adapterPlayingNow = MovieHomeAdapter()

    // trending
    binding.rvTrending.adapter = adapterTrending.withLoadStateFooter(
      footer = LoadingStateAdapter { adapterTrending.retry() }
    )
    homeViewModel.getTrending(region).observe(viewLifecycleOwner) {
      adapterTrending.submitData(lifecycle, it)
    }

    // upcoming movie
    binding.rvUpcoming.adapter = adapterUpcoming.withLoadStateFooter(
      footer = LoadingStateAdapter { adapterUpcoming.retry() }
    )
    homeViewModel.getUpcomingMovies(region).observe(viewLifecycleOwner) {
      adapterUpcoming.submitData(lifecycle, it)
    }

    // playing not at theater
    binding.rvPlayingNow.adapter = adapterPlayingNow.withLoadStateFooter(
      footer = LoadingStateAdapter { adapterPlayingNow.retry() }
    )
    homeViewModel.getPlayingNowMovies(region).observe(viewLifecycleOwner) {
      adapterPlayingNow.submitData(lifecycle, it)
    }

    adapterPlayingNow.addLoadStateListener { loadState ->
      if (loadState.source.refresh is LoadState.NotLoading
        && loadState.append.endOfPaginationReached
        && adapterPlayingNow.itemCount < 1
      ) {
        showToastShort(
          requireContext(),
          getString(R.string.no_movie_currently_playing, Locale("", region).displayCountry)
        )
      }
    }

    adapterUpcoming.addLoadStateListener { loadState ->
      if (loadState.source.refresh is LoadState.NotLoading
        && loadState.append.endOfPaginationReached
        && adapterUpcoming.itemCount < 1
      ) {
        showToastShort(
          requireContext(),
          getString(R.string.no_upcoming_movie, Locale("", region).displayCountry)
        )
      }
    }
  }

  private fun animFadeOut() {
    val animation = Helper.animFadeOutLong(requireContext())
    binding.progressBar.startAnimation(animation)

    Handler(Looper.getMainLooper()).postDelayed({
      binding.progressBar.visibility = View.GONE
    }, 600)
  }

  private fun showLoading(isLoading: Boolean) {
    if (isLoading) {
      binding.progressBar.visibility = View.VISIBLE
    } else animFadeOut()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}