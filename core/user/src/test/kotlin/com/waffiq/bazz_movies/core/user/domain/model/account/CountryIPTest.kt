package com.waffiq.bazz_movies.core.user.domain.model.account

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class CountryIPTest {

  @Test
  fun `test CountryIP creation with all properties set`() {
    val avatarTmdb = CountryIP(country = "ID", ip = "117.25.82.134")
    assertEquals("ID", avatarTmdb.country)
    assertEquals("117.25.82.134", avatarTmdb.ip)
  }

  @Test
  fun `test CountryIP creation with all properties null`() {
    val avatarTmdb = CountryIP(null, null)
    assertNull(avatarTmdb.country)
    assertNull(avatarTmdb.ip)
  }

  @Test
  fun `test CountryIP creation only some properties are provided`() {
    val avatarTmdb = CountryIP("US", null)
    assertEquals("US", avatarTmdb.country)
    assertNull(avatarTmdb.ip)
  }

  @Test
  fun `test CountryIP creation with all default values`() {
    val avatarTmdb = CountryIP()
    assertNull(avatarTmdb.country)
    assertNull(avatarTmdb.ip)
  }
}
