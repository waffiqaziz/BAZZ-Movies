package com.waffiq.bazz_movies.feature.search.utils

import com.waffiq.bazz_movies.feature.search.domain.model.KnownForItem
import junit.framework.TestCase.assertEquals
import org.junit.Test

class SearchHelperTest {

  @Test
  fun `getKnownFor should return concatenated titles`() {
    val knownForItems = listOf(
      KnownForItem(title = "Movie 1"),
      KnownForItem(title = "Movie 2"),
      KnownForItem(title = "Movie 3")
    )

    val result = SearchHelper.getKnownFor(knownForItems)

    // verify the result is correctly concatenated
    assertEquals("Movie 1, Movie 2, Movie 3", result)
  }

  @Test
  fun `getKnownFor should return empty string for empty list`() {
    val result = SearchHelper.getKnownFor(emptyList())

    // verify the result is an empty string
    assertEquals("", result)
  }

  @Test
  fun `getKnownFor should handle null titles gracefully`() {
    // prepare mock KnownForItem list with null titles
    val knownForItems = listOf(
      KnownForItem(title = "Movie 1"),
      KnownForItem(title = null),
      KnownForItem(title = "Movie 3")
    )

    val result = SearchHelper.getKnownFor(knownForItems)

    // verify the result skips null titles
    assertEquals("Movie 1, Movie 3", result)
  }
}