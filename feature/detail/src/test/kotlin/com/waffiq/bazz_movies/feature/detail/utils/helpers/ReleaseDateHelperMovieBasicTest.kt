package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDates
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItemValue
import com.waffiq.bazz_movies.feature.detail.testutils.BaseReleaseDateHelperMovieTest
import org.junit.Test

/**
 * This test to testing ReleaseDateHelperMovieBasicTest core function
 */
class ReleaseDateHelperMovieBasicTest : BaseReleaseDateHelperMovieTest() {

  @Test
  fun getReleaseDateRegion_matchUserRegion_returnsCorrectly() {
    val data = movieWithRelease(
      releaseRegion = "US",
      releaseDate = "2023-10-20T00:00:00.000Z",
      productionCountries = listOf("GB"),
      fallbackDate = "2022-01-01"
    )
    checkMovieReleaseDate(data, "US", "US", "Oct 20, 2023")
  }

  @Test
  fun getReleaseDateRegion_noValidRegion_usesProductionCountry() {
    val data = movieWithRelease(
      releaseRegion = "FR",
      releaseDate = null,
      productionCountries = listOf("DE"),
      fallbackDate = "2022-05-01"
    )
    checkMovieReleaseDate(data, "US", "DE", "May 01, 2022")
  }

  @Test
  fun getReleaseDateRegion_matchingRegionButEmptyDate_usesProductionCountry() {
    val data = movieWithRelease(
      releaseRegion = "US",
      releaseDate = "",
      productionCountries = listOf("GB"),
      fallbackDate = "2022-07-01"
    )
    checkMovieReleaseDate(data, "US", "GB", "Jul 01, 2022")
  }

  @Test
  fun getReleaseDateRegion_withNoRegionOrProduction_usesAnyValidRegion() {
    val data = movieWithRelease(
      releaseRegion = "JP",
      releaseDate = "2021-08-15T00:00:00.000Z",
      productionCountries = emptyList(),
      fallbackDate = ""
    )
    checkMovieReleaseDate(data, "IT", "JP", "Aug 15, 2021")
  }

  @Test
  fun getReleaseDateRegion_withNullData_returnsEmpty() {
    checkMovieReleaseDate(null, "US", "", "")
  }

  @Test
  fun getReleaseDateRegion_withNoValidRegionsAtAll_returnsEmptyResult() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "US",
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(releaseDate = "")
            )
          )
        )
      ),
      listProductionCountriesItem = listOf(ProductionCountriesItem("")),
      releaseDate = ""
    )
    checkMovieReleaseDate(data, "US", "", "")
  }
}
