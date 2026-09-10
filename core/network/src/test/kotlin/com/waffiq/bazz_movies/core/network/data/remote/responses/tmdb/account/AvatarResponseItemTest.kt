package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AvatarResponseItemTest {

  @Test
  fun genresResponseItem_withValidValues_setsPropertiesCorrectly() {
    val genresResponseItem = AvatarResponseItem(
      avatarTMDbResponse = AvatarTMDbResponse("/35709850723705.pjg"),
      gravatarResponse = GravatarResponse("gravatar_hash"),
    )
    assertEquals("/35709850723705.pjg", genresResponseItem.avatarTMDbResponse?.avatarPath)
    assertEquals("gravatar_hash", genresResponseItem.gravatarResponse?.hash)
  }

  @Test
  fun genresResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val genresResponseItem = AvatarResponseItem()
    assertNull(genresResponseItem.avatarTMDbResponse)
    assertNull(genresResponseItem.gravatarResponse)
  }

  @Test
  fun genresResponseItem_withSomeNullValues_setsPropertiesCorrectly() {
    val genresResponseItem = AvatarResponseItem(
      gravatarResponse = GravatarResponse("gravatar_hash"),
    )
    assertEquals("gravatar_hash", genresResponseItem.gravatarResponse?.hash)
    assertNull(genresResponseItem.avatarTMDbResponse)
  }
}
