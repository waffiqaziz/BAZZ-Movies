package com.waffiq.bazz_movies.feature.search.utils

import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error
import com.waffiq.bazz_movies.feature.search.domain.model.KnownForItem
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.getKnownFor
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.profileImageSource
import junit.framework.TestCase.assertEquals
import org.junit.Test

class SearchHelperTest {

  @Test
  fun getKnownFor_whenCalled_returnsConcatenatedTitles() {
    val knownForItems = listOf(
      KnownForItem(title = "Movie 1"),
      KnownForItem(title = "Movie 2"),
      KnownForItem(title = "Movie 3"),
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
      KnownForItem(title = "Movie 3"),
    )

    val result = getKnownFor(knownForItems)
    assertEquals("Movie 1, Movie 3", result)
  }

  @Test
  fun getKnownFor_whenUsingNameField_ignoresNullsAndJoinsValidNames() {
    val knownForItems = listOf(
      KnownForItem(name = "Movie 2"),
      KnownForItem(name = null),
      KnownForItem(name = "Movie 4"),
    )

    val result = getKnownFor(knownForItems)
    assertEquals("Movie 2, Movie 4", result)
  }

  @Test
  fun getKnownFor_whenUsingOriginalNameField_ignoresNullsAndJoinsValidNames() {
    val knownForItems = listOf(
      KnownForItem(originalName = "Movie 2"),
      KnownForItem(originalName = null),
      KnownForItem(originalName = "Movie 4"),
    )

    val result = getKnownFor(knownForItems)
    assertEquals("Movie 2, Movie 4", result)
  }

  @Test
  fun profileImageSource_whenProfileIsAvailable_returnsProfileURL() {
    val data = MultiSearchItem(profilePath = "profile", id = 1)
    val result = data.profileImageSource
    assertEquals(TMDB_IMG_LINK_POSTER_W185 + "profile", result)
  }

  @Test
  fun profileImageSource_whenProfileIsMissing_returnsFallback() {
    // profile is null
    val data1 = MultiSearchItem(profilePath = null, id = 1)
    assertEquals(ic_backdrop_error, data1.profileImageSource)

    // profile is empty
    val data2 = MultiSearchItem(profilePath = "", id = 1)
    assertEquals(ic_backdrop_error, data2.profileImageSource)

    // profile is blank
    val data3 = MultiSearchItem(profilePath = " ", id = 1)
    assertEquals(ic_backdrop_error, data3.profileImageSource)
  }
}
