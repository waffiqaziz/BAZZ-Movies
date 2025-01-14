package com.waffiq.bazz_movies.core.user.domain.model.account

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.junit.Test

class AccountDetailsTest {

  @Test
  fun accountDetails_withValidValues_setsPropertiesCorrectly() {
    val accountDetails = AccountDetails(
      includeAdult = true,
      iso31661 = "US",
      name = "John Doe",
      avatarItem = AvatarItem(avatarTMDb = null, gravatar = null),
      id = 123,
      iso6391 = "en",
      username = "johndoe"
    )

    assertEquals(true, accountDetails.includeAdult)
    assertEquals("US", accountDetails.iso31661)
    assertEquals("John Doe", accountDetails.name)
    assertNotNull(accountDetails.avatarItem)
    assertEquals(123, accountDetails.id)
    assertEquals("en", accountDetails.iso6391)
    assertEquals("johndoe", accountDetails.username)
  }

  @Test
  fun accountDetails_withDefaultValues_setsPropertiesCorrectly() {
    val accountDetails1 = AccountDetails()
    assertNull(accountDetails1.includeAdult)
    assertNull(accountDetails1.iso31661)
    assertNull(accountDetails1.name)
    assertNull(accountDetails1.avatarItem)
    assertNull(accountDetails1.id)
    assertNull(accountDetails1.iso6391)
    assertNull(accountDetails1.username)
  }

  @Test
  fun accountDetails_withSomeNullValues_setsPropertiesCorrectly() {
    val accountDetails2 = AccountDetails(
      includeAdult = false,
      iso31661 = "FR",
      name = null,
      avatarItem = null,
      id = null,
      iso6391 = "fr",
      username = "user123"
    )
    assertEquals(false, accountDetails2.includeAdult)
    assertEquals("FR", accountDetails2.iso31661)
    assertNull(accountDetails2.name)
    assertNull(accountDetails2.avatarItem)
    assertNull(accountDetails2.id)
    assertEquals("fr", accountDetails2.iso6391)
    assertEquals("user123", accountDetails2.username)
  }
}
