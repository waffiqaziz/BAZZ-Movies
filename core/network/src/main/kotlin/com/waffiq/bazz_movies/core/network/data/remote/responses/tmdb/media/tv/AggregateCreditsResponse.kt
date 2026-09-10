package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AggregateCreditsResponse(

  @Json(name = "cast")
  val cast: List<TvCastResponseItem?>? = null,

  @Json(name = "crew")
  val crew: List<TvCrewResponseItem?>? = null,
)
