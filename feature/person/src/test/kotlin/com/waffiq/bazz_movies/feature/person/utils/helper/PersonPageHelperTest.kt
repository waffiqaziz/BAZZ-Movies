package com.waffiq.bazz_movies.feature.person.utils.helper

import android.content.Context
import com.waffiq.bazz_movies.core.designsystem.R.string.no_data
import com.waffiq.bazz_movies.core.designsystem.R.string.years_old
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.formatBirthInfo
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.formatDeathInfo
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.getAge
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.getAgeDeath
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.hasAnySocialMediaIds
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class PersonPageHelperTest {

  val fixedNow: LocalDate = LocalDate.of(2025, 5, 15) // use date 15 May 2025
  val context: Context = mockk()
  val born90 = "1990-05-15"

  @Before
  fun setup() {
    every { context.getString(no_data) } returns "no data"
  }

  @Test
  fun hasAnySocialMediaIds_whenCheckingVariousIds_returnsExpectedResult() {
    // should return true if there's id
    assertTrue(hasAnySocialMediaIds(ExternalIDPerson(instagramId = "instagramId")))
    assertTrue(hasAnySocialMediaIds(ExternalIDPerson(twitterId = "twitterId")))
    assertTrue(hasAnySocialMediaIds(ExternalIDPerson(facebookId = "facebookId")))
    assertTrue(hasAnySocialMediaIds(ExternalIDPerson(tiktokId = "tiktokId")))
    assertTrue(hasAnySocialMediaIds(ExternalIDPerson(youtubeId = "youtubeId")))

    // should return false if there's no id at all
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
          twitterId = "twitterId",
        ),
      ),
    )
  }

  @Test
  fun formatBirthInfo_whenVariousInputValues_formatsCorrectly() {
    every { context.getString(years_old) } returns "years old"
    // valid birthday, place of birth, and death day
    assertEquals(
      "May 15, 1990\nNew York",
      context.formatBirthInfo(born90, "New York", "2000-12-12"),
    )

    // all null
    assertEquals("", context.formatBirthInfo(null, null, null))

    // all empty
    assertEquals("", context.formatBirthInfo("", "", ""))

    // date null
    assertEquals("New York", context.formatBirthInfo(null, "New York", null))

    // place null
    assertEquals(
      "May 15, 1990 (35 years old)\n",
      context.formatBirthInfo(born90, null, null, fixedNow),
    )

    // date empty
    assertEquals("New York", context.formatBirthInfo("", "New York", null))

    // place empty
    assertEquals(
      "May 15, 1990 (35 years old)\n",
      context.formatBirthInfo(born90, "", "", fixedNow),
    )

    // date null, place  empty
    assertEquals("", context.formatBirthInfo(null, "", null))

    // date empty, place  null
    assertEquals("", context.formatBirthInfo("", null, null))
  }

  @Test
  fun getAge_validParameters_returnsAgeCorrectly() {
    assertEquals(35L, getAge(born90, fixedNow))

    // use default locale to fulfill JaCoCo coverage
    getAge(born90)
  }

  @Test
  fun formatDeathInfo_whenValidDatesProvided_returnsFormattedDateAndAge() {
    val birthDate = born90
    val deathDate = "2020-10-10"

    val formattedDeathDate = "Oct 10, 2020"
    val ageAtDeath = 30

    mockkObject(PersonPageHelper)
    every { getAgeDeath(birthDate, deathDate) } returns ageAtDeath
    every { context.getString(any()) } returns "years old"

    val result = context.formatDeathInfo(birthDate, deathDate)
    assertEquals("$formattedDeathDate ($ageAtDeath years old)", result)

    verify { getAgeDeath(birthDate, deathDate) }
    verify { context.getString(any()) }

    unmockkObject(PersonPageHelper)
  }

  @Test
  fun formatDeathInfo_whenAllInputsAreNull_returnsNoData() {
    // mock the behavior of getString to return "no data" for any input
    // getString(no_data)

    val result = context.formatDeathInfo(birthday = null, deathday = null)
    assertEquals("no data", result)
    verify { context.getString(any()) }
  }

  @Test
  fun formatDeathInfo_whenBirthdayIsNull_returnsNoData() {
    val result = context.formatDeathInfo(birthday = null, deathday = "2020-10-10")
    assertEquals("no data", result)
  }

  @Test
  fun formatDeathInfo_whenDeathdayIsNull_returnsNoData() {
    val result = context.formatDeathInfo(birthday = born90, deathday = null)
    assertEquals("no data", result)
  }

  @Test
  fun formatDeathInfo_whenOnlyDeathdayProvided_returnsNoData() {
    val result = context.formatDeathInfo(birthday = null, deathday = "2020-10-10")
    assertEquals("no data", result)
  }
}
