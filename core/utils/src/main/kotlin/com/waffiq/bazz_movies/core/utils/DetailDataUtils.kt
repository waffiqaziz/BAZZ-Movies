package com.waffiq.bazz_movies.core.utils

import android.content.Context
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.utils.DateFormatter.dateFormatterStandard
import java.text.NumberFormat
import java.util.Locale

object DetailDataUtils {
  /**
   * Returns the title from `MediaItem`. The function checks the following properties in order:
   * - `name`
   * - `title`
   * - `originalTitle`
   * - `originalName`
   *
   * If none of those properties are available, it defaults to "N/A".
   *
   * @param item The `MediaItem` whose title is to be determined.
   * @param Context The `Context` to get string resources
   * @return The title of the item, or "Item" if no title is found.
   */
  fun Context.titleHandler(item: MediaItem): String =
    listOf(
      item.name,
      item.title,
      item.originalTitle,
      item.originalName,
    ).firstNotNullOfOrNull { it } ?: getString(not_available)

  /**
   * Returns the title from `MediaItem`. The function checks the following properties in order:
   * - `name`
   * - `title`
   * - `originalTitle`
   * - `originalName`
   *
   * If none of those properties are available, it defaults to "Item".
   *
   * @param item The `MediaItem` whose title is to be determined.
   * @return The title of the item, or "Item" if no title is found.
   */
  fun titleHandler(item: MediaItem): String =
    item.name ?: item.title ?: item.originalTitle ?: item.originalName ?: "Item"

  /**
   * Returns valid years. The function checks the following properties in order:
   * - `firstAirDate`
   * - `releaseDate`
   *
   * If none of those properties are available, it defaults to "N/A".
   *
   * @param data The `MediaItem` whose `firstAirDate` and `releaseDate` is to be determined.
   */
  fun Context.releaseDateHandler(data: MediaItem): String =
    (data.firstAirDate ?: data.releaseDate)
      ?.let { dateFormatterStandard(it) }
      .takeUnless { it.isNullOrBlank() } ?: getString(not_available)

  /**
   * Formats movie revenue/budget as USD currency.
   * Returns "-" for null, zero, or negative values (missing/unavailable data).
   *
   * @param amount The movie revenue or budget amount
   * @return Formatted USD string (e.g., "$1,234,567") or "-" for unavailable data
   */
  fun toUsd(amount: Number?): String {
    val longAmount = amount?.toLong() ?: 0L

    return if (longAmount <= 0) {
      "-"
    } else {
      val formatter = NumberFormat.getCurrencyInstance(Locale.US)
      formatter.format(longAmount)
    }
  }
}
