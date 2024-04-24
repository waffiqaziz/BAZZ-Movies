package com.waffiq.bazz_movies.data.remote.response.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class DetailMovieResponse(

  @Json(name="original_language")
  val originalLanguage: String? = null,

  @Json(name="imdb_id")
  val imdbId: String? = null,

  @Json(name="video")
  val video: Boolean? = null,

  @Json(name="title")
  val title: String? = null,

  @Json(name="backdrop_path")
  val backdropPath: String? = null,

  @Json(name="revenue")
  val revenue: Long? = null,

  @Json(name="genres")
  val genres: List<GenresItem?>? = null,

  @Json(name="popularity")
  val popularity: Double? = null,

  @Json(name="release_dates")
  val releaseDates: ReleaseDates? = null,

  @Json(name="production_countries")
  val productionCountries: List<ProductionCountriesItem?>? = null,

  @Json(name="id")
  val id: Int? = null,

  @Json(name="vote_count")
  val voteCount: Int? = null,

  @Json(name="budget")
  val budget: Int? = null,

  @Json(name="overview")
  val overview: String? = null,

  @Json(name="original_title")
  val originalTitle: String? = null,

  @Json(name="runtime")
  val runtime: Int? = null,

  @Json(name="poster_path")
  val posterPath: String? = null,

  @Json(name="spoken_languages")
  val spokenLanguages: List<SpokenLanguagesItem?>? = null,

  @Json(name="production_companies")
  val productionCompanies: List<ProductionCompaniesItem?>? = null,

  @Json(name="release_date")
  val releaseDate: String? = null,

  @Json(name="vote_average")
  val voteAverage: Double? = null,

  @Json(name="belongs_to_collection")
  val belongsToCollection: BelongsToCollection? = null,

  @Json(name="tagline")
  val tagline: String? = null,

  @Json(name="adult")
  val adult: Boolean? = null,

  @Json(name="homepage")
  val homepage: String? = null,

  @Json(name="status")
  val status: String? = null
)

@JsonClass(generateAdapter = false)
data class ReleaseDates(
  @Json(name="results")
  val results: List<ReleaseDatesItem?>? = null
)

@JsonClass(generateAdapter = false)
data class ReleaseDatesItem(
  @Json(name="iso_3166_1")
  val iso31661: String? = null,

  @Json(name="release_dates")
  val releaseDateValue: List<ReleaseDatesItemValue>? = null,
)

@JsonClass(generateAdapter = false)
data class ReleaseDatesItemValue(
  @Json(name="descriptors")
  val descriptors: List<Any?>? = null,

  @Json(name="note")
  val note: String? = null,

  @Json(name="type")
  val type: Int? = null,

  @Json(name="iso_639_1")
  val iso6391: String? = null,

  @Json(name="certification")
  val certification: String? = null
)

@JsonClass(generateAdapter = false)
data class ProductionCountriesItem(

  @Json(name="iso_3166_1")
  val iso31661: String? = null,

  @Json(name="name")
  val name: String? = null,

  @Json(name="type")
  val type: Int? = null,

  @Json(name="iso_639_1")
  val iso6391: String? = null,

  @Json(name="certification")
  val certification: String? = null
)

@JsonClass(generateAdapter = false)
data class BelongsToCollection(

  @Json(name="backdrop_path")
  val backdropPath: String? = null,

  @Json(name="name")
  val name: String? = null,

  @Json(name="id")
  val id: Int? = null,

  @Json(name="poster_path")
  val posterPath: String? = null
)


