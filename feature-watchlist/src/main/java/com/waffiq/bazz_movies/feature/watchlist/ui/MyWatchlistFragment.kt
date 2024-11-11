package com.waffiq.bazz_movies.feature.watchlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.waffiq.bazz_movies.core.movie.utils.common.Constants.tabMoviesTvHeadingArray
import com.waffiq.bazz_movies.core.ui.R.string.binding_error
import com.waffiq.bazz_movies.core.ui.viewpager.GenericViewPagerAdapter
import com.waffiq.bazz_movies.feature.watchlist.databinding.FragmentMyWatchlistBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyWatchlistFragment : Fragment() {

  private var _binding: FragmentMyWatchlistBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private lateinit var viewpager: ViewPager2
  private lateinit var tabLayout: TabLayout
  private var tabLayoutMediator: TabLayoutMediator? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMyWatchlistBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupView()
  }

  private fun setupView() {
    viewpager = binding.viewPager
    tabLayout = binding.tabs
    viewpager.isUserInputEnabled = false // disable swiping between tabs

    val adapter = GenericViewPagerAdapter(
      childFragmentManager,
      lifecycle,
      listOf(MyWatchlistMoviesFragment(), MyWatchlistTvSeriesFragment())
    )
    viewpager.adapter = adapter

    tabLayoutMediator = TabLayoutMediator(tabLayout, viewpager) { tab, position ->
      tab.text = requireActivity().getString(tabMoviesTvHeadingArray[position])
    }
    tabLayoutMediator?.attach()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    tabLayoutMediator?.detach()
    tabLayoutMediator = null
    binding.viewPager.adapter = null
    binding.viewPager.removeAllViews()
    _binding = null
  }
}
