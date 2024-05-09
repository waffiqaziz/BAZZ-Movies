package com.waffiq.bazz_movies.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class FavoritePostModel(
  @Json(name="media_type") val mediaType: String?,
  @Json(name="media_id") val mediaId: Int?,
  @Json(name="favorite") val favorite: Boolean?,
)

@JsonClass(generateAdapter = false)
data class WatchlistPostModel(
  @Json(name="media_type") val mediaType: String?,
  @Json(name="media_id") val mediaId: Int?,
  @Json(name="watchlist") val watchlist: Boolean?,
)

@JsonClass(generateAdapter = false)
data class RatePostModel(
  @Json(name = "value") val value: Any,
)

@JsonClass(generateAdapter = false)
data class SessionIDPostModel(
  @Json(name = "session_id") val sessionID: String,
)

data class PostModelState(
  val isSuccess: Boolean,
  val isDelete: Boolean,
  val isFavorite: Boolean,
  val isWatchlist: Boolean
)