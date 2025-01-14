package com.waffiq.bazz_movies.core.user.domain.model.account

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class AvatarItemTest {

  @Test
  fun avatarItem_withValidValues_setsPropertiesCorrectly() {
    val avatarItem = AvatarItem(
      avatarTMDb = AvatarTMDb("/2578003545.jpg"),
      gravatar = Gravatar("/235098640315743905.jpg")
    )
    assertEquals("/2578003545.jpg", avatarItem.avatarTMDb?.avatarPath)
    assertEquals("/235098640315743905.jpg", avatarItem.gravatar?.hash)
  }

  @Test
  fun avatarItem_withNullValues_setsPropertiesToNull() {
    val avatarItem = AvatarItem(null, null)
    assertNull(avatarItem.avatarTMDb?.avatarPath)
    assertNull(avatarItem.gravatar?.hash)
  }

  @Test
  fun avatarItem_withSomeNullValues_setsPropertiesCorrectly() {
    val avatarItem = AvatarItem(
      avatarTMDb = null,
      gravatar = Gravatar("/257803475803425.jpg")
    )
    assertNull(avatarItem.avatarTMDb?.avatarPath)
    assertEquals("/257803475803425.jpg", avatarItem.gravatar?.hash)
  }

  @Test
  fun avatarItem_withDefaultValues_setsPropertiesCorrectly() {
    val avatarItem = AvatarItem()
    assertNull(avatarItem.avatarTMDb?.avatarPath)
    assertNull(avatarItem.gravatar?.hash)
  }
}
