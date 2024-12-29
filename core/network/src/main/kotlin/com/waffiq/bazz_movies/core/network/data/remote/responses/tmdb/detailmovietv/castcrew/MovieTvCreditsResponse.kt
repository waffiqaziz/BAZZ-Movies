package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.castcrew

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class MovieTvCreditsResponse(

  @Json(name = "cast")
  val cast: List<MovieTvCastItemResponse>,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "crew")
  val crew: List<MovieTvCrewItemResponse>
)
