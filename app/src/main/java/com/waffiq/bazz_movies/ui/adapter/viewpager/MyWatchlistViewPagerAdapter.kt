package com.waffiq.bazz_movies.ui.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.waffiq.bazz_movies.ui.activity.mywatchlist.MyWatchlistMoviesFragment
import com.waffiq.bazz_movies.ui.activity.mywatchlist.MyWatchlistTvSeriesFragment

class MyWatchlistViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
  FragmentStateAdapter(fragmentManager, lifecycle) {

  override fun getItemCount(): Int {
    return NUM_TABS
  }

  override fun createFragment(position: Int): Fragment {
    return when (position) {
      0 -> MyWatchlistMoviesFragment()
      else -> MyWatchlistTvSeriesFragment()
    }
  }

  companion object {
    const val NUM_TABS = 2
  }
}
