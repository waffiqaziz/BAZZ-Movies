package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.view.LayoutInflater
import android.widget.FrameLayout
import com.waffiq.bazz_movies.feature.detail.databinding.ChipGenreBinding
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywordsItem
import com.waffiq.bazz_movies.feature.detail.testutils.BaseAdapterTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class KeywordsAdapterTest : BaseAdapterTest() {

  private lateinit var adapter: KeywordsAdapter

  @Before
  fun setup() {
    super.baseSetup()
    adapter = KeywordsAdapter()
    recyclerView.adapter = adapter
  }

  @Test
  fun submitList_whenCalled_updatesListAndNotifiesChanges() {
    val oldList = listOf(MediaKeywordsItem(id = 1, name = "family"))
    val newList = listOf(
      MediaKeywordsItem(id = 2, name = "war"),
      MediaKeywordsItem(id = 3, name = "superhero")
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
    val inflater = LayoutInflater.from(context)
    val binding = ChipGenreBinding.inflate(inflater, null, false)
    val viewHolder = adapter.ViewHolder(binding)

    val testCases = listOf(
      MediaKeywordsItem(name = "war") to "war",
      MediaKeywordsItem(name = "tragedy") to "tragedy",
    )

    // test name
    for ((name, expectedText) in testCases) {
      adapter.submitList(listOf(name)) {
        viewHolder.bind(name)
        adapter.onBindViewHolder(viewHolder, 0)
        assertEquals(expectedText, binding.chip.text.toString())
      }
    }
  }

  @Test
  fun onCreateViewHolder_whenCalled_createsViewHolderCorrectly() {
    val parent = FrameLayout(context)
    val adapter = KeywordsAdapter()
    val viewHolder = adapter.onCreateViewHolder(parent, 0)
    assertNotNull(viewHolder)

    // verify ViewHolder is correctly inflated
    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
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
