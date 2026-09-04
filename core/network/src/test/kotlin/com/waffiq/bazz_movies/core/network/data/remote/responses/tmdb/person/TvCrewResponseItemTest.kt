package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person

import com.waffiq.bazz_movies.core.network.testutils.DummyData.crewResponseItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TvCrewResponseItemTest {

  @Test
  fun crewResponseItem_withValidValues_setsPropertiesCorrectly() {
    assertTrue(crewResponseItem.adult == false)
    assertEquals("/z354BaTVzKj7E60WLzDoSmUuO4u.jpg", crewResponseItem.backdropPath)
    assertEquals(listOf(18, 28, 10752), crewResponseItem.genreIds)
    assertEquals(616, crewResponseItem.id)
    assertEquals("en", crewResponseItem.originalLanguage)
    assertEquals("The Last Samurai", crewResponseItem.originalTitle)
    assertEquals(
      "Nathan Algren is an American hired",
      crewResponseItem.overview,
    )
    assertEquals(44.954f, crewResponseItem.popularity)
    assertEquals("/lsasOSgYI85EHygtT5SvcxtZVYT.jpg", crewResponseItem.posterPath)
    assertEquals("2003-12-05", crewResponseItem.releaseDate)
    assertEquals("The Last Samurai", crewResponseItem.title)
    assertTrue(crewResponseItem.video == false)
    assertEquals(7.6f, crewResponseItem.voteAverage)
    assertEquals(6884, crewResponseItem.voteCount)
    assertEquals("52fe425ec3a36847f8018e1f", crewResponseItem.creditId)
    assertEquals("Production", crewResponseItem.department)
    assertEquals("Producer", crewResponseItem.job)
    assertEquals("movie", crewResponseItem.mediaType)
  }

  @Test
  fun crewResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val crewResponseItem = CrewResponseItem()
    assertNull(crewResponseItem.adult)
    assertNull(crewResponseItem.backdropPath)
    assertNull(crewResponseItem.genreIds)
    assertNull(crewResponseItem.id)
    assertNull(crewResponseItem.originalLanguage)
    assertNull(crewResponseItem.originalTitle)
    assertNull(crewResponseItem.overview)
    assertNull(crewResponseItem.popularity)
    assertNull(crewResponseItem.posterPath)
    assertNull(crewResponseItem.releaseDate)
    assertNull(crewResponseItem.title)
    assertNull(crewResponseItem.video)
    assertNull(crewResponseItem.voteAverage)
    assertNull(crewResponseItem.voteCount)
    assertNull(crewResponseItem.creditId)
    assertNull(crewResponseItem.department)
    assertNull(crewResponseItem.job)
    assertNull(crewResponseItem.mediaType)
  }

  @Test
  fun crewResponseItem_withSomeNullValues_setsPropertiesCorrectly() {
    val crewResponseItem = CrewResponseItem(
      id = 345784867,
    )
    assertEquals(345784867, crewResponseItem.id)
    assertNull(crewResponseItem.adult)
    assertNull(crewResponseItem.backdropPath)
    assertNull(crewResponseItem.genreIds)
    assertNull(crewResponseItem.originalLanguage)
    assertNull(crewResponseItem.originalTitle)
    assertNull(crewResponseItem.overview)
    assertNull(crewResponseItem.popularity)
    assertNull(crewResponseItem.posterPath)
    assertNull(crewResponseItem.releaseDate)
    assertNull(crewResponseItem.title)
    assertNull(crewResponseItem.video)
    assertNull(crewResponseItem.voteAverage)
    assertNull(crewResponseItem.voteCount)
    assertNull(crewResponseItem.creditId)
    assertNull(crewResponseItem.department)
    assertNull(crewResponseItem.job)
    assertNull(crewResponseItem.mediaType)
  }
}
