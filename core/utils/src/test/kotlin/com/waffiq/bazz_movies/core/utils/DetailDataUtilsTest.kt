package com.waffiq.bazz_movies.core.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_BACKDROP_W300
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.dateOf
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.imageSource
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.nameHandler
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
  fun contextTitleHandler_allValid_returnsName() {
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
  fun contextTitleHandler_nameNull_returnsTitle() {
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
  fun contextTitleHandler_nameAndTitleAreNull_returnsOriginalTitle() {
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
  fun contextTitleHandler_onlyOriginalNameValid_returnsOriginalName() {
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
  fun contextTitleHandler_allTitleNull_returnsNotAvailable() {
    val result =
      context.titleHandler(
        MediaItem(name = null, title = null, originalTitle = null, originalName = null)
      )
    assertEquals("N/A", result)
  }

  @Test
  fun titleHandler_nameValid_returnsName() {
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
  fun titleHandler_nameIsNull_returnsTitle() {
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
  fun titleHandler_nameAndTitleAreNull_returnsOriginalTitle() {
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
  fun titleHandler_whenAllNullExceptOriginalName_returnsOriginalName() {
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
  fun titleHandler_whenAllNull_returnsItem() {
    val result =
      titleHandler(
        MediaItem(name = null, title = null, originalTitle = null, originalName = null)
      )
    assertEquals("Item", result)
  }

  @Test
  fun nameHandler_whenAllNameAvailable_returnsName() {
    val result = nameHandler(MediaItem(name = "name", originalName = "original name"))
    assertEquals("name", result)
  }

  @Test
  fun nameHandler_whenOriginalNameNull_returnsName() {
    val result = nameHandler(MediaItem(name = "name", originalName = null))
    assertEquals("name", result)
  }

  @Test
  fun nameHandler_whenNameIsNull_returnsOriginalName() {
    val result = nameHandler(MediaItem(name = null, originalName = "original name"))
    assertEquals("original name", result)
  }

  @Test
  fun nameHandler_whenAllNull_returnsItem() {
    val result = nameHandler(MediaItem())
    assertEquals("Item", result)
  }

  @Test
  fun releaseDateHandler_allDateValid_showsDateCorrectly() {
    val result = context.releaseDateHandler(
      MediaItem(
        firstAirDate = "2007-06-27",
        releaseDate = "2007-06-27",
      )
    )
    assertEquals("Jun 27, 2007", result)
  }

  @Test
  fun releaseDateHandler_firstAirDateNull_showsDateFromReleaseDate() {
    val result = context.releaseDateHandler(
      MediaItem(
        releaseDate = "2007-06-27",
        firstAirDate = null,
      )
    )
    assertEquals("Jun 27, 2007", result)
  }

  @Test
  fun releaseDateHandler_releaseDateNull_showsDateFromFirstAirDate() {
    val result = context.releaseDateHandler(
      MediaItem(
        releaseDate = null,
        firstAirDate = "2007-06-27",
      )
    )
    assertEquals("Jun 27, 2007", result)
  }

  @Test
  fun releaseDateHandler_allDateNull_showsNotAvailable() {
    val result = context.releaseDateHandler(
      MediaItem(
        releaseDate = null,
        firstAirDate = null,
      )
    )
    assertEquals("N/A", result)
  }

  @Test
  fun releaseDateHandler_allDateInvalid_showsNotAvailable() {
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
  fun toUsd_validData_returnsCorrectCurrency() {
    assertEquals("$35,000,000.00", toUsd(35000000))
  }

  @Test
  fun imageSource_whenImageFieldAvailable_returnsBackdrop() {
    val data = MediaItem(backdropPath = "backdrop", posterPath = "poster")
    assertEquals(TMDB_IMG_LINK_BACKDROP_W300 + "backdrop", data.imageSource)
  }

  @Test
  fun imageSource_whenPathsAreMissing_returnsCorrectValue() {
    // all missing
    val data1 = MediaItem()
    assertEquals(ic_backdrop_error, data1.imageSource)

    // poster is null
    val data2 = MediaItem(backdropPath = "backdrop", posterPath = null)
    assertEquals(TMDB_IMG_LINK_BACKDROP_W300 + "backdrop", data2.imageSource)

    // backdrop is null
    val data3 = MediaItem(backdropPath = null, posterPath = "poster")
    assertEquals(TMDB_IMG_LINK_BACKDROP_W300 + "poster", data3.imageSource)

    // poster is empty
    val data4 = MediaItem(backdropPath = "backdrop", posterPath = "")
    assertEquals(TMDB_IMG_LINK_BACKDROP_W300 + "backdrop", data4.imageSource)

    // backdrop is empty
    val data5 = MediaItem(backdropPath = "", posterPath = "poster")
    assertEquals(TMDB_IMG_LINK_BACKDROP_W300 + "poster", data5.imageSource)

    // all empty
    val data6 = MediaItem(backdropPath = "", posterPath = "")
    assertEquals(ic_backdrop_error, data6.imageSource)
  }

  @Test
  fun dateOf_whenAllDateFieldAvailable_returnsReleaseDate() {
    val data = context.dateOf(MediaItem(releaseDate = "2023-05-15", firstAirDate = "2010-10-10"))
    assertEquals("2023-05-15", data)
  }

  @Test
  fun dateOf_whenDateIsMissing_returnsCorrectValue() {
    // all date is null
    val data1 = context.dateOf(MediaItem())
    assertEquals("N/A", data1)

    // releaseDate is null
    val data2 = context.dateOf(MediaItem(firstAirDate = "2023-05-15"))
    assertEquals("2023-05-15", data2)

    // releaseDate is empty
    val data3 = context.dateOf(MediaItem(releaseDate = "", firstAirDate = "2023-05-15"))
    assertEquals("2023-05-15", data3)

    // releaseDate is blank
    val data4 = context.dateOf(MediaItem(releaseDate = " ", firstAirDate = "2023-05-15"))
    assertEquals("2023-05-15", data4)

    // firstAirDate is null
    val data5 = context.dateOf(MediaItem(firstAirDate = "2023-05-15"))
    assertEquals("2023-05-15", data5)

    // firstAirDate is empty
    val data6 = context.dateOf(MediaItem(firstAirDate = "", releaseDate = "2023-05-15"))
    assertEquals("2023-05-15", data6)

    // firstAirDate is blank
    val data7 = context.dateOf(MediaItem(firstAirDate = " ", releaseDate = "2023-05-15"))
    assertEquals("2023-05-15", data7)
  }
}
