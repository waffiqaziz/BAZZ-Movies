package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.castcrew

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieTvCrewItemResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class MovieTvCrewItemResponseTest {

  @Test
  fun movieTvCrewItemResponse_withValidValues_setsPropertiesCorrectly() {
    val movieTvCrewItemResponse = movieTvCrewItemResponseDump
    assertEquals(0, movieTvCrewItemResponse.gender)
    assertEquals("64fc09ebf85958011ca070b4", movieTvCrewItemResponse.creditId)
    assertEquals("Visual Effects", movieTvCrewItemResponse.knownForDepartment)
    assertEquals("Frank Schlegel", movieTvCrewItemResponse.originalName)
    assertEquals(0.001, movieTvCrewItemResponse.popularity)
    assertEquals("Frank Schlegel", movieTvCrewItemResponse.name)
    assertEquals(null, movieTvCrewItemResponse.profilePath)
    assertEquals(3014542, movieTvCrewItemResponse.id)
    assertEquals(false, movieTvCrewItemResponse.adult)
    assertEquals("Visual Effects", movieTvCrewItemResponse.department)
    assertEquals("VFX Supervisor", movieTvCrewItemResponse.job)
  }

  @Test
  fun movieTvCrewItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val movieTvCrewItemResponse = MovieTvCrewItemResponse()
    assertNull(movieTvCrewItemResponse.gender)
    assertNull(movieTvCrewItemResponse.creditId)
    assertNull(movieTvCrewItemResponse.knownForDepartment)
    assertNull(movieTvCrewItemResponse.originalName)
    assertNull(movieTvCrewItemResponse.popularity)
    assertNull(movieTvCrewItemResponse.name)
    assertNull(movieTvCrewItemResponse.profilePath)
    assertNull(movieTvCrewItemResponse.id)
    assertNull(movieTvCrewItemResponse.adult)
    assertNull(movieTvCrewItemResponse.department)
    assertNull(movieTvCrewItemResponse.job)
  }

  @Test
  fun movieTvCrewItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val movieTvCrewItemResponse = MovieTvCrewItemResponse(
      name = "Martin Freeman",
      id = 7060

    )
    assertEquals("Martin Freeman", movieTvCrewItemResponse.name)
    assertEquals(7060, movieTvCrewItemResponse.id)
    assertNull(movieTvCrewItemResponse.originalName)
  }
}
