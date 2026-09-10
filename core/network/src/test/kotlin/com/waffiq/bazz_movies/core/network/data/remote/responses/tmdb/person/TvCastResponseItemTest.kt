package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person

import com.waffiq.bazz_movies.core.network.testutils.DummyData.castResponseItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TvCastResponseItemTest {

  @Test
  fun castResponseItem_withValidValues_setsPropertiesCorrectly() {
    assertEquals("2022-10-10", castResponseItem.firstAirDate)
    assertTrue(castResponseItem.adult == false)
    assertEquals("/nH6hPhJq3EEv9CnBZgXU3IQnpJo.jpg", castResponseItem.backdropPath)
    assertEquals(listOf(12, 53, 878), castResponseItem.genreIds)
    assertEquals(74, castResponseItem.id)
    assertEquals("en", castResponseItem.originalLanguage)
    assertEquals(1, castResponseItem.episodeCount)
    assertEquals("War of the Worlds", castResponseItem.originalTitle)
    assertEquals(
      """
        Ray Ferrier is a divorced dockworker and less-than-perfect father. Soon after his ex-wife and 
        her new husband drop off his teenage son and young daughter for a rare weekend visit, a strange 
        and powerful lightning storm touches down.
      """.trimIndent(),
      castResponseItem.overview,
    )
    assertEquals(50.065, castResponseItem.popularity)
    assertEquals("/6Biy7R9LfumYshur3YKhpj56MpB.jpg", castResponseItem.posterPath)
    assertEquals("US", castResponseItem.originCountry?.get(0))
    assertEquals("2005-06-13", castResponseItem.releaseDate)
    assertEquals("War of the Worlds", castResponseItem.title)
    assertTrue(castResponseItem.video == false)
    assertEquals("Name", castResponseItem.name)
    assertEquals(6.5f, castResponseItem.voteAverage)
    assertEquals(8409, castResponseItem.voteCount)
    assertEquals("Ray Ferrier", castResponseItem.character)
    assertEquals("52fe4213c3a36847f800226b", castResponseItem.creditId)
    assertEquals(0, castResponseItem.order)
    assertEquals("movie", castResponseItem.mediaType)
    assertEquals("War of the Worlds", castResponseItem.originalName)
  }

  @Test
  fun castResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val castResponseItem = CastResponseItem()
    assertNull(castResponseItem.firstAirDate)
    assertNull(castResponseItem.adult)
    assertNull(castResponseItem.backdropPath)
    assertNull(castResponseItem.genreIds)
    assertNull(castResponseItem.id)
    assertNull(castResponseItem.originalLanguage)
    assertNull(castResponseItem.episodeCount)
    assertNull(castResponseItem.originalTitle)
    assertNull(castResponseItem.overview)
    assertNull(castResponseItem.popularity)
    assertNull(castResponseItem.posterPath)
    assertNull(castResponseItem.originCountry)
    assertNull(castResponseItem.releaseDate)
    assertNull(castResponseItem.title)
    assertNull(castResponseItem.video)
    assertNull(castResponseItem.name)
    assertNull(castResponseItem.voteAverage)
    assertNull(castResponseItem.voteCount)
    assertNull(castResponseItem.character)
    assertNull(castResponseItem.creditId)
    assertNull(castResponseItem.order)
    assertNull(castResponseItem.mediaType)
    assertNull(castResponseItem.originalName)
  }

  @Test
  fun castResponseItem_withSomeNullValues_setsPropertiesCorrectly() {
    val castResponseItem = CastResponseItem(id = 4355423, title = "Movie Title")
    assertNull(castResponseItem.name)
    assertEquals(4355423, castResponseItem.id)
    assertEquals("Movie Title", castResponseItem.title)
  }
}
