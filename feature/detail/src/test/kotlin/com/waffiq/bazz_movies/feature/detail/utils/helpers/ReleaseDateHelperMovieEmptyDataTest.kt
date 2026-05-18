package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.testutils.BaseReleaseDateHelperMovieTest
import org.junit.Test

class ReleaseDateHelperMovieEmptyDataTest : BaseReleaseDateHelperMovieTest() {

  @Test
  fun getReleaseDateRegion_withEmptyIso31661_usesProductionCountry() {
    val data = movieDetail(
      releaseDates = listOf(
        releaseDateItem("", "2023-12-01T00:00:00.000Z"),
      ),
      productionCountries = listOf(ProductionCountriesItem("GB")),
      releaseDate = "2022-07-01",
    )

    checkMovieReleaseDate(data, "US", "GB", "Jul 01, 2022")
  }

  @Test
  fun getReleaseDateRegion_noMatchingEmptyProductionAndReleaseDate_usesAnyValidRegion() {
    val data = movieDetail(
      releaseDates = listOf(
        releaseDateItem("JP", "2022-09-10T00:00:00.000Z"),
      ),
      productionCountries = listOf(
        ProductionCountriesItem(""),
        ProductionCountriesItem(null),
      ),
      releaseDate = "",
    )

    checkMovieReleaseDate(data, "US", "JP", "Sep 10, 2022")
  }

  @Test
  fun getReleaseDateRegion_withProductionCountryButNullFallbackDate_usesAnyValidRegion() {
    val data = movieDetail(
      releaseDates = listOf(
        releaseDateItem("FR", "2021-11-01T00:00:00.000Z"),
      ),
      productionCountries = listOf(
        ProductionCountriesItem("GB"),
      ),
      releaseDate = null,
    )

    checkMovieReleaseDate(data, "US", "FR", "Nov 01, 2021")
  }

  @Test
  fun getReleaseDateRegion_withProductionCountryButEmptyRegionRelease_usesAnyValidRegion() {
    val data = movieDetail(
      releaseDates = listOf(
        releaseDateItem("FR", "2021-11-01T00:00:00.000Z"),
      ),
      productionCountries = listOf(
        ProductionCountriesItem(""),
      ),
      releaseDate = "2022-07-01",
    )

    checkMovieReleaseDate(data, "US", "FR", "Nov 01, 2021")
  }
}
