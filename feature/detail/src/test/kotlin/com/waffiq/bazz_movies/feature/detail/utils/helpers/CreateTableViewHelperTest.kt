package com.waffiq.bazz_movies.feature.detail.utils.helpers

import android.content.Context
import android.widget.FrameLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.feature.detail.utils.helpers.CreateTableViewHelper.createTable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CreateTableViewHelperTest {

  private lateinit var context: Context
  private lateinit var table: FrameLayout

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext()
    table = FrameLayout(context)
  }

  @Test
  fun createTable_withValidJobAndCrewNames_addsCorrectTableLayout() {
    val jobs = listOf("Director", "Producer")
    val crewNames = listOf("Jane Doe", "John Smith")
    val data = jobs to crewNames

    createTable(context, data, table)

    val tableLayout = table.getChildAt(0) as TableLayout
    assertEquals(2, tableLayout.childCount)

    val firstRow = tableLayout.getChildAt(0) as TableRow
    val firstJobCell = firstRow.getChildAt(0) as TextView
    val firstCrewCell = firstRow.getChildAt(1) as TextView
    assertEquals("Director", firstJobCell.text)
    assertEquals(": Jane Doe", firstCrewCell.text)

    val secondRow = tableLayout.getChildAt(1) as TableRow
    val secondJobCell = secondRow.getChildAt(0) as TextView
    val secondCrewCell = secondRow.getChildAt(1) as TextView
    assertEquals("Producer", secondJobCell.text)
    assertEquals(": John Smith", secondCrewCell.text)
  }
}
