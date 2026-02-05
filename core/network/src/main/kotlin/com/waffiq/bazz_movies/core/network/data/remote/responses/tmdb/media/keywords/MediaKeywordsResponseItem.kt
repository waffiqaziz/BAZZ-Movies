package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords

import com.squareup.moshi.Json

data class MediaKeywordsResponseItem(

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "id")
  val id: Int? = null,
)
