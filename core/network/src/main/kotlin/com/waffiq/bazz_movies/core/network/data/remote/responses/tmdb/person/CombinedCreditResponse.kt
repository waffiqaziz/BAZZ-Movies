package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CombinedCreditResponse(

  @Json(name = "cast")
  val cast: List<CastResponseItem>? = null,

  @Json(name = "crew")
  val crew: List<CrewResponseItem>? = null,
)
