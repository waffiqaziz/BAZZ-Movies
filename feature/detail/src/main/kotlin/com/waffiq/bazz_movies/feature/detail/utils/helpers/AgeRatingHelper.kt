package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.DetailTv

/**
 * Used to retrieving age ratings based on the user's or others region
 */
@Suppress("unused")
object AgeRatingHelper {

  private const val FLAG_ANY_COUNTRY = "any country"

  // region CALCULATE AGE RATING MOVIE
  fun getAgeRating(
    data: MovieDetail?,
    userRegion: String,
  ): String {
    // if age rating based on user region return empty, get age rating from any region
    return getTransformAgeRatingMovie(data, userRegion).takeIf { it.isNotEmpty() }
      ?: getTransformAgeRatingMovie(data, FLAG_ANY_COUNTRY)
  }

  private fun getTransformAgeRatingMovie(data: MovieDetail?, region: String): String {
    // return early if data is null
    if (data == null || data.releaseDates == null || data.releaseDates.listReleaseDatesItem == null) {
      return ""
    }

    val releaseDatesItems = data.releaseDates.listReleaseDatesItem

    return if (region != FLAG_ANY_COUNTRY) { // find age rating based on user region
      releaseDatesItems
        .find { it?.iso31661 == region }
        ?.listReleaseDatesItemValue
        ?.find { !it.certification.isNullOrEmpty() }
        ?.certification.orEmpty()
    } else { // find age rating from any country
      releaseDatesItems
        .asSequence()
        .flatMap { item -> item?.listReleaseDatesItemValue?.asSequence() ?: emptySequence() }
        .firstOrNull { !it.certification.isNullOrEmpty() }
        ?.certification.orEmpty()
    }
  }
  // endregion CALCULATE AGE RATING MOVIE

  // region CALCULATE AGE RATING TV
  fun getAgeRating(
    data: DetailTv?,
    userRegion: String,
  ): String {
    // if age rating based on user region return empty, get age rating from any region
    return getTransformAgeRatingTv(data, userRegion).takeIf { it.isNotEmpty() }
      ?: getTransformAgeRatingTv(data, "false")
  }

  private fun getTransformAgeRatingTv(data: DetailTv?, region: String): String =
    if (region != "false") {
      // try to find rating for the specific user region
      data?.contentRatings?.contentRatingsItem
        ?.find { it?.iso31661 == region }
        ?.rating?.takeIf { it.isNotEmpty() }
        ?: // if not found or empty, try US as fallback
        data?.contentRatings?.contentRatingsItem
          ?.find { it?.iso31661 == "US" }
          ?.rating?.takeIf { it.isNotEmpty() }.orEmpty()
    } else {
      data?.contentRatings?.contentRatingsItem
        // Map to ratings, exclude empty/null values and get the first value
        ?.firstNotNullOfOrNull { contentRatingsItem ->
          contentRatingsItem?.rating?.takeIf { it.isNotEmpty() }
        }.orEmpty() // get the first
    }
  // endregion CALCULATE AGE RATING TV
}
