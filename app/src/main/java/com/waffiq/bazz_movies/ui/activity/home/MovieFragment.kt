package com.waffiq.bazz_movies.ui.activity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.waffiq.bazz_movies.databinding.FragmentMovieBinding
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.MovieAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory

class MovieFragment : Fragment() {

  private var _binding: FragmentMovieBinding? = null
  private val binding get() = _binding!!

  private lateinit var viewModel: HomeViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMovieBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

    setData()

    return root
  }

  private fun setData(){
    binding.rvPopular.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val popularAdapter = MovieAdapter()
    binding.rvPopular.adapter = popularAdapter.withLoadStateFooter(
      footer = LoadingStateAdapter {
        popularAdapter.retry()
      }
    )
    viewModel.getPopularMovies().observe(viewLifecycleOwner) {
      popularAdapter.submitData(lifecycle,it)
    }

    binding.rvNowPlaying.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val nowPlayingAdapter = MovieAdapter()
    binding.rvNowPlaying.adapter = nowPlayingAdapter.withLoadStateFooter(
      footer = LoadingStateAdapter {
        nowPlayingAdapter.retry()
      }
    )
    viewModel.getPlayingNowMovies().observe(viewLifecycleOwner) {
      nowPlayingAdapter.submitData(lifecycle,it)
    }

    binding.rvUpcoming.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val upComingAdapter = MovieAdapter()
    binding.rvUpcoming.adapter = upComingAdapter.withLoadStateFooter(
      footer = LoadingStateAdapter {
        nowPlayingAdapter.retry()
      }
    )
    viewModel.getUpcomingMovies().observe(viewLifecycleOwner) {
      upComingAdapter.submitData(lifecycle,it)
    }

    binding.rvTopRated.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val topRatedAdapter = MovieAdapter()
    binding.rvTopRated.adapter = topRatedAdapter.withLoadStateFooter(
      footer = LoadingStateAdapter {
        topRatedAdapter.retry()
      }
    )
    viewModel.getTopRatedMovies().observe(viewLifecycleOwner) {
      topRatedAdapter.submitData(lifecycle,it)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}