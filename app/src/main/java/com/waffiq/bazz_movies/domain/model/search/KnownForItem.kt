package com.waffiq.bazz_movies.domain.model.search

data class KnownForItem(
  val overview: String? = null,
  val originalLanguage: String? = null,
  val originalTitle: String? = null,
  val video: Boolean? = null,
  val title: String? = null,
  val genreIds: List<Int?>? = null,
  val posterPath: String? = null,
  val backdropPath: String? = null,
  val releaseDate: String? = null,
  val mediaType: String? = null,
  val popularity: Double? = null,
  val voteAverage: Double? = null,
  val id: Int? = null,
  val adult: Boolean? = null,
  val voteCount: Int? = null,
  val firstAirDate: String? = null,
  val originCountry: List<String?>? = null,
  val originalName: String? = null,
  val name: String? = null
)
