package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class GravatarResponse(

  @Json(name = "hash")
  val hash: String? = null,
)
