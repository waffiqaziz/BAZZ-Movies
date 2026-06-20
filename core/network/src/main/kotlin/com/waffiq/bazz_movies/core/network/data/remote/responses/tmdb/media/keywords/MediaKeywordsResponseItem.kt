package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MediaKeywordsResponseItem(

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "id")
  val id: Int? = null,
)
