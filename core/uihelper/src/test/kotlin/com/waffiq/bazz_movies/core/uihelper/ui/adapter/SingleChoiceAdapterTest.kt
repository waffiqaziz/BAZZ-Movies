package com.waffiq.bazz_movies.core.uihelper.ui.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.waffiq.bazz_movies.core.designsystem.R.color.gray_400
import com.waffiq.bazz_movies.core.designsystem.R.color.gray_800
import com.waffiq.bazz_movies.core.designsystem.R.color.white
import com.waffiq.bazz_movies.core.designsystem.R.color.yellow_alpha_9
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemSingleChoiceBinding
import com.waffiq.bazz_movies.core.uihelper.testutils.BaseAdapterTest
import com.waffiq.bazz_movies.core.uihelper.testutils.SortOption
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.robolectric.annotation.LooperMode

@LooperMode(LooperMode.Mode.PAUSED)
class SingleChoiceAdapterTest : BaseAdapterTest() {

  private lateinit var adapter: SingleChoiceAdapter<SortOption>
  private lateinit var binding: ItemSingleChoiceBinding

  private val items = SortOption.entries
  private val onSelected = mockk<(SortOption) -> Unit>(relaxed = true)

  @Before
  override fun setup() {
    super.setup()

    parent = FrameLayout(context)
    inflater = LayoutInflater.from(context)
    adapter = SingleChoiceAdapter(
      items,
      SortOption.TITLE_AZ,
      onSelected,
      postDelayed = { _, action -> action() },
    )
    binding = ItemSingleChoiceBinding.inflate(inflater, parent, false)
  }

  private fun bindAt(position: Int): SingleChoiceAdapter<SortOption>.ViewHolder {
    val holder = adapter.ViewHolder(binding)
    adapter.onBindViewHolder(holder, position)
    return holder
  }

  @Test
  fun getItemCount_returnsCorrectSize() {
    assertEquals(items.size, adapter.itemCount)
  }

  @Test
  fun onCreateViewHolder_returnsViewHolder_withInflatedView() {
    val viewHolder = adapter.onCreateViewHolder(parent, 0)

    assertNotNull(viewHolder)
    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
  }

  @Test
  fun onBindViewHolder_selectedItem_showsSelectedVisualState() {
    bindAt(0)

    assertTrue(binding.ivSelected.isVisible)
    assertEquals(
      ContextCompat.getColor(context, yellow_alpha_9),
      binding.item.cardBackgroundColor.defaultColor,
    )
    assertEquals(Typeface.BOLD, binding.tvLabel.typeface.style)
    assertEquals(ContextCompat.getColor(context, white), binding.tvLabel.currentTextColor)
  }

  @Test
  fun onBindViewHolder_unselectedItem_showsUnselectedVisualState() {
    bindAt(1)

    assertFalse(binding.ivSelected.isVisible)
    assertEquals(
      ContextCompat.getColor(context, gray_800),
      binding.item.cardBackgroundColor.defaultColor,
    )
    assertEquals(Typeface.NORMAL, binding.tvLabel.typeface.style)
    assertEquals(ContextCompat.getColor(context, gray_400), binding.tvLabel.currentTextColor)
  }

  @Test
  fun onBindViewHolder_setsLabelText_fromStringResource() {
    bindAt(0)
    assertEquals(
      context.getString(SortOption.TITLE_AZ.label),
      binding.tvLabel.text.toString(),
    )
  }

  @Test
  fun click_whenSuccessful_invokesOnSelected() {
    val holder = bindAt(1)

    holder.binding.item.performClick()

    verify(exactly = 1) { onSelected(SortOption.TITLE_ZA) }
  }

  @Test
  fun initialSelection_isHighlighted_onFirstBind() {
    adapter = SingleChoiceAdapter(items, SortOption.TITLE_AZ, onSelected)

    bindAt(0)
    assertEquals(context.getString(SortOption.TITLE_AZ.label), binding.tvLabel.text)
    assertTrue(binding.ivSelected.isVisible)
  }
}
