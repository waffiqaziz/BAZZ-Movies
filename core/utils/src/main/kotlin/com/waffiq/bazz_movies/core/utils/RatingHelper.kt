package com.waffiq.bazz_movies.core.utils

import java.text.DecimalFormat

object RatingHelper {

  /**
   * Handle rating and shows with format `rate/10`
   * For example: 10/10 or 8.5/10
   */
  fun ratingHandler(rating: Float?): String =
    DecimalFormat("#.#").format(rating ?: 0F).toString() + "/10"

  /**
   * Handle rating bar to set the value
   */
  fun setRatingBar(rating: Float?): Float = (rating ?: 0F) / 2
}
