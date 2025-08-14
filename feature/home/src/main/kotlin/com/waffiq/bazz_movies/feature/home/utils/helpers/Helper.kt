package com.waffiq.bazz_movies.feature.home.utils.helpers

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

object Helper {

  fun getDateTwoWeeksFromToday(pattern: String = "yyyy-MM-dd"): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      // For API level 26 and above
      LocalDate.now().plusWeeks(2).format(DateTimeFormatter.ofPattern(pattern))
    } else {
      // For API levels below 26
      val calendar = Calendar.getInstance().apply {
        add(Calendar.WEEK_OF_YEAR, 2) // Add 2 weeks
      }

      val formatter = SimpleDateFormat(pattern, Locale.getDefault())
      formatter.format(calendar.time)
    }
  }

  fun getDateToday(pattern: String = "yyyy-MM-dd"): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      LocalDate.now().format(DateTimeFormatter.ofPattern(pattern))
    } else {
      val formatter = SimpleDateFormat(pattern, Locale.getDefault())
      formatter.format(Calendar.getInstance().time)
    }
  }

  fun getCountryDisplayName(region: String): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
      Locale.of("", region).displayCountry
    } else {
      @Suppress("DEPRECATION")
      Locale("", region).displayCountry
    }
  }
}
