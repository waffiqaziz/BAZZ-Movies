package com.waffiq.bazz_movies.feature.person.utils.helper

import android.content.Context
import com.waffiq.bazz_movies.core.designsystem.R.string.no_data
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
  fun hasAnySocialMediaIds_whenCheckingVariousIds_returnsExpectedResult() {
    // should return true if theres id
    assertTrue(hasAnySocialMediaIds(ExternalIDPerson(instagramId = "instagramId")))
    assertTrue(hasAnySocialMediaIds(ExternalIDPerson(twitterId = "twitterId")))
    assertTrue(hasAnySocialMediaIds(ExternalIDPerson(facebookId = "facebookId")))
    assertTrue(hasAnySocialMediaIds(ExternalIDPerson(tiktokId = "tiktokId")))
    assertTrue(hasAnySocialMediaIds(ExternalIDPerson(youtubeId = "youtubeId")))

    // should return false if theres no id at all
    assertFalse(hasAnySocialMediaIds(ExternalIDPerson(instagramId = "")))
    assertFalse(hasAnySocialMediaIds(ExternalIDPerson(twitterId = "")))
    assertFalse(hasAnySocialMediaIds(ExternalIDPerson(facebookId = "")))
    assertFalse(hasAnySocialMediaIds(ExternalIDPerson(tiktokId = "")))
    assertFalse(hasAnySocialMediaIds(ExternalIDPerson(youtubeId = "")))
    assertFalse(hasAnySocialMediaIds(ExternalIDPerson()))

    // all id is available
    assertTrue(
      hasAnySocialMediaIds(
        ExternalIDPerson(
          youtubeId = "youtubeId",
          instagramId = "instagramId",
          facebookId = "facebookId",
          tiktokId = "tiktokId",
          twitterId = "twitterId"
        )
      )
    )
  }

  @Test
  fun formatBirthInfo_whenVariousInputValues_formatsCorrectly() {
    // valid birth day and place of birth
    assertEquals(
      "May 15, 1990\nNew York",
      formatBirthInfo(birthday = "1990-05-15", placeOfBirth = "New York")
    )

    // all null
    assertEquals("", formatBirthInfo(birthday = null, placeOfBirth = null))

    // all empty
    assertEquals("\n", formatBirthInfo(birthday = "", placeOfBirth = ""))

    // date null
    assertEquals("New York", formatBirthInfo(birthday = null, placeOfBirth = "New York"))

    // place null
    assertEquals("May 15, 1990\n", formatBirthInfo(birthday = "1990-05-15", placeOfBirth = null))

    // date empty
    assertEquals("\nNew York", formatBirthInfo(birthday = "", placeOfBirth = "New York"))

    // place empty
    assertEquals("May 15, 1990\n", formatBirthInfo(birthday = "1990-05-15", placeOfBirth = ""))

    // date null, place  empty
    assertEquals("", formatBirthInfo(birthday = null, placeOfBirth = ""))

    // date empty, place  null
    assertEquals("\n", formatBirthInfo(birthday = "", placeOfBirth = null))
  }

  @Test
  fun formatDeathInfo_whenValidDatesProvided_returnsFormattedDateAndAge() {
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
  fun formatDeathInfo_whenAllInputsAreNull_returnsNoData() {
    val context: Context = mockk()

    // mock the behavior of getString to return "no data" for any input
    // getString(no_data)
    every { context.getString(no_data) } returns "no data"

    val result = context.formatDeathInfo(birthday = null, deathday = null)
    assertEquals("no data", result)
    verify { context.getString(any()) }
  }

  @Test
  fun formatDeathInfo_whenBirthdayIsNull_returnsNoData() {
    val context: Context = mockk()

    every { context.getString(no_data) } returns "no data"

    val result = context.formatDeathInfo(birthday = null, deathday = "2020-10-10")
    assertEquals("no data", result)
  }

  @Test
  fun formatDeathInfo_whenDeathdayIsNull_returnsNoData() {
    val context: Context = mockk()
    every { context.getString(no_data) } returns "no_data"

    val result = context.formatDeathInfo(birthday = "1990-05-15", deathday = null)
    assertEquals("no_data", result)
  }

  @Test
  fun formatDeathInfo_whenOnlyDeathdayProvided_returnsNoData() {
    val context: Context = mockk()
    val deathDate = "2020-10-10"

    every { context.getString(no_data) } returns "no data"

    val result = context.formatDeathInfo(birthday = null, deathday = deathDate)
    assertEquals("no data", result)
  }
}
