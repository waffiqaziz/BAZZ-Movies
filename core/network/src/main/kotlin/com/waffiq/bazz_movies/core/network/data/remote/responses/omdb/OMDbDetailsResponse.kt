package com.waffiq.bazz_movies.core.network.data.remote.responses.omdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class OMDbDetailsResponse(

  @Json(name = "Metascore")
  val metascore: String? = null,

  @Json(name = "BoxOffice")
  val boxOffice: String? = null,

  @Json(name = "Website")
  val website: String? = null,

  @Json(name = "imdbRating")
  val imdbRating: String? = null,

  @Json(name = "imdbVotes")
  val imdbVotes: String? = null,

  @Json(name = "Ratings")
  val ratings: List<RatingsItemResponse>? = null,

  @Json(name = "Runtime")
  val runtime: String? = null,

  @Json(name = "Language")
  val language: String? = null,

  @Json(name = "Rated")
  val rated: String? = null,

  @Json(name = "Production")
  val production: String? = null,

  @Json(name = "Released")
  val released: String? = null,

  @Json(name = "imdbID")
  val imdbID: String? = null,

  @Json(name = "Plot")
  val plot: String? = null,

  @Json(name = "Director")
  val director: String? = null,

  @Json(name = "Title")
  val title: String? = null,

  @Json(name = "Actors")
  val actors: String? = null,

  @Json(name = "Response")
  val response: String? = null,

  @Json(name = "Type")
  val type: String? = null,

  @Json(name = "Awards")
  val awards: String? = null,

  @Json(name = "DVD")
  val dVD: String? = null,

  @Json(name = "Year")
  val year: String? = null,

  @Json(name = "Poster")
  val poster: String? = null,

  @Json(name = "Country")
  val country: String? = null,

  @Json(name = "Genre")
  val genre: String? = null,

  @Json(name = "Writer")
  val writer: String? = null
)

@JsonClass(generateAdapter = false)
data class RatingsItemResponse(

  @Json(name = "Value")
  val value: String? = null,

  @Json(name = "Source")
  val source: String? = null
)
