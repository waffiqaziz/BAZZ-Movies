package com.waffiq.bazz_movies.core.models

import junit.framework.TestCase.assertEquals
import org.junit.Test

class SearchHistoryTest {

  @Test
  fun favorite_withValidValue_returnsCorrectData() {
    val searchHistory = SearchHistory(888, "query test", 88L)
    assertEquals(888, searchHistory.id)
    assertEquals("query test", searchHistory.query)
    assertEquals(88L, searchHistory.createdAt)
  }

  @Test
  fun favorite_defaultValue_returnsCorrectData() {
    val searchHistory = SearchHistory(query = "query test default", createdAt = 999L)
    assertEquals(1, searchHistory.id)
    assertEquals("query test default", searchHistory.query)
    assertEquals(999L, searchHistory.createdAt)
  }
}
