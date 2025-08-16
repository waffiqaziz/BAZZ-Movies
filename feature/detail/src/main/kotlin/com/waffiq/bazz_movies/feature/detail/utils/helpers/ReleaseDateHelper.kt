package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.core.utils.DateFormatter.dateFormatterISO8601
import com.waffiq.bazz_movies.core.utils.DateFormatter.dateFormatterStandard
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvDetail

/**
 * Helper object responsible for retrieving release dates and their associated regions for movies.
 *
 * The `ReleaseDateHelper` provides a mechanism to find a movie's release date and region based
 * on a prioritized search strategy. This includes checking for a user's  region, falling  back
 * to production countries, and using any valid release data available. It handles various scenarios
 * to ensure that an appropriate release date and region are returned.
 */
object ReleaseDateHelper {

  private const val YEAR_LENGTH = 4

  /**
   * For Movie
   *
   * Determines and returns the most relevant release date and its associated region for a given movie.
   *
   * The function employs a multi-step search strategy to locate a release date:
   * 1. Checks if there is a release date corresponding to the specified user region.
   * 2. If no matching region is found, it falls back to using the production country's release date.
   * 3. If the above options are unavailable, it searches for any valid release date in any region.
   *
   * @param data A `DetailMovie` object containing details about the movie, including its release
   *        dates and production countries.
   * @param userRegion A string representing the user's desired region code.
   * @return A `ReleaseDateRegion` object that contains the selected release date and region.
   */
  fun getReleaseDateRegion(data: MovieDetail?, userRegion: String): ReleaseDateRegion {
    var releaseDateRegion: ReleaseDateRegion? = null

    // Step 1: get release date based user's region.
    val userRegionAndDate =
      getMatchingRegionAndReleaseDateMovie(data?.releaseDates?.listReleaseDatesItem, userRegion)
    if (userRegionAndDate != null) {
      releaseDateRegion = ReleaseDateRegion(
        regionRelease = userRegionAndDate.first,
        releaseDate = dateFormatterISO8601(userRegionAndDate.second)
      )
      println("STEP 1")
    }

    // Step 2: fallback - use production country and its release date
    // if no match is found for the user region.
    if (releaseDateRegion == null) {
      // get production country
      val productionCountryRegionAndDate = ReleaseDateRegion(
        regionRelease = data?.listProductionCountriesItem?.firstOrNull {
          !it?.iso31661.isNullOrEmpty()
        }?.iso31661.orEmpty(),
        releaseDate = dateFormatterStandard(data?.releaseDate)
      )
      if (productionCountryRegionAndDate.releaseDate.isNotEmpty() &&
        productionCountryRegionAndDate.regionRelease.isNotEmpty()
      ) {
        releaseDateRegion = productionCountryRegionAndDate
      }
      println("STEP 2")
    }

    // Step 3: final Fallback - use any valid region and release date.
    if (releaseDateRegion == null) {
      val fallback = getAnyValidRegionAndReleaseDateMovie(data?.releaseDates?.listReleaseDatesItem)
      releaseDateRegion = ReleaseDateRegion(
        regionRelease = fallback.first,
        releaseDate = dateFormatterISO8601(fallback.second)
      )
      println("STEP 3")
    }

    return releaseDateRegion
  }

  /**
   * Finds a release date and associated region that matches the specified region.
   *
   * @param data A list of `ReleaseDatesItem` objects representing release dates and their regions.
   * @param region A string specifying the desired region code.
   * @return A pair containing the region code and the release date, or `null` if no match is found.
   */
  private fun getMatchingRegionAndReleaseDateMovie(
    data: List<ReleaseDatesItem?>?,
    region: String,
  ): Pair<String, String>? {
    return data?.firstOrNull { isValidRegionAndReleaseDate(it, region) }
      ?.let {
        Pair(
          it.iso31661.orEmpty(),
          it.listReleaseDatesItemValue?.firstOrNull()?.releaseDate.orEmpty()
        )
      }
  }

  /**
   * Finds any valid release date and associated region when no specific match is found.
   *
   * @param data A list of `ReleaseDatesItem` objects representing release dates and their regions.
   * @return A pair containing the region code and the release date, or a pair of empty strings if
   *        no valid data is found.
   */
  private fun getAnyValidRegionAndReleaseDateMovie(data: List<ReleaseDatesItem?>?): Pair<String, String> {
    return data?.firstOrNull { isValidRegionAndReleaseDate(it) }
      ?.let {
        Pair(
          it.iso31661.orEmpty(),
          it.listReleaseDatesItemValue?.firstOrNull()?.releaseDate.orEmpty()
        )
      }
      ?: Pair("", "")
  }

  /**
   * Checks if a release date is valid for the specified region (or any region if `region` is `null`).
   *
   * @param item A `ReleaseDatesItem` representing a region and associated release dates.
   * @param region (Optional) A string representing the desired region code to match.
   * @return A boolean indicating whether the release date is valid and matches the specified region (if provided).
   */
  private fun isValidRegionAndReleaseDate(
    item: ReleaseDatesItem?,
    region: String? = null,
  ): Boolean {
    return item?.iso31661 != null &&
      (region == null || item.iso31661 == region) &&
      item.listReleaseDatesItemValue?.any { !it.releaseDate.isNullOrEmpty() } == true
  }

  /**
   * For TV-Series
   *
   * Determines and returns the most relevant release date and its associated region for a given
   * tv-series.
   *
   * @param data A `DetailTv` object containing details about the tv-series, including its release
   *        dates and original country.
   * @return A `ReleaseDateRegion` object that contains the release date and original country.
   */
  fun getReleaseDateRegion(data: TvDetail): ReleaseDateRegion {
    return ReleaseDateRegion(
      "(${getOriginalCountryTv(data)})",
      getYearRangeTv(data)
    )
  }

  /**
   * Finds tv-series release date
   *
   * @param data A list of `DetailTv` objects representing tv data.
   *
   * @return string of selected year.
   *
   * Example: return `2017-2019` for multiple seasons, return `2017` if only one season`
   */
  private fun getYearRangeTv(data: TvDetail): String {
    // extract years from season items that start with "Season"
    val seasonYears = data.listSeasonsItem
      ?.filter { it?.name?.startsWith("Season") == true } // Filter only "Season X"
      ?.mapNotNull { season ->
        val airDate = season?.airDate

        // take the first 4 characters of airDate if available, and convert to Int
        if (airDate != null && airDate.length >= YEAR_LENGTH) {
          val yearStr = airDate.take(YEAR_LENGTH)
          yearStr.toIntOrNull()
        } else {
          null
        }
      }

    // if valid season years exist, return a year or range
    if (!seasonYears.isNullOrEmpty()) {
      return if (seasonYears.size > 1) {
        // format as year range, e.g., "2018–2025"
        "${seasonYears.minOrNull()}-${seasonYears.maxOrNull()}"
      } else {
        // only one valid season year, return as string
        seasonYears.first().toString()
      }
    }

    // fallback: use the show's firstAirDate if no valid seasons exist
    val yearStr = data.firstAirDate?.takeIf { it.length >= YEAR_LENGTH }?.take(YEAR_LENGTH)
    val firstAirYear = yearStr?.toIntOrNull()

    // return the fallback year or an empty string if invalid
    return firstAirYear?.toString().orEmpty()
  }

  /**
   * Get origin country of the series
   *
   * @param data A list of `DetailTv` objects representing tv data.
   *
   * @return string of origin country.
   */
  private fun getOriginalCountryTv(data: TvDetail): String =
    data.listOriginCountry?.firstOrNull().orEmpty()
}
