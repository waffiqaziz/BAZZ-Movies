package com.waffiq.bazz_movies.ui.activity.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.databinding.FragmentFeaturedBinding
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.TrendingAdapter
import com.waffiq.bazz_movies.ui.adapter.UpcomingMovieAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory


class FeaturedFragment : Fragment() {

  private var _binding: FragmentFeaturedBinding? = null
  private val binding get() = _binding!!

  private val viewModel: HomeViewModel by viewModels {
    ViewModelFactory(requireContext())
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentFeaturedBinding.inflate(inflater, container, false)
    val root: View = binding.root

//    // Inflate the layout for this fragment
//    return inflater.inflate(R.layout.fragment_featured, container, false)

    setMoveNowPlaying()
    setTrending()
    return root
  }

  private fun setMoveNowPlaying() {
//    val data = viewModel.getFirstMovieNowPlaying()
//    viewModel.getFirstMovieNowPlaying().observe(viewLifecycleOwner) {
//      Log.e("Cek Link : ", it[0].backdropPath)
//    }
    Glide.with(binding.imgMainFeatured)
//      .load("http://image.tmdb.org/t/p/w500/" + data.backdropPath) // URL movie poster
      .load("https://image.tmdb.org/t/p/w1280/bQXAqRx2Fgc46uCVWgoPz5L5Dtr.jpg") // URL movie poster
      .placeholder(R.mipmap.ic_launcher)
      .error(R.drawable.ic_broken_image)
      .into(binding.imgMainFeatured)
  }

  private fun setTrending(){
    binding.rvTrending.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val adapter = TrendingAdapter()

    binding.rvTrending.adapter = adapter.withLoadStateFooter(
      footer = LoadingStateAdapter {
        adapter.retry()
      }
    )

    viewModel.getTrending().observe(viewLifecycleOwner) {
      adapter.submitData(lifecycle,it)
    }

    binding.rvUpcoming.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val adapterUpcoming = UpcomingMovieAdapter()

    binding.rvUpcoming.adapter = adapterUpcoming.withLoadStateFooter(
      footer = LoadingStateAdapter {
        adapterUpcoming.retry()
      }
    )

    viewModel.getUpcomingMovies().observe(viewLifecycleOwner) {
      adapterUpcoming.submitData(lifecycle,it)
    }
  }


  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

}