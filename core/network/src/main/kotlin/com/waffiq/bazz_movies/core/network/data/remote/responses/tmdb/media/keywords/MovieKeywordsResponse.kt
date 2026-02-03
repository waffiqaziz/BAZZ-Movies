package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords

import com.squareup.moshi.Json

data class MovieKeywordsResponse(

  @Json(name = "keywords")
  val keywords: List<MediaKeywordsResponseItem?>? = null,

  @Json(name = "id")
  val id: Int? = null
)
