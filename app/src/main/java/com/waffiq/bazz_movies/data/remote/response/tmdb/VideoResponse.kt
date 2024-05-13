package com.waffiq.bazz_movies.data.remote.response.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class VideoResponse(

	@Json(name="id")
	val id: Int? = null,

	@Json(name="results")
	val results: List<VideoItemResponse>
)

@JsonClass(generateAdapter = false)
data class VideoItemResponse(

	@Json(name="site")
	val site: String? = null,

	@Json(name="size")
	val size: Int? = null,

	@Json(name="iso_3166_1")
	val iso31661: String? = null,

	@Json(name="name")
	val name: String,

	@Json(name="official")
	val official: Boolean? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="published_at")
	val publishedAt: String? = null,

	@Json(name="type")
	val type: String? = null,

	@Json(name="iso_639_1")
	val iso6391: String? = null,

	@Json(name="key")
	val key: String? = null
)
