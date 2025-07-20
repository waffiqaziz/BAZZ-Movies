package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieSearchDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.personDump1
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class MultiSearchItemResponseTest {

  @Test
  fun multiSearchResponseItem_withValidValues_setsPropertiesCorrectly() {
    val multiSearchResponseItem = personDump1
    assertEquals("person", multiSearchResponseItem.mediaType)
    assertEquals(
      "Jumanji: Welcome to the Jungle",
      multiSearchResponseItem.listKnownFor?.get(0)?.title
    )
    assertEquals("Acting", multiSearchResponseItem.knownForDepartment)
    assertEquals(102.851, multiSearchResponseItem.popularity)
    assertEquals("Dwayne Johnson", multiSearchResponseItem.name)
    assertEquals("/kuqFzlYMc2IrsOyPznMd1FroeGq.jpg", multiSearchResponseItem.profilePath)
    assertEquals(18918, multiSearchResponseItem.id)
    assertTrue(multiSearchResponseItem.adult == false)
    assertEquals("Dwayne Johnson", multiSearchResponseItem.originalName)
  }

  @Test
  fun multiSearchResponseItem_withOtherValidValues_setsPropertiesCorrectly() {
    val multiSearchResponseItem = movieSearchDump
    assertEquals("movie", multiSearchResponseItem.mediaType)
    assertEquals("Avatar", multiSearchResponseItem.title)
    assertEquals("Avatar", multiSearchResponseItem.originalTitle)
    assertEquals(185.268, multiSearchResponseItem.popularity)
    assertEquals(19995, multiSearchResponseItem.id)
    assertTrue(multiSearchResponseItem.adult == false)
    assertTrue(multiSearchResponseItem.video == false)
    assertEquals(
      """
        In the 22nd century, a paraplegic Marine is dispatched to the moon Pandora on a unique 
        mission, but becomes torn between following orders and protecting an alien civilization.
      """.trimIndent(),
      multiSearchResponseItem.overview
    )
    assertEquals("en", multiSearchResponseItem.originalLanguage)
    assertEquals("Avatar", multiSearchResponseItem.originalTitle)
    assertEquals("Avatar", multiSearchResponseItem.title)
    assertEquals(listOf(28, 12, 14, 878), multiSearchResponseItem.listGenreIds)
    assertEquals("/kyeqWdyUXW608qlYkRqosgbbJyK.jpg", multiSearchResponseItem.posterPath)
    assertEquals("/vL5LR6WdxWPjLPFRLe133jXWsh5.jpg", multiSearchResponseItem.backdropPath)
    assertEquals("2009-12-15", multiSearchResponseItem.releaseDate)
    assertEquals(7.583, multiSearchResponseItem.voteAverage)
    assertEquals(31734.0, multiSearchResponseItem.voteCount)
    assertEquals("2009-12-15", multiSearchResponseItem.firstAirDate)
    assertEquals(listOf("US"), multiSearchResponseItem.listOriginCountry)
  }

  @Test
  fun multiSearchResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val multiSearchResponseItemNull = MultiSearchResponseItem()
    assertNull(multiSearchResponseItemNull.mediaType)
    assertNull(multiSearchResponseItemNull.listKnownFor)
    assertNull(multiSearchResponseItemNull.knownForDepartment)
    assertNull(multiSearchResponseItemNull.popularity)
    assertNull(multiSearchResponseItemNull.name)
    assertNull(multiSearchResponseItemNull.profilePath)
    assertNull(multiSearchResponseItemNull.id)
    assertNull(multiSearchResponseItemNull.adult)
    assertNull(multiSearchResponseItemNull.overview)
    assertNull(multiSearchResponseItemNull.originalLanguage)
    assertNull(multiSearchResponseItemNull.originalTitle)
    assertNull(multiSearchResponseItemNull.video)
    assertNull(multiSearchResponseItemNull.title)
    assertNull(multiSearchResponseItemNull.listGenreIds)
    assertNull(multiSearchResponseItemNull.posterPath)
    assertNull(multiSearchResponseItemNull.backdropPath)
    assertNull(multiSearchResponseItemNull.releaseDate)
    assertNull(multiSearchResponseItemNull.voteAverage)
    assertNull(multiSearchResponseItemNull.voteCount)
    assertNull(multiSearchResponseItemNull.firstAirDate)
    assertNull(multiSearchResponseItemNull.listOriginCountry)
    assertNull(multiSearchResponseItemNull.originalName)
  }

  @Test
  fun multiSearchResponseItem_withSomeNullValues_setsPropertiesCorrectly() {
    val multiSearchResponseItem = MultiSearchResponseItem(name = null, id = 2)
    assertNull(multiSearchResponseItem.name)
    assertEquals(2, multiSearchResponseItem.id)
  }
}
