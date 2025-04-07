package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.personDump1
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class MultiSearchResponseTest {

  @Test
  fun multiSearchResponse_withValidValues_setsPropertiesCorrectly() {
    val multiSearchResponse = MultiSearchResponse(
      page = 1,
      totalPages = 20,
      results = listOf(personDump1),
      totalResults = 1
    )
    assertEquals(1, multiSearchResponse.page)
    assertEquals(1, multiSearchResponse.totalResults)
    assertEquals(20, multiSearchResponse.totalPages)
    assertEquals("Dwayne Johnson", multiSearchResponse.results?.get(0)?.name)
  }

  @Test
  fun multiSearchResponse_withDefaultValues_setsPropertiesCorrectly() {
    val multiSearchResponse = MultiSearchResponse()
    assertNull(multiSearchResponse.page)
    assertNull(multiSearchResponse.totalResults)
    assertNull(multiSearchResponse.totalPages)
    assertNull(multiSearchResponse.results)
  }

  @Test
  fun multiSearchResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val multiSearchResponse = MultiSearchResponse(
      page = 1,
      totalPages = 1
    )
    assertEquals(1, multiSearchResponse.page)
    assertEquals(1, multiSearchResponse.totalPages)
    assertNull(multiSearchResponse.results)
    assertNull(multiSearchResponse.totalResults)
  }
}
