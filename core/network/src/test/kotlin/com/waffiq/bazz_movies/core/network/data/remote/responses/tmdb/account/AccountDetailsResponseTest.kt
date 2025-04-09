package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.accountDetailsResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class AccountDetailsResponseTest {

  @Test
  fun accountDetailsResponse_withValidValues_setsPropertiesCorrectly() {
    val accountDetailsResponse = accountDetailsResponse
    assertEquals("ID", accountDetailsResponse.iso31661)
    assertEquals("YOUR_NAME", accountDetailsResponse.name)
    assertEquals(
      "gravatar_hash2",
      accountDetailsResponse.avatarItemResponse?.gravatarResponse?.hash
    )
    assertEquals(
      "avatar_path",
      accountDetailsResponse.avatarItemResponse?.avatarTMDbResponse?.avatarPath
    )
    assertEquals(543798538, accountDetailsResponse.id)
    assertEquals("en", accountDetailsResponse.iso6391)
    assertEquals("USERNAME", accountDetailsResponse.username)
    assertTrue(accountDetailsResponse.includeAdult == false)
  }

  @Test
  fun accountDetailsResponse_withDefaultValues_setsPropertiesCorrectly() {
    val accountDetailsResponse = AccountDetailsResponse()
    assertNull(accountDetailsResponse.includeAdult)
    assertNull(accountDetailsResponse.iso31661)
    assertNull(accountDetailsResponse.name)
    assertNull(accountDetailsResponse.avatarItemResponse)
    assertNull(accountDetailsResponse.id)
    assertNull(accountDetailsResponse.iso6391)
    assertNull(accountDetailsResponse.username)
  }

  @Test
  fun accountDetailsResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val accountDetailsResponse = AccountDetailsResponse(
      name = null,
      id = 436456365
    )
    assertNull(accountDetailsResponse.name)
    assertEquals(436456365, accountDetailsResponse.id)
  }
}
