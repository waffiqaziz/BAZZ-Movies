package com.waffiq.bazz_movies.core.data.remote.responses.tmdb.person

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class CrewItemResponse(

  @Json(name = "overview")
  val overview: String? = null,

  @Json(name = "original_language")
  val originalLanguage: String? = null,

  @Json(name = "original_title")
  val originalTitle: String? = null,

  @Json(name = "video")
  val video: Boolean? = null,

  @Json(name = "title")
  val title: String? = null,

  @Json(name = "genre_ids")
  val genreIds: List<Int?>? = null,

  @Json(name = "poster_path")
  val posterPath: String? = null,

  @Json(name = "backdrop_path")
  val backdropPath: String? = null,

  @Json(name = "release_date")
  val releaseDate: String? = null,

  @Json(name = "credit_id")
  val creditId: String? = null,

  @Json(name = "media_type")
  val mediaType: String? = null,

  @Json(name = "popularity")
  val popularity: Float? = null,

  @Json(name = "vote_average")
  val voteAverage: Float? = null,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "adult")
  val adult: Boolean? = null,

  @Json(name = "department")
  val department: String? = null,

  @Json(name = "job")
  val job: String? = null,

  @Json(name = "vote_count")
  val voteCount: Int? = null
)
