package com.waffiq.bazz_movies.ui.activity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.databinding.FragmentTvSeriesBinding
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.TvAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.utils.Helper

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

    showSnackBarNoAction(Helper.checkInternet(requireContext()))
    setData()

    return root
  }

  private fun setData(){
    binding.rvPopular.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val popularAdapter = TvAdapter()
    binding.rvPopular.adapter = popularAdapter.withLoadStateFooter(
      footer = LoadingStateAdapter {
        popularAdapter.retry()
      }
    )
    viewModel.getPopularTv().observe(viewLifecycleOwner) {
      popularAdapter.submitData(lifecycle,it)
    }

    binding.rvNowPlaying.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val nowPlayingAdapter = TvAdapter()
    binding.rvNowPlaying.adapter = nowPlayingAdapter.withLoadStateFooter(
      footer = LoadingStateAdapter {
        nowPlayingAdapter.retry()
      }
    )
    viewModel.getAiringTodayTv().observe(viewLifecycleOwner) {
      nowPlayingAdapter.submitData(lifecycle,it)
    }

    binding.rvUpcoming.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val upComingAdapter = TvAdapter()
    binding.rvUpcoming.adapter = upComingAdapter.withLoadStateFooter(
      footer = LoadingStateAdapter {
        upComingAdapter.retry()
      }
    )
    viewModel.getOnTv().observe(viewLifecycleOwner) {
      upComingAdapter.submitData(lifecycle,it)
    }

    binding.rvTopRated.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val topRatedAdapter = TvAdapter()
    binding.rvTopRated.adapter = topRatedAdapter.withLoadStateFooter(
      footer = LoadingStateAdapter {
        topRatedAdapter.retry()
      }
    )
    viewModel.getTopRatedTv().observe(viewLifecycleOwner) {
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