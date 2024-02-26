package com.waffiq.bazz_movies.ui.activity.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.databinding.FragmentTvSeriesBinding
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.TvAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.utils.Helper.checkInternet
import com.waffiq.bazz_movies.utils.Helper.showToastShort
import com.waffiq.bazz_movies.utils.Helper.animFadeOutLong

class TvSeriesFragment : Fragment() {

  private var _binding: FragmentTvSeriesBinding? = null
  private val binding get() = _binding!!

  private lateinit var viewModel: HomeViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentTvSeriesBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

    showSnackBarNoAction(checkInternet(requireContext()))
    setData()

    return root
  }

  private fun setData() {
    // setup adapter
    val popularAdapter = TvAdapter()
    val nowPlayingAdapter = TvAdapter()
    val onTvAdapter = TvAdapter()
    val topRatedAdapter = TvAdapter()
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
      rvUpcoming.adapter = onTvAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter { onTvAdapter.retry() }
      )

      rvTopRated.layoutManager =
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      rvTopRated.adapter = topRatedAdapter.withLoadStateFooter(
        footer = LoadingStateAdapter {
          topRatedAdapter.retry()
        }
      )
    }

    // get data movie popular, airing today, on tv, and top rated
    viewModel.getPopularTv()
      .observe(viewLifecycleOwner) { popularAdapter.submitData(lifecycle, it) }
    viewModel.getAiringTodayTv().observe(viewLifecycleOwner) {
      nowPlayingAdapter.submitData(lifecycle, it)
    }
    viewModel.getOnTv().observe(viewLifecycleOwner) {
      onTvAdapter.submitData(lifecycle, it)
    }
    viewModel.getTopRatedTv().observe(viewLifecycleOwner) {
      topRatedAdapter.submitData(lifecycle, it)
    }

    // refresh whe swipe down
    binding.swipeRefresh.setOnRefreshListener {
      popularAdapter.refresh()
      topRatedAdapter.refresh()
      popularAdapter.refresh()
      onTvAdapter.refresh()
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

      val errorState = when {
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        else -> null
      }
      errorState?.let { showToastShort(requireActivity(), it.error.toString()) }
    }
  }

  private fun animationFadeOut() {
    val animation = animFadeOutLong(requireContext())
    binding.backgroundDimMovie.startAnimation(animation)
    binding.progressBar.startAnimation(animation)

    Handler(Looper.getMainLooper()).postDelayed({
      binding.backgroundDimMovie.visibility = View.GONE
      binding.progressBar.visibility = View.GONE
    }, FeaturedFragment.DELAY_TIME)
  }

  private fun showLoading(isLoading: Boolean) {
    if (isLoading) {
      binding.backgroundDimMovie.visibility = View.VISIBLE
      binding.progressBar.visibility = View.VISIBLE
    } else animationFadeOut()
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