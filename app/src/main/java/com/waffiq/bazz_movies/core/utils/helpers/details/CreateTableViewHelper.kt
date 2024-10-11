package com.waffiq.bazz_movies.core.utils.helpers.details

import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.waffiq.bazz_movies.R.color.gray_100
import com.waffiq.bazz_movies.R.font.nunito_sans_regular

/**
 * Used to create a table view to display movie or TV crew details.
 */
object CreateTableViewHelper {
  fun createTable(
    context: Context,
    pair: Pair<MutableList<String>, MutableList<String>>,
    table: FrameLayout
  ) {
    val (job, crewName) = pair

    // Create a TableLayout
    val tableLayout = TableLayout(context)
    tableLayout.layoutParams = TableLayout.LayoutParams(
      TableLayout.LayoutParams.MATCH_PARENT,
      TableLayout.LayoutParams.WRAP_CONTENT
    )

    // Create rows
    for (i in 0..<job.size) {
      val tableRow = TableRow(context)
      tableRow.layoutParams = TableRow.LayoutParams(
        TableRow.LayoutParams.MATCH_PARENT,
        TableRow.LayoutParams.WRAP_CONTENT
      )

      val cell1 = createTableCell(context, job[i])
      val cell2 = createTableCell(context, ": " + crewName[i])

      tableRow.addView(cell1)
      tableRow.addView(cell2)

      tableLayout.addView(tableRow)
    }

    table.addView(tableLayout)
  }

  private fun createTableCell(context: Context, text: String): TextView {
    val textView = TextView(context)
    textView.text = text
    textView.layoutParams = TableRow.LayoutParams(
      TableRow.LayoutParams.WRAP_CONTENT,
      TableRow.LayoutParams.WRAP_CONTENT
    )
    textView.typeface = ResourcesCompat.getFont(context, nunito_sans_regular)
    textView.gravity = Gravity.START
    textView.textSize = 14F
    textView.setPadding(0, 7, 24, 7)
    textView.setTextColor(ActivityCompat.getColor(context, gray_100))
    return textView
  }
}
