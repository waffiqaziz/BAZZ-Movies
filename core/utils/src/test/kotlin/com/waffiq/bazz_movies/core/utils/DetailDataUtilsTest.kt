package com.waffiq.bazz_movies.core.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.releaseDateHandler
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.toUsd
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DetailDataUtilsTest {
  private val context: Context = ApplicationProvider.getApplicationContext()

  @Test
  fun contextTitleHandler_allValid_returnName() {
    val result =
      context.titleHandler(
        MediaItem(
          name = "Test Name",
          title = "Test Title",
          originalTitle = "Test Original Title",
          originalName = "Test Original Name"
        )
      )
    assertEquals("Test Name", result)
  }

  @Test
  fun contextTitleHandler_nameNull_returnTitle() {
    val result =
      context.titleHandler(
        MediaItem(
          name = null,
          title = "Test Title",
          originalTitle = "Test Original Title",
          originalName = "Test Original Name"
        )
      )
    assertEquals("Test Title", result)
  }

  @Test
  fun contextTitleHandler_nameAndTitleAreNull_returnOriginalTitle() {
    val result =
      context.titleHandler(
        MediaItem(
          name = null,
          title = null,
          originalTitle = "Test Original Title",
          originalName = "Test Original Name"
        )
      )
    assertEquals("Test Original Title", result)
  }

  @Test
  fun contextTitleHandler_onlyOriginalNameValid_returnOriginalName() {
    val result =
      context.titleHandler(
        MediaItem(
          name = null,
          title = null,
          originalTitle = null,
          originalName = "Test Original Name"
        )
      )
    assertEquals("Test Original Name", result)
  }

  @Test
  fun contextTitleHandler_allTitleNull_returnNotAvailable() {
    val result =
      context.titleHandler(
        MediaItem(name = null, title = null, originalTitle = null, originalName = null)
      )
    assertEquals("N/A", result)
  }

  @Test
  fun titleHandler_nameValid_returnName() {
    val result =
      titleHandler(
        MediaItem(
          name = "Test Name",
          title = "Test Title",
          originalTitle = "Test Original Title",
          originalName = "Test Original Name"
        )
      )
    assertEquals("Test Name", result)
  }

  @Test
  fun titleHandler_nameIsNull_returnTitle() {
    val result =
      titleHandler(
        MediaItem(
          name = null,
          title = "Test Title",
          originalTitle = "Test Original Title",
          originalName = "Test Original Name"
        )
      )
    assertEquals("Test Title", result)
  }

  @Test
  fun titleHandler_nameAndTitleAreNull_returnOriginalTitle() {
    val result =
      titleHandler(
        MediaItem(
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
    val result =
      titleHandler(
        MediaItem(
          name = null,
          title = null,
          originalTitle = null,
          originalName = "Test Original Name"
        )
      )
    assertEquals("Test Original Name", result)
  }

  @Test
  fun titleHandler_returnNotAvailable_whenAllTitleFieldsAreNull() {
    val result =
      titleHandler(
        MediaItem(name = null, title = null, originalTitle = null, originalName = null)
      )
    assertEquals("Item", result)
  }

  @Test
  fun releaseDateHandler_allDateValid_showDateCorrectly() {
    val result = context.releaseDateHandler(
      MediaItem(
        firstAirDate = "2007-06-27",
        releaseDate = "2007-06-27",
      )
    )
    assertEquals("Jun 27, 2007", result)
  }

  @Test
  fun releaseDateHandler_firstAirDateNull_showDateFromReleaseDate() {
    val result = context.releaseDateHandler(
      MediaItem(
        releaseDate = "2007-06-27",
        firstAirDate = null,
      )
    )
    assertEquals("Jun 27, 2007", result)
  }

  @Test
  fun releaseDateHandler_releaseDateNull_showDateFromFirstAirDate() {
    val result = context.releaseDateHandler(
      MediaItem(
        releaseDate = null,
        firstAirDate = "2007-06-27",
      )
    )
    assertEquals("Jun 27, 2007", result)
  }

  @Test
  fun releaseDateHandler_allDateNull_showNotAvailable() {
    val result = context.releaseDateHandler(
      MediaItem(
        releaseDate = null,
        firstAirDate = null,
      )
    )
    assertEquals("N/A", result)
  }

  @Test
  fun releaseDateHandler_allDateInvalid_showNotAvailable() {
    val result = context.releaseDateHandler(
      MediaItem(
        releaseDate = "invalid date",
        firstAirDate = "  ",
      )
    )
    assertEquals("N/A", result)
  }


  @Test
  fun toUsd_invalidData_returnsCorrectly() {
    assertEquals("-", toUsd(null))
    assertEquals("-", toUsd(0))
    assertEquals("-", toUsd(0L))
    assertEquals("-", toUsd(0.0))
    assertEquals("-", toUsd(-200))
    assertEquals("-", toUsd(-200.0))
    assertEquals("-", toUsd(-200L))
  }

  @Test
  fun toUsd_validData_returnsCorrectly() {
    assertEquals("$35,000,000.00", toUsd(35000000))
  }
}
