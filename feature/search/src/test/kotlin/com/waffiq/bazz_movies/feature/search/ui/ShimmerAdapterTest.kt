package com.waffiq.bazz_movies.feature.search.ui

import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ShimmerAdapterTest {

  private lateinit var adapter: ShimmerAdapter
  private lateinit var recyclerView: RecyclerView

  @Before
  fun setup() {
    adapter = ShimmerAdapter()
    recyclerView = RecyclerView(ApplicationProvider.getApplicationContext())
    recyclerView.adapter = adapter
  }

  @Test
  fun shimmerAdapter_whenUsingDefaultCount_returnsShimmerDataSize() {
    assertEquals(ShimmerAdapter.SHIMMER_DATA, adapter.itemCount)
  }

  @Test
  fun shimmerAdapter_whenCustomItemCountProvided_returnsExpectedItemCount() {
    val customItemCount = 15
    val customAdapter = ShimmerAdapter(itemCount = customItemCount)
    assertEquals(customItemCount, customAdapter.itemCount)
  }

  @Test
  fun shimmerAdapter_whenCreatingViewHolder_returnsShimmerViewHolder() {
    val parent = FrameLayout(ApplicationProvider.getApplicationContext())
    val viewHolder = adapter.onCreateViewHolder(parent, 0)

    assertNotNull(viewHolder)
  }

  @Test
  fun shimmerAdapter_whenAttachedToRecyclerView_bindsShimmerItems() {
    recyclerView.layoutManager = LinearLayoutManager(ApplicationProvider.getApplicationContext())
    recyclerView.measure(0, 0)
    recyclerView.layout(0, 0, 1000, 3000)

    assertEquals(ShimmerAdapter.SHIMMER_DATA, recyclerView.childCount)
  }
}
