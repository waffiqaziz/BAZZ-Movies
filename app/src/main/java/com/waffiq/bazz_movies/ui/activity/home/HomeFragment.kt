package com.waffiq.bazz_movies.ui.activity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.waffiq.bazz_movies.databinding.FragmentHomeBinding
import com.waffiq.bazz_movies.ui.adapter.viewpager.HomeViewPagerAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.utils.Constants.tabHomeHeadingArray

class HomeFragment : Fragment() {

  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding!!

  private lateinit var viewpager: ViewPager2
  private lateinit var tabLayout: TabLayout

  private lateinit var homeViewModel: HomeViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

    setupTabLayoutViewPager()
    return root
  }

  private fun setupTabLayoutViewPager(){
    viewpager = binding.viewPager
    tabLayout = binding.tabs
    viewpager.isUserInputEnabled = false //disable swiping between tabs

    val adapter = HomeViewPagerAdapter(childFragmentManager,lifecycle)
    viewpager.adapter = adapter

    TabLayoutMediator(tabLayout, viewpager) { tab, position ->
      tab.text = tabHomeHeadingArray[position]
    }.attach()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

}