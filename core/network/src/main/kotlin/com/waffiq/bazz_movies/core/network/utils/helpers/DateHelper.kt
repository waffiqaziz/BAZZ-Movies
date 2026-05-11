package com.waffiq.bazz_movies.core.network.utils.helpers

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateHelper {

  private val formatter = DateTimeFormatter.ISO_DATE

  val Long.monthsAgo: String
    get() = LocalDate.now()
      .minusMonths(this)
      .format(formatter)

  val Long.monthsLater: String
    get() = LocalDate.now()
      .plusMonths(this)
      .format(formatter)
}
