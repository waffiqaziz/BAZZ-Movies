package com.waffiq.bazz_movies.core.user.domain.model.account

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class AvatarTMDbTest {

  @Test
  fun avatarTMDb_withAllPropertiesSet_setsPropertiesCorrectly() {
    val avatarTmdb = AvatarTMDb(avatarPath = "/1243513451443.jpg")
    assertEquals("/1243513451443.jpg", avatarTmdb.avatarPath)
  }

  @Test
  fun avatarTMDb_withAllPropertiesNull_setsPropertiesCorrectly() {
    val avatarTmdb = AvatarTMDb(avatarPath = null)
    assertNull(avatarTmdb.avatarPath)
  }

  @Test
  fun avatarTMDb_withDefaultValues_setsPropertiesCorrectly() {
    val avatarTmdb = AvatarTMDb()
    assertNull(avatarTmdb.avatarPath)
  }
}
