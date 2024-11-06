package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.movie.DetailMovie
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItemValue
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.DetailTv

/**
 * Used to retrieving age ratings based on the user's or others region
 */
object AgeRatingHelper {
  // region CALCULATE AGE RATING MOVIE
  fun getAgeRating(
    data: DetailMovie?,
    userRegion: String,
  ): String {
    // if age rating based on user region return empty, get age rating from any region
    return getTransformAgeRatingMovie(data, userRegion).takeIf { it.isNotEmpty() }
      ?: getTransformAgeRatingMovie(data, "false")
  }

  private fun getTransformAgeRatingMovie(data: DetailMovie?, region: String): String {
    return if (region != "false") { // find age rating based on user region
      data?.releaseDates?.listReleaseDatesItem
        ?.find { it?.iso31661 == region }
        ?.let { regionItem ->
          regionItem.listReleaseDatesitemValue
            ?.find { it.certification?.isNotEmpty() == true }
            ?.certification
        } ?: ""
    } else { // find age rating from any country
      data?.releaseDates?.listReleaseDatesItem
        ?.asSequence() // Convert to sequence for lazy evaluation
        ?.flatMap {
          it?.listReleaseDatesitemValue?.asSequence() ?: emptySequence()
        } // Flatten the list of release dates
        ?.find { it.certification?.isNotEmpty() == true } // Find the first non-null and non-empty certification
        ?.certification ?: ""
    }
  }
  // endregion CALCULATE AGE RATING MOVIE

  // region CALCULATE AGE RATING TV
  fun getAgeRating(
    data: DetailTv?,
    userRegion: String,
  ): String {
    // if age rating based on user region return empty, get age rating from US
    return getTransformAgeRatingTv(data, userRegion).takeIf { it.isNotEmpty() }
      ?: getTransformAgeRatingTv(data, "false")
  }

  private fun getTransformAgeRatingTv(data: DetailTv?, region: String): String =
    if (region != "false") {
      data?.contentRatings?.contentRatingsItem
        ?.filter { it?.iso31661 == "US" || it?.iso31661 == region } // Filter by US or specific region
        ?.mapNotNull { contentRatingsItem -> // Map to ratings and exclude empty/null values
          contentRatingsItem?.rating?.takeIf { it.isNotEmpty() }
        }
        ?.joinToString(separator = ", ") ?: ""
    } else {
      data?.contentRatings?.contentRatingsItem
        ?.mapNotNull { contentRatingsItem -> // Map to ratings and exclude empty/null values
          contentRatingsItem?.rating?.takeIf { it.isNotEmpty() }
        }
        ?.joinToString(separator = ", ") ?: ""
    }
  // endregion CALCULATE AGE RATING TV
}
