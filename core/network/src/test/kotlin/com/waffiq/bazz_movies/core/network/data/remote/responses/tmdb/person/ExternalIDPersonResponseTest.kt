package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.externalIDPersonResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class ExternalIDPersonResponseTest {

  @Test
  fun externalIDPersonResponse_withValidValues_setsPropertiesCorrectly() {
    val externalIDPersonResponse = externalIDPersonResponseDump
    assertEquals(114253, externalIDPersonResponse.id)
    assertEquals("/m/027xw9j", externalIDPersonResponse.freebaseMid)
    assertEquals(null, externalIDPersonResponse.freebaseId)
    assertEquals("nm1375030", externalIDPersonResponse.imdbId)
    assertEquals(null, externalIDPersonResponse.tvrageId)
    assertEquals("Q7518724", externalIDPersonResponse.wikidataId)
    assertEquals(null, externalIDPersonResponse.facebookId)
    assertEquals(null, externalIDPersonResponse.instagramId)
    assertEquals(null, externalIDPersonResponse.tiktokId)
    assertEquals("simonfarnaby", externalIDPersonResponse.twitterId)
    assertEquals(null, externalIDPersonResponse.youtubeId)
  }

  @Test
  fun externalIDPersonResponse_withDefaultValues_setsPropertiesCorrectly() {
    val externalIDPersonResponse = ExternalIDPersonResponse()
    assertNull(externalIDPersonResponse.id)
    assertNull(externalIDPersonResponse.freebaseMid)
    assertNull(externalIDPersonResponse.freebaseId)
    assertNull(externalIDPersonResponse.imdbId)
    assertNull(externalIDPersonResponse.tvrageId)
    assertNull(externalIDPersonResponse.wikidataId)
    assertNull(externalIDPersonResponse.facebookId)
    assertNull(externalIDPersonResponse.instagramId)
    assertNull(externalIDPersonResponse.tiktokId)
    assertNull(externalIDPersonResponse.twitterId)
    assertNull(externalIDPersonResponse.youtubeId)
  }

  @Test
  fun externalIDPersonResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val externalIDPersonResponse = ExternalIDPersonResponse(
      id = 6542654
    )
    assertEquals(6542654, externalIDPersonResponse.id)
    assertNull(externalIDPersonResponse.imdbId)
    assertNull(externalIDPersonResponse.instagramId)
  }
}
