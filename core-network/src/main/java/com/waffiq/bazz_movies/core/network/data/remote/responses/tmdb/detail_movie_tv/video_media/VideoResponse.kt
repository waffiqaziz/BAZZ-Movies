package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detail_movie_tv.video_media

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class VideoResponse(

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "results")
  val results: List<VideoItemResponse>
)
