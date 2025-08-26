package com.waffiq.bazz_movies.core.domain

/**
 * Represents the basic data structure for media models.
 * Used in various operations like watchlist and favorite actions.
 */
interface MediaData {
  val mediaId: Int
  val mediaType: String
}
