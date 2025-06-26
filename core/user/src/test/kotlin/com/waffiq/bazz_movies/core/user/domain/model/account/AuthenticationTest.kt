package com.waffiq.bazz_movies.core.user.domain.model.account

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class AuthenticationTest {

  @Test
  fun authentication_withAllPropertiesSet_setsPropertiesCorrectly() {
    val authentication = Authentication(
      success = true,
      expireAt = "expire_date",
      requestToken = "request_token"
    )
    assertTrue(authentication.success)
    assertEquals("expire_date", authentication.expireAt)
    assertEquals("request_token", authentication.requestToken)
  }

  @Test
  fun authentication_withSomePropertiesProvided_setsPropertiesCorrectly() {
    val authentication = Authentication(success = false, expireAt = null, requestToken = null)
    assertFalse(authentication.success)
    assertNull(authentication.expireAt)
    assertNull(authentication.requestToken)
  }

  @Test
  fun authentication_withWithDefaultValues_setsPropertiesCorrectly() {
    val authentication = Authentication(success = true)
    assertTrue(authentication.success)
    assertNull(authentication.expireAt)
    assertNull(authentication.requestToken)
  }
}
