package com.waffiq.bazz_movies.core.uihelper.dialog

import android.content.Context
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.id.rv_options
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.designsystem.R.style.ThemeOverlay_App_AlertDialog_Sort
import com.waffiq.bazz_movies.core.uihelper.testutils.SortOption
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.SingleChoiceAdapter
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowDialog

@RunWith(RobolectricTestRunner::class)
class SingleChoiceDialogTest {

  private lateinit var context: Context
  private val items = SortOption.entries
  private val onSelected: (SortOption) -> Unit = mockk(relaxed = true)

  @Before
  fun setUp() {
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies)
    }
  }

  private fun showDialog(selected: SortOption = SortOption.TITLE_AZ) {
    SingleChoiceDialog.show(
      context = context,
      items = items,
      selected = selected,
      onSelected = onSelected,
    )
    shadowOf(Looper.getMainLooper()).idle()
  }

  @Test
  fun show_displaysDialog() {
    showDialog()
    val dialog = ShadowDialog.getLatestDialog()
    assertNotNull(dialog)
    assertTrue(dialog.isShowing)
  }

  @Test
  fun show_recyclerView_hasCorrectItemCount() {
    showDialog()
    val dialog = ShadowDialog.getLatestDialog()
    val rv = dialog.findViewById<RecyclerView>(rv_options)
    assertNotNull(rv)
    assertEquals(items.size, rv.adapter?.itemCount)
  }

  @Test
  fun show_recyclerView_usesLinearLayoutManager() {
    showDialog()
    val dialog = ShadowDialog.getLatestDialog()
    val rv = dialog.findViewById<RecyclerView>(rv_options)
    assertTrue(rv.layoutManager is LinearLayoutManager)
  }

  @Test
  fun show_recyclerView_adapterIsSingleChoiceAdapter() {
    showDialog()
    val dialog = ShadowDialog.getLatestDialog()
    val rv = dialog.findViewById<RecyclerView>(rv_options)
    assertTrue(rv.adapter is SingleChoiceAdapter<*>)
  }

  @Test
  fun selectItem_invokesOnSelected_andDismissesDialog() {
    showDialog(selected = SortOption.TITLE_AZ)

    val dialog = ShadowDialog.getLatestDialog()
    val rv = dialog.findViewById<RecyclerView>(rv_options)

    // create ViewHolder for position 1
    @Suppress("UNCHECKED_CAST")
    val holder = rv.findViewHolderForAdapterPosition(1)
      as? SingleChoiceAdapter<SortOption>.ViewHolder
    assertNotNull(holder)

    holder?.binding?.item?.performClick()
    holder?.binding?.item?.performClick()
    shadowOf(Looper.getMainLooper()).runToEndOfTasks()

    assertFalse(dialog.isShowing)
  }

  @Test
  fun show_withCustomStyle_appliesStyle() {
    SingleChoiceDialog.show(
      context = context,
      style = ThemeOverlay_App_AlertDialog_Sort,
      items = items,
      selected = SortOption.TITLE_AZ,
      onSelected = onSelected,
    )
    shadowOf(Looper.getMainLooper()).idle()

    val dialog = ShadowDialog.getLatestDialog()
    assertNotNull(dialog)
    assertTrue(dialog.isShowing)
  }
}
