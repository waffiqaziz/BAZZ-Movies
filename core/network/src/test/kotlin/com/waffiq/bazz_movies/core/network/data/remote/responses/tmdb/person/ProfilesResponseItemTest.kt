package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person

import com.waffiq.bazz_movies.core.network.testutils.DummyData.profileResponseItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProfilesResponseItemTest {

  @Test
  fun profileResponseItem_withValidValues_setsPropertiesCorrectly() {
    assertEquals(0.667, profileResponseItem.aspectRatio)
    assertEquals("/83fLAMMb1LGT8YZ4dgRI0fti3az.jpg", profileResponseItem.filePath)
    assertEquals(5.25f, profileResponseItem.voteAverage)
    assertEquals(8, profileResponseItem.voteCount)
    assertEquals(736, profileResponseItem.width)
    assertEquals("en", profileResponseItem.iso6391)
    assertEquals(1104, profileResponseItem.height)
  }

  @Test
  fun profileResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val profileResponseItem = ProfilesResponseItem()
    assertNull(profileResponseItem.aspectRatio)
    assertNull(profileResponseItem.filePath)
    assertNull(profileResponseItem.voteAverage)
    assertNull(profileResponseItem.voteCount)
    assertNull(profileResponseItem.width)
    assertNull(profileResponseItem.iso6391)
    assertNull(profileResponseItem.height)
  }

  @Test
  fun profileResponseItem_withSomeNullValues_setsPropertiesCorrectly() {
    val profileResponseItem = ProfilesResponseItem(
      iso6391 = "id",
    )
    assertEquals("id", profileResponseItem.iso6391)
    assertNull(profileResponseItem.aspectRatio)
    assertNull(profileResponseItem.filePath)
    assertNull(profileResponseItem.voteAverage)
    assertNull(profileResponseItem.voteCount)
    assertNull(profileResponseItem.width)
    assertNull(profileResponseItem.height)
  }
}
