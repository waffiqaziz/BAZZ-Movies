package com.waffiq.bazz_movies.data.remote.responses.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ResultItemResponse(

  @Json(name = "first_air_date")
  val firstAirDate: String? = null,

  @Json(name = "overview")
  val overview: String? = null,

  @Json(name = "original_language")
  val originalLanguage: String? = null,

  @Json(name = "genre_ids")
  val genreIds: List<Int>? = null,

  @Json(name = "poster_path")
  val posterPath: String? = null,

  @Json(name = "backdrop_path")
  val backdropPath: String? = null,

  @Json(name = "media_type")
  val mediaType: String? = null,

  @Json(name = "original_name")
  val originalName: String? = null,

  @Json(name = "popularity")
  val popularity: Double? = null,

  @Json(name = "vote_average")
  val voteAverage: Float? = null,

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "adult")
  val adult: Boolean? = null,

  @Json(name = "vote_count")
  val voteCount: Int? = null,

  @Json(name = "original_title")
  val originalTitle: String? = null,

  @Json(name = "video")
  val video: Boolean? = null,

  @Json(name = "title")
  val title: String? = null,

  @Json(name = "release_date")
  val releaseDate: String? = null
)
