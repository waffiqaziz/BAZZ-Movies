package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.castcrew

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieTvCastItemResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class MovieTvCastItemResponseTest {

  @Test
  fun movieTvCastItemResponse_withValidValues_setsPropertiesCorrectly() {
    val movieTvCastItemResponse = movieTvCastItemResponseDump
    assertEquals(13, movieTvCastItemResponse.castId)
    assertEquals("Momo", movieTvCastItemResponse.character)
    assertEquals(1, movieTvCastItemResponse.gender)
    assertEquals("6638f569ae38430122ca1143", movieTvCastItemResponse.creditId)
    assertEquals("Acting", movieTvCastItemResponse.knownForDepartment)
    assertEquals("Alexa Goodall", movieTvCastItemResponse.originalName)
    assertEquals(3.822, movieTvCastItemResponse.popularity)
    assertEquals("Alexa Goodall", movieTvCastItemResponse.name)
    assertEquals("/39Pk0wdjD2TC4QgnrODxWD8bubH.jpg", movieTvCastItemResponse.profilePath)
    assertEquals(3771374, movieTvCastItemResponse.id)
    assertTrue(movieTvCastItemResponse.adult == false)
    assertEquals(0, movieTvCastItemResponse.order)
  }

  @Test
  fun movieTvCastItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val movieTvCastItemResponse = MovieTvCastItemResponse()
    assertNull(movieTvCastItemResponse.castId)
    assertNull(movieTvCastItemResponse.character)
    assertNull(movieTvCastItemResponse.gender)
    assertNull(movieTvCastItemResponse.creditId)
    assertNull(movieTvCastItemResponse.knownForDepartment)
    assertNull(movieTvCastItemResponse.originalName)
    assertNull(movieTvCastItemResponse.popularity)
    assertNull(movieTvCastItemResponse.name)
    assertNull(movieTvCastItemResponse.profilePath)
    assertNull(movieTvCastItemResponse.id)
    assertNull(movieTvCastItemResponse.adult)
    assertNull(movieTvCastItemResponse.order)
  }

  @Test
  fun movieTvCastItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val movieTvCastItemResponse = MovieTvCastItemResponse(
      name = "Martin Freeman"
    )
    assertEquals("Martin Freeman", movieTvCastItemResponse.name)
    assertNull(movieTvCastItemResponse.originalName)
  }
}
