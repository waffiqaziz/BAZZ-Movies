package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class MediaCreditsResponse(

  @Json(name = "cast")
  val cast: List<MediaCastResponseItem>,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "crew")
  val crew: List<MediaCrewResponseItem>
)
