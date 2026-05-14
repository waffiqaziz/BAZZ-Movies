package com.waffiq.bazz_movies.core.uihelper.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * A custom adapter for managing fragments in a `ViewPager2` with a `FragmentStateAdapter`.
 * This adapter allows dynamic handling of fragments by passing a list of fragments to be
 * displayed in the `ViewPager2`. It ensures efficient fragment management by leveraging
 * the `FragmentStateAdapter`'s capabilities to handle fragment lifecycle and memory usage.
 *
 * @param fragmentManager The `FragmentManager` used to interact with fragments.
 * @param lifecycle The `Lifecycle` of the parent component, typically the activity or fragment,
 *                  to manage the lifecycle of the fragments.
 */
class GenericViewPagerAdapter(
  fragmentManager: FragmentManager,
  lifecycle: Lifecycle,
  private val fragmentFactories: List<Pair<Class<out Fragment>, () -> Fragment>>,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

  override fun getItemCount() = fragmentFactories.size

  override fun createFragment(position: Int) = fragmentFactories[position].second()

  override fun getItemId(position: Int): Long =
    fragmentFactories[position].first.name.hashCode().toLong()

  override fun containsItem(itemId: Long): Boolean =
    fragmentFactories.any { it.first.name.hashCode().toLong() == itemId }
}
