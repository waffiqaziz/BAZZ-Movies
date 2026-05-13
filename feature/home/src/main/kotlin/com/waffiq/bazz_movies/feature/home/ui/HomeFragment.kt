@file:Suppress("BackingPropertyNaming")

package com.waffiq.bazz_movies.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.uihelper.utils.GenericViewPagerAdapter
import com.waffiq.bazz_movies.feature.home.databinding.FragmentHomeBinding
import com.waffiq.bazz_movies.feature.home.utils.common.Constants.tabHomeHeadingArray
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private var tabLayoutMediator: TabLayoutMediator? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupTabLayoutViewPager()
  }

  private fun setupTabLayoutViewPager() {
    binding.viewPager.isUserInputEnabled = false // disable swipe action between tabs

    val adapter = GenericViewPagerAdapter(
      childFragmentManager,
      viewLifecycleOwner.lifecycle,
      listOf(
        FeaturedFragment::class.java to { FeaturedFragment() },
        MovieFragment::class.java    to { MovieFragment() },
        TvSeriesFragment::class.java to { TvSeriesFragment() },
        AsianFragment::class.java    to { AsianFragment() },
      ),
    )
    binding.viewPager.adapter = adapter

    tabLayoutMediator = TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
      tab.text = requireActivity().getString(tabHomeHeadingArray[position])
    }.also { it.attach() }
  }

  override fun onDestroyView() {
    super.onDestroyView()

    tabLayoutMediator?.detach()
    tabLayoutMediator = null
    binding.viewPager.adapter = null
    _binding = null
  }
}
