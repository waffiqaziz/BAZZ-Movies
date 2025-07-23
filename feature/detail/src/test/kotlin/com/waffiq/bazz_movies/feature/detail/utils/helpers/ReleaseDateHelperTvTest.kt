package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.tv.DetailTv
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.SeasonsItem
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.detailTv
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ReleaseDateHelper.getReleaseDateRegion
import org.junit.Assert.assertEquals
import org.junit.Test

class ReleaseDateHelperTvTest {

  // region TV
  @Test
  fun getReleaseDateRegionTv_withMultipleSeasons_returnsYearRange() {
    val data = tvWithSeasons(
      origin = "US",
      "Season 1" to "2018-05-01",
      "Season 2" to "2020-04-01"
    )
    checkTvReleaseDate(data, "(US)", "2018-2020")
  }

  @Test
  fun getReleaseDateRegionTv_withSingleSeason_returnsSingleYear() {
    val data = tvWithSeasons(
      origin = "JP",
      "Season 1" to "2019-10-01"
    )
    checkTvReleaseDate(data, "(JP)", "2019")
  }

  @Test
  fun getReleaseDateRegionTv_withNoValidSeason_returnsEmptyDate() {
    val data = tvWithSeasons(
      origin = "KR",
      "Specials" to "2019-01-01"
    )
    checkTvReleaseDate(data, "(KR)", "")
  }

  @Test
  fun getReleaseDateRegionTv_withNullSeasonName_returnsEmpty() {
    val seasonData = SeasonsItem(name = null, airDate = "2020-01-01")
    val data = DetailTv(
      listOriginCountry = listOf("US"),
      listSeasonsItem = listOf(seasonData)
    )
    checkTvReleaseDate(data, "(US)", "")
  }

  @Test
  fun getReleaseDateRegionTv_withInvalidSeason_returnsEmpty() {
    val seasonData = SeasonsItem(name = "Season 1", airDate = "12")
    val data = DetailTv(
      listOriginCountry = null,
      listSeasonsItem = listOf(seasonData)
    )

    checkTvReleaseDate(data, "()", "")
    checkTvReleaseDate(
      data.copy(listSeasonsItem = listOf(seasonData.copy(airDate = "invalid"))), "()", ""
    )
    checkTvReleaseDate(
      data.copy(listSeasonsItem = listOf(seasonData.copy(airDate = null))), "()", ""
    )
    checkTvReleaseDate(data.copy(listSeasonsItem = listOf(null)), "()", "")
    checkTvReleaseDate(data.copy(listSeasonsItem = null), "()", "")
  }

  @Test
  fun getReleaseDateRegionTv_withEmptyOriginCountry_returnsEmptyRegion() {
    val data = tvWithSeasons(
      origin = "",
      "Season 1" to "2020-06-01"
    )
    checkTvReleaseDate(data, "()", "2020")
  }

  @Test
  fun getReleaseDateRegionTv_withNullSeasonsList_returnsEmptyDate() {
    val data = DetailTv(
      listOriginCountry = listOf("US"),
      listSeasonsItem = emptyList()
    )
    checkTvReleaseDate(data, "(US)", "")
  }

  @Test
  fun getReleaseDateRegionTv_withInvalidSeasonDates_returnsEmptyDate() {
    val data = tvWithSeasons(
      origin = "KR",
      "Season 1" to ""
    )
    checkTvReleaseDate(data, "(KR)", "")
  }

  @Test
  fun getReleaseDateRegionTv_withNullOriginCountryList_returnsEmptyRegion() {
    val data = DetailTv(
      listOriginCountry = null,
      listSeasonsItem = listOf(
        SeasonsItem(name = "Season 1", airDate = "2020-06-01")
      )
    )
    checkTvReleaseDate(data, "()", "2020")
  }

  @Test
  fun getReleaseDateRegionTv_withFirstReleaseDateValid_returnsFirstReleaseDate() {
    checkTvReleaseDate(detailTv, "()", "2023")
  }

