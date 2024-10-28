package com.waffiq.bazz_movies.core.domain.model.person

import com.waffiq.bazz_movies.core.utils.common.Constants.MOVIE_MEDIA_TYPE

data class CastItem(
  val firstAirDate: String? = null,
  val overview: String? = null,
  val originalLanguage: String? = null,
  val episodeCount: Int = 0,
  val listGenreIds: List<Int>? = null,
  val posterPath: String? = null,
  val listOriginCountry: List<String>? = null,
  val backdropPath: String? = null,
  val character: String? = null,
  val creditId: String? = null,
  val mediaType: String = MOVIE_MEDIA_TYPE,
  val originalName: String? = null,
  val popularity: Double = 0.0,
  val voteAverage: Float = 0f,
  val name: String? = null,
  val id: Int = 0,
  val adult: Boolean = false,
  val voteCount: Int = 0,
  val originalTitle: String? = null,
  val video: Boolean = false,
  val title: String? = null,
  val releaseDate: String? = null,
  val order: Int = 0
)
