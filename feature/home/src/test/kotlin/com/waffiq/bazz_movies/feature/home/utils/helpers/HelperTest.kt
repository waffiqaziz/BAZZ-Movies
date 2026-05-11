package com.waffiq.bazz_movies.feature.home.utils.helpers

import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_BACKDROP_W780
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error_filled
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.feature.home.utils.helpers.Helper.backdropSource
import com.waffiq.bazz_movies.feature.home.utils.helpers.Helper.rating
import com.waffiq.bazz_movies.feature.home.utils.helpers.Helper.year
import org.junit.Assert.assertEquals
import org.junit.Test

class HelperTest {

  @Test
  fun year_withValidInput_returnsYearCorrectly() {
    // all available
    assertEquals("2022", MediaItem(firstAirDate = "2022-12-12", releaseDate = "2022-12-12").year)

    // mixed value
    assertEquals("2022", MediaItem(firstAirDate = "2022-12-12", releaseDate = null).year)
    assertEquals("", MediaItem(firstAirDate = "2022-12-12", releaseDate = "").year)
    assertEquals("2022", MediaItem(firstAirDate = null, releaseDate = "2022-12-12").year)
    assertEquals("2022", MediaItem(firstAirDate = "", releaseDate = "2022-12-12").year)
    assertEquals("", MediaItem(firstAirDate = "", releaseDate = null).year)
    assertEquals("", MediaItem(firstAirDate = null, releaseDate = "").year)
    assertEquals("N/A", MediaItem(firstAirDate = null, releaseDate = null).year)
  }

  @Test
  fun rating_withValidInput_returnsValueCorrectly() {
    assertEquals(11.0F, 22F.rating)
    assertEquals(0.0F, null.rating)
    assertEquals(0.0F, 0.0F.rating)
  }

  @Test
  fun backdropSource_withValidInput_returnsValueCorrectly() {
    assertEquals(
      TMDB_IMG_LINK_BACKDROP_W780 + "backdrop",
      MediaItem(backdropPath = "backdrop").backdropSource,
    )
    assertEquals(ic_backdrop_error_filled, MediaItem(backdropPath = "").backdropSource)
    assertEquals(ic_backdrop_error_filled, MediaItem(backdropPath = null).backdropSource)
  }
}
