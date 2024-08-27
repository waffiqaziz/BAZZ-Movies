package com.waffiq.bazz_movies.ui.activity.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.drawable.ic_bazz_placeholder_search
import com.waffiq.bazz_movies.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.R.id.nav_view
import com.waffiq.bazz_movies.R.string.binding_error
import com.waffiq.bazz_movies.R.string.data
import com.waffiq.bazz_movies.R.string.no_data
import com.waffiq.bazz_movies.R.string.no_movie_currently_playing
import com.waffiq.bazz_movies.R.string.no_upcoming_movie
import com.waffiq.bazz_movies.databinding.FragmentFeaturedBinding
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.MovieHomeAdapter
import com.waffiq.bazz_movies.ui.adapter.TrendingAdapter
import com.waffiq.bazz_movies.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Helper.animFadeOutLong
import com.waffiq.bazz_movies.utils.Helper.showToastShort
import com.waffiq.bazz_movies.utils.common.Constants.TMDB_IMG_LINK_BACKDROP_W780
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.helpers.GetRegionHelper.getLocation
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.utils.helpers.SnackBarManager
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class FeaturedFragment : Fragment() {

  private var _binding: FragmentFeaturedBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private lateinit var movieViewModel: MovieViewModel
  private lateinit var userPreferenceViewModel: UserPreferenceViewModel
  private lateinit var regionViewModel: RegionViewModel

  private var mSnackbar: Snackbar? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentFeaturedBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    movieViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]

    val pref = requireContext().dataStore
    val factory2 = ViewModelUserFactory.getInstance(pref)
    userPreferenceViewModel = ViewModelProvider(this, factory2)[UserPreferenceViewModel::class.java]
    regionViewModel = ViewModelProvider(this, factory2)[RegionViewModel::class.java]

    setRegion()
    showMainPicture()

    return root
  }

  private fun showMainPicture() {
    Glide.with(binding.imgMainFeatured)
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
    // Initialize adapters
    val adapterTrending = TrendingAdapter()
    val adapterUpcoming = MovieHomeAdapter()
    val adapterPlayingNow = MovieHomeAdapter()
    adapterPlayingNow.addLoadStateListener { combinedLoadStatesHandle(it) } // show loading(progressbar)

    // Setup RecyclerViews
    binding.apply {
      rvTrending.layoutManager = setupRecyclerView()
      rvTrending.adapter = adapterTrending.withLoadStateFooter(
        footer = LoadingStateAdapter { adapterTrending.retry() }
      )

      rvUpcoming.layoutManager = setupRecyclerView()
      rvUpcoming.adapter = adapterUpcoming.withLoadStateFooter(
        footer = LoadingStateAdapter { adapterUpcoming.retry() }
      )

      rvPlayingNow.layoutManager = setupRecyclerView()
      rvPlayingNow.adapter = adapterPlayingNow.withLoadStateFooter(
        footer = LoadingStateAdapter { adapterPlayingNow.retry() }
      )
    }

    // Observe ViewModel data and submit to adapters
    observeTrendingMovies(region, adapterTrending)
    observeUpcomingMovies(region, adapterUpcoming)
    observePlayingNowMovies(region, adapterPlayingNow)

    // Handle LoadState for RecyclerViews
    handleLoadState(
      adapterPlayingNow,
      binding.rvPlayingNow,
      binding.tvPlayingNow,
      no_movie_currently_playing,
      region
    )
    handleLoadState(
      adapterUpcoming,
      binding.rvUpcoming,
      binding.tvUpcomingMovie,
      no_upcoming_movie,
      region
    )

    // Set up swipe-to-refresh
    setupSwipeRefresh(adapterTrending, adapterPlayingNow, adapterUpcoming)

    // Set up retry button
    setupRetryButton(adapterTrending, adapterPlayingNow, adapterUpcoming)
  }

  private fun setupRecyclerView() =
    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

  private fun observeTrendingMovies(region: String, adapter: TrendingAdapter) {
    movieViewModel.getTrendingWeek(region).observe(viewLifecycleOwner) {
      adapter.submitData(lifecycle, it)
    }

    binding.rbToday.setOnClickListener {
      movieViewModel.getTrendingDay(region).distinctUntilChanged().observe(viewLifecycleOwner) {
        Log.d("TrendingDay", "Data received: $it")
        adapter.submitData(lifecycle, it)
      }
    }
    binding.rbThisWeek.setOnClickListener {
      movieViewModel.getTrendingWeek(region).distinctUntilChanged().observe(viewLifecycleOwner) {
        Log.d("TrendingWeek", "Data received: $it")
        adapter.submitData(lifecycle, it)
      }
    }
  }

  private fun observeUpcomingMovies(region: String, adapter: MovieHomeAdapter) {
    movieViewModel.getUpcomingMovies(region).observe(viewLifecycleOwner) {
      adapter.submitData(lifecycle, it)
    }
  }

  private fun observePlayingNowMovies(region: String, adapter: MovieHomeAdapter) {
    movieViewModel.getPlayingNowMovies(region).observe(viewLifecycleOwner) {
      adapter.submitData(lifecycle, it)
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
      if (loadState.source.refresh is LoadState.NotLoading
        && loadState.append.endOfPaginationReached
        && adapter.itemCount < 1
      ) {
        showToastShort(
          requireContext(),
          getString(noMoviesStringRes, Locale("", region).displayCountry)
        )
        recyclerView.visibility = View.INVISIBLE
        if (!textView.text.contains(getString(data))) {
          textView.append(" (${getString(no_data)})")
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
    binding.illustrationError.btnTryAgain.setOnClickListener {
      adapters.forEach { it.refresh() }
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
        showView(false)
        mSnackbar = SnackBarManager.snackBarWarning(
          requireContext(),
          binding.root,
          requireActivity().findViewById(nav_view),
          Event(pagingErrorHandling(it.error))
        )
      } ?: run {
        showView(true) // hide the error illustration if no error
      }
    }
  }

  private fun showView(isShow: Boolean) {
    // Toggle visibility based on the flag
    setMainContentVisibility(isShow)
    setErrorIllustrationVisibility(!isShow)
  }

  private fun setMainContentVisibility(isVisible: Boolean) {
    val visibility = if (isVisible) View.VISIBLE else View.GONE
    binding.apply {
      imgMainFeatured.visibility = visibility
      tvTrending.visibility = visibility
      toggle.visibility = visibility
      rvTrending.visibility = visibility
      tvUpcomingMovie.visibility = visibility
      rvUpcoming.visibility = visibility
      rvPlayingNow.visibility = visibility
      tvPlayingNow.visibility = visibility
    }
  }

  private fun setErrorIllustrationVisibility(isVisible: Boolean) {
    val visibility = if (isVisible) View.VISIBLE else View.GONE
    binding.illustrationError.icGeneralErrror.visibility = visibility
    binding.illustrationError.root.visibility = visibility
  }

  private fun animationFadeOut() {
    val animation = animFadeOutLong(requireContext())
    binding.backgroundDimMovie.startAnimation(animation)
    binding.progressBar.startAnimation(animation)

    Handler(Looper.getMainLooper()).post {
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