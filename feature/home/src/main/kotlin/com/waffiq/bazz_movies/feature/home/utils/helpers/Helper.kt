package com.waffiq.bazz_movies.feature.home.utils.helpers

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

object Helper {

  fun getDateTwoWeeksFromToday(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      // For API level 26 and above
      LocalDate.now().plusWeeks(2)
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    } else {
      // For API levels below 26
      val calendar = Calendar.getInstance()
      calendar.add(Calendar.WEEK_OF_YEAR, 2) // Add 2 weeks

      val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
      formatter.format(calendar.time)
    }
  }
}
