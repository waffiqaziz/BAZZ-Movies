package com.waffiq.bazz_movies.feature.home.utils.helpers

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Helper {

  fun getDateTwoWeeksFromToday(): String {
    return LocalDate.now().plusWeeks(2) // get date two weeks from now
      .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) // formatter
  }
}