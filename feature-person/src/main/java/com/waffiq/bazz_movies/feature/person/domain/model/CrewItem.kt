package com.waffiq.bazz_movies.feature.person.domain.model

data class CrewItem(
  val overview: String? = null,
  val originalLanguage: String? = null,
  val originalTitle: String? = null,
  val video: Boolean? = null,
  val title: String? = null,
  val genreIds: List<Int?>? = null,
  val posterPath: String? = null,
  val backdropPath: String? = null,
  val releaseDate: String? = null,
  val creditId: String? = null,
  val mediaType: String? = null,
  val popularity: Float? = null,
  val voteAverage: Float? = null,
  val id: Int? = null,
  val adult: Boolean? = null,
  val department: String? = null,
  val job: String? = null,
  val voteCount: Int? = null
)
