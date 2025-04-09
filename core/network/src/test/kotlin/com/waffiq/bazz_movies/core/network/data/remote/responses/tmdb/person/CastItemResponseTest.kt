package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.castItemResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class CastItemResponseTest {

  @Test
  fun castItemResponse_withValidValues_setsPropertiesCorrectly() {
    val castItemResponse = castItemResponseDump
    assertEquals("2022-10-10", castItemResponse.firstAirDate)
    assertTrue(castItemResponse.adult == false)
    assertEquals("/nH6hPhJq3EEv9CnBZgXU3IQnpJo.jpg", castItemResponse.backdropPath)
    assertEquals(listOf(12, 53, 878), castItemResponse.genreIds)
    assertEquals(74, castItemResponse.id)
    assertEquals("en", castItemResponse.originalLanguage)
    assertEquals(1, castItemResponse.episodeCount)
    assertEquals("War of the Worlds", castItemResponse.originalTitle)
    assertEquals(
      """
        Ray Ferrier is a divorced dockworker and less-than-perfect father. Soon after his ex-wife and 
        her new husband drop off his teenage son and young daughter for a rare weekend visit, a strange 
        and powerful lightning storm touches down.
      """.trimIndent(),
      castItemResponse.overview
    )
    assertEquals(50.065, castItemResponse.popularity)
    assertEquals("/6Biy7R9LfumYshur3YKhpj56MpB.jpg", castItemResponse.posterPath)
    assertEquals("US", castItemResponse.originCountry?.get(0))
    assertEquals("2005-06-13", castItemResponse.releaseDate)
    assertEquals("War of the Worlds", castItemResponse.title)
    assertTrue(castItemResponse.video == false)
    assertEquals("Name", castItemResponse.name)
    assertEquals(6.5f, castItemResponse.voteAverage)
    assertEquals(8409, castItemResponse.voteCount)
    assertEquals("Ray Ferrier", castItemResponse.character)
    assertEquals("52fe4213c3a36847f800226b", castItemResponse.creditId)
    assertEquals(0, castItemResponse.order)
    assertEquals("movie", castItemResponse.mediaType)
    assertEquals("War of the Worlds", castItemResponse.originalName)
  }

  @Test
  fun castItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val castItemResponse = CastItemResponse()
    assertNull(castItemResponse.firstAirDate)
    assertNull(castItemResponse.adult)
    assertNull(castItemResponse.backdropPath)
    assertNull(castItemResponse.genreIds)
    assertNull(castItemResponse.id)
    assertNull(castItemResponse.originalLanguage)
    assertNull(castItemResponse.episodeCount)
    assertNull(castItemResponse.originalTitle)
    assertNull(castItemResponse.overview)
    assertNull(castItemResponse.popularity)
    assertNull(castItemResponse.posterPath)
    assertNull(castItemResponse.originCountry)
    assertNull(castItemResponse.releaseDate)
    assertNull(castItemResponse.title)
    assertNull(castItemResponse.video)
    assertNull(castItemResponse.name)
    assertNull(castItemResponse.voteAverage)
    assertNull(castItemResponse.voteCount)
    assertNull(castItemResponse.character)
    assertNull(castItemResponse.creditId)
    assertNull(castItemResponse.order)
    assertNull(castItemResponse.mediaType)
    assertNull(castItemResponse.originalName)
  }

  @Test
  fun castItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val castItemResponse = CastItemResponse(id = 4355423, title = "Movie Title")
    assertNull(castItemResponse.name)
    assertEquals(4355423, castItemResponse.id)
    assertEquals("Movie Title", castItemResponse.title)
  }
}
