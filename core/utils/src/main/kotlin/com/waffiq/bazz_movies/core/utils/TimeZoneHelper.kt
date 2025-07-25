package com.waffiq.bazz_movies.core.utils

import java.util.TimeZone

/**
 * Used as wrapper time zone
 *
 * @return A string region code. Example : "ID", "MY", "RU", etc
 */
object TimeZoneHelper {
  fun getDefaultTimeZoneId(): String {
    return TimeZone.getDefault().id
  }
}
