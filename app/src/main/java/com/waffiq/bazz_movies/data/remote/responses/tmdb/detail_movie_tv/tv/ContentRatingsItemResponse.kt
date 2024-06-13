package com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ContentRatingsItemResponse(

	@Json(name="descriptors")
	val descriptors: List<Any?>? = null,

	@Json(name="iso_3166_1")
	val iso31661: String? = null,

	@Json(name="rating")
	val rating: String? = null
)