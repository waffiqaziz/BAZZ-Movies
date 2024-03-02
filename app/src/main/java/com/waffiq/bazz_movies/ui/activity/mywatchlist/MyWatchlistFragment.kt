package com.waffiq.bazz_movies.ui.activity.mywatchlist

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
import com.waffiq.bazz_movies.databinding.FragmentMyWatchlistBinding
import com.waffiq.bazz_movies.ui.adapter.viewpager.MyWatchlistViewPagerAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.utils.Constants.tabMoviesTvHeadingArray

class MyWatchlistFragment : Fragment() {

  private var _binding: FragmentMyWatchlistBinding? = null
  private val binding get() = _binding!!

  private lateinit var viewModel : MyWatchlistViewModel

  private lateinit var viewpager: ViewPager2
  private lateinit var tabLayout: TabLayout

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMyWatchlistBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    viewModel = ViewModelProvider(this, factory)[MyWatchlistViewModel::class.java]

    (activity as AppCompatActivity).supportActionBar?.show()

    setupView()
    hideActionBar()
    return root
  }

  private fun setupView(){
    viewpager = binding.viewPager
    tabLayout = binding.tabs
    viewpager.isUserInputEnabled = false //disable swiping between tabs

    val adapter = MyWatchlistViewPagerAdapter(childFragmentManager, lifecycle)
    viewpager.adapter = adapter

    TabLayoutMediator(tabLayout, viewpager){ tab, position ->
      tab.text = requireActivity().getString(tabMoviesTvHeadingArray[position])
    }.attach()
  }

  private fun hideActionBar(){
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