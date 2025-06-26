package com.waffiq.bazz_movies.feature.search.utils

import com.waffiq.bazz_movies.feature.search.domain.model.KnownForItem
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.getKnownFor
import junit.framework.TestCase.assertEquals
import org.junit.Test

class SearchHelperTest {

  @Test
  fun getKnownFor_whenCalled_returnsConcatenatedTitles() {
    val knownForItems = listOf(
      KnownForItem(title = "Movie 1"),
      KnownForItem(title = "Movie 2"),
      KnownForItem(title = "Movie 3")
    )

    val result = getKnownFor(knownForItems)
    assertEquals("Movie 1, Movie 2, Movie 3", result)
  }

  @Test
  fun getKnownFor_whenListIsEmpty_returnsEmptyString() {
    val result = getKnownFor(emptyList())
    assertEquals("", result)
  }

  @Test
  fun getKnownFor_whenSomeTitlesAreNull_ignoresNullsAndJoinsOthers() {
    val knownForItems = listOf(
      KnownForItem(title = "Movie 1"),
      KnownForItem(title = null),
      KnownForItem(title = "Movie 3")
    )

    val result = getKnownFor(knownForItems)
    assertEquals("Movie 1, Movie 3", result)
  }

  @Test
  fun getKnownFor_whenUsingNameField_ignoresNullsAndJoinsValidNames() {
    val knownForItems = listOf(
      KnownForItem(name = "Movie 2"),
      KnownForItem(name = null),
      KnownForItem(name = "Movie 4")
    )

    val result = getKnownFor(knownForItems)
    assertEquals("Movie 2, Movie 4", result)
  }

  @Test
  fun getKnownFor_whenUsingOriginalNameField_ignoresNullsAndJoinsValidNames() {
    val knownForItems = listOf(
      KnownForItem(originalName = "Movie 2"),
      KnownForItem(originalName = null),
      KnownForItem(originalName = "Movie 4")
    )

    val result = getKnownFor(knownForItems)
    assertEquals("Movie 2, Movie 4", result)
  }
}
