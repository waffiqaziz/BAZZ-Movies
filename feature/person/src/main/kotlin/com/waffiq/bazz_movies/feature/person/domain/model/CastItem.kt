package com.waffiq.bazz_movies.feature.person.domain.model

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.Imageble
import com.waffiq.bazz_movies.core.domain.Titleable

data class CastItem(
  override val name: String? = null,
  override val originalName: String? = null,
  override val title: String? = null,
  override val originalTitle: String? = null,
  override val posterPath: String? = null,
  override val backdropPath: String? = null,
  val firstAirDate: String? = null,
  val overview: String? = null,
  val originalLanguage: String? = null,
  val episodeCount: Int = 0,
  val listGenreIds: List<Int>? = null,
  val listOriginCountry: List<String>? = null,
  val character: String? = null,
  val creditId: String? = null,
  val mediaType: String = MOVIE_MEDIA_TYPE,
  val popularity: Double = 0.0,
  val voteAverage: Float = 0f,
  val id: Int = 0,
  val adult: Boolean = false,
  val voteCount: Int = 0,
  val video: Boolean = false,
  val releaseDate: String? = null,
  val order: Int = 0,
) : Imageble,
  Titleable
