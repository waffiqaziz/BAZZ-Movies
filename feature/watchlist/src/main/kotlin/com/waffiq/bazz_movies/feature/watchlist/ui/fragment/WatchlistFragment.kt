@file:Suppress("BackingPropertyNaming")

package com.waffiq.bazz_movies.feature.watchlist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.common.Constants.tabMoviesTvHeadingArray
import com.waffiq.bazz_movies.feature.watchlist.databinding.FragmentWatchlistBinding
import com.waffiq.bazz_movies.feature.watchlist.ui.WatchlistViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchlistFragment : Fragment() {

  private var _binding: FragmentWatchlistBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private lateinit var viewpager: ViewPager2
  private lateinit var tabLayout: TabLayout
  private var tabLayoutMediator: TabLayoutMediator? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    (activity as? AppCompatActivity)?.let {
      it.setSupportActionBar(binding.layoutToolbar.toolbar)
      it.supportActionBar?.title = null
    }
    setupView()
  }

  private fun setupView() {
    viewpager = binding.viewPager
    tabLayout = binding.tabs
    viewpager.isUserInputEnabled = false // disable swiping between tabs

    val adapter = WatchlistViewPagerAdapter(childFragmentManager, lifecycle)
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
