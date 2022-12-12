package com.waffiq.bazz_movies.ui.activity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.waffiq.bazz_movies.databinding.FragmentHomeBinding
import com.waffiq.bazz_movies.ui.adapter.ViewPagerAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {

  private var _binding: FragmentHomeBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  lateinit var viewpager: ViewPager2
  lateinit var tabLayout: TabLayout

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val homeViewModel: HomeViewModel by viewModels {
      ViewModelFactory(requireContext())
    }

    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textHome
    homeViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }

    setupTabLayoutViewPager()

    return root
  }

  fun setupTabLayoutViewPager(){
    viewpager = binding.viewPager
    tabLayout = binding.tabs
    viewpager.isUserInputEnabled = false //disable swiping between tabs

    val adapter = ViewPagerAdapter(childFragmentManager,lifecycle)
    viewpager.adapter = adapter

    TabLayoutMediator(tabLayout, viewpager) { tab, position ->
      tab.text = tabHeadingArray[position]
    }.attach()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    private val tabHeadingArray = arrayOf(
      "Featured",
      "Movies",
      "TV Series"
    )
  }
}