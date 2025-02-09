package com.waffiq.bazz_movies.feature.home.ui

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
import com.waffiq.bazz_movies.core.designsystem.viewpager.GenericViewPagerAdapter
import com.waffiq.bazz_movies.feature.home.databinding.FragmentHomeBinding
import com.waffiq.bazz_movies.feature.home.utils.common.Constants.tabHomeHeadingArray
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private var viewpager: ViewPager2? = null
  private var tabLayout: TabLayout? = null
  private var tabLayoutMediator: TabLayoutMediator? = null

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
    (activity as? AppCompatActivity)?.let {
      it.setSupportActionBar(binding.layoutToolbar.toolbar)
      it.supportActionBar?.title = null
    }
    setupTabLayoutViewPager()
  }

  private fun setupTabLayoutViewPager() {
    viewpager = binding.viewPager
    tabLayout = binding.tabs
    viewpager?.isUserInputEnabled = false // disable swipe action between tabs

    val adapter = GenericViewPagerAdapter(
      childFragmentManager,
      lifecycle,
      listOf(FeaturedFragment(), MovieFragment(), TvSeriesFragment())
    )
    viewpager?.adapter = adapter

    // Number of pages to keep in memory, make it more smooth
    viewpager?.offscreenPageLimit = 2

    tabLayout?.let { tabLayoutNonNull ->
      viewpager?.let { viewpagerNonNull ->
        tabLayoutMediator = TabLayoutMediator(tabLayoutNonNull, viewpagerNonNull) { tab, position ->
          tab.text = requireActivity().getString(tabHomeHeadingArray[position])
        }
      }
    }
    tabLayoutMediator?.attach()
  }

  override fun onDestroyView() {
    super.onDestroyView()

    tabLayoutMediator?.detach()
    tabLayoutMediator = null

    binding.viewPager.adapter = null
    binding.viewPager.removeAllViews()

    tabLayout = null
    viewpager = null

    _binding = null
  }
}
