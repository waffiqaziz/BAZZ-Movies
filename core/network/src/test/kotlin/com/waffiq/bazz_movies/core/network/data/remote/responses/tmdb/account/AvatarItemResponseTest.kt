package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class AvatarItemResponseTest {

  @Test
  fun genresItemResponse_withValidValues_setsPropertiesCorrectly() {
    val genresItemResponse = AvatarItemResponse(
      avatarTMDbResponse = AvatarTMDbResponse("/35709850723705.pjg"),
      gravatarResponse = GravatarResponse("gravatar_hash")
    )
    assertEquals("/35709850723705.pjg", genresItemResponse.avatarTMDbResponse?.avatarPath)
    assertEquals("gravatar_hash", genresItemResponse.gravatarResponse?.hash)
  }

  @Test
  fun genresItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val genresItemResponse = AvatarItemResponse()
    assertNull(genresItemResponse.avatarTMDbResponse)
    assertNull(genresItemResponse.gravatarResponse)
  }

  @Test
  fun genresItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val genresItemResponse = AvatarItemResponse(
      gravatarResponse = GravatarResponse("gravatar_hash")
    )
    assertEquals("gravatar_hash", genresItemResponse.gravatarResponse?.hash)
    assertNull(genresItemResponse.avatarTMDbResponse)
  }
}
