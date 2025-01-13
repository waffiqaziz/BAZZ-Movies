package com.waffiq.bazz_movies.feature.person.utils.helper

import android.content.Context
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.formatBirthInfo
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.formatDeathInfo
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.getAgeDeath
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.hasAnySocialMediaIds
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class PersonPageHelperTest {

  @Test
  fun hasAnySocialMediaIds_returnsTrue_ifAnyIdNotNullOrEmpty() {
    val externalId = ExternalIDPerson(instagramId = "instagram_id", twitterId = null)
    val result = hasAnySocialMediaIds(externalId)
    assertTrue(result)
  }

  @Test
  fun hasAnySocialMediaIds_returnsFalse_ifAllIdsNullOrEmpty() {
    val externalId = ExternalIDPerson(instagramId = null, twitterId = null)
    val result = hasAnySocialMediaIds(externalId)
    assertFalse(result)
  }

  @Test
  fun formatBirthInfo_formatsBirthdateAndPlaceCorrectly() {
    val birthDate = "1990-05-15"
    val placeOfBirth = "New York"
    val result = formatBirthInfo(birthDate, placeOfBirth)
    assertEquals("May 15, 1990\nNew York", result)
  }

  @Test
  fun formatBirthInfo_returnsPlaceIfBirthdateNull() {
    val birthDate: String? = null
    val placeOfBirth = "New York"
    val result = formatBirthInfo(birthDate, placeOfBirth)
    assertEquals("New York", result)
  }

  @Test
  fun formatDeathInfo_formatsDeathDateAndAgeCorrectly() {
    val context: Context = mockk()
    val birthDate = "1990-05-15"
    val deathDate = "2020-10-10"

    val formattedDeathDate = "Oct 10, 2020"
    val ageAtDeath = 30

    mockkObject(PersonPageHelper) // This allows mocking the object methods
    every { getAgeDeath(birthDate, deathDate) } returns ageAtDeath
    every { context.getString(any()) } returns "years old"

    val result = context.formatDeathInfo(birthDate, deathDate)
    assertEquals("$formattedDeathDate ($ageAtDeath years old)", result)

    verify { getAgeDeath(birthDate, deathDate) }
    verify { context.getString(any()) }
  }

  @Test
  fun formatDeathInfo_returnsNoData_ifDeathDateNull() {
    val context: Context = mockk()

    // Mock the behavior of getString to return "no data" for any input
    // getString(no_data)
    every { context.getString(any()) } returns "no data"

    val result = context.formatDeathInfo(null, null)
    assertEquals("no data", result)
    verify { context.getString(any()) }
  }
}
