package com.waffiq.bazz_movies.data.remote.response.tmdb

data class AgeRatingResponse(
	val id: Int? = null,
	val results: List<ResultsItem?>? = null
)

data class ResultsItem(
	val releaseDates: List<ReleaseDatesItem?>? = null,
	val iso31661: String? = null
)

data class ReleaseDatesItem(
	val descriptors: List<Any?>? = null,
	val note: String? = null,
	val releaseDate: String? = null,
	val type: Int? = null,
	val iso6391: String? = null,
	val certification: String? = null
)

