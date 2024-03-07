package com.waffiq.bazz_movies.ui.activity.myfavorite

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.waffiq.bazz_movies.databinding.FragmentMyFavoriteBinding
import com.waffiq.bazz_movies.ui.adapter.viewpager.MyFavoriteViewPagerAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.utils.Constants.tabMoviesTvHeadingArray
import com.waffiq.bazz_movies.R.string.binding_error

class MyFavoriteFragment : Fragment() {

  private var _binding: FragmentMyFavoriteBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private lateinit var viewModel: MyFavoriteViewModel

  private lateinit var viewpager: ViewPager2
  private lateinit var tabLayout: TabLayout

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMyFavoriteBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    viewModel = ViewModelProvider(this, factory)[MyFavoriteViewModel::class.java]

    (activity as AppCompatActivity).supportActionBar?.show()

    setupTabLayoutViewPager()
    hideActionBar()
    return root
  }

  private fun setupTabLayoutViewPager() {
    viewpager = binding.viewPager
    tabLayout = binding.tabs
    viewpager.isUserInputEnabled = false //disable swiping between tabs

    val adapter = MyFavoriteViewPagerAdapter(childFragmentManager, lifecycle)
    viewpager.adapter = adapter

    TabLayoutMediator(tabLayout, viewpager) { tab, position ->
      tab.text = requireActivity().getString(tabMoviesTvHeadingArray[position])
    }.attach()
  }

  private fun hideActionBar() {
    // disable action bar
    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      (activity as AppCompatActivity).supportActionBar?.hide()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}