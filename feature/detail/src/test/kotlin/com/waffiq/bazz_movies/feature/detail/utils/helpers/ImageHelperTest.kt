package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W300
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W500
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error_filled
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_no_profile_rounded
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCrewItem
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ImageHelper.backdropOriginalSource
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ImageHelper.backdropPathSource
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ImageHelper.isBackdropNotAvailable
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ImageHelper.posterDetailSource
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ImageHelper.profileImageSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ImageHelperTest {

  @Test
  fun backdropPathSource_whenAllPathAvailable_returnsBackdrop() {
    val data = MediaItem(backdropPath = "backdrop", posterPath = "poster")
    assertEquals("backdrop", data.backdropPathSource)
  }

  @Test
  fun backdropPathSource_whenBackdropPathMissing_returnsBackdrop() {
    val data = MediaItem(backdropPath = "backdrop", posterPath = "poster")
    assertEquals("backdrop", data.backdropPathSource)
  }

  @Test
  fun backdropOriginalSource_whenAllPathAvailable_returnsCorrectly() {
    // all null, return empty
    val data1 = MediaItem()
    assertEquals("", data1.backdropPathSource)

    // all N/A value, return empty
    val data2 = MediaItem(backdropPath = "N/A", posterPath = "N/A")
    assertEquals("", data2.backdropPathSource)

    // all empty value
    val data3 = MediaItem(backdropPath = "", posterPath = "")
    assertEquals("", data3.backdropPathSource)

    // all blank
    val data4 = MediaItem(backdropPath = " ", posterPath = " ")
    assertEquals("", data4.backdropPathSource)

    // backdrop null
    val data5 = MediaItem(posterPath = "poster")
    assertEquals("poster", data5.backdropPathSource)

    // backdrop empty
    val data6 = MediaItem(backdropPath = "", posterPath = "poster")
    assertEquals("poster", data6.backdropPathSource)

    // backdrop blank
    val data7 = MediaItem(backdropPath = " ", posterPath = "poster")
    assertEquals("poster", data7.backdropPathSource)
  }

  @Test
  fun backdropOriginalSource_whenBackdropPathMissing_returnsCorrectValue() {
    // all null
    val data1 = MediaItem()
    assertEquals(ic_backdrop_error_filled, data1.backdropOriginalSource)

    // all N/A value
    val data2 = MediaItem(backdropPath = "N/A", posterPath = "N/A")
    assertEquals(ic_backdrop_error_filled, data2.backdropOriginalSource)

    // all empty value
    val data3 = MediaItem(backdropPath = "", posterPath = "")
    assertEquals(ic_backdrop_error_filled, data3.backdropOriginalSource)

    // all blank
    val data4 = MediaItem(backdropPath = " ", posterPath = " ")
    assertEquals(ic_backdrop_error_filled, data4.backdropOriginalSource)

    // backdrop null
    val data5 = MediaItem(posterPath = "poster")
    assertEquals(TMDB_IMG_LINK_POSTER_W500 + "poster", data5.backdropOriginalSource)

    // backdrop empty
    val data6 = MediaItem(backdropPath = "", posterPath = "poster")
    assertEquals(TMDB_IMG_LINK_POSTER_W500 + "poster", data6.backdropOriginalSource)

    // backdrop blank
    val data7 = MediaItem(backdropPath = " ", posterPath = "poster")
    assertEquals(TMDB_IMG_LINK_POSTER_W500 + "poster", data7.backdropOriginalSource)
  }

  @Test
  fun isBackdropNotAvailable_whenPathIsAvailable_returnsFalse() {
    val data = MediaItem(backdropPath = "backdrop")
    assertFalse(data.isBackdropNotAvailable)
  }

  @Test
  fun isBackdropNotAvailable_whenPathIsNotAvailable_returnsTrue() {
    // backdrop null
    assertTrue(MediaItem(backdropPath = null).isBackdropNotAvailable)

    // backdrop blank
    assertTrue(MediaItem(backdropPath = " ").isBackdropNotAvailable)

    // backdrop empty
    assertTrue(MediaItem(backdropPath = "").isBackdropNotAvailable)

    // backdrop N/A
    assertTrue(MediaItem(backdropPath = "N/A").isBackdropNotAvailable)
  }

  @Test
  fun posterDetailSource_whenPosterPathIsAvailable_returnsPoster() {
    val data = MediaItem(posterPath = "poster")
    assertEquals(TMDB_IMG_LINK_POSTER_W500 + "poster", data.posterDetailSource)
  }

  @Test
  fun posterDetailSource_whenPosterPathIsMissing_returnsDrawable() {
    // null
    assertEquals(ic_poster_error, MediaItem(posterPath = null).posterDetailSource)

    // empty
    assertEquals(ic_poster_error, MediaItem(posterPath = "").posterDetailSource)

    // blank
    assertEquals(ic_poster_error, MediaItem(posterPath = " ").posterDetailSource)

    // N/A
    assertEquals(ic_poster_error, MediaItem(posterPath = "N/A").posterDetailSource)
  }

  @Test
  fun profileImageSource_whenPathIsAvailable_returnsPoster() {
    val data = MediaCrewItem(profilePath = "path")
    assertEquals(TMDB_IMG_LINK_POSTER_W300 + "path", data.profileImageSource)
  }

  @Test
  fun profileImageSource_whenPathPathMissing_returnsCorrectValue() {
    // all null
    val data1 = MediaCrewItem()
    assertEquals(ic_no_profile_rounded, data1.profileImageSource)

    // all N/A value
    val data2 = MediaCrewItem(profilePath = "N/A", )
    assertEquals(ic_no_profile_rounded, data2.profileImageSource)

    // all empty value
    val data3 = MediaCrewItem(profilePath = "", )
    assertEquals(ic_no_profile_rounded, data3.profileImageSource)

    // all blank
    val data4 = MediaCrewItem(profilePath = " ", )
    assertEquals(ic_no_profile_rounded, data4.profileImageSource)
  }
}
