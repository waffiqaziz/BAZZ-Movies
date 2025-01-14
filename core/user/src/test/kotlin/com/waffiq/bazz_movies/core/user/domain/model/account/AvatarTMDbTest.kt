package com.waffiq.bazz_movies.core.user.domain.model.account

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class AvatarTMDbTest {

  @Test
  fun `test AvatarTMDb creation with all properties set`() {
    val avatarTmdb = AvatarTMDb(avatarPath = "/1243513451443.jpg")
    assertEquals("/1243513451443.jpg", avatarTmdb.avatarPath)
  }

  @Test
  fun `test AvatarTMDb creation with all properties null`() {
    val avatarTmdb = AvatarTMDb(avatarPath = null)
    assertNull(avatarTmdb.avatarPath)
  }

  @Test
  fun `test AvatarTMDb creation with all default values`() {
    val avatarTmdb = AvatarTMDb()
    assertNull(avatarTmdb.avatarPath)
  }
}
