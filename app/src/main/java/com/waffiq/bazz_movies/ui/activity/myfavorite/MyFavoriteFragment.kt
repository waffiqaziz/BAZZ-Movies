package com.waffiq.bazz_movies.ui.activity.myfavorite

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
import com.waffiq.bazz_movies.databinding.FragmentMyFavoriteBinding
import com.waffiq.bazz_movies.ui.adapter.viewpager.MyFavoriteViewPagerAdapter
import com.waffiq.bazz_movies.utils.common.Constants.tabMoviesTvHeadingArray

class MyFavoriteFragment : Fragment() {

  private var _binding: FragmentMyFavoriteBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private lateinit var viewpager: ViewPager2
  private lateinit var tabLayout: TabLayout
  private var tabLayoutMediator: TabLayoutMediator? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMyFavoriteBinding.inflate(inflater, container, false)
    (activity as AppCompatActivity).supportActionBar?.show()
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupTabLayoutViewPager()
  }

  private fun setupTabLayoutViewPager() {
    viewpager = binding.viewPager
    tabLayout = binding.tabs
    viewpager.isUserInputEnabled = false // disable swiping between tabs

    val adapter = MyFavoriteViewPagerAdapter(childFragmentManager, lifecycle)
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