  @Test
  fun getReleaseDateRegionTv_withFirstReleaseDateInvalid_returnsEmpty() {
    checkTvReleaseDate(detailTv.copy(firstAirDate = null), "()", "")
    checkTvReleaseDate(detailTv.copy(firstAirDate = "invalid"), "()", "")
    checkTvReleaseDate(detailTv.copy(firstAirDate = "abcd-12-20"), "()", "")
    checkTvReleaseDate(detailTv.copy(firstAirDate = "12"), "()", "")
  }

  @Test
  fun getReleaseDateRegionTv_withEmptyOriginCountryList_returnsEmptyRegion() {
    val data = DetailTv(
      listOriginCountry = emptyList(),
      listSeasonsItem = listOf(
        SeasonsItem(name = "Season 1", airDate = "2020-06-01")
      )
    )
    checkTvReleaseDate(data, "()", "2020")
  }

  @Test
  fun getReleaseDateRegionTv_withNullSeasonsItem_returnsEmptyDate() {
    val data = DetailTv(
      listOriginCountry = listOf("US"),
      listSeasonsItem = null // Null seasons list
    )
    checkTvReleaseDate(data, "(US)", "")
  }

  @Test
  fun getReleaseDateRegionTv_withSeasonsContainingNullItems_filtersCorrectly() {
    val data = DetailTv(
      listOriginCountry = listOf("US"),
      listSeasonsItem = listOf(
        null, // Null season item
        SeasonsItem(name = "Season 1", airDate = "2020-06-01"),
        SeasonsItem(name = "Season 2", airDate = "2021-06-01")
      )
    )
    checkTvReleaseDate(data, "(US)", "2020-2021")
  }

  @Test
  fun getReleaseDateRegionTv_withSeasonsContainingNullNames_filtersCorrectly() {
    val data = DetailTv(
      listOriginCountry = listOf("US"),
      listSeasonsItem = listOf(
        SeasonsItem(name = null, airDate = "2019-06-01"), 
        SeasonsItem(name = "Season 1", airDate = "2020-06-01")
      )
    )
    checkTvReleaseDate(data, "(US)", "2020")
  }

  @Test
  fun getReleaseDateRegionTv_withSeasonsNotStartingWithSeason_filtersCorrectly() {
    val data = DetailTv(
      listOriginCountry = listOf("US"),
      listSeasonsItem = listOf(
        SeasonsItem(name = "Specials", airDate = "2019-06-01"),
        SeasonsItem(name = "Season 1", airDate = "2020-06-01"),
        SeasonsItem(name = "Bonus Content", airDate = "2021-06-01")
      )
    )
    checkTvReleaseDate(data, "(US)", "2020")
  }

  @Test
  fun getReleaseDateRegionTv_withInvalidYearFormat_filtersCorrectly() {
    val data = DetailTv(
      listOriginCountry = listOf("US"),
      listSeasonsItem = listOf(
        SeasonsItem(name = "Season 1", airDate = "invalid-date"),
        SeasonsItem(name = "Season 2", airDate = "2020-06-01")
      )
    )
    checkTvReleaseDate(data, "(US)", "2020")
  }

  @Test
  fun getReleaseDateRegionTv_withShortAirDate_filtersCorrectly() {
    val data = DetailTv(
      listOriginCountry = listOf("US"),
      listSeasonsItem = listOf(
        SeasonsItem(name = "Season 1", airDate = "20"), // Too short for year
        SeasonsItem(name = "Season 2", airDate = "2020-06-01")
      )
    )
    checkTvReleaseDate(data, "(US)", "2020")
  }

  @Test
  fun getReleaseDateRegionTv_withNullAirDate_filtersCorrectly() {
    val data = DetailTv(
      listOriginCountry = listOf("US"),
      listSeasonsItem = listOf(
        SeasonsItem(name = "Season 1", airDate = null), // Null air date
        SeasonsItem(name = "Season 2", airDate = "2020-06-01")
      )
    )
    checkTvReleaseDate(data, "(US)", "2020")
  }

