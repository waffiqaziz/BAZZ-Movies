package com.waffiq.bazz_movies.feature.person.utils.helper

import android.os.Build
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.getAgeDeath
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class GetAgeDeathTest {

  @Test
  @Config(sdk = [Build.VERSION_CODES.N])
  fun getAgeDeath_whenDeathBeforeBirthday_returnsReducedAge() {
    val age = getAgeDeath(dateBirth = "2000-05-10", dateDeath = "2021-03-15")
    assertEquals(20, age)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.O])
  fun getAgeDeath_whenApiLevelIs26OrAbove_returnsCorrectAge() {
    val age = getAgeDeath(dateBirth = "1990-05-15", dateDeath = "2020-10-10")
    assertEquals(30, age)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.M])
  fun getAgeDeath_whenApiLevelIsBelow26_returnsCorrectAge() {
    val age = getAgeDeath(dateBirth = "1990-05-15", dateDeath = "2024-10-10")
    assertEquals(34, age)
  }

  @Test
  fun getAgeDeath_whenDatesAreValid_returnsCalculatedAge() {
    val age = getAgeDeath(dateBirth = "1990-05-15", dateDeath = "2024-10-10")
    assertEquals(34, age)
  }
}
