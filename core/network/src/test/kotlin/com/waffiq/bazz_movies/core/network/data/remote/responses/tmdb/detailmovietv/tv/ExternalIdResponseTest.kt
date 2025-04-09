package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.externalIdResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class ExternalIdResponseTest {

  @Test
  fun externalIdResponse_withValidValues_setsPropertiesCorrectly() {
    val externalIdResponse = externalIdResponseDump
    assertEquals("tt0417299", externalIdResponse.imdbId)
    assertEquals("/m/05h95s", externalIdResponse.freebaseMid)
    assertEquals(74852, externalIdResponse.tvdbId)
    assertEquals("/en/avatar_the_last_airbender", externalIdResponse.freebaseId)
    assertEquals(246, externalIdResponse.id)
    assertEquals(null, externalIdResponse.twitterId)
    assertEquals(2680, externalIdResponse.tvrageId)
    assertEquals("avatarthelastairbender", externalIdResponse.facebookId)
    assertEquals("avatarthelastairbender", externalIdResponse.instagramId)
  }

  @Test
  fun externalIdResponse_withDefaultValues_setsPropertiesCorrectly() {
    val externalIdResponse = ExternalIdResponse()
    assertNull(externalIdResponse.imdbId)
    assertNull(externalIdResponse.freebaseMid)
    assertNull(externalIdResponse.tvdbId)
    assertNull(externalIdResponse.freebaseId)
    assertNull(externalIdResponse.id)
    assertNull(externalIdResponse.twitterId)
    assertNull(externalIdResponse.tvrageId)
    assertNull(externalIdResponse.facebookId)
    assertNull(externalIdResponse.instagramId)
  }

  @Test
  fun externalIdResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val externalIdResponse = ExternalIdResponse(
      imdbId = null,
      id = 6718354
    )
    assertNull(externalIdResponse.imdbId)
    assertEquals(6718354, externalIdResponse.id)
  }
}
