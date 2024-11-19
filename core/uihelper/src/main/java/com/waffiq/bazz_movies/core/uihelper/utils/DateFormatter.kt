package com.waffiq.bazz_movies.core.uihelper.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatter {
  const val TAG = "DateFormatter"

  fun dateFormatterStandard(date: String?): String {
    if (date.isNullOrEmpty()) return ""
    return try {
      val oldFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
      val newFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
      val parsedDate = oldFormatter.parse(date)
      parsedDate?.let { newFormatter.format(it) } ?: ""
    } catch (e: ParseException) {
      Log.e(TAG, "Date parsing failed: ${e.message}")
      ""
    } catch (e: IllegalArgumentException) {
      Log.e(TAG, "Invalid date pattern: ${e.message}")
      ""
    }
  }

  fun dateFormatterISO8601(date: String?): String {
    if (date.isNullOrEmpty()) return ""
    return try {
      val oldFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
      val newFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
      val dateWithAdjustedFormat = date.replace("Z", "+0000")
      val parsedDate = oldFormatter.parse(dateWithAdjustedFormat)
      parsedDate?.let { newFormatter.format(it) } ?: ""
    } catch (e: ParseException) {
      Log.e(TAG, "Date parsing failed: ${e.message}")
      ""
    } catch (e: IllegalArgumentException) {
      Log.e(TAG, "Invalid date pattern: ${e.message}")
      ""
    }
  }

}