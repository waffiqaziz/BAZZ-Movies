package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump1
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MediaResponseTest {

  @Test
  fun mediaResponse_withValidValues_setsPropertiesCorrectly() {
    val mediaResponse = MediaResponse(
      page = 1,
      totalPages = 100,
      results = listOf(movieDump1),
      totalResults = 3,
    )
    assertEquals(1, mediaResponse.page)
    assertEquals(100, mediaResponse.totalPages)
    assertEquals("The Shawshank Redemption", mediaResponse.results[0].title)
    assertEquals(3, mediaResponse.totalResults)
  }
}
