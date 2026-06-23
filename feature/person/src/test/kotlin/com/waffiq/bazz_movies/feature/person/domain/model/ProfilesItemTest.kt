package com.waffiq.bazz_movies.feature.person.domain.model

import org.junit.Assert.assertNull
import org.junit.Test

class ProfilesItemTest {

  @Test
  fun profileItem_withNullValue_returnsDefaultValue() {
    val profilesItem = ProfilesItem()

    assertNull(profilesItem.aspectRatio)
    assertNull(profilesItem.filePath)
    assertNull(profilesItem.voteAverage)
    assertNull(profilesItem.width)
    assertNull(profilesItem.iso6391)
    assertNull(profilesItem.voteCount)
    assertNull(profilesItem.height)
  }
}
