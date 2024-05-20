package com.waffiq.bazz_movies.domain.model

import com.waffiq.bazz_movies.data.remote.response.tmdb.KnownForItem

data class ResultsItemSearch(
  val mediaType: String? = null,
  val knownFor: List<KnownForItem>? = null,
  val knownForDepartment: String? = null,
  val popularity: Double? = null,
  val name: String? = null,
  val profilePath: String? = null,
  val id: Int? = null,
  val adult: Boolean? = null,
  val overview: String? = null,
  val originalLanguage: String? = null,
  val originalTitle: String? = null,
  val video: Boolean? = null,
  val title: String? = null,
  val genreIds: List<Int>? = null,
  val posterPath: String? = null,
  val backdropPath: String? = null,
  val releaseDate: String? = null,
  val voteAverage: Double? = null,
  val voteCount: Double? = null,
  val firstAirDate: String? = null,
  val originCountry: List<String?>? = null,
  val originalName: String? = null
)