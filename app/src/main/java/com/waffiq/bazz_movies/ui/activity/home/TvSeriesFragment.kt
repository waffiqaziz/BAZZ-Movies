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
import com.waffiq.bazz_movies.R.color.red_matte
import com.waffiq.bazz_movies.R.string.binding_error
import com.waffiq.bazz_movies.databinding.FragmentTvSeriesBinding
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.TvAdapter
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelFactory
import com.waffiq.bazz_movies.utils.Helper.animFadeOutLong
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.helpers.PagingLoadStateHelper.pagingErrorHandling
import com.waffiq.bazz_movies.utils.helpers.SnackBarManager

class TvSeriesFragment : Fragment() {

  private var _binding: FragmentTvSeriesBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private lateinit var tvSeriesViewModel: TvSeriesViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentTvSeriesBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    tvSeriesViewModel = ViewModelProvider(this, factory)[TvSeriesViewModel::class.java]

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
        footer = LoadingStateAdapter { topRatedAdapter.retry() }
      )
    }

    // get data movie popular, airing today, on tv, and top rated
    tvSeriesViewModel.getPopularTv()
      .observe(viewLifecycleOwner) { popularAdapter.submitData(lifecycle, it) }
    tvSeriesViewModel.getAiringTodayTv().observe(viewLifecycleOwner) {
      nowPlayingAdapter.submitData(lifecycle, it)
    }
    tvSeriesViewModel.getOnTv().observe(viewLifecycleOwner) {
      onTvAdapter.submitData(lifecycle, it)
    }
    tvSeriesViewModel.getTopRatedTv().observe(viewLifecycleOwner) {
      topRatedAdapter.submitData(lifecycle, it)
    }

    // refresh whe swipe down
    binding.swipeRefresh.setOnRefreshListener {
      popularAdapter.refresh()
      topRatedAdapter.refresh()
      popularAdapter.refresh()
      onTvAdapter.refresh()
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
        val errorMessage = pagingErrorHandling(it.error)
        SnackBarManager.snackBarWarning(
          requireContext(),
          binding.root,
          binding.guideSnackbar,
          Event(errorMessage)
        )
      }
    }
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

  private fun showSnackBarNoAction(message: String) {
    val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
      .setAnchorView(binding.guideSnackbar)

    val snackbarView = snackBar.view
    snackbarView.setBackgroundColor(ContextCompat.getColor(requireContext(), red_matte))
    if (message.isNotEmpty()) snackBar.show()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}