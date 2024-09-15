package com.waffiq.bazz_movies.data.remote.responses.tmdb.person

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class CombinedCreditResponse(

  @Json(name = "cast")
  val cast: List<CastItemResponse>? = null,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "crew")
  val crew: List<CrewItemResponse>? = null
)
