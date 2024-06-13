package com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ContentRatingsResponse(

	@Json(name="results")
	val contentRatingsItemResponse: List<ContentRatingsItemResponse?>? = null
)