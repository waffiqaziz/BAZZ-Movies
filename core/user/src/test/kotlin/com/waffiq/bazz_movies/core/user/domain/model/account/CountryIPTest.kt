package com.waffiq.bazz_movies.core.user.domain.model.account

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class CountryIPTest {

  @Test
  fun countryIP_withAllPropertiesSet_setsPropertiesCorrectly() {
    val avatarTmdb = CountryIP(country = "ID", ip = "117.25.82.134")
    assertEquals("ID", avatarTmdb.country)
    assertEquals("117.25.82.134", avatarTmdb.ip)
  }

  @Test
  fun countryIP_withAllPropertiesNull_setsPropertiesCorrectly() {
    val avatarTmdb = CountryIP(null, null)
    assertNull(avatarTmdb.country)
    assertNull(avatarTmdb.ip)
  }

  @Test
  fun countryIP_withSomePropertiesProvided_setsPropertiesCorrectly() {
    val avatarTmdb = CountryIP("US", null)
    assertEquals("US", avatarTmdb.country)
    assertNull(avatarTmdb.ip)
  }

  @Test
  fun countryIP_withDefaultValues_setsPropertiesCorrectly() {
    val avatarTmdb = CountryIP()
    assertNull(avatarTmdb.country)
    assertNull(avatarTmdb.ip)
  }
}
