package com.waffiq.bazz_movies.ui.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.waffiq.bazz_movies.ui.activity.myfavorite.MyFavoriteMoviesFragment
import com.waffiq.bazz_movies.ui.activity.myfavorite.MyFavoriteTvSeriesFragment
import com.waffiq.bazz_movies.utils.common.Constants.NUM_TABS

class MyFavoriteViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
  FragmentStateAdapter(fragmentManager, lifecycle) {

  override fun getItemCount(): Int {
    return NUM_TABS
  }

  override fun createFragment(position: Int): Fragment {
    return when (position) {
      0 -> MyFavoriteMoviesFragment()
      else -> MyFavoriteTvSeriesFragment()
    }
  }
}
