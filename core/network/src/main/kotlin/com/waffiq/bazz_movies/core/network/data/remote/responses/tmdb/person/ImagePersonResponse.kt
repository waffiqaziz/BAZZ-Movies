package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ImagePersonResponse(

  @Json(name = "profiles")
  val profiles: List<ProfilesItemResponse>? = null,

  @Json(name = "id")
  val id: Int? = null,
)
