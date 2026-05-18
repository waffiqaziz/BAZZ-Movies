package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.testutils.BaseReleaseDateHelperMovieTest
import org.junit.Test

class ReleaseDateHelperMovieProductionCountryTest : BaseReleaseDateHelperMovieTest() {

  @Test
  fun getReleaseDateRegion_withNullProductionCountriesItem_usesAnyValidRegion() {
    checkUsesAnyValidRegion(null)
  }

  @Test
  fun getReleaseDateRegion_withEmptyProductionCountriesItem_usesAnyValidRegion() {
    checkUsesAnyValidRegion(emptyList())
  }

  @Test
  fun getReleaseDateRegion_withProductionCountriesMixedNullAndEmptyIso_usesAnyValidRegion() {
    checkUsesAnyValidRegion(
      listOf(
        null,
        ProductionCountriesItem(""),
        ProductionCountriesItem(null),
      ),
    )
  }

  @Test
  fun getReleaseDateRegion_withNullItemInProductionCountries_usesAnyValidRegion() {
    checkUsesAnyValidRegion(
      listOf(
        null,
        ProductionCountriesItem(""),
      ),
    )
  }

  @Test
  fun getReleaseDateRegion_withProductionCountriesAllEmptyIso_usesAnyValidRegion() {
    val data = movieDetail(
      releaseRegion = "JP",
      releaseDate = "2022-09-10T00:00:00.000Z",
      productionCountries = listOf(
        ProductionCountriesItem(""),
        ProductionCountriesItem(""),
      ),
    )

    checkMovieReleaseDate(data, "US", "JP", "Sep 10, 2022")
  }

  @Test
  fun getReleaseDateRegion_withValidProductionCountryButEmptyRegionRelease_usesProductionCountry() {
    val data = movieDetail(
      releaseRegion = "FR",
      releaseDate = "",
      productionCountries = listOf(
        ProductionCountriesItem("GB"),
      ),
      fallbackReleaseDate = "2022-07-01",
    )

    checkMovieReleaseDate(data, "US", "GB", "Jul 01, 2022")
  }

  private fun checkUsesAnyValidRegion(productionCountries: List<ProductionCountriesItem?>?) {
    val data = movieDetail(
      releaseRegion = "FR",
      releaseDate = "2023-12-01T00:00:00.000Z",
      productionCountries = productionCountries,
    )

    checkMovieReleaseDate(data, "US", "FR", "Dec 01, 2023")
  }
}
