package com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.cast_crew

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class MovieTvCreditsResponse(

	@Json(name="cast")
	val cast: List<MovieTvCastItemResponse>,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="crew")
	val crew: List<MovieTvCrewItemResponse>
)