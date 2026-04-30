package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.os.Looper
import com.waffiq.bazz_movies.feature.detail.databinding.ChipGenreBinding
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywordsItem
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

class KeywordsAdapterTest : BaseAdapterTest() {

  private lateinit var adapter: KeywordsAdapter
  private lateinit var viewHolder: KeywordsAdapter.ViewHolder
  private lateinit var binding: ChipGenreBinding

  private val mediaKeywordsItemList = listOf(
    MediaKeywordsItem(name = "war", id = 21),
    MediaKeywordsItem(name = "tragedy", id = 22),
  )

  @Before
  fun setup() {
    super.baseSetup()
    adapter = KeywordsAdapter(navigator)
    recyclerView.adapter = adapter
    viewHolder = adapter.onCreateViewHolder(parent, 0)
    binding = ChipGenreBinding.bind(viewHolder.itemView)
  }

  @Test
  fun submitList_whenCalled_updatesListAndNotifiesChanges() {
    val oldList = listOf(MediaKeywordsItem(id = 1, name = "family"))
    val newList = listOf(
      MediaKeywordsItem(id = 2, name = "war"),
      MediaKeywordsItem(id = 3, name = "superhero"),
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
    adapter.submitList(mediaKeywordsItemList)

    // Let ListAdapter process the diff
    shadowOf(Looper.getMainLooper()).idle()

    mediaKeywordsItemList.forEachIndexed { index, item ->
      adapter.onBindViewHolder(viewHolder, index) // Covers getItem(position)
      assertEquals(item.name, binding.chip.text.toString())
    }
  }

  @Test
  fun bind_withEmptyData_showsNothing() {
    val testData = emptyList<MediaKeywordsItem>()

    adapter.submitList(testData)
    shadowOf(Looper.getMainLooper()).idle()

    testData.forEachIndexed { index, _ ->
      adapter.onBindViewHolder(viewHolder, index)
      viewHolder.bind(MediaKeywordsItem())

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
  fun bind_whenIdIsNull_doesNotBindData() {
    viewHolder.bind(MediaKeywordsItem(id = null, name = "war"))

    val binding = ChipGenreBinding.bind(viewHolder.itemView)
    assertEquals("", binding.chip.text.toString())
  }

  @Test
  fun bind_whenNameIsNull_doesNotBindData() {
    viewHolder.bind(MediaKeywordsItem(id = 1, name = null))
    assertEquals("", binding.chip.text.toString())
  }

  @Test
  fun bind_whenNameIsEmpty_doesNotBindData() {
    viewHolder.bind(MediaKeywordsItem(id = 1, name = ""))
    assertEquals("", binding.chip.text.toString())
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
    adapter.submitList(mediaKeywordsItemList)
    shadowOf(Looper.getMainLooper()).idle()

    adapter.onBindViewHolder(viewHolder, 0)
    binding.chip.performClick()

    verify { navigator.openList(any(), any()) }
  }

  @Test
  fun areContentsTheSame_whenContentIsSame_returnsTrue() {
    val oldItem = MediaKeywordsItem(id = 1, name = "Test Name")
    val newItem = MediaKeywordsItem(id = 1, name = "Test Name")

    val diffCallback = KeywordsAdapter.CastDiffCallback()
    assertTrue(diffCallback.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenDifferent_returnsFalse() {
    val oldItem = MediaKeywordsItem(id = 1, name = "Test Name")
    val newItem = MediaKeywordsItem(id = 4, name = "Test Name")

    val diffCallback = KeywordsAdapter.CastDiffCallback()
    assertFalse(diffCallback.areContentsTheSame(oldItem, newItem))
  }
}
