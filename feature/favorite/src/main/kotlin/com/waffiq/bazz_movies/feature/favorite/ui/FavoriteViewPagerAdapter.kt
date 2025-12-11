package com.waffiq.bazz_movies.feature.favorite.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.feature.favorite.ui.fragment.FavoriteChildFragment

/**
 * Adapter responsible for displaying favorite media tabs inside a ViewPager.
 * It provides fragments for movie and TV favorites based on the selected position.
 *
 * @param fragmentManager manager used to handle fragment transactions.
 * @param lifecycle lifecycle tied to the pager's hosting component.
 */
class FavoriteViewPagerAdapter(
  fragmentManager: FragmentManager,
  lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

  override fun getItemCount(): Int = 2

  override fun createFragment(position: Int): Fragment {
    return when (position) {
      0 -> FavoriteChildFragment.newInstance(MOVIE_MEDIA_TYPE)
      1 -> FavoriteChildFragment.newInstance(TV_MEDIA_TYPE)
      else -> error("Invalid position: $position")
    }
  }
}
