package com.waffiq.bazz_movies.feature.home.ui.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class GenericViewPagerAdapter(
  fragmentManager: FragmentManager,
  lifecycle: Lifecycle,
  private val fragments: List<Fragment>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

  override fun getItemCount(): Int =
    fragments.size

  override fun createFragment(position: Int): Fragment =
    fragments[position]

  override fun getItemId(position: Int): Long =
    fragments[position].javaClass.simpleName.hashCode().toLong()

  override fun containsItem(itemId: Long): Boolean =
    fragments.any { it.javaClass.simpleName.hashCode().toLong() == itemId }
}