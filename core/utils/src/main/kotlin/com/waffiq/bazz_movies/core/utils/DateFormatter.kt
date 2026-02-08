package com.waffiq.bazz_movies.core.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility object that provides functions for formatting date strings into a human-readable format.
 * It includes methods to handle standard date formats and ISO 8601 date strings.
 */
object DateFormatter {
  const val TAG = "DateFormatter"

  /**
   * Converts a date string in the format "yyyy-MM-dd" to the format "MMM dd, yyyy".
   * This method ensures that the date is strictly parsed and handled accurately.
   *
   * @param date The date string in "yyyy-MM-dd" format to be parsed.
   * @return The formatted date string in "MMM dd, yyyy" format, or an empty string if the input
   *         is invalid.
   */
  fun dateFormatterStandard(date: String?): String = formatDate(date, "yyyy-MM-dd")

  /**
   * Converts an ISO 8601 formatted date string (e.g., "2025-01-06T15:30:00.000+0000")
   * to the format "MMM dd, yyyy".
   * This method adjusts for the "Z" (UTC) and ensures the date is parsed and formatted correctly.
   *
   * @param date The ISO 8601 date string to be parsed.
   * @return The formatted date string in "MMM dd, yyyy" format, or an empty string if the input
   *         is invalid.
   */
  fun dateFormatterISO8601(date: String?): String {
    val adjustedDate = date?.replace("Z", "+0000")
    return formatDate(adjustedDate, "yyyy-MM-dd'T'HH:mm:ss.SSSX")
  }

  internal fun parseDate(input: String?, pattern: String): Date? {
    if (input.isNullOrEmpty()) return null
    return try {
      val formatter = try {
        SimpleDateFormat(pattern, Locale.getDefault()).apply { isLenient = false }
      } catch (_: IllegalArgumentException) {
        Log.e(TAG, "Invalid date format pattern: $pattern")
        null
      }
      formatter?.parse(input)
    } catch (e: ParseException) {
      Log.e(TAG, "Date parsing failed: ${e.message}")
      null
    }
  }

  internal fun formatDate(input: String?, pattern: String): String {
    val parsedDate = parseDate(input, pattern) ?: return ""
    return SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(parsedDate)
  }
}
