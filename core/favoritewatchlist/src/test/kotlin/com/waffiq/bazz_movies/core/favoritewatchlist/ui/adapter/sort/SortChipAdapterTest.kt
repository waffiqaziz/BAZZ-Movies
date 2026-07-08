package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.sort

import android.os.Looper
import com.waffiq.bazz_movies.core.designsystem.R.string.oldest_added
import com.waffiq.bazz_movies.core.designsystem.R.string.recently_added
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemHeaderBinding
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.BaseAdapterTest
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import org.robolectric.Shadows.shadowOf

class SortChipAdapterTest : BaseAdapterTest<Nothing, SortChipAdapter>() {

  private lateinit var adapter: SortChipAdapter
  private val onSortClicked: () -> Unit = mockk(relaxed = true)

  private lateinit var binding: ItemHeaderBinding
  private lateinit var viewHolder: SortChipAdapter.ViewHolder

  @Before
  override fun setUp() {
    super.setUp()
    adapter = SortChipAdapter(onSortClicked)
    binding = ItemHeaderBinding.inflate(inflater, null, false)
    viewHolder = adapter.ViewHolder(binding)
    adapter.onBindViewHolder(viewHolder, 0)
  }

  @Test
  fun getItemCount_withOneItem_shouldReturnsOne() {
    assertEquals(1, adapter.itemCount)
  }

  @Test
  fun onCreateViewHolder_whenCalled_createsViewHolderCorrectly() {
    val viewHolder = adapter.onCreateViewHolder(parent, 0)

    assertNotNull(viewHolder)
    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
  }

  @Test
  fun onBindViewHolder_whenCalled_bindsCorrectForAllData() {
    assertEquals(
      context.getString(recently_added),
      binding.chipSort.text,
    )
  }

  @Test
  fun updateSort_setDifferentSort_changesChipText() {
    adapter.updateSort(oldest_added)
    adapter.onBindViewHolder(viewHolder, 0) // rebind
    assertEquals(context.getString(oldest_added), binding.chipSort.text)
  }

  @Test
  fun updateSort_successful_callsNotifyItemChanged() {
    val spyAdapter = spyk(adapter)
    attachAdapter(spyAdapter)

    spyAdapter.updateSort(oldest_added)
    shadowOf(Looper.getMainLooper()).idle()

    verify(exactly = 1) { spyAdapter.notifyItemChanged(0) }
  }

  @Test
  fun clickChip_whenSuccessful_invokesOnSortClicked() {
    binding.chipSort.performClick()
    shadowOf(Looper.getMainLooper()).idle()
    verify(exactly = 1) { onSortClicked() }
  }
}
