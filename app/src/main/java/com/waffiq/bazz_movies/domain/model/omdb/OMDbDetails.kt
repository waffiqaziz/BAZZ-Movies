package com.waffiq.bazz_movies.domain.model.omdb

data class OMDbDetails(
  val metascore: String? = null,
  val boxOffice: String? = null,
  val website: String? = null,
  val imdbRating: String? = null,
  val imdbVotes: String? = null,
  val ratings: List<RatingsItem>? = null,
  val runtime: String? = null,
  val language: String? = null,
  val rated: String? = null,
  val production: String? = null,
  val released: String? = null,
  val imdbID: String? = null,
  val plot: String? = null,
  val director: String? = null,
  val title: String? = null,
  val actors: String? = null,
  val response: String? = null,
  val type: String? = null,
  val awards: String? = null,
  val dVD: String? = null,
  val year: String? = null,
  val poster: String? = null,
  val country: String? = null,
  val genre: String? = null,
  val writer: String? = null
)
