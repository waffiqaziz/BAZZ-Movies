package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.castcrew

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieTvCastItemResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieTvCrewItemResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class MovieTvCreditsResponseTest {

  @Test
  fun movieTvCreditsResponse_withValidValues_setsPropertiesCorrectly() {
    val movieTvCreditsResponse = MovieTvCreditsResponse(
      crew = listOf(movieTvCrewItemResponseDump),
      id = 543977290,
      cast = listOf(movieTvCastItemResponseDump)
    )
    assertEquals("Alexa Goodall", movieTvCreditsResponse.cast[0].name)
    assertEquals(543977290, movieTvCreditsResponse.id)
    assertEquals("Frank Schlegel", movieTvCreditsResponse.crew[0].name)
  }

  @Test
  fun movieTvCreditsResponse_withDefaultValues_setsPropertiesCorrectly() {
    val movieTvCreditsResponse = MovieTvCreditsResponse(
      cast = emptyList(),
      crew = emptyList()
    )
    assertNull(movieTvCreditsResponse.id)
  }

  @Test
  fun movieTvCreditsResponse_withSomeEmptyValues_setsPropertiesCorrectly() {
    val movieTvCreditsResponse = MovieTvCreditsResponse(
      id = 2,
      cast = emptyList(),
      crew = emptyList()
    )
    assertEquals(2, movieTvCreditsResponse.id)
  }
}
