package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AvatarTMDbResponseTest {

  @Test
  fun avatarTMDbResponse_withValidValues_setsPropertiesCorrectly() {
    val avatarTMDbResponse = AvatarTMDbResponse(avatarPath = "/374950370589.jpg")
    assertEquals("/374950370589.jpg", avatarTMDbResponse.avatarPath)
  }

  @Test
  fun avatarTMDbResponse_withDefaultValues_setsPropertiesCorrectly() {
    val avatarTMDbResponse = AvatarTMDbResponse()
    assertNull(avatarTMDbResponse.avatarPath)
  }
}
