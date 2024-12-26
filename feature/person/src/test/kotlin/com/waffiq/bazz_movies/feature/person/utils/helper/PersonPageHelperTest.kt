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
  fun `test hasAnySocialMediaIds returns true when any ID is not null or empty`() {
    val externalId = ExternalIDPerson(instagramId = "instagram_id", twitterId = null)
    val result = hasAnySocialMediaIds(externalId)
    assertTrue(result)
  }

  @Test
  fun `test hasAnySocialMediaIds returns false when all IDs are null or empty`() {
    val externalId = ExternalIDPerson(instagramId = null, twitterId = null)
    val result = hasAnySocialMediaIds(externalId)
    assertFalse(result)
  }

  @Test
  fun `test formatBirthInfo formats birthdate and place of birth correctly`() {
    val birthDate = "1990-05-15"
    val placeOfBirth = "New York"
    val result = formatBirthInfo(birthDate, placeOfBirth)
    assertEquals("May 15, 1990\nNew York", result)
  }

  @Test
  fun `test formatBirthInfo returns place of birth when birthdate is null`() {
    val birthDate: String? = null
    val placeOfBirth = "New York"
    val result = formatBirthInfo(birthDate, placeOfBirth)
    assertEquals("New York", result)
  }

  @Test
  fun `test formatDeathInfo formats death date and age at death correctly`() {
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
  fun `test formatDeathInfo returns no data when death date is null`() {
    val context: Context = mockk()

    // Mock the behavior of getString to return "no data" for any input
    // getString(no_data)
    every { context.getString(any()) } returns "no data"

    val result = context.formatDeathInfo(null, null)
    assertEquals("no data", result)
    verify { context.getString(any()) }
  }
}
