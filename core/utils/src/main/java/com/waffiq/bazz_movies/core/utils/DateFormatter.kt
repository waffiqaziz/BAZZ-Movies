package com.waffiq.bazz_movies.core.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Utility object that provides functions for formatting date strings into a human-readable format.
 * It includes methods to handle standard date formats and ISO 8601 date strings.
 *
 * - [dateFormatterStandard] method takes a date string in the format "yyyy-MM-dd" and converts it to
 *   a more readable format, "MMM dd, yyyy".
 * - [dateFormatterISO8601] method takes an ISO 8601 formatted date string (e.g., "2025-01-06T15:30:00.000+0000")
 *   and converts it to "MMM dd, yyyy".
 */
object DateFormatter {
  const val TAG = "DateFormatter"

  /**
   * Converts a date string in the format "yyyy-MM-dd" to the format "MMM dd, yyyy".
   * This method ensures that the date is strictly parsed and handled accurately.
   *
   * @param date The date string in "yyyy-MM-dd" format to be parsed.
   * @return The formatted date string in "MMM dd, yyyy" format, or an empty string if the input is invalid.
   */
  fun dateFormatterStandard(date: String?): String {
    if (date.isNullOrEmpty()) return ""
    return try {
      val oldFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
        // non-lenient, throw a ParseException if the input doesn't strictly match the expected format.
        isLenient = false
      }
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

  /**
   * Converts an ISO 8601 formatted date string (e.g., "2025-01-06T15:30:00.000+0000")
   * to the format "MMM dd, yyyy".
   * This method adjusts for the "Z" (UTC) and ensures the date is parsed and formatted correctly.
   *
   * @param date The ISO 8601 date string to be parsed.
   * @return The formatted date string in "MMM dd, yyyy" format, or an empty string if the input is invalid.
   */
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
