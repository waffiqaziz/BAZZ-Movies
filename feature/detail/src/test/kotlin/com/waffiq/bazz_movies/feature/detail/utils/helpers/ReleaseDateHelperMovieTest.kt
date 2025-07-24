package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDates
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItemValue
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ReleaseDateHelper.getReleaseDateRegion
import org.junit.Assert.assertEquals
import org.junit.Test

class ReleaseDateHelperMovieTest {

  // region Movie
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
  fun getReleaseDateRegion_withNullItemInProductionCountries_usesAnyValidRegion() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "FR",
            listReleaseDatesitemValue = listOf(
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
  fun getReleaseDateRegion_withNullReleaseDates_usesProductionCountry() {
    val data = MovieDetail(
      releaseDates = null,
      listProductionCountriesItem = listOf(ProductionCountriesItem("CA")),
      releaseDate = "2023-03-15"
    )
    checkMovieReleaseDate(data, "US", "CA", "Mar 15, 2023")
  }

  @Test
  fun getReleaseDateRegion_withEmptyProductionCountry_usesAnyValidRegion() {
    val data = movieWithRelease(
      releaseRegion = "FR",
      releaseDate = "2021-11-01T00:00:00.000Z",
      productionCountries = emptyList(),
      fallbackDate = ""
    )
    checkMovieReleaseDate(data, "US", "FR", "Nov 01, 2021")
  }

  @Test
  fun getReleaseDateRegion_withProductionCountriesAllEmptyIso_usesAnyValidRegion() {
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
        ProductionCountriesItem(""), // all iso31661 are empty
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
            listReleaseDatesitemValue = listOf(
              ReleaseDatesItemValue(releaseDate = "2023-12-01T00:00:00.000Z")
            )
          )
        )
      ),
      listProductionCountriesItem = listOf(
        null,
        ProductionCountriesItem(""), // empty iso31661
        ProductionCountriesItem(null) // another null item
      ),
      releaseDate = ""
    )
    checkMovieReleaseDate(data, "US", "FR", "Dec 01, 2023")
  }

  @Test
  fun getReleaseDateRegion_withValidProductionCountryButEmptyRegionRelease_usesAnyValidRegion() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "FR",
            listReleaseDatesitemValue = listOf(
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


  @Test
  fun getReleaseDateRegion_withNullIso31661InReleaseItem_triggersElvisBranch() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = null,
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
  fun getReleaseDateRegion_withNullReleaseDate_triggersElvisBranch() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "US",
            listReleaseDatesitemValue = listOf(
              ReleaseDatesItemValue(releaseDate = null)
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
  fun getReleaseDateRegion_withEmptyIso31661_triggersIsValidRegionCheck() {
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
  fun getReleaseDateRegion_withMatchingRegionButNullReleaseDate_usesProductionCountry() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "US",
            listReleaseDatesitemValue = listOf(
              ReleaseDatesItemValue(releaseDate = null)
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
  fun getReleaseDateRegion_withMatchingRegionButEmptyReleaseValuesList_usesProductionCountry() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "US",
            listReleaseDatesitemValue = emptyList()
          )
        )
      ),
      listProductionCountriesItem = listOf(ProductionCountriesItem("GB")),
      releaseDate = "2022-07-01"
    )
    checkMovieReleaseDate(data, "US", "GB", "Jul 01, 2022")
  }

  @Test
  fun getReleaseDateRegion_withMatchingRegionButNullReleaseValuesList_usesProductionCountry() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "US",
            listReleaseDatesitemValue = null
          )
        )
      ),
      listProductionCountriesItem = listOf(ProductionCountriesItem("GB")),
      releaseDate = "2022-07-01"
    )
    checkMovieReleaseDate(data, "US", "GB", "Jul 01, 2022")
  }

  @Test
  fun getReleaseDateRegion_withEmptyReleaseDatesItemList_usesProductionCountry() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = emptyList()
      ),
      listProductionCountriesItem = listOf(ProductionCountriesItem("CA")),
      releaseDate = "2023-03-15"
    )
    checkMovieReleaseDate(data, "US", "CA", "Mar 15, 2023")
  }

  @Test
  fun getReleaseDateRegion_withProductionCountryButEmptyDate_usesAnyValidRegion() {
    val data = movieWithRelease(
      releaseRegion = "FR",
      releaseDate = "2021-11-01T00:00:00.000Z",
      productionCountries = listOf("GB"),
      fallbackDate = ""
    )
    checkMovieReleaseDate(data, "US", "FR", "Nov 01, 2021")
  }

  @Test
  fun getReleaseDateRegion_withProductionCountryButNullDate() {
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
  fun getReleaseDateRegion_withNullProductionCountriesItem_usesAnyValidRegion() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "FR",
            listReleaseDatesitemValue = listOf(
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
            listReleaseDatesitemValue = listOf(
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
  fun getReleaseDateRegion_withNoValidRegionsAtAll_returnsEmptyResult() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "US",
            listReleaseDatesitemValue = listOf(
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

  @Test
  fun getReleaseDateRegion_withNullIso31661InReleaseItem_usesProductionCountry() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = null, // Null ISO code
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
  fun getReleaseDateRegion_withAllProductionCountriesHavingEmptyIso_skipsProductionCountry() {
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
      listProductionCountriesItem = listOf(
        ProductionCountriesItem(""),
        ProductionCountriesItem(null)
      ),
      releaseDate = "2022-07-01"
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

  @Test
  fun getReleaseDateRegion_withNullReleaseDatesList_returnsEmptyResult() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = null
      ),
      listProductionCountriesItem = listOf(ProductionCountriesItem("")),
      releaseDate = ""
    )
    checkMovieReleaseDate(data, "US", "", "")
  }

  @Test
  fun getReleaseDateRegion_withNullIso31661InMatchingRegion_triggersElvisBranch() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = null,
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
  fun getReleaseDateRegion_withNullReleaseValuesList_triggersElvisBranch() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "US",
            listReleaseDatesitemValue = null
          )
        )
      ),
      listProductionCountriesItem = listOf(ProductionCountriesItem("GB")),
      releaseDate = "2022-07-01"
    )
    checkMovieReleaseDate(data, "US", "GB", "Jul 01, 2022")
  }

  @Test
  fun getReleaseDateRegion_withNullFirstReleaseValue_triggersElvisBranch() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "US",
            listReleaseDatesitemValue = listOf()
          )
        )
      ),
      listProductionCountriesItem = listOf(ProductionCountriesItem("GB")),
      releaseDate = "2022-07-01"
    )
    checkMovieReleaseDate(data, "US", "GB", "Jul 01, 2022")
  }

  @Test
  fun getReleaseDateRegion_withNullIso31661InAnyValidRegion_triggersElvisBranch() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = null,
            listReleaseDatesitemValue = listOf(
              ReleaseDatesItemValue(releaseDate = "2023-12-01T00:00:00.000Z")
            )
          )
        )
      ),
      listProductionCountriesItem = listOf(ProductionCountriesItem("")),
      releaseDate = ""
    )
    checkMovieReleaseDate(data, "US", "", "")
  }

  @Test
  fun getReleaseDateRegion_withNullReleaseItem_triggersIsValidRegionCheck() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(null)
      ),
      listProductionCountriesItem = listOf(ProductionCountriesItem("GB")),
      releaseDate = "2022-07-01"
    )
    checkMovieReleaseDate(data, "US", "GB", "Jul 01, 2022")
  }
  // endregion

  // region Helpers
  private fun movieWithRelease(
    releaseRegion: String,
    releaseDate: String?,
    productionCountries: List<String>,
    fallbackDate: String,
  ): MovieDetail {
    return MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = releaseRegion,
            listReleaseDatesitemValue = listOf(
              ReleaseDatesItemValue(releaseDate = releaseDate)
            )
          )
        )
      ),
      listProductionCountriesItem = productionCountries.map { ProductionCountriesItem(it) },
      releaseDate = fallbackDate
    )
  }

  private fun checkMovieReleaseDate(
    data: MovieDetail?,
    region: String,
    expectedRegion: String,
    expectedDate: String,
  ) {
    val result = getReleaseDateRegion(data, region)
    assertEquals(expectedRegion, result.regionRelease)
    assertEquals(expectedDate, result.releaseDate)
  }
  // endregion
}
