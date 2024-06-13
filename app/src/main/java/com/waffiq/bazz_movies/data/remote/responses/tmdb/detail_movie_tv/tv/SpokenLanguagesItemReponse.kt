package com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class SpokenLanguagesItemReponse(

	@Json(name="name")
	val name: String? = null,

	@Json(name="iso_639_1")
	val iso6391: String? = null,

	@Json(name="english_name")
	val englishName: String? = null
)