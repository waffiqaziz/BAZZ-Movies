package com.waffiq.bazz_movies.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class Favorite(
  @Json(name="media_type") val mediaType: String?,
  @Json(name="media_id") val mediaId: Int?,
  @Json(name="favorite") val favorite: Boolean?,
)

@JsonClass(generateAdapter = false)
data class Watchlist(
  @Json(name="media_type") val mediaType: String?,
  @Json(name="media_id") val mediaId: Int?,
  @Json(name="watchlist") val watchlist: Boolean?,
)

@JsonClass(generateAdapter = false)
data class Rate(
  @Json(name="value") val value: Any,
)

@JsonClass(generateAdapter = false)
data class SessionID(
  @Json(name="session_id") val sessionID: String,
)
