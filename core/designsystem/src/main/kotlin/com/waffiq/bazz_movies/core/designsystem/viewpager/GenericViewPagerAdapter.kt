package com.waffiq.bazz_movies.core.designsystem.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
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
 * @param fragments A list of `Fragment` objects that will be displayed within the `ViewPager2`.
 */
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

  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    super.onDetachedFromRecyclerView(recyclerView)
  }

  override fun unregisterFragmentTransactionCallback(callback: FragmentTransactionCallback) {
    super.unregisterFragmentTransactionCallback(callback)
  }
}
