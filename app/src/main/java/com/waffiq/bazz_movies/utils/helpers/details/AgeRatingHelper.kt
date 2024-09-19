package com.waffiq.bazz_movies.utils.helpers.details

import com.waffiq.bazz_movies.domain.model.detail.DetailMovie
import com.waffiq.bazz_movies.domain.model.detail.tv.DetailTv

object AgeRatingHelper {
  // region CALCULATE AGE RATING MOVIE
  fun getAgeRating(
    data: DetailMovie?,
    userRegion: String,
  ): String {
    // get age rating based on region
    val certification = getTransformAgeRatingMovie(data, userRegion)

    // if certification return empty, get age rating from US as default
    return certification.takeIf { it.isNotEmpty() } ?: getTransformAgeRatingMovie(data, "false")
  }

  private fun getTransformAgeRatingMovie(data: DetailMovie?, region: String): String {
    return if (region != "false") {
      data?.releaseDates?.listReleaseDatesItem
        ?.find { it?.iso31661 == region }
        ?.let { regionItem ->
          regionItem.listReleaseDatesitemValue
            ?.find { it.certification?.isNotEmpty() == true }
            ?.certification
        } ?: ""
    } else {
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
    // get age rating based on region
    val certification = getTransformAgeRatingTv(data, userRegion)

    // if certification return empty, get age rating from others
    return certification.takeIf { it.isNotEmpty() } ?: getTransformAgeRatingTv(data, "false")
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