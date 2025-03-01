package com.waffiq.bazz_movies.feature.person.domain.model

import junit.framework.TestCase.assertNull
import org.junit.Test

class ProfilesItemTest {

  @Test
  fun profileItem_nullValue_returnDefaultValue(){
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
