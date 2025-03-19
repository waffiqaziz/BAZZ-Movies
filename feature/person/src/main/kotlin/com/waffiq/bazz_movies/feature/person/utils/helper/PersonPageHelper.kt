package com.waffiq.bazz_movies.feature.person.utils.helper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import androidx.annotation.VisibleForTesting
import androidx.core.view.isVisible
import com.waffiq.bazz_movies.core.designsystem.R.string.no_data
import com.waffiq.bazz_movies.core.designsystem.R.string.years_old
import com.waffiq.bazz_movies.core.utils.DateFormatter.dateFormatterStandard
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import java.time.LocalDate
import java.time.Period
import java.util.Calendar

/**
 * Used for person fragment
 */
object PersonPageHelper {

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun getAgeDeath(dateBirth: String, dateDeath: String): Int {
    var dateParts = dateBirth.split("-").toTypedArray()
    val yearBirth = dateParts[0].toInt()
    val monthBirth = dateParts[1].toInt()
    val dayBirth = dateParts[2].toInt()

    dateParts = dateDeath.split("-").toTypedArray()
    val yearDeath = dateParts[0].toInt()
    val monthDeath = dateParts[1].toInt()
    val dayDeath = dateParts[2].toInt()

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      // Use LocalDate and Period for API 26 and above
      val birthDate = LocalDate.of(yearBirth, monthBirth, dayBirth)
      val deathDate = LocalDate.of(yearDeath, monthDeath, dayDeath)
      Period.between(birthDate, deathDate).years
    } else {
      // Use Calendar for lower API levels
      val birthDate = Calendar.getInstance().apply {
        set(yearBirth, monthBirth - 1, dayBirth) // Months are zero-based in Calendar
      }
      val deathDate = Calendar.getInstance().apply {
        set(yearDeath, monthDeath - 1, dayDeath)
      }

      var age = deathDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
      if (deathDate.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
        age--
      }
      age
    }
  }

  fun hasAnySocialMediaIds(externalID: ExternalIDPerson): Boolean {
    return !externalID.instagramId.isNullOrEmpty() ||
      !externalID.twitterId.isNullOrEmpty() ||
      !externalID.facebookId.isNullOrEmpty() ||
      !externalID.tiktokId.isNullOrEmpty() ||
      !externalID.youtubeId.isNullOrEmpty()
  }

  fun formatBirthInfo(birthday: String?, placeOfBirth: String?): String {
    var birthText = birthday?.let { dateFormatterStandard(it) }
    return if (birthText == null) {
      placeOfBirth.orEmpty()
    } else {
      birthText += "\n${placeOfBirth.orEmpty()}"
      birthText
    }
  }

  fun Context.formatDeathInfo(birthday: String?, deathday: String?): String {
    if (deathday == null || birthday == null) {
      return getString(no_data)
    } else {
      val ageAtDeath = getAgeDeath(birthday, deathday).toString()
      return "${dateFormatterStandard(deathday)} ($ageAtDeath ${getString(years_old)})"
    }
  }

  fun Context.setupSocialLink(
    socialId: String?,
    imageView: ImageView,
    baseUrl: String
  ) {
    if (!socialId.isNullOrEmpty()) {
      imageView.isVisible = true
      imageView.setOnClickListener {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(baseUrl + socialId)))
      }
    } else {
      imageView.isVisible = false
    }
  }
}
