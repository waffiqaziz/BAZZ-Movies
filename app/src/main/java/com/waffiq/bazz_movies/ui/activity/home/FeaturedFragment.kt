package com.waffiq.bazz_movies.ui.activity.home

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.databinding.FragmentFeaturedBinding
import com.waffiq.bazz_movies.ui.activity.more.MoreViewModelUser
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.MovieHomeAdapter
import com.waffiq.bazz_movies.ui.adapter.TrendingAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Constants.TMDB_IMG_LINK_BACKDROP_W780
import com.waffiq.bazz_movies.utils.Helper.animFadeOutLong
import com.waffiq.bazz_movies.utils.Helper.checkInternet
import com.waffiq.bazz_movies.utils.Helper.getLocation
import com.waffiq.bazz_movies.utils.Helper.showToastShort
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class FeaturedFragment : Fragment() {

  private var _binding: FragmentFeaturedBinding? = null
  private val binding get() = _binding!!

  private lateinit var homeViewModel: HomeViewModel
  private lateinit var moreViewModelUser: MoreViewModelUser

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

    showSnackBarNoAction(checkInternet(requireContext()))
    setRegion()
    setMoveNowPlaying()
    hideActionBar()

    return root
  }

  private fun setMoveNowPlaying() {
    // show main picture
    Glide.with(binding.imgMainFeatured)
      //.load("http://image.tmdb.org/t/p/w500/" + data.backdropPath) // URL movie poster
      .load(TMDB_IMG_LINK_BACKDROP_W780 + "bQXAqRx2Fgc46uCVWgoPz5L5Dtr.jpg") // URL movie poster
      .placeholder(R.drawable.ic_bazz_placeholder_search)
      .transition(DrawableTransitionOptions.withCrossFade())
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
    // setup adapter
    val adapterTrending = TrendingAdapter()
    val adapterUpcoming = MovieHomeAdapter()
    val adapterPlayingNow = MovieHomeAdapter()
    adapterPlayingNow.addLoadStateListener { combinedLoadStatesHandle(it) } // show loading(progressbar)

    // setup recyclerview
    binding.apply {
      rvTrending.layoutManager =
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      rvTrending.adapter = adapterTrending.withLoadStateFooter(
        footer = LoadingStateAdapter { adapterTrending.retry() }
      )

      rvUpcoming.layoutManager =
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      rvUpcoming.adapter = adapterUpcoming.withLoadStateFooter(
        footer = LoadingStateAdapter { adapterUpcoming.retry() }
      )

      rvPlayingNow.layoutManager =
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      rvPlayingNow.adapter = adapterPlayingNow.withLoadStateFooter(
        footer = LoadingStateAdapter { adapterPlayingNow.retry() }
      )
    }

    // trending week, default will be week
    if (binding.rbThisWeek.isChecked) {
      homeViewModel.getTrendingWeek(region).observe(viewLifecycleOwner) {
        adapterTrending.submitData(lifecycle, it)
      }
    }

    // handle movie trending based on switch button
    binding.rbToday.setOnClickListener {
      homeViewModel.getTrendingDay(region).observe(viewLifecycleOwner) {
        adapterTrending.submitData(lifecycle, it)
      }
      adapterTrending.retry()
      adapterTrending.refresh()
    }
    binding.rbThisWeek.setOnClickListener {
      homeViewModel.getTrendingWeek(region).observe(viewLifecycleOwner) {
        adapterTrending.submitData(lifecycle, it)
      }
      adapterTrending.retry()
      adapterTrending.refresh()
    }

    // get movie upcoming and playing now on cinema
    homeViewModel.getUpcomingMovies(region).observe(viewLifecycleOwner) {
      adapterUpcoming.submitData(lifecycle, it)
    }
    homeViewModel.getPlayingNowMovies(region).observe(viewLifecycleOwner) {
      adapterPlayingNow.submitData(lifecycle, it)
    }

    // check if there any movie that play on selected region, if not show info on toast and edit text
    adapterPlayingNow.addLoadStateListener { loadState ->
      if (loadState.source.refresh is LoadState.NotLoading
        && loadState.append.endOfPaginationReached
        && adapterPlayingNow.itemCount < 1
      ) {
        showToastShort(
          requireContext(),
          getString(R.string.no_movie_currently_playing, Locale("", region).displayCountry)
        )
        binding.rvPlayingNow.visibility = View.INVISIBLE
        if (!binding.tvPlayingNow.text.contains(getString(R.string.data)))
          binding.tvPlayingNow.append(" (" + getString(R.string.no_data) + ")")
      } else binding.rvPlayingNow.visibility = View.VISIBLE
    }

    // check if there any upcoming movie on selected region, if not show info on toast and edit text
    adapterUpcoming.addLoadStateListener { loadState ->
      if (loadState.source.refresh is LoadState.NotLoading
        && loadState.append.endOfPaginationReached
        && adapterUpcoming.itemCount < 1
      ) {
        showToastShort(
          requireContext(),
          getString(R.string.no_upcoming_movie, Locale("", region).displayCountry)
        )
        binding.rvUpcoming.visibility = View.INVISIBLE
        if (!binding.tvUpcomingMovie.text.contains(getString(R.string.data)))
          binding.tvUpcomingMovie.append(" (" + getString(R.string.no_data) + ")")
      } else binding.rvUpcoming.visibility = View.VISIBLE

    }

    binding.swipeRefresh.setOnRefreshListener {
      adapterTrending.refresh()
      adapterPlayingNow.refresh()
      adapterUpcoming.refresh()
      showSnackBarNoAction(checkInternet(requireContext()))
      binding.swipeRefresh.isRefreshing = false
    }
  }

  private fun combinedLoadStatesHandle(loadState: CombinedLoadStates) {
    if (loadState.refresh is LoadState.Loading ||
      loadState.append is LoadState.Loading
    )
      showLoading(true) // show ProgressBar
    else {
      showLoading(false) // hide ProgressBar

      val errorState = when { // If theres an error, show a toast
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        else -> null
      }
      errorState?.let { showToastShort(requireActivity(), it.error.toString()) }
    }
  }

  private fun showSnackBarNoAction(message: String) {
    val snackBar = Snackbar.make(
      activity?.findViewById(android.R.id.content)!!,
      message,
      Snackbar.LENGTH_SHORT
    ).setAnchorView(binding.guideSnackbar)

    val snackbarView = snackBar.view
    snackbarView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red_matte))
    if (message.isNotEmpty()) snackBar.show()
  }

  private fun animationFadeOut() {
    val animation = animFadeOutLong(requireContext())
    binding.backgroundDimMovie.startAnimation(animation)
    binding.progressBar.startAnimation(animation)

    Handler(Looper.getMainLooper()).postDelayed({
      binding.backgroundDimMovie.visibility = View.GONE
      binding.progressBar.visibility = View.GONE
    }, DELAY_TIME)
  }

  private fun showLoading(isLoading: Boolean) {
    if (isLoading) {
      binding.backgroundDimMovie.visibility = View.VISIBLE
      binding.progressBar.visibility = View.VISIBLE
    } else animationFadeOut()
  }

  private fun hideActionBar() {
    // disable action bar
    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      (activity as AppCompatActivity).supportActionBar?.hide()
    }
  }

  override fun onResume() {
    super.onResume()
    hideActionBar()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    const val DELAY_TIME = 600L
  }
}