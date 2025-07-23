//package com.waffiq.bazz_movies.feature.detail.utils.helpers
//
//import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
//import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItemValue
//import com.waffiq.bazz_movies.feature.detail.domain.model.tv.DetailTv
//import com.waffiq.bazz_movies.feature.detail.domain.model.tv.SeasonsItem
//import org.junit.Assert.assertEquals
//import org.junit.Assert.assertFalse
//import org.junit.Assert.assertNull
//import org.junit.Assert.assertTrue
//import org.junit.Test
//
//// Tests for public helper methods
//
//class ReleaseDateHelperIndividualTest {
//
//  // region getMatchingRegionAndReleaseDateMovie tests
//  @Test
//  fun getMatchingRegionAndReleaseDateMovie_withMatchingRegion_returnsCorrectPair() {
//    val data = listOf(
//      ReleaseDatesItem(
//        iso31661 = "US",
//        listReleaseDatesitemValue = listOf(
//          ReleaseDatesItemValue(releaseDate = "2023-10-20T00:00:00.000Z")
//        )
//      ),
//      ReleaseDatesItem(
//        iso31661 = "FR",
//        listReleaseDatesitemValue = listOf(
//          ReleaseDatesItemValue(releaseDate = "2023-11-15T00:00:00.000Z")
//        )
//      )
//    )
//
//    val result = ReleaseDateHelper.getMatchingRegionAndReleaseDateMovie(data, "US")
//    assertEquals("US", result?.first)
//    assertEquals("2023-10-20T00:00:00.000Z", result?.second)
//  }
//
//  @Test
//  fun getMatchingRegionAndReleaseDateMovie_withNoMatchingRegion_returnsNull() {
//    val data = listOf(
//      ReleaseDatesItem(
//        iso31661 = "FR",
//        listReleaseDatesitemValue = listOf(
//          ReleaseDatesItemValue(releaseDate = "2023-11-15T00:00:00.000Z")
//        )
//      )
//    )
//
//    val result = ReleaseDateHelper.getMatchingRegionAndReleaseDateMovie(data, "US")
//    assertNull(result)
//  }
//
//  @Test
//  fun getMatchingRegionAndReleaseDateMovie_withNullData_returnsNull() {
//    val result = ReleaseDateHelper.getMatchingRegionAndReleaseDateMovie(null, "US")
//    assertNull(result)
//  }
//
//  @Test
//  fun getMatchingRegionAndReleaseDateMovie_withNullIso31661_returnsEmptyString() {
//    val data = listOf(
//      ReleaseDatesItem(
//        iso31661 = null, // Triggers iso31661 ?: "" branch
//        listReleaseDatesitemValue = listOf(
//          ReleaseDatesItemValue(releaseDate = "2023-10-20T00:00:00.000Z")
//        )
//      )
//    )
//
//    val result = ReleaseDateHelper.getMatchingRegionAndReleaseDateMovie(data, "US")
//    assertNull(result) // Should not match because iso31661 is null
//  }
//
//  @Test
//  fun getMatchingRegionAndReleaseDateMovie_withNullReleaseValuesList_returnsEmptyReleaseDate() {
//    val data = listOf(
//      ReleaseDatesItem(
//        iso31661 = "US",
//        listReleaseDatesitemValue = null // Triggers ?.firstOrNull() branch
//      )
//    )
//
//    val result = ReleaseDateHelper.getMatchingRegionAndReleaseDateMovie(data, "US")
//    assertNull(result) // Should not match because no valid release date
//  }
//
//  @Test
//  fun getMatchingRegionAndReleaseDateMovie_withNullFirstReleaseValue_returnsEmptyReleaseDate() {
//    val data = listOf(
//      ReleaseDatesItem(
//        iso31661 = "US",
//        listReleaseDatesitemValue = listOf() // Triggers ?.releaseDate branch
//      )
//    )
//
//    val result = ReleaseDateHelper.getMatchingRegionAndReleaseDateMovie(data, "US")
//    assertNull(result) // Should not match because no valid release date
//  }
//
//  @Test
//  fun getMatchingRegionAndReleaseDateMovie_withEmptyReleaseDate_returnsNull() {
//    val data = listOf(
//      ReleaseDatesItem(
//        iso31661 = "US",
//        listReleaseDatesitemValue = listOf(
//          ReleaseDatesItemValue(releaseDate = "") // Empty release date
//        )
//      )
//    )
//
//    val result = ReleaseDateHelper.getMatchingRegionAndReleaseDateMovie(data, "US")
//    assertNull(result) // Should not match because release date is empty
//  }
//  // endregion
//
//  // region getAnyValidRegionAndReleaseDateMovie tests
//  @Test
//  fun getAnyValidRegionAndReleaseDateMovie_withValidData_returnsFirstValid() {
//    val data = listOf(
//      ReleaseDatesItem(
//        iso31661 = "US",
//        listReleaseDatesitemValue = listOf(
//          ReleaseDatesItemValue(releaseDate = "2023-10-20T00:00:00.000Z")
//        )
//      ),
//      ReleaseDatesItem(
//        iso31661 = "FR",
//        listReleaseDatesitemValue = listOf(
//          ReleaseDatesItemValue(releaseDate = "2023-11-15T00:00:00.000Z")
//        )
//      )
//    )
//
//    val result = ReleaseDateHelper.getAnyValidRegionAndReleaseDateMovie(data)
//    assertEquals("US", result.first)
//    assertEquals("2023-10-20T00:00:00.000Z", result.second)
//  }
//
//  @Test
//  fun getAnyValidRegionAndReleaseDateMovie_withNullData_returnsEmptyPair() {
//    val result = ReleaseDateHelper.getAnyValidRegionAndReleaseDateMovie(null)
//    assertEquals("", result.first)
//    assertEquals("", result.second)
//  }
//
//  @Test
//  fun getAnyValidRegionAndReleaseDateMovie_withEmptyData_returnsEmptyPair() {
//    val result = ReleaseDateHelper.getAnyValidRegionAndReleaseDateMovie(emptyList())
//    assertEquals("", result.first)
//    assertEquals("", result.second)
//  }
//
//  @Test
//  fun getAnyValidRegionAndReleaseDateMovie_withNullIso31661_returnsEmptyString() {
//    val data = listOf(
//      ReleaseDatesItem(
//        iso31661 = null, // Triggers iso31661 ?: "" branch
//        listReleaseDatesitemValue = listOf(
//          ReleaseDatesItemValue(releaseDate = "2023-10-20T00:00:00.000Z")
//        )
//      )
//    )
//
//    val result = ReleaseDateHelper.getAnyValidRegionAndReleaseDateMovie(data)
//    assertEquals("", result.first)
//    assertEquals("", result.second)
//  }
//
//  @Test
//  fun getAnyValidRegionAndReleaseDateMovie_withNullReleaseValuesList_returnsEmptyReleaseDate() {
//    val data = listOf(
//      ReleaseDatesItem(
//        iso31661 = "US",
//        listReleaseDatesitemValue = null // Triggers ?.firstOrNull() branch
//      )
//    )
//
//    val result = ReleaseDateHelper.getAnyValidRegionAndReleaseDateMovie(data)
//    assertEquals("", result.first)
//    assertEquals("", result.second)
//  }
//
//  @Test
//  fun getAnyValidRegionAndReleaseDateMovie_withNullFirstReleaseValue_returnsEmptyReleaseDate() {
//    val data = listOf(
//      ReleaseDatesItem(
//        iso31661 = "US",
//        listReleaseDatesitemValue = listOf() // Triggers ?.releaseDate branch
//      )
//    )
//
//    val result = ReleaseDateHelper.getAnyValidRegionAndReleaseDateMovie(data)
//    assertEquals("", result.first)
//    assertEquals("", result.second)
//  }
//
//  @Test
//  fun getAnyValidRegionAndReleaseDateMovie_withInvalidItems_returnsEmptyPair() {
//    val data = listOf(
//      ReleaseDatesItem(
//        iso31661 = "US",
//        listReleaseDatesitemValue = listOf(
//          ReleaseDatesItemValue(releaseDate = "") // Empty release date
//        )
//      ),
//      ReleaseDatesItem(
//        iso31661 = null, // Null iso
//        listReleaseDatesitemValue = listOf(
//          ReleaseDatesItemValue(releaseDate = "2023-10-20T00:00:00.000Z")
//        )
//      )
//    )
//
//    val result = ReleaseDateHelper.getAnyValidRegionAndReleaseDateMovie(data)
//    assertEquals("", result.first)
//    assertEquals("", result.second)
//  }
//  // endregion
//
//  // region isValidRegionAndReleaseDate tests
//  @Test
//  fun isValidRegionAndReleaseDate_withValidItemAndMatchingRegion_returnsTrue() {
//    val item = ReleaseDatesItem(
//      iso31661 = "US",
//      listReleaseDatesitemValue = listOf(
//        ReleaseDatesItemValue(releaseDate = "2023-10-20T00:00:00.000Z")
//      )
//    )
//
//    val result = ReleaseDateHelper.isValidRegionAndReleaseDate(item, "US")
//    assertTrue(result)
//  }
//
//  @Test
//  fun isValidRegionAndReleaseDate_withValidItemAndNoRegionFilter_returnsTrue() {
//    val item = ReleaseDatesItem(
//      iso31661 = "US",
//      listReleaseDatesitemValue = listOf(
//        ReleaseDatesItemValue(releaseDate = "2023-10-20T00:00:00.000Z")
//      )
//    )
//
//    val result = ReleaseDateHelper.isValidRegionAndReleaseDate(item, null)
//    assertTrue(result)
//  }
//
//  @Test
//  fun isValidRegionAndReleaseDate_withValidItemAndNonMatchingRegion_returnsFalse() {
//    val item = ReleaseDatesItem(
//      iso31661 = "US",
//      listReleaseDatesitemValue = listOf(
//        ReleaseDatesItemValue(releaseDate = "2023-10-20T00:00:00.000Z")
//      )
//    )
//
//    val result = ReleaseDateHelper.isValidRegionAndReleaseDate(item, "FR")
//    assertFalse(result)
//  }
//
//  @Test
//  fun isValidRegionAndReleaseDate_withNullItem_returnsFalse() {
//    val result = ReleaseDateHelper.isValidRegionAndReleaseDate(null, "US")
//    assertFalse(result) // Triggers item?.iso31661 != null branch
//  }
//
//  @Test
//  fun isValidRegionAndReleaseDate_withNullIso31661_returnsFalse() {
//    val item = ReleaseDatesItem(
//      iso31661 = null, // Triggers iso31661 != null check
//      listReleaseDatesitemValue = listOf(
//        ReleaseDatesItemValue(releaseDate = "2023-10-20T00:00:00.000Z")
//      )
//    )
//
//    val result = ReleaseDateHelper.isValidRegionAndReleaseDate(item, "US")
//    assertFalse(result)
//  }
//
//  @Test
//  fun isValidRegionAndReleaseDate_withNullReleaseValuesList_returnsFalse() {
//    val item = ReleaseDatesItem(
//      iso31661 = "US",
//      listReleaseDatesitemValue = null
//    )
//
//    val result = ReleaseDateHelper.isValidRegionAndReleaseDate(item, "US")
//    assertFalse(result)
//  }
//
//  @Test
//  fun isValidRegionAndReleaseDate_withEmptyReleaseValuesList_returnsFalse() {
//    val item = ReleaseDatesItem(
//      iso31661 = "US",
//      listReleaseDatesitemValue = emptyList()
//    )
//
//    val result = ReleaseDateHelper.isValidRegionAndReleaseDate(item, "US")
//    assertFalse(result)
//  }
//
//  @Test
//  fun isValidRegionAndReleaseDate_withAllNullReleaseValues_returnsFalse() {
//    val item = ReleaseDatesItem(
//      iso31661 = "US",
//      listReleaseDatesitemValue = listOf()
//    )
//
//    val result = ReleaseDateHelper.isValidRegionAndReleaseDate(item, "US")
//    assertFalse(result)
//  }
//
//  @Test
//  fun isValidRegionAndReleaseDate_withAllEmptyReleaseValues_returnsFalse() {
//    val item = ReleaseDatesItem(
//      iso31661 = "US",
//      listReleaseDatesitemValue = listOf(
//        ReleaseDatesItemValue(releaseDate = ""),
//        ReleaseDatesItemValue(releaseDate = null)
//      )
//    )
//
//    val result = ReleaseDateHelper.isValidRegionAndReleaseDate(item, "US")
//    assertFalse(result)
//  }
//
//  @Test
//  fun isValidRegionAndReleaseDate_withMixedReleaseValues_returnsTrue() {
//    val item = ReleaseDatesItem(
//      iso31661 = "US",
//      listReleaseDatesitemValue = listOf(
//        ReleaseDatesItemValue(releaseDate = ""),
//        ReleaseDatesItemValue(releaseDate = "2023-10-20T00:00:00.000Z") // At least one valid
//      )
//    )
//
//    val result = ReleaseDateHelper.isValidRegionAndReleaseDate(item, "US")
//    assertTrue(result)
//  }
//  // endregion
//
//  // region getYearRangeTv
//  @Test
//  fun getYearRangeTv_withMultipleSeasons_returnsYearRange() {
//    val data = DetailTv(
//      listOriginCountry = listOf("US"),
//      listSeasonsItem = listOf(
//        SeasonsItem(name = "Season 1", airDate = "2018-05-01"),
//        SeasonsItem(name = "Season 2", airDate = "2020-04-01")
//      )
//    )
//
//    val result = ReleaseDateHelper.getYearRangeTv(data)
//    assertEquals("2018-2020", result)
//  }
//
//  @Test
//  fun getYearRangeTv_withSingleSeason_returnsSingleYear() {
//    val data = DetailTv(
//      listOriginCountry = listOf("US"),
//      listSeasonsItem = listOf(
//        SeasonsItem(name = "Season 1", airDate = "2019-10-01")
//      )
//    )
//
//    val result = ReleaseDateHelper.getYearRangeTv(data)
//    assertEquals("2019", result)
//  }
//
//  @Test
//  fun getYearRangeTv_withNullSeasonItem_filtersCorrectly() {
//    val data = DetailTv(
//      listOriginCountry = listOf("US"),
//      listSeasonsItem = listOf(
//        null, // Triggers it?.airDate branch in mapNotNull
//        SeasonsItem(name = "Season 1", airDate = "2020-06-01")
//      )
//    )
//
//    val result = ReleaseDateHelper.getYearRangeTv(data)
//    assertEquals("2020", result)
//  }
//
//  @Test
//  fun getYearRangeTv_withNullAirDate_filtersCorrectly() {
//    val data = DetailTv(
//      listOriginCountry = listOf("US"),
//      listSeasonsItem = listOf(
//        SeasonsItem(name = "Season 1", airDate = null), // Triggers ?.take(YEAR) branch
//        SeasonsItem(name = "Season 2", airDate = "2020-06-01")
//      )
//    )
//
//    val result = ReleaseDateHelper.getYearRangeTv(data)
//    assertEquals("2020", result)
//  }
//
//  @Test
//  fun getYearRangeTv_withShortAirDate_filtersCorrectly() {
//    val data = DetailTv(
//      listOriginCountry = listOf("US"),
//      listSeasonsItem = listOf(
//        SeasonsItem(name = "Season 1", airDate = "20"), // Too short for YEAR characters
//        SeasonsItem(name = "Season 2", airDate = "2020-06-01")
//      )
//    )
//
//    val result = ReleaseDateHelper.getYearRangeTv(data)
//    assertEquals("2020", result)
//  }
//
//  @Test
//  fun getYearRangeTv_withInvalidYearFormat_filtersCorrectly() {
//    val data = DetailTv(
//      listOriginCountry = listOf("US"),
//      listSeasonsItem = listOf(
//        SeasonsItem(name = "Season 1", airDate = "abcd-06-01"), // Invalid year format
//        SeasonsItem(name = "Season 2", airDate = "2020-06-01")
//      )
//    )
//
//    val result = ReleaseDateHelper.getYearRangeTv(data)
//    assertEquals("2020", result)
//  }
//
//
//  @Test
//  fun getYearRangeTv_withMultipleSeasonsButOnlyOneValidYear_returnsSingleYear() {
//    // Multiple seasons in filteredSeasons, but only one valid year in years
//    val data = DetailTv(
//      listOriginCountry = listOf("US"),
//      listSeasonsItem = listOf(
//        SeasonsItem(name = "Season 1", airDate = "2020-05-01"),
//        SeasonsItem(name = "Season 2", airDate = "invalid-date"), // Invalid, filtered out by mapNotNull
//        SeasonsItem(name = "Season 3", airDate = null) // Null, filtered out by mapNotNull
//      )
//    )
//
//    val result = ReleaseDateHelper.getYearRangeTv(data)
//    assertEquals("2020", result) // Should return single year, not range
//  }
//
//  @Test
//  fun getYearRangeTv_withOneSeasonButMultipleValidYears_returnsRange() {
//    // This scenario might not be possible with current data structure,
//    // but testing edge case where filteredSeasons.size <= 1 but years has multiple items
//    val data = DetailTv(
//      listOriginCountry = listOf("US"),
//      listSeasonsItem = listOf(
//        SeasonsItem(name = "Season 1", airDate = "2020-05-01"),
//        SeasonsItem(name = "Not a Season", airDate = "2021-05-01") // Filtered out by name filter
//      )
//    )
//
//    val result = ReleaseDateHelper.getYearRangeTv(data)
//    assertEquals("2020", result) // Only one season passes name filter
//  }
//  // endregion
//
//  // region getOriginalCountryTv
//  @Test
//  fun getOriginalCountryTv_withValidOriginCountry_returnsFirstCountry() {
//    val data = DetailTv(
//      listOriginCountry = listOf("US", "CA"),
//      listSeasonsItem = emptyList()
//    )
//
//    val result = ReleaseDateHelper.getOriginalCountryTv(data)
//    assertEquals("US", result)
//  }
//
//  @Test
//  fun getOriginalCountryTv_withNullOriginCountry_returnsEmptyString() {
//    val data = DetailTv(
//      listOriginCountry = null,
//      listSeasonsItem = emptyList()
//    )
//
//    val result = ReleaseDateHelper.getOriginalCountryTv(data)
//    assertEquals("", result)
//  }
//
//  @Test
//  fun getOriginalCountryTv_withEmptyOriginCountry_returnsEmptyString() {
//    val data = DetailTv(
//      listOriginCountry = emptyList(),
//      listSeasonsItem = emptyList()
//    )
//
//    val result = ReleaseDateHelper.getOriginalCountryTv(data)
//    assertEquals("", result)
//  }
//  // endregion
//}
