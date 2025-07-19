package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieSearchDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.personDump1
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ResultsItemSearchResponseTest {

  @Test
  fun resultsItemSearchResponse_withValidValues_setsPropertiesCorrectly() {
    val resultsItemSearchResponse = personDump1
    assertEquals("person", resultsItemSearchResponse.mediaType)
    assertEquals(
      "Jumanji: Welcome to the Jungle",
      resultsItemSearchResponse.listKnownFor?.get(0)?.title
    )
    assertEquals("Acting", resultsItemSearchResponse.knownForDepartment)
    assertEquals(102.851, resultsItemSearchResponse.popularity)
    assertEquals("Dwayne Johnson", resultsItemSearchResponse.name)
    assertEquals("/kuqFzlYMc2IrsOyPznMd1FroeGq.jpg", resultsItemSearchResponse.profilePath)
    assertEquals(18918, resultsItemSearchResponse.id)
    assertTrue(resultsItemSearchResponse.adult == false)
    assertEquals("Dwayne Johnson", resultsItemSearchResponse.originalName)
  }

  @Test
  fun resultsItemSearchResponse_withOtherValidValues_setsPropertiesCorrectly() {
    val resultsItemSearchResponse = movieSearchDump
    assertEquals("movie", resultsItemSearchResponse.mediaType)
    assertEquals("Avatar", resultsItemSearchResponse.title)
    assertEquals("Avatar", resultsItemSearchResponse.originalTitle)
    assertEquals(185.268, resultsItemSearchResponse.popularity)
    assertEquals(19995, resultsItemSearchResponse.id)
    assertTrue(resultsItemSearchResponse.adult == false)
    assertTrue(resultsItemSearchResponse.video == false)
    assertEquals(
      """
        In the 22nd century, a paraplegic Marine is dispatched to the moon Pandora on a unique 
        mission, but becomes torn between following orders and protecting an alien civilization.
      """.trimIndent(),
      resultsItemSearchResponse.overview
    )
    assertEquals("en", resultsItemSearchResponse.originalLanguage)
    assertEquals("Avatar", resultsItemSearchResponse.originalTitle)
    assertEquals("Avatar", resultsItemSearchResponse.title)
    assertEquals(listOf(28, 12, 14, 878), resultsItemSearchResponse.listGenreIds)
    assertEquals("/kyeqWdyUXW608qlYkRqosgbbJyK.jpg", resultsItemSearchResponse.posterPath)
    assertEquals("/vL5LR6WdxWPjLPFRLe133jXWsh5.jpg", resultsItemSearchResponse.backdropPath)
    assertEquals("2009-12-15", resultsItemSearchResponse.releaseDate)
    assertEquals(7.583, resultsItemSearchResponse.voteAverage)
    assertEquals(31734.0, resultsItemSearchResponse.voteCount)
    assertEquals("2009-12-15", resultsItemSearchResponse.firstAirDate)
    assertEquals(listOf("US"), resultsItemSearchResponse.listOriginCountry)
  }

  @Test
  fun resultsItemSearchResponse_withDefaultValues_setsPropertiesCorrectly() {
    val resultsItemSearchResponse = MultiSearchResponseItem()
    assertNull(resultsItemSearchResponse.mediaType)
    assertNull(resultsItemSearchResponse.listKnownFor)
    assertNull(resultsItemSearchResponse.knownForDepartment)
    assertNull(resultsItemSearchResponse.popularity)
    assertNull(resultsItemSearchResponse.name)
    assertNull(resultsItemSearchResponse.profilePath)
    assertNull(resultsItemSearchResponse.id)
    assertNull(resultsItemSearchResponse.adult)
    assertNull(resultsItemSearchResponse.overview)
    assertNull(resultsItemSearchResponse.originalLanguage)
    assertNull(resultsItemSearchResponse.originalTitle)
    assertNull(resultsItemSearchResponse.video)
    assertNull(resultsItemSearchResponse.title)
    assertNull(resultsItemSearchResponse.listGenreIds)
    assertNull(resultsItemSearchResponse.posterPath)
    assertNull(resultsItemSearchResponse.backdropPath)
    assertNull(resultsItemSearchResponse.releaseDate)
    assertNull(resultsItemSearchResponse.voteAverage)
    assertNull(resultsItemSearchResponse.voteCount)
    assertNull(resultsItemSearchResponse.firstAirDate)
    assertNull(resultsItemSearchResponse.listOriginCountry)
    assertNull(resultsItemSearchResponse.originalName)
  }

  @Test
  fun resultsItemSearchResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val resultsItemSearchResponse = MultiSearchResponseItem(name = null, id = 2)
    assertNull(resultsItemSearchResponse.name)
    assertEquals(2, resultsItemSearchResponse.id)
  }
}
