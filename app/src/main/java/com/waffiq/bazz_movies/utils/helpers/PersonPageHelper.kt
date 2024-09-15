package com.waffiq.bazz_movies.utils.helpers

import com.waffiq.bazz_movies.domain.model.person.ExternalIDPerson
import java.time.LocalDate
import java.time.Period

object PersonPageHelper {
  fun getAgeBirth(date: String): Int {
    val dateParts = date.split("-").toTypedArray()
    val year = dateParts[0].toInt()
    val month = dateParts[1].toInt()
    val day = dateParts[2].toInt()

    return Period.between(LocalDate.of(year, month, day), LocalDate.now()).years
  }

  fun getAgeDeath(dateBirth: String, dateDeath: String): Int {
    var dateParts = dateBirth.split("-").toTypedArray()
    val yearBirth = dateParts[0].toInt()
    val monthBirth = dateParts[1].toInt()
    val dayBirth = dateParts[2].toInt()

    dateParts = dateDeath.split("-").toTypedArray()
    val yearDeath = dateParts[0].toInt()
    val monthDeath = dateParts[1].toInt()
    val dayDeath = dateParts[2].toInt()

    return Period.between(
      LocalDate.of(yearBirth, monthBirth, dayBirth),
      LocalDate.of(yearDeath, monthDeath, dayDeath)
    ).years
  }

  fun hasAnySocialMediaIds(externalID: ExternalIDPerson): Boolean {
    return !externalID.instagramId.isNullOrEmpty() ||
      !externalID.twitterId.isNullOrEmpty() ||
      !externalID.facebookId.isNullOrEmpty() ||
      !externalID.tiktokId.isNullOrEmpty() ||
      !externalID.youtubeId.isNullOrEmpty()
  }
}