  @Test
  fun getReleaseDateRegionTv_withOnlyInvalidSeasons_returnsEmptyDate() {
    val data = DetailTv(
      listOriginCountry = listOf("US"),
      listSeasonsItem = listOf(
        SeasonsItem(name = "Specials", airDate = "2019-06-01"),
        SeasonsItem(name = "Behind the Scenes", airDate = "2020-06-01")
      )
    )
    checkTvReleaseDate(data, "(US)", "")
  }

  @Test
  fun getReleaseDateRegionTv_withMultipleSeasonsButOnlyOneValidYear_returnsSingleYear() {
    val data = DetailTv(
      listOriginCountry = listOf("US"),
      listSeasonsItem = listOf(
        SeasonsItem(name = "Season 1", airDate = "2020-06-01"),
        SeasonsItem(name = "Season 2", airDate = "invalid-date")
      )
    )
    checkTvReleaseDate(data, "(US)", "2020")
  }

  @Test
  fun getReleaseDateRegionTv_withNullSeasonItemInList_triggersMapNotNullBranch() {
    val data = DetailTv(
      listOriginCountry = listOf("US"),
      listSeasonsItem = listOf(
        null, // This triggers the it?.airDate branch in mapNotNull
        SeasonsItem(name = "Season 1", airDate = "2020-06-01")
      )
    )
    checkTvReleaseDate(data, "(US)", "2020")
  }

  @Test
  fun getReleaseDateRegionTv_withNullAirDateInSeason_triggersMapNotNullBranch() {
    val data = DetailTv(
      listOriginCountry = listOf("US"),
      listSeasonsItem = listOf(
        SeasonsItem(name = "Season 1", airDate = null), // This triggers the ?.take(YEAR) branch
        SeasonsItem(name = "Season 2", airDate = "2020-06-01")
      )
    )
    checkTvReleaseDate(data, "(US)", "2020")
  }

  // Test for: val years = filteredSeasons?.mapNotNull { it?.airDate?.take(YEAR)?.toIntOrNull() } // 2 of 8 branches is missed
  @Test
  fun getReleaseDateRegionTv_withNullSeasonItemInFilteredList_triggersMapNotNullBranch() {
    val data = DetailTv(
      listOriginCountry = listOf("US"),
      listSeasonsItem = listOf(
        null, // This null item should be filtered out, but triggers it?.airDate branch
        SeasonsItem(name = "Season 1", airDate = "2020-06-01")
      )
    )
    checkTvReleaseDate(data, "(US)", "2020")
  }

  // Test for: val years = filteredSeasons?.mapNotNull { it?.airDate?.take(YEAR)?.toIntOrNull() } // Another branch
  @Test
  fun getReleaseDateRegionTv_withNullAirDateInFilteredSeason_triggersMapNotNullBranch() {
    val data = DetailTv(
      listOriginCountry = listOf("US"),
      listSeasonsItem = listOf(
        SeasonsItem(name = "Season 1", airDate = null), // Null airDate triggers ?.take(YEAR) branch
        SeasonsItem(name = "Season 2", airDate = "2020-06-01")
      )
    )
    checkTvReleaseDate(data, "(US)", "2020")
  }
  // endregion

  // region Helpers
  private fun tvWithSeasons(origin: String, vararg seasons: Pair<String, String>): DetailTv {
    return DetailTv(
      listOriginCountry = listOf(origin),
      listSeasonsItem = seasons.map {
        SeasonsItem(name = it.first, airDate = it.second)
      }
    )
  }

  private fun checkTvReleaseDate(
    data: DetailTv,
    expectedRegion: String,
    expectedDate: String,
  ) {
    val result = getReleaseDateRegion(data)
    assertEquals(expectedRegion, result.regionRelease)
    assertEquals(expectedDate, result.releaseDate)
  }
  // endregion
}
