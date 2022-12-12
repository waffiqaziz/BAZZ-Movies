package com.waffiq.bazz_movies.data.remote.response

import com.google.gson.annotations.SerializedName

data class TrendingAllResponse(

	@SerializedName("page")
	val page: Int,

	@SerializedName("total_pages")
	val totalPages: Int,

	@SerializedName("results")
	val results: List<ResultItem>,

	@SerializedName("total_results")
	val totalResults: Int
)

data class ResultItem(

	@SerializedName("first_air_date")
	val firstAirDate: String,

	@SerializedName("overview")
	val overview: String,

	@SerializedName("original_language")
	val originalLanguage: String,

//	@SerializedName("genre_ids")
//	val genreIds: List<Int>,

	@SerializedName("poster_path")
	val posterPath: String,

//	@SerializedName("origin_country")
//	val originCountry: List<String?>,

	@SerializedName("backdrop_path")
	val backdropPath: String,

	@SerializedName("media_type")
	val mediaType: String,

	@SerializedName("original_name")
	val originalName: String,

	@SerializedName("popularity")
	val popularity: Double,

	@SerializedName("vote_average")
	val voteAverage: Double,

	@SerializedName("name")
	val name: String,

	@SerializedName("id")
	val id: Int,

	@SerializedName("adult")
	val adult: Boolean,

	@SerializedName("vote_count")
	val voteCount: Int,

	@SerializedName("original_title")
	val originalTitle: String,

	@SerializedName("video")
	val video: Boolean,

	@SerializedName("title")
	val title: String,

	@SerializedName("release_date")
	val releaseDate: String
)