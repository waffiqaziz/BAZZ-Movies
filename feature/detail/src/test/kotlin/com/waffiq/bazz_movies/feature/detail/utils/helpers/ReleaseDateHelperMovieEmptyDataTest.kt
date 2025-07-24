package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDates
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItemValue
import com.waffiq.bazz_movies.feature.detail.testutils.ReleaseDateHelperMovieTestBase
import org.junit.Test

class ReleaseDateHelperMovieEmptyDataTest : ReleaseDateHelperMovieTestBase() {


  @Test
  fun getReleaseDateRegion_withEmptyIso31661_usesProductionCountry() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "",
            listReleaseDatesitemValue = listOf(
              ReleaseDatesItemValue(releaseDate = "2023-12-01T00:00:00.000Z")
            )
          )
        )
      ),
      listProductionCountriesItem = listOf(ProductionCountriesItem("GB")),
      releaseDate = "2022-07-01"
    )
    checkMovieReleaseDate(data, "US", "GB", "Jul 01, 2022")
  }

  @Test
  fun getReleaseDateRegion_noMatchingEmptyProductionAndReleaseDate_usesAnyValidRegion() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "JP",
            listReleaseDatesitemValue = listOf(
              ReleaseDatesItemValue(releaseDate = "2022-09-10T00:00:00.000Z")
            )
          )
        )
      ),
      listProductionCountriesItem = listOf(
        ProductionCountriesItem(""),
        ProductionCountriesItem(null)
      ),
      releaseDate = ""
    )
    checkMovieReleaseDate(data, "US", "JP", "Sep 10, 2022")
  }

  @Test
  fun getReleaseDateRegion_withProductionCountryButNullFallbackDate_usesAnyValidRegion() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "FR",
            listReleaseDatesitemValue = listOf(
              ReleaseDatesItemValue(releaseDate = "2021-11-01T00:00:00.000Z")
            )
          )
        )
      ),
      listProductionCountriesItem = listOf(ProductionCountriesItem("GB")),
      releaseDate = null
    )
    checkMovieReleaseDate(data, "US", "FR", "Nov 01, 2021")
  }

  @Test
  fun getReleaseDateRegion_withProductionCountryButEmptyRegionRelease_usesAnyValidRegion() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "FR",
            listReleaseDatesitemValue = listOf(
              ReleaseDatesItemValue(releaseDate = "2021-11-01T00:00:00.000Z")
            )
          )
        )
      ),
      listProductionCountriesItem = listOf(ProductionCountriesItem("")),
      releaseDate = "2022-07-01"
    )
    checkMovieReleaseDate(data, "US", "FR", "Nov 01, 2021")
  }
}
