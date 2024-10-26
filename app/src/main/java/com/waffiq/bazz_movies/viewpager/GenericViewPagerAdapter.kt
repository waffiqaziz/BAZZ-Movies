package com.waffiq.bazz_movies.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class GenericViewPagerAdapter(
  fragmentManager: FragmentManager,
  lifecycle: Lifecycle,
  private val fragments: List<Fragment>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

  override fun getItemCount(): Int = fragments.size

  override fun createFragment(position: Int): Fragment {
    return fragments[position]
  }
}