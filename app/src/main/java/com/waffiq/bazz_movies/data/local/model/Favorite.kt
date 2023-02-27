package com.waffiq.bazz_movies.data.local.model

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
