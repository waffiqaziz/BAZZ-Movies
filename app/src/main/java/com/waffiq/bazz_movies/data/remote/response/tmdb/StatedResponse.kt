package com.waffiq.bazz_movies.data.remote.response.tmdb

import com.google.gson.annotations.SerializedName

data class StatedResponse(
  @field:SerializedName("id")
  val id: Int? = null,

  @field:SerializedName("favorite")
  val favorite: Boolean? = null,

  @field:SerializedName("rated")
  val rated: Any? = null,

  @field:SerializedName("watchlist")
  val watchlist: Boolean? = null,
)

data class PostFavoriteWatchlistResponse(
  @field:SerializedName("status_code")
  val statusCode: Int? = null,

  @field:SerializedName("status_message")
  val statusMessage: String? = null,
)




