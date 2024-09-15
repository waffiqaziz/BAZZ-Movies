package com.waffiq.bazz_movies.data.remote.responses.tmdb.account

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class AccountDetailsResponse(

  @Json(name = "include_adult")
  val includeAdult: Boolean? = null,

  @Json(name = "iso_3166_1")
  val iso31661: String? = null,

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "avatar")
  val avatarItemResponse: AvatarItemResponse? = null,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "iso_639_1")
  val iso6391: String? = null,

  @Json(name = "username")
  val username: String? = null
)
