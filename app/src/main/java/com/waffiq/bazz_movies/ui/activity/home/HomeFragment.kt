package com.waffiq.bazz_movies.ui.activity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.waffiq.bazz_movies.R.string.binding_error
import com.waffiq.bazz_movies.databinding.FragmentHomeBinding
import com.waffiq.bazz_movies.ui.adapter.viewpager.HomeViewPagerAdapter
import com.waffiq.bazz_movies.utils.common.Constants.tabHomeHeadingArray

class HomeFragment : Fragment() {

  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private lateinit var viewpager: ViewPager2
  private lateinit var tabLayout: TabLayout

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    (activity as AppCompatActivity).supportActionBar?.title = null
    setupTabLayoutViewPager()
  }

  private fun setupTabLayoutViewPager() {
    viewpager = binding.viewPager
    tabLayout = binding.tabs
    viewpager.isUserInputEnabled = false //disable swipe action between tabs

    val adapter = HomeViewPagerAdapter(childFragmentManager, lifecycle)
    viewpager.adapter = adapter

    TabLayoutMediator(tabLayout, viewpager) { tab, position ->
      tab.text = requireActivity().getString(tabHomeHeadingArray[position])
    }.attach()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}