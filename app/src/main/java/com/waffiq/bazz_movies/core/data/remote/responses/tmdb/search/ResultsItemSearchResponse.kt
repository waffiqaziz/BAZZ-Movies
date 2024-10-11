package com.waffiq.bazz_movies.core.data.remote.responses.tmdb.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ResultsItemSearchResponse(

  @Json(name = "media_type")
  val mediaType: String? = null,

  @Json(name = "known_for")
  val listKnownFor: List<KnownForItemResponse>? = null,

  @Json(name = "known_for_department")
  val knownForDepartment: String? = null,

  @Json(name = "popularity")
  val popularity: Double? = null,

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "profile_path")
  val profilePath: String? = null,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "adult")
  val adult: Boolean? = null,

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
  val listGenreIds: List<Int>? = null,

  @Json(name = "poster_path")
  val posterPath: String? = null,

  @Json(name = "backdrop_path")
  val backdropPath: String? = null,

  @Json(name = "release_date")
  val releaseDate: String? = null,

  @Json(name = "vote_average")
  val voteAverage: Double? = null,

  @Json(name = "vote_count")
  val voteCount: Double? = null,

  @Json(name = "first_air_date")
  val firstAirDate: String? = null,

  @Json(name = "origin_country")
  val listOriginCountry: List<String?>? = null,

  @Json(name = "original_name")
  val originalName: String? = null
)
