package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.os.Looper
import androidx.core.view.isVisible
import com.waffiq.bazz_movies.feature.detail.databinding.ItemCollectionPartBinding
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.PartsItem
import com.waffiq.bazz_movies.feature.detail.testutils.BaseAdapterTest
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.robolectric.Shadows.shadowOf

class CollectionPartsAdapterTest : BaseAdapterTest() {

  private lateinit var adapter: CollectionPartsAdapter
  private lateinit var viewHolder: CollectionPartsAdapter.ViewHolder
  private lateinit var binding: ItemCollectionPartBinding

  private val partsItems = listOf(
    PartsItem(title = "movie 1", id = 21, voteAverage = 21f),
    PartsItem(title = "movie 2", id = 22, voteAverage = 33f),
  )

  @Before
  fun setup() {
    super.baseSetup()
    adapter = CollectionPartsAdapter(navigator)
    recyclerView.adapter = adapter
    viewHolder = adapter.onCreateViewHolder(parent, 0)
    binding = ItemCollectionPartBinding.bind(viewHolder.itemView)
  }

  @Test
  fun submitList_whenCalled_updatesListAndNotifiesChanges() {
    val oldList = listOf(PartsItem(id = 1, title = "movie 4"))
    val newList = listOf(
      PartsItem(id = 2, title = "movie 5"),
      PartsItem(id = 3, title = "movie 6"),
    )

    adapter.submitList(oldList) {
      assertEquals(1, adapter.itemCount)

      adapter.submitList(newList) {
        assertEquals(2, adapter.itemCount)
        assertEquals(newList, adapter.currentList)
      }
    }
  }

  @Test
  fun onBindViewHolder_whenCalled_bindsCorrectForAllData() {
    adapter.submitList(partsItems)

    shadowOf(Looper.getMainLooper()).idle()

    partsItems.forEachIndexed { index, item ->
      adapter.onBindViewHolder(viewHolder, index)
      assertEquals(item.title, binding.tvMovieTitle.text.toString())
    }
  }

  @Test
  fun bind_withEmptyData_showsNothing() {
    val testData = emptyList<PartsItem>()

    adapter.submitList(testData)
    shadowOf(Looper.getMainLooper()).idle()

    testData.forEachIndexed { index, _ ->
      adapter.onBindViewHolder(viewHolder, index)
      viewHolder.bind(PartsItem())

      assertEquals(adapter.itemCount, 0)
    }
  }

  @Test
  fun bind_withNullData_showsNothing() {
    val testData = null

    adapter.submitList(testData)
    shadowOf(Looper.getMainLooper()).idle()
  }

  @Test
  fun bind_whenVoteAverageIsNull_hidesTheView() {
    val data = PartsItem(id = 32, voteAverage = null)

    viewHolder.bind(data)

    val binding = ItemCollectionPartBinding.bind(viewHolder.itemView)
    assertFalse(binding.tvMovieRating.isVisible)
    assertFalse(binding.metaDivider.isVisible)
  }

  @Test
  fun bind_whenVoteAverageIsNegative_hidesTheView() {
    val data = PartsItem(id = 32, voteAverage = -78f)

    viewHolder.bind(data)

    val binding = ItemCollectionPartBinding.bind(viewHolder.itemView)
    assertFalse(binding.tvMovieRating.isVisible)
    assertFalse(binding.metaDivider.isVisible)
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
  fun onClick_whenClicked_opensMovieDetails() {
    adapter.submitList(partsItems)
    shadowOf(Looper.getMainLooper()).idle()

    adapter.onBindViewHolder(viewHolder, 0)
    binding.container.performClick()

    verify { navigator.openDetails(any(), any()) }
  }

  @Test
  fun areContentsTheSame_whenContentIsSame_returnsTrue() {
    val oldItem = PartsItem(id = 1, title = "Test Title")
    val newItem = PartsItem(id = 1, title = "Test Title")

    val diffCallback = CollectionPartsAdapter.CollectionsDiffCallback()
    assertTrue(diffCallback.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenDifferent_returnsFalse() {
    val oldItem = PartsItem(id = 1, title = "Test Title")
    val newItem = PartsItem(id = 4, title = "Test Title")

    val diffCallback = CollectionPartsAdapter.CollectionsDiffCallback()
    assertFalse(diffCallback.areContentsTheSame(oldItem, newItem))
  }
}
