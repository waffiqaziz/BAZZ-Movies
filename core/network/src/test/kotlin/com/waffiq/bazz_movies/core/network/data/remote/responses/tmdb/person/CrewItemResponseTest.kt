package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.crewItemResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class CrewItemResponseTest {

  @Test
  fun crewItemResponse_withValidValues_setsPropertiesCorrectly() {
    val crewItemResponse = crewItemResponseDump
    assertTrue(crewItemResponse.adult == false)
    assertEquals("/z354BaTVzKj7E60WLzDoSmUuO4u.jpg", crewItemResponse.backdropPath)
    assertEquals(listOf(18, 28, 10752), crewItemResponse.genreIds)
    assertEquals(616, crewItemResponse.id)
    assertEquals("en", crewItemResponse.originalLanguage)
    assertEquals("The Last Samurai", crewItemResponse.originalTitle)
    assertEquals(
      "Nathan Algren is an American hired",
      crewItemResponse.overview
    )
    assertEquals(44.954f, crewItemResponse.popularity)
    assertEquals("/lsasOSgYI85EHygtT5SvcxtZVYT.jpg", crewItemResponse.posterPath)
    assertEquals("2003-12-05", crewItemResponse.releaseDate)
    assertEquals("The Last Samurai", crewItemResponse.title)
    assertTrue(crewItemResponse.video == false)
    assertEquals(7.6f, crewItemResponse.voteAverage)
    assertEquals(6884, crewItemResponse.voteCount)
    assertEquals("52fe425ec3a36847f8018e1f", crewItemResponse.creditId)
    assertEquals("Production", crewItemResponse.department)
    assertEquals("Producer", crewItemResponse.job)
    assertEquals("movie", crewItemResponse.mediaType)
  }

  @Test
  fun crewItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val crewItemResponse = CrewItemResponse()
    assertNull(crewItemResponse.adult)
    assertNull(crewItemResponse.backdropPath)
    assertNull(crewItemResponse.genreIds)
    assertNull(crewItemResponse.id)
    assertNull(crewItemResponse.originalLanguage)
    assertNull(crewItemResponse.originalTitle)
    assertNull(crewItemResponse.overview)
    assertNull(crewItemResponse.popularity)
    assertNull(crewItemResponse.posterPath)
    assertNull(crewItemResponse.releaseDate)
    assertNull(crewItemResponse.title)
    assertNull(crewItemResponse.video)
    assertNull(crewItemResponse.voteAverage)
    assertNull(crewItemResponse.voteCount)
    assertNull(crewItemResponse.creditId)
    assertNull(crewItemResponse.department)
    assertNull(crewItemResponse.job)
    assertNull(crewItemResponse.mediaType)
  }

  @Test
  fun crewItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val crewItemResponse = CrewItemResponse(
      id = 345784867
    )
    assertEquals(345784867, crewItemResponse.id)
    assertNull(crewItemResponse.adult)
    assertNull(crewItemResponse.backdropPath)
    assertNull(crewItemResponse.genreIds)
    assertNull(crewItemResponse.originalLanguage)
    assertNull(crewItemResponse.originalTitle)
    assertNull(crewItemResponse.overview)
    assertNull(crewItemResponse.popularity)
    assertNull(crewItemResponse.posterPath)
    assertNull(crewItemResponse.releaseDate)
    assertNull(crewItemResponse.title)
    assertNull(crewItemResponse.video)
    assertNull(crewItemResponse.voteAverage)
    assertNull(crewItemResponse.voteCount)
    assertNull(crewItemResponse.creditId)
    assertNull(crewItemResponse.department)
    assertNull(crewItemResponse.job)
    assertNull(crewItemResponse.mediaType)
  }
}
