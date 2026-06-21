package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TvKeywordsResponse(

  @Json(name = "results")
  override val keywords: List<MediaKeywordsResponseItem?>? = null,
) : KeywordsResponse
