package com.waffiq.bazz_movies.utils.helpers.details

import com.waffiq.bazz_movies.domain.model.detail.DetailMovie
import com.waffiq.bazz_movies.domain.model.detail.ReleaseDateRegion
import com.waffiq.bazz_movies.domain.model.detail.ReleaseDatesItem
import com.waffiq.bazz_movies.utils.Helper

/**
 * Used to get release date and its region
 */
object ReleaseDateHelper {
  fun getReleaseDateRegion(data: DetailMovie?, userRegion: String): ReleaseDateRegion {
    var releaseDateRegion: ReleaseDateRegion? = null

    // Step 1: Check if user region matches
    val userRegionAndDate =
      getMatchingRegionAndReleaseDate(data?.releaseDates?.listReleaseDatesItem, userRegion)
    if (userRegionAndDate != null) {
      releaseDateRegion = ReleaseDateRegion(
        regionRelease = userRegionAndDate.first,
        releaseDate = Helper.dateFormatterISO8601(userRegionAndDate.second)
      )
    }

    // Step 2: Fallback to use production country and its release date
    if (releaseDateRegion == null) {
      val productionCountryRegionAndDate = ReleaseDateRegion(
        regionRelease = data?.listProductionCountriesItem?.firstOrNull { !it?.iso31661.isNullOrEmpty() }?.iso31661
          ?: "",
        releaseDate = Helper.dateFormatterStandard(data?.releaseDate)
      )
      if (productionCountryRegionAndDate.releaseDate.isNotEmpty() &&
        productionCountryRegionAndDate.regionRelease.isNotEmpty()
      ) {
        releaseDateRegion = productionCountryRegionAndDate
      }
    }

    // Step 3: Ifs still null Fallback to any valid region and release date
    if (releaseDateRegion == null) {
      val fallback = getAnyValidRegionAndReleaseDate(data?.releaseDates?.listReleaseDatesItem)
      releaseDateRegion = ReleaseDateRegion(
        regionRelease = fallback.first,
        releaseDate = Helper.dateFormatterISO8601(fallback.second)
      )
    }

    return releaseDateRegion
  }

  private fun getMatchingRegionAndReleaseDate(
    data: List<ReleaseDatesItem?>?,
    region: String
  ): Pair<String, String>? {
    return data?.firstOrNull { isValidRegionAndReleaseDate(it, region) }
      ?.let {
        Pair(
          it.iso31661 ?: "",
          it.listReleaseDatesitemValue?.firstOrNull()?.releaseDate ?: ""
        )
      }
  }

  private fun getAnyValidRegionAndReleaseDate(data: List<ReleaseDatesItem?>?): Pair<String, String> {
    return data?.firstOrNull { isValidRegionAndReleaseDate(it) }
      ?.let {
        Pair(
          it.iso31661 ?: "",
          it.listReleaseDatesitemValue?.firstOrNull()?.releaseDate ?: ""
        )
      }
      ?: Pair("", "")
  }

  private fun isValidRegionAndReleaseDate(
    item: ReleaseDatesItem?,
    region: String? = null
  ): Boolean {
    return item?.iso31661 != null &&
      (region == null || item.iso31661 == region) &&
      item.listReleaseDatesitemValue?.any { !it.releaseDate.isNullOrEmpty() } == true
  }
}
