package com.waffiq.bazz_movies.feature.home.ui.shimmer

import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.home.databinding.ShimmerItemPosterBinding
import com.waffiq.bazz_movies.feature.home.testutils.BaseAdapterTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ShimmerAdapterTest : BaseAdapterTest() {

  private lateinit var adapter: ShimmerAdapter
  private lateinit var binding: ShimmerItemPosterBinding

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  override fun setup() {
    super.setup()
    adapter = ShimmerAdapter()
    recyclerView.adapter = adapter
    binding = ShimmerItemPosterBinding.inflate(inflater, parent, false)
  }

  @Test
  fun itemCount_defaultValue_returnsTen() =
    runTest {
      assertEquals(10, adapter.itemCount)
    }

  @Test
  fun itemCount_customValue_returnsValueCorrectly() =
    runTest {
      adapter = ShimmerAdapter(7)
      assertEquals(7, adapter.itemCount)
    }
}
