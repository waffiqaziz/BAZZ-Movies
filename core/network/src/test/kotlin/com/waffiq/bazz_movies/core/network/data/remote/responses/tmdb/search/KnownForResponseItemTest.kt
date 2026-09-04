package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search

import com.waffiq.bazz_movies.core.network.testutils.DummyData.knownForResponseItem1
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
      knownForResponseItem1.overview,
    )
    assertEquals("en", knownForResponseItem1.originalLanguage)
    assertEquals("Jumanji: The Next Level", knownForResponseItem1.originalTitle)
    assertEquals(false, knownForResponseItem1.video)
    assertEquals("Jumanji: The Next Level", knownForResponseItem1.title)
    assertEquals(listOf(12, 35, 14), knownForResponseItem1.genreIds)
    assertEquals("/4kh9dxAiClS2GMUpkRyzGwpNWWX.jpg", knownForResponseItem1.posterPath)
    assertEquals("/zTxHf9iIOCqRbxvl8W5QYKrsMLq.jpg", knownForResponseItem1.backdropPath)
    assertEquals("2019-12-04", knownForResponseItem1.releaseDate)
    assertEquals("movie", knownForResponseItem1.mediaType)
    assertEquals(92.641, knownForResponseItem1.popularity)
    assertEquals(6.9, knownForResponseItem1.voteAverage)
    assertEquals(512200, knownForResponseItem1.id)
    assertEquals(false, knownForResponseItem1.adult)
    assertEquals(8692, knownForResponseItem1.voteCount)
    assertEquals("2019-12-04", knownForResponseItem1.firstAirDate)
    assertEquals(listOf("US"), knownForResponseItem1.originCountry)
    assertEquals("Jumanji: The Next Level", knownForResponseItem1.originalName)
    assertEquals("Jumanji: The Next Level", knownForResponseItem1.name)
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
