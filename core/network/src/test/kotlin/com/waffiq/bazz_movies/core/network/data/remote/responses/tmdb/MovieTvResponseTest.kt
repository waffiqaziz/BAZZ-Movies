package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump1
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MovieTvResponseTest {

  @Test
  fun movieTvResponse_withValidValues_setsPropertiesCorrectly() {
    val movieTvResponse = MovieTvResponse(
      page = 1,
      totalPages = 100,
      results = listOf(movieDump1),
      totalResults = 3,
    )
    assertEquals(1, movieTvResponse.page)
    assertEquals(100, movieTvResponse.totalPages)
    assertEquals("The Shawshank Redemption", movieTvResponse.results[0].title)
    assertEquals(3, movieTvResponse.totalResults)
  }
}
