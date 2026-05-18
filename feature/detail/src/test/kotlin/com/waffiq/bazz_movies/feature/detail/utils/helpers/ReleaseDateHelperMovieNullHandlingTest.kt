package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDates
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItemValue
import com.waffiq.bazz_movies.feature.detail.testutils.BaseReleaseDateHelperMovieTest
import org.junit.Test

class ReleaseDateHelperMovieNullHandlingTest : BaseReleaseDateHelperMovieTest() {

  @Test
  fun getReleaseDateRegion_withNullReleaseDates_usesProductionCountry() {
    val data = movie(
      releaseDates = null,
      productionCountries = listOf("CA"),
      fallback = "2023-03-15",
    )

    checkMovieReleaseDate(data, "US", "CA", "Mar 15, 2023")
  }

  @Test
  fun getReleaseDateRegion_withNullReleaseDatesList_returnsEmptyResult() {
    val data = movie(
      releaseDates = ReleaseDates(listReleaseDatesItem = null),
      productionCountries = listOf(""),
      fallback = "",
    )

    checkMovieReleaseDate(data, "US", "", "")
  }

  @Test
  fun getReleaseDateRegion_withEmptyReleaseDatesItemList_usesProductionCountry() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = emptyList(),
      ),
      listProductionCountriesItem = listOf(ProductionCountriesItem("CA")),
      releaseDate = "2023-03-15",
    )
    checkMovieReleaseDate(data, "US", "CA", "Mar 15, 2023")
  }

  @Test
  fun getReleaseDateRegion_withNullReleaseItem_usesProductionCountry() {
    val data = movie(
      releaseDatesList = listOf(null),
      productionCountries = listOf("GB"),
      fallback = "2022-07-01",
    )

    checkMovieReleaseDate(data, "US", "GB", "Jul 01, 2022")
  }

  @Test
  fun getReleaseDateRegion_withNullIso31661InReleaseItem_usesProductionCountry() {
    val data = movie(
      releaseDatesList = listOf(
        ReleaseDatesItem(
          iso31661 = null,
          listReleaseDatesItemValue = listOf(
            ReleaseDatesItemValue(releaseDate = "2023-12-01T00:00:00.000Z"),
          ),
        ),
      ),
      productionCountries = listOf("GB"),
      fallback = "2022-07-01",
    )

    checkMovieReleaseDate(data, "US", "GB", "Jul 01, 2022")
  }

  @Test
  fun getReleaseDateRegion_withNullReleaseDate_usesProductionCountry() {
    val data = movie(
      releaseDatesList = listOf(
        ReleaseDatesItem(
          iso31661 = "US",
          listReleaseDatesItemValue = listOf(
            ReleaseDatesItemValue(releaseDate = null),
          ),
        ),
      ),

      productionCountries = listOf("GB"),
      fallback = "2022-07-01",
    )

    checkMovieReleaseDate(data, "US", "GB", "Jul 01, 2022")
  }

  @Test
  fun getReleaseDateRegion_withMatchingRegionButNullReleaseValuesList_usesProductionCountry() {
    val data = movie(
      releaseDatesList = listOf(
        ReleaseDatesItem(
          iso31661 = "US",
          listReleaseDatesItemValue = null,
        ),
      ),
      productionCountries = listOf("GB"),
      fallback = "2022-07-01",
    )

    checkMovieReleaseDate(data, "US", "GB", "Jul 01, 2022")
  }

  @Test
  fun getReleaseDateRegion_withMatchingRegionButEmptyReleaseValuesList_usesProductionCountry() {
    val data = movie(
      releaseDatesList = listOf(
        ReleaseDatesItem(
          iso31661 = "US",
          listReleaseDatesItemValue = emptyList(),
        ),
      ),
      productionCountries = listOf("GB"),
      fallback = "2022-07-01",
    )

    checkMovieReleaseDate(data, "US", "GB", "Jul 01, 2022")
  }
}
