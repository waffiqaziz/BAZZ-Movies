package com.waffiq.bazz_movies.data.remote.responses.tmdb.account

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class AvatarItemResponse(

  @Json(name = "tmdb")
  val avatarTMDbResponse: AvatarTMDbResponse? = null,

  @Json(name = "gravatar")
  val gravatarResponse: GravatarResponse? = null
)
