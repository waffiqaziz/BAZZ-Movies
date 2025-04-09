package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.knownForItemResponseDump2
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class KnownForItemResponseTest {

  @Test
  fun knownForItemResponse_withValidValues_setsPropertiesCorrectly() {
    val knownForItemResponse = knownForItemResponseDump2
    assertEquals(
      """
        As the gang return to Jumanji to rescue one of their own, they discover that nothing is as 
        they expect. The players will have to brave parts unknown and unexplored in order to escape 
        the world’s most dangerous game.
      """.trimIndent(),
      knownForItemResponse.overview
    )
    assertEquals("en", knownForItemResponse.originalLanguage)
    assertEquals("Jumanji: The Next Level", knownForItemResponse.originalTitle)
    assertEquals(false, knownForItemResponse.video)
    assertEquals("Jumanji: The Next Level", knownForItemResponse.title)
    assertEquals(listOf(12, 35, 14), knownForItemResponse.genreIds)
    assertEquals("/4kh9dxAiClS2GMUpkRyzGwpNWWX.jpg", knownForItemResponse.posterPath)
    assertEquals("/zTxHf9iIOCqRbxvl8W5QYKrsMLq.jpg", knownForItemResponse.backdropPath)
    assertEquals("2019-12-04", knownForItemResponse.releaseDate)
    assertEquals("movie", knownForItemResponse.mediaType)
    assertEquals(92.641, knownForItemResponse.popularity)
    assertEquals(6.9, knownForItemResponse.voteAverage)
    assertEquals(512200, knownForItemResponse.id)
    assertEquals(false, knownForItemResponse.adult)
    assertEquals(8692, knownForItemResponse.voteCount)
    assertEquals("2019-12-04", knownForItemResponse.firstAirDate)
    assertEquals(listOf("US"), knownForItemResponse.originCountry)
    assertEquals("Jumanji: The Next Level", knownForItemResponse.originalName)
    assertEquals("Jumanji: The Next Level", knownForItemResponse.name)
  }

  @Test
  fun knownForItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val knownForItemResponse = KnownForItemResponse()
    assertNull(knownForItemResponse.overview)
    assertNull(knownForItemResponse.originalLanguage)
    assertNull(knownForItemResponse.originalTitle)
    assertNull(knownForItemResponse.video)
    assertNull(knownForItemResponse.title)
    assertNull(knownForItemResponse.genreIds)
    assertNull(knownForItemResponse.posterPath)
    assertNull(knownForItemResponse.backdropPath)
    assertNull(knownForItemResponse.releaseDate)
    assertNull(knownForItemResponse.mediaType)
    assertNull(knownForItemResponse.popularity)
    assertNull(knownForItemResponse.voteAverage)
    assertNull(knownForItemResponse.id)
    assertNull(knownForItemResponse.adult)
    assertNull(knownForItemResponse.voteCount)
    assertNull(knownForItemResponse.firstAirDate)
    assertNull(knownForItemResponse.originCountry)
    assertNull(knownForItemResponse.originalName)
    assertNull(knownForItemResponse.name)
  }

  @Test
  fun knownForItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val knownForItemResponse = KnownForItemResponse(id = 154325)
    assertEquals(154325, knownForItemResponse.id)
    assertNull(knownForItemResponse.overview)
    assertNull(knownForItemResponse.originalLanguage)
    assertNull(knownForItemResponse.originalTitle)
    assertNull(knownForItemResponse.video)
    assertNull(knownForItemResponse.title)
    assertNull(knownForItemResponse.genreIds)
    assertNull(knownForItemResponse.posterPath)
    assertNull(knownForItemResponse.backdropPath)
    assertNull(knownForItemResponse.releaseDate)
    assertNull(knownForItemResponse.mediaType)
    assertNull(knownForItemResponse.popularity)
    assertNull(knownForItemResponse.voteAverage)
    assertNull(knownForItemResponse.adult)
    assertNull(knownForItemResponse.voteCount)
    assertNull(knownForItemResponse.firstAirDate)
    assertNull(knownForItemResponse.originCountry)
    assertNull(knownForItemResponse.originalName)
    assertNull(knownForItemResponse.name)
  }
}
