package com.waffiq.bazz_movies.core.uihelper.ui.adapter

import android.view.LayoutInflater
import android.widget.FrameLayout
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemSingleChoiceBinding
import com.waffiq.bazz_movies.core.uihelper.testutils.BaseAdapterTest
import com.waffiq.bazz_movies.core.uihelper.testutils.SortOption
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.robolectric.annotation.LooperMode

@LooperMode(LooperMode.Mode.PAUSED)
class SingleChoiceAdapterDefaultHandlerTest : BaseAdapterTest() {

  private lateinit var adapter: SingleChoiceAdapter<SortOption>
  private lateinit var binding: ItemSingleChoiceBinding

  private val items = SortOption.entries
  private val onSelected = mockk<(SortOption) -> Unit>(relaxed = true)

  @Before
  override fun setup() {
    super.setup()
    parent = FrameLayout(context)
    inflater = LayoutInflater.from(context)
    adapter = SingleChoiceAdapter(items, SortOption.TITLE_AZ, onSelected)
    binding = ItemSingleChoiceBinding.inflate(inflater, parent, false)
  }

  @Test
  fun clickItem_withNoPosition_doesNotInvokeCallback() {
    val holder = adapter.ViewHolder(binding)
    holder.binding.item.performClick()

    verify(exactly = 0) { onSelected(any()) }
  }
}
