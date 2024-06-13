package com.waffiq.bazz_movies.data.remote.responses.tmdb.person

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ProfilesItemResponse(

	@Json(name="aspect_ratio")
	val aspectRatio: Any? = null,

	@Json(name="file_path")
	val filePath: String? = null,

	@Json(name="vote_average")
	val voteAverage: Float? = null,

	@Json(name="width")
	val width: Int? = null,

	@Json(name="iso_639_1")
	val iso6391: Any? = null,

	@Json(name="vote_count")
	val voteCount: Int? = null,

	@Json(name="height")
	val height: Int? = null
)