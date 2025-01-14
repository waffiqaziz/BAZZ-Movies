package com.waffiq.bazz_movies.core.user.domain.model.account

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class AvatarTMDbTest {

  @Test
  fun avatarTMDb_creationWithAllPropertiesSet() {
    val avatarTmdb = AvatarTMDb(avatarPath = "/1243513451443.jpg")
    assertEquals("/1243513451443.jpg", avatarTmdb.avatarPath)
  }

  @Test
  fun avatarTMDb_creationWithAllPropertiesNull() {
    val avatarTmdb = AvatarTMDb(avatarPath = null)
    assertNull(avatarTmdb.avatarPath)
  }

  @Test
  fun avatarTMDb_creationWithDefaultValues() {
    val avatarTmdb = AvatarTMDb()
    assertNull(avatarTmdb.avatarPath)
  }
}
