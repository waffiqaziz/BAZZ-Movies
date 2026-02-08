package com.waffiq.bazz_movies.feature.search.domain.model

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE

data class MultiSearchItem(
  val mediaType: String = MOVIE_MEDIA_TYPE,
  val listKnownFor: List<KnownForItem>? = null,
  val knownForDepartment: String? = null,
  val popularity: Double = 0.0,
  val name: String? = null,
  val profilePath: String? = null,
  val id: Int,
  val adult: Boolean = false,
  val overview: String? = null,
  val originalLanguage: String? = null,
  val originalTitle: String? = null,
  val video: Boolean = false,
  val title: String? = null,
  val listGenreIds: List<Int>? = null,
  val posterPath: String? = null,
  val backdropPath: String? = null,
  val releaseDate: String? = null,
  val voteAverage: Double = 0.0,
  val voteCount: Double = 0.0,
  val firstAirDate: String? = null,
  val listOriginCountry: List<String?>? = null,
  val originalName: String? = null,
)
