package com.waffiq.bazz_movies.feature.home.ui.adapter

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.home.databinding.ItemWideBinding
import com.waffiq.bazz_movies.feature.home.testutils.BaseAdapterTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ItemWIdeAdapterTest : BaseAdapterTest() {

  private lateinit var adapter: ItemWIdeAdapter
  private lateinit var binding: ItemWideBinding
  private lateinit var viewHolder: ItemWIdeAdapter.ViewHolder

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  override fun setup() {
    super.setup()
    adapter = ItemWIdeAdapter(navigator)
    recyclerView.adapter = adapter
    binding = ItemWideBinding.inflate(inflater, parent, false)
    viewHolder = adapter.ViewHolder(binding)
  }

  @Test
  fun submitData_withPagingData_bindsCorrectMovieData() =
    runTest {
      adapter.submitData(fakeMovieMediaItemPagingData)
      adapter.onBindViewHolder(viewHolder, 0)
      assertEquals(2, adapter.itemCount)

      submitPagingAndWait(mediaMovieItem)
      assertEquals("Inception", binding.tvTitle.text)
    }

  @Test
  fun onBindViewHolder_withNullData_doesNotCrash() =
    runTest {
      val fakePagingData = PagingData.empty<MediaItem>()
      adapter.submitData(fakePagingData)
      advanceUntilIdle()

      assertEquals(0, adapter.itemCount)

      if (adapter.itemCount > 0) adapter.onBindViewHolder(viewHolder, 0)
    }

  @Test
  fun onCreateViewHolder_whenCalled_createsViewHolderCorrectly() {
    val viewHolder = adapter.onCreateViewHolder(parent, 0)
    assertNotNull(viewHolder)

    // verify ViewHolder is correctly inflated
    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
  }

  @Test
  fun movieSearchItem_whenClicked_opensMovieDetails() =
    runTest {
      submitPagingAndWait(mediaMovieItem)
      binding.container.performClick()

      val expectedItem = mediaMovieItem
      verify(navigator).openDetails(context, expectedItem)
    }

  @Test
  fun areContentsTheSame_whenContentIsSame_returnsTrue() {
    val oldItem = mediaMovieItem
    val newItem = mediaMovieItem

    assertTrue(ItemWIdeAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem))
    assertTrue(ItemWIdeAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenDifferent_returnsFalse() {
    val oldItem = mediaMovieItem
    val newItem = mediaMovieItem.copy(id = 2)

    assertFalse(ItemWIdeAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem))
    assertFalse(ItemWIdeAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem))
  }

  private fun submitPagingAndWait(item: MediaItem) =
    runTest {
      adapter.submitData(PagingData.from(listOf(item)))
      advanceUntilIdle()
      adapter.onBindViewHolder(viewHolder, 0)
    }
}
