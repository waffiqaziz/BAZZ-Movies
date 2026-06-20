package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoResponse(

  @Json(name = "results")
  val results: List<VideoResponseItem>,
)
