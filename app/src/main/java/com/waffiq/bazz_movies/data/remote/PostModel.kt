package com.waffiq.bazz_movies.data.remote

import com.google.gson.annotations.SerializedName

data class Favorite(
  @SerializedName("media_type") val mediaType: String?,
  @SerializedName("media_id") val mediaId: Int?,
  @SerializedName("favorite") val favorite: Boolean?,
)

data class Watchlist(
  @SerializedName("media_type") val mediaType: String?,
  @SerializedName("media_id") val mediaId: Int?,
  @SerializedName("watchlist") val watchlist: Boolean?,
)

data class Rate(
  @SerializedName("value") val value: Any,
)

data class SessionID(
  @SerializedName("session_id") val sessionID: String,
)