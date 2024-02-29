package com.waffiq.bazz_movies.data.local.model

import com.google.gson.annotations.SerializedName

data class Favorite(
  @SerializedName("media_type") val mediaType: String?,
  @SerializedName("media_id") val mediaId: Int?,
  @SerializedName("favorite") var favorite: Boolean?,
)

data class Watchlist(
  @SerializedName("media_type") val mediaType: String?,
  @SerializedName("media_id") val mediaId: Int?,
  @SerializedName("watchlist") var watchlist: Boolean?,
)

data class Rate(
  @SerializedName("value") val value: Any,
)

data class SessionID(
  @SerializedName("session_id") val sessionID: String,
)
