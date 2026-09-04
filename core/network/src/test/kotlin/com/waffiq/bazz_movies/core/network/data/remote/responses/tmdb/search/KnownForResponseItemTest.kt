package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search

import com.waffiq.bazz_movies.core.network.testutils.DummyData.knownForResponseItem2
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class KnownForResponseItemTest {

  @Test
  fun knownForResponseItem_withValidValues_setsPropertiesCorrectly() {
    assertEquals(
      """
        As the gang return to Jumanji to rescue one of their own, they discover that nothing is as 
        they expect. The players will have to brave parts unknown and unexplored in order to escape 
        the world’s most dangerous game.
      """.trimIndent(),
      knownForResponseItem2.overview,
    )
    assertEquals("en", knownForResponseItem2.originalLanguage)
    assertEquals("Jumanji: The Next Level", knownForResponseItem2.originalTitle)
    assertEquals(false, knownForResponseItem2.video)
    assertEquals("Jumanji: The Next Level", knownForResponseItem2.title)
    assertEquals(listOf(12, 35, 14), knownForResponseItem2.genreIds)
    assertEquals("/4kh9dxAiClS2GMUpkRyzGwpNWWX.jpg", knownForResponseItem2.posterPath)
    assertEquals("/zTxHf9iIOCqRbxvl8W5QYKrsMLq.jpg", knownForResponseItem2.backdropPath)
    assertEquals("2019-12-04", knownForResponseItem2.releaseDate)
    assertEquals("movie", knownForResponseItem2.mediaType)
    assertEquals(92.641, knownForResponseItem2.popularity)
    assertEquals(6.9, knownForResponseItem2.voteAverage)
    assertEquals(512200, knownForResponseItem2.id)
    assertEquals(false, knownForResponseItem2.adult)
    assertEquals(8692, knownForResponseItem2.voteCount)
    assertEquals("2019-12-04", knownForResponseItem2.firstAirDate)
    assertEquals(listOf("US"), knownForResponseItem2.originCountry)
    assertEquals("Jumanji: The Next Level", knownForResponseItem2.originalName)
    assertEquals("Jumanji: The Next Level", knownForResponseItem2.name)
  }

  @Test
  fun knownForResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val knownForResponseItem = KnownForResponseItem()
    assertNull(knownForResponseItem.overview)
    assertNull(knownForResponseItem.originalLanguage)
    assertNull(knownForResponseItem.originalTitle)
    assertNull(knownForResponseItem.video)
    assertNull(knownForResponseItem.title)
    assertNull(knownForResponseItem.genreIds)
    assertNull(knownForResponseItem.posterPath)
    assertNull(knownForResponseItem.backdropPath)
    assertNull(knownForResponseItem.releaseDate)
    assertNull(knownForResponseItem.mediaType)
    assertNull(knownForResponseItem.popularity)
    assertNull(knownForResponseItem.voteAverage)
    assertNull(knownForResponseItem.id)
    assertNull(knownForResponseItem.adult)
    assertNull(knownForResponseItem.voteCount)
    assertNull(knownForResponseItem.firstAirDate)
    assertNull(knownForResponseItem.originCountry)
    assertNull(knownForResponseItem.originalName)
    assertNull(knownForResponseItem.name)
  }
}
