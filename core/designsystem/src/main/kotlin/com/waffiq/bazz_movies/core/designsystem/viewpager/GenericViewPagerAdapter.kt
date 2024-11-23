package com.waffiq.bazz_movies.core.designsystem.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
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

  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    super.onDetachedFromRecyclerView(recyclerView)
  }

  override fun unregisterFragmentTransactionCallback(callback: FragmentTransactionCallback) {
    super.unregisterFragmentTransactionCallback(callback)
  }
}
