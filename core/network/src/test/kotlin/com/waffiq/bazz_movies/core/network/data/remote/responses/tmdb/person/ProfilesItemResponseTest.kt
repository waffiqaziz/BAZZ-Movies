package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.profileItemResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class ProfilesItemResponseTest {

  @Test
  fun profilesItemResponse_withValidValues_setsPropertiesCorrectly() {
    val profilesItemResponse = profileItemResponseDump
    assertEquals(0.667, profilesItemResponse.aspectRatio)
    assertEquals("/83fLAMMb1LGT8YZ4dgRI0fti3az.jpg", profilesItemResponse.filePath)
    assertEquals(5.25f, profilesItemResponse.voteAverage)
    assertEquals(8, profilesItemResponse.voteCount)
    assertEquals(736, profilesItemResponse.width)
    assertEquals("en", profilesItemResponse.iso6391)
    assertEquals(1104, profilesItemResponse.height)
  }

  @Test
  fun profilesItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val profilesItemResponse = ProfilesItemResponse()
    assertNull(profilesItemResponse.aspectRatio)
    assertNull(profilesItemResponse.filePath)
    assertNull(profilesItemResponse.voteAverage)
    assertNull(profilesItemResponse.voteCount)
    assertNull(profilesItemResponse.width)
    assertNull(profilesItemResponse.iso6391)
    assertNull(profilesItemResponse.height)
  }

  @Test
  fun profilesItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val profilesItemResponse = ProfilesItemResponse(
      iso6391 = "id"
    )
    assertEquals("id", profilesItemResponse.iso6391)
    assertNull(profilesItemResponse.aspectRatio)
    assertNull(profilesItemResponse.filePath)
    assertNull(profilesItemResponse.voteAverage)
    assertNull(profilesItemResponse.voteCount)
    assertNull(profilesItemResponse.width)
    assertNull(profilesItemResponse.height)
  }
}
