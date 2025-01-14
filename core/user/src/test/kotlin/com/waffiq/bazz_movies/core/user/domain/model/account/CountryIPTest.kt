package com.waffiq.bazz_movies.core.user.domain.model.account

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class CountryIPTest {

  @Test
  fun countryIP_WithAllPropertiesSet() {
    val avatarTmdb = CountryIP(country = "ID", ip = "117.25.82.134")
    assertEquals("ID", avatarTmdb.country)
    assertEquals("117.25.82.134", avatarTmdb.ip)
  }

  @Test
  fun countryIP_WithAllPropertiesNull() {
    val avatarTmdb = CountryIP(null, null)
    assertNull(avatarTmdb.country)
    assertNull(avatarTmdb.ip)
  }

  @Test
  fun countryIP_WithSomePropertiesProvided() {
    val avatarTmdb = CountryIP("US", null)
    assertEquals("US", avatarTmdb.country)
    assertNull(avatarTmdb.ip)
  }

  @Test
  fun countryIP_WithDefaultValues() {
    val avatarTmdb = CountryIP()
    assertNull(avatarTmdb.country)
    assertNull(avatarTmdb.ip)
  }
}
