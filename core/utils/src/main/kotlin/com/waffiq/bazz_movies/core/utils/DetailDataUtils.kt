package com.waffiq.bazz_movies.core.utils

import android.content.Context
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_BACKDROP_W300
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.domain.Dateable
import com.waffiq.bazz_movies.core.domain.Imageble
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.Titleable
import com.waffiq.bazz_movies.core.utils.DateFormatter.dateFormatterStandard
import java.text.NumberFormat
import java.util.Locale

object DetailDataUtils {
  /**
   * Returns the title from [Titleable] based the following properties in order:
   * - `name`
   * - `title`
   * - `originalTitle`
   * - `originalName`
   *
   * @return The title of the item, or "N/A" if no title is found.
   */
  fun Context.titleHandler(item: Titleable): String =
    listOf(
      item.name,
      item.title,
      item.originalTitle,
      item.originalName,
    ).firstNotNullOfOrNull { it } ?: getString(not_available)

  /**
   * Returns the title from [Titleable] based the following properties in order:
   * - `name`
   * - `title`
   * - `originalTitle`
   * - `originalName`
   *
   * @return The title of the item, or "Item" if no title is found.
   */
  fun titleHandler(item: Titleable): String =
    item.name ?: item.title ?: item.originalTitle ?: item.originalName ?: "Item"

  /**
   * Returns name from [Titleable] person item based the following properties in order:
   * - `name`
   * - `originalName`
   *
   * @return The name of the person, or "Item" if no title is found.
   */
  fun nameHandler(item: Titleable): String = item.name ?: item.originalName ?: "Item"

  /**
   * Returns valid years. The function checks the following properties in order:
   * - `firstAirDate`
   * - `releaseDate`
   *
   * @return release data, otherwise fallback to not "N/A"
   */
  fun Context.releaseDateHandler(data: MediaItem): String =
    (data.firstAirDate ?: data.releaseDate)
      ?.let { dateFormatterStandard(it) }
      .takeUnless { it.isNullOrBlank() } ?: getString(not_available)

  /**
   * Formats movie revenue/budget as USD currency.
   * Returns "-" for null, zero, or negative values (missing/unavailable data).
   *
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

  /**
   * Returns the first available image path.
   *
   * Priority:
   * - backdropPath
   * - posterPath
   *
   * @return image path or null if both are unavailable.
   */
  private val Imageble.imagePath: String?
    get() = when {
      !backdropPath.isNullOrBlank() -> backdropPath
      !posterPath.isNullOrBlank() -> posterPath
      else -> null
    }

  /**
   * Builds full image URL from the available image path.
   *
   * @return full image URL or null if no image path exists.
   */
  private fun Imageble.fullImageUrl(): String? = imagePath?.let { TMDB_IMG_LINK_BACKDROP_W300 + it }

  /**
   * Provides image source for UI.
   *
   * @return full image URL if available, otherwise fallback drawable.
   */
  val Imageble.imageSource: Any
    get() = fullImageUrl() ?: ic_backdrop_error

  /**
   * Returns the first available date.
   *
   * Priority:
   * - releaseDate
   * - firstAirDate
   *
   * @return date string or null if both are unavailable.
   */
  private val Dateable.displayDate: String?
    get() = listOf(
      releaseDate,
      firstAirDate,
    ).firstOrNull { !it.isNullOrBlank() }

  /**
   * Provides formatted date for UI.
   *
   * @return date if available, otherwise a fallback text.
   */
  fun Context.dateOf(item: Dateable): String = item.displayDate ?: getString(not_available)
}
