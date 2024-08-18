package com.waffiq.bazz_movies.data.remote.post_body

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class FavoritePostModel(
  @Json(name="media_type") val mediaType: String,
  @Json(name="media_id") val mediaId: Int,
  @Json(name="favorite") val favorite: Boolean,
)