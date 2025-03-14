package com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers

import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.FavWatchlistHelper.titleHandler
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TitleHandlerTest {

  @Test
  fun titleHandler_returnNameWhenAvailable() {
    val result = titleHandler(
      ResultItem(
        name = "Test Name",
        title = "Test Title",
        originalTitle = "Test Original Title",
        originalName = "Test Original Name"
      )
    )
    assertEquals("Test Name", result)
  }

  @Test
  fun titleHandler_returnTitleWhenNameIsNull() {
    val result = titleHandler(
      ResultItem(
        name = null,
        title = "Test Title",
        originalTitle = "Test Original Title",
        originalName = "Test Original Name"
      )
    )
    assertEquals("Test Title", result)
  }

  @Test
  fun titleHandler_returnOriginalTitleWhenNameAndTitleAreNull() {
    val result = titleHandler(
      ResultItem(
        name = null,
        title = null,
        originalTitle = "Test Original Title",
        originalName = "Test Original Name"
      )
    )
    assertEquals("Test Original Title", result)
  }

  @Test
  fun titleHandler_returnOriginalNameWhenOthersNull() {
    val result = titleHandler(
      ResultItem(
        name = null,
        title = null,
        originalTitle = null,
        originalName = "Test Original Name"
      )
    )
    assertEquals("Test Original Name", result)
  }

  @Test
  fun titleHandler_returnDefaultWhenAllTitleFieldsAreNull() {
    val result = titleHandler(
      ResultItem(name = null, title = null, originalTitle = null, originalName = null)
    )
    assertEquals("Item", result)
  }
}
