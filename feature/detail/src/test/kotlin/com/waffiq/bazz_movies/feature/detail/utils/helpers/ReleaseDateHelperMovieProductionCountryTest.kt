package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDates
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItemValue
import com.waffiq.bazz_movies.feature.detail.testutils.BaseReleaseDateHelperMovieTest
import org.junit.Test

class ReleaseDateHelperMovieProductionCountryTest : BaseReleaseDateHelperMovieTest() {

  @Test
  fun getReleaseDateRegion_withNullProductionCountriesItem_usesAnyValidRegion() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "FR",
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(releaseDate = "2023-12-01T00:00:00.000Z")
            )
          )
        )
      ),
      listProductionCountriesItem = null,
      releaseDate = ""
    )
    checkMovieReleaseDate(data, "US", "FR", "Dec 01, 2023")
  }

  @Test
  fun getReleaseDateRegion_withEmptyProductionCountriesItem_usesAnyValidRegion() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "FR",
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(releaseDate = "2023-12-01T00:00:00.000Z")
            )
          )
        )
      ),
      listProductionCountriesItem = emptyList(),
      releaseDate = ""
    )
    checkMovieReleaseDate(data, "US", "FR", "Dec 01, 2023")
  }

  @Test
  fun getReleaseDateRegion_withProductionCountriesAllEmptyIso_usesAnyValidRegion() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "JP",
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(releaseDate = "2022-09-10T00:00:00.000Z")
            )
          )
        )
      ),
      listProductionCountriesItem = listOf(
        ProductionCountriesItem(""),
        ProductionCountriesItem("")
      ),
      releaseDate = ""
    )
    checkMovieReleaseDate(data, "US", "JP", "Sep 10, 2022")
  }

  @Test
  fun getReleaseDateRegion_withProductionCountriesMixedNullAndEmptyIso_usesAnyValidRegion() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "FR",
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(releaseDate = "2023-12-01T00:00:00.000Z")
            )
          )
        )
      ),
      listProductionCountriesItem = listOf(
        null,
        ProductionCountriesItem(""),
        ProductionCountriesItem(null)
      ),
      releaseDate = ""
    )
    checkMovieReleaseDate(data, "US", "FR", "Dec 01, 2023")
  }

  @Test
  fun getReleaseDateRegion_withNullItemInProductionCountries_usesAnyValidRegion() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "FR",
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(releaseDate = "2023-12-01T00:00:00.000Z")
            )
          )
        )
      ),
      listProductionCountriesItem = listOf(
        null,
        ProductionCountriesItem("")
      ),
      releaseDate = ""
    )
    checkMovieReleaseDate(data, "US", "FR", "Dec 01, 2023")
  }

  @Test
  fun getReleaseDateRegion_withValidProductionCountryButEmptyRegionRelease_usesProductionCountry() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "FR",
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(releaseDate = "")
            )
          )
        )
      ),
      listProductionCountriesItem = listOf(ProductionCountriesItem("GB")),
      releaseDate = "2022-07-01"
    )
    checkMovieReleaseDate(data, "US", "GB", "Jul 01, 2022")
  }
}
