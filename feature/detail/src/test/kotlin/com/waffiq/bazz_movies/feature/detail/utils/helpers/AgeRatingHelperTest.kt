package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDates
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItemValue
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ContentRatings
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ContentRatingsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvDetail
import com.waffiq.bazz_movies.feature.detail.utils.helpers.AgeRatingHelper.getAgeRating
import org.junit.Assert.assertEquals
import org.junit.Test

class AgeRatingHelperTest {

  // region Movie Tests
  @Test
  fun getAgeRating_withRegionHavingValidCertification_returnsThatCertification() {
    val data = movieWithRegionCertification("US", "PG-13")
    checkAgeRatingMovie(data, "US", "PG-13")
  }

  @Test
  fun getAgeRating_withEmptyCertificationInRegion_fallbacksToOtherValidRegion() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          movieReleaseItem("US", ""),
          movieReleaseItem("UK", "15")
        )
      )
    )
    checkAgeRatingMovie(data, "US", "15")
  }

  @Test
  fun getAgeRating_withOnlyEmptyCertifications_returnsEmptyString() {
    val data = movieWithRegionCertification("US", "")
    checkAgeRatingMovie(data, "US", "")
  }

  @Test
  fun getAgeRating_withNullMovie_returnsEmptyString() {
    checkAgeRatingMovie(null, "US", "")
  }

  @Test
  fun getAgeRating_withNullReleaseDates_returnsEmptyString() {
    val data = MovieDetail(releaseDates = null)
    checkAgeRatingMovie(data, "US", "")
  }

  @Test
  fun getAgeRating_withReleaseDatesItemIsNull_returnsEmptyString() {
    val data = MovieDetail(releaseDates = ReleaseDates(listReleaseDatesItem = null))
    checkAgeRatingMovie(data, "UK", "")
  }

  @Test
  fun getAgeRating_withValidReleaseDatesItem_returnsEmptyString() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "US",
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(
                iso6391 = "US",
                certification = ""
              )
            )
          )
        )
      )
    )
    checkAgeRatingMovie(data, "US", "")
  }

  @Test
  fun getAgeRating_withNullListReleaseDatesItem_returnsEmptyString() {
    val data = MovieDetail(releaseDates = ReleaseDates(listReleaseDatesItem = null))
    checkAgeRatingMovie(data, "US", "")
  }

  @Test
  fun getAgeRating_withEmptyListReleaseDatesItem_returnsEmptyString() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(listReleaseDatesItem = emptyList())
    )
    checkAgeRatingMovie(data, "US", "")
  }

  @Test
  fun getAgeRating_withNullReleaseDateItemValue_returnsEmptyString() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "US",
            listReleaseDatesItemValue = null
          )
        )
      )
    )
    checkAgeRatingMovie(data, "US", "")
  }

  @Test
  fun getAgeRating_withEmptyReleaseDateItemValue_returnsEmptyString() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "US",
            listReleaseDatesItemValue = emptyList()
          )
        )
      )
    )
    checkAgeRatingMovie(data, "US", "")
  }

  @Test
  fun getAgeRating_withNullCertificationInItem_returnsEmptyString() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "US",
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(certification = null)
            )
          )
        )
      )
    )
    checkAgeRatingMovie(data, "US", "")
  }

  @Test
  fun getAgeRating_withMultipleItemsInSameRegion_returnsFirstValid() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "US",
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(certification = ""),
              ReleaseDatesItemValue(certification = "PG-13"),
              ReleaseDatesItemValue(certification = "R")
            )
          )
        )
      )
    )
    checkAgeRatingMovie(data, "US", "PG-13")
  }

  @Test
  fun getAgeRating_withInvalidRegionFallback_returnsAnyValidCertification() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          movieReleaseItem("JP", ""),
          movieReleaseItem("UK", "15")
        )
      )
    )
    checkAgeRatingMovie(data, "JP", "15")
  }

  @Test
  fun getAgeRating_withNullReleaseDatesItemInList_skipsNull() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          null,
          ReleaseDatesItem(
            iso31661 = "US",
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(certification = "PG-13")
            )
          )
        )
      )
    )
    checkAgeRatingMovie(data, "US", "PG-13")
  }

  @Test
  fun getAgeRating_withNullReleaseDatesItemValueInList_skipsNull() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "US",
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(descriptors = null, certification = "PG-13")
            )
          )
        )
      )
    )
    checkAgeRatingMovie(data, "US", "PG-13")
  }

  @Test
  fun getAgeRatingMovie_withFalseRegionAndNullItem_skipsNull() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          null,
          ReleaseDatesItem(
            iso31661 = "UK",
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(certification = "15")
            )
          )
        )
      )
    )
    checkAgeRatingMovie(data, "US", "15")
  }

  @Test
  fun getAgeRatingMovie_withFalseRegionAndNullReleaseDate_skipsNull() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "UK",
            listReleaseDatesItemValue = null
          ),
          ReleaseDatesItem(
            iso31661 = "FR",
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(certification = "16")
            )
          )
        )
      )
    )
    checkAgeRatingMovie(data, "UK", "16")
  }

  @Test
  fun getAgeRatingMovie_withFalseRegionAndEmptyCertification_findFirstValid() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "UK",
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(certification = ""),
              ReleaseDatesItemValue(certification = "15")
            )
          )
        )
      )
    )
    checkAgeRatingMovie(data, "US", "15")
  }

  @Test
  fun getAgeRatingMovie_withFalseRegionAndNullCertification_findFirstValid() {
    val data = MovieDetail(
      releaseDates = ReleaseDates(
        listReleaseDatesItem = listOf(
          ReleaseDatesItem(
            iso31661 = "UK",
            listReleaseDatesItemValue = listOf(
              ReleaseDatesItemValue(certification = null),
              ReleaseDatesItemValue(certification = "15")
            )
          )
        )
      )
    )
    checkAgeRatingMovie(data, "US", "15")
  }
  // endregion

  // region TV Tests
  @Test
  fun getAgeRatingTv_withRegionHavingRating_returnsThatRating() {
    val data = tvWithRatings(
      "JP" to "",
      "US" to "TV-14"
    )
    checkAgeRatingTv(data, "US", "TV-14")
  }

  @Test
  fun getAgeRatingTv_withEmptyRatingInRegion_fallbacksToOtherValidRegion() {
    val data = tvWithRatings(
      "JP" to "",
      "UK" to "15"
    )
    checkAgeRatingTv(data, "JP", "15")
  }

  @Test
  fun getAgeRatingTv_withOnlyEmptyRatings_returnsEmptyString() {
    val data = tvWithRatings(
      "US" to ""
    )
    checkAgeRatingTv(data, "US", "")
  }

  @Test
  fun getAgeRatingTv_withNullTv_returnsEmptyString() {
    checkAgeRatingTv(null, "US", "")
  }

  // NEW TESTS FOR TV COVERAGE
  @Test
  fun getAgeRatingTv_withNullContentRatings_returnsEmptyString() {
    val data = TvDetail(contentRatings = null)
    checkAgeRatingTv(data, "US", "")
  }

  @Test
  fun getAgeRatingTv_withNullContentRatingsItem_returnsEmptyString() {
    val data = TvDetail(
      contentRatings = ContentRatings(contentRatingsItem = null)
    )
    checkAgeRatingTv(data, "US", "")
  }

  @Test
  fun getAgeRatingTv_withEmptyContentRatingsItem_returnsEmptyString() {
    val data = TvDetail(
      contentRatings = ContentRatings(contentRatingsItem = emptyList())
    )
    checkAgeRatingTv(data, "US", "")
  }

  @Test
  fun getAgeRatingTv_withNullRating_returnsEmptyString() {
    val data = TvDetail(
      contentRatings = ContentRatings(
        contentRatingsItem = listOf(
          ContentRatingsItem(
            iso31661 = "US",
            rating = null
          )
        )
      )
    )
    checkAgeRatingTv(data, "US", "")
  }

  @Test
  fun getAgeRatingTv_withNullListContentRatingsItem_returnsEmptyString() {
    val data = TvDetail(
      contentRatings = ContentRatings(
        contentRatingsItem = listOf(null)
      )
    )
    checkAgeRatingTv(data, "US", "")
  }

  @Test
  fun getAgeRatingTv_withUserRegionAvailable_returnsUserRegionRating() {
    val data = tvWithRatings(
      "UK" to "15",
      "US" to "TV-14"
    )
    checkAgeRatingTv(data, "UK", "15")
  }

  @Test
  fun getAgeRatingTv_withUserRegionEmpty_returnsUSCredential() {
    val data = tvWithRatings(
      "UK" to "",
      "FR" to "16",
      "US" to "TV-14",
    )
    checkAgeRatingTv(data, "UK", "TV-14")
  }

  @Test
  fun getAgeRatingTv_withUserRegionAndUSEmpty_fallsBackToAny() {
    val data = tvWithRatings(
      "UK" to "",
      "US" to "",
      "FR" to "16"
    )
    checkAgeRatingTv(data, "UK", "16")
  }

  @Test
  fun getAgeRatingTv_withUSAsUserRegion_returnsUSRating() {
    val data = tvWithRatings(
      "UK" to "15",
      "US" to "TV-14"
    )
    checkAgeRatingTv(data, "US", "TV-14")
  }

  @Test
  fun getAgeRatingTv_withOnlyUserRegion_returnsUserRegionRating() {
    val data = tvWithRatings(
      "UK" to "15"
    )
    checkAgeRatingTv(data, "UK", "15")
  }

  @Test
  fun getAgeRatingTv_withFalseRegionFallback_returnsAnyValidRating() {
    val data = tvWithRatings(
      "JP" to "",
      "FR" to "16"
    )
    checkAgeRatingTv(data, "US", "16")
  }

  @Test
  fun getAgeRatingTv_withNullContentRatingsItemInList_skipsNull() {
    val data = TvDetail(
      contentRatings = ContentRatings(
        contentRatingsItem = listOf(
          null,
          ContentRatingsItem(
            iso31661 = "US",
            rating = "TV-14"
          )
        )
      )
    )
    checkAgeRatingTv(data, "US", "TV-14")
  }

  @Test
  fun getAgeRatingTv_withUserRegionEmptyAndUSValid_fallsBackToUS() {
    val data = tvWithRatings(
      "UK" to "",
      "US" to "TV-14",
      "FR" to "16"
    )
    checkAgeRatingTv(data, "UK", "TV-14")
  }

  @Test
  fun getAgeRatingTv_withUserRegionEmptyAndUSNull_fallsBackToAny() {
    val data = tvWithRatings(
      "UK" to "",
      "US" to null,
      "FR" to "16"
    )
    checkAgeRatingTv(data, "UK", "16")
  }

  @Test
  fun getAgeRatingTv_withUserRegionNullAndUSValid_fallsBackToUS() {
    val data = tvWithRatings(
      "UK" to null,
      "US" to "TV-14",
      "FR" to "16"
    )
    checkAgeRatingTv(data, "UK", "TV-14")
  }

  @Test
  fun getAgeRatingTv_withFalseRegionAndNullRating_skipsNull() {
    val data = tvWithRatings(
      "UK" to null,
      "FR" to "16"
    )
    checkAgeRatingTv(data, "US", "16")
  }

  @Test
  fun getAgeRatingTv_withFalseRegionAndEmptyRating_skipsEmpty() {
    val data = tvWithRatings(
      "UK" to "",
      "FR" to "16"
    )
    checkAgeRatingTv(data, "US", "16")
  }

  @Test
  fun getAgeRatingTv_withUserRegionNotFound_fallsBackToUS() {
    val data = tvWithRatings(
      "UK" to "15",
      "US" to "TV-14"
    )
    checkAgeRatingTv(data, "FR", "TV-14")
  }

  @Test
  fun getAgeRatingTv_withUserRegionNotFoundAndUSNotFound_fallsBackToAny() {
    val data = tvWithRatings(
      "UK" to "15"
    )
    checkAgeRatingTv(data, "FR", "15")
  }

  // endregion

  // region Helpers
  private fun checkAgeRatingMovie(data: MovieDetail?, region: String, expected: String) {
    val result = getAgeRating(data, region)
    assertEquals(expected, result)
  }

  private fun checkAgeRatingTv(data: TvDetail?, region: String, expected: String) {
    val result = getAgeRating(data, region)
    assertEquals(expected, result)
  }

  private fun movieWithRegionCertification(region: String?, certification: String?) = MovieDetail(
    releaseDates = ReleaseDates(
      listReleaseDatesItem = listOf(
        movieReleaseItem(region, certification)
      )
    )
  )

  private fun movieReleaseItem(region: String?, certification: String?) = ReleaseDatesItem(
    iso31661 = region,
    listReleaseDatesItemValue = listOf(
      ReleaseDatesItemValue(certification = certification)
    )
  )

  private fun tvWithRatings(vararg regionToRating: Pair<String?, String?>) = TvDetail(
    contentRatings = ContentRatings(
      contentRatingsItem = regionToRating.map {
        ContentRatingsItem(
          iso31661 = it.first,
          rating = it.second
        )
      }
    )
  )
  // endregion
}
