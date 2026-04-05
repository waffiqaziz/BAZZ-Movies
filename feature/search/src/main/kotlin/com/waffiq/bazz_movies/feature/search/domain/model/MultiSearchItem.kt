package com.waffiq.bazz_movies.feature.search.domain.model

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.Dateable
import com.waffiq.bazz_movies.core.domain.Imageble
import com.waffiq.bazz_movies.core.domain.ProfileImageable
import com.waffiq.bazz_movies.core.domain.Titleable

data class MultiSearchItem(
  override val name: String? = null,
  override val originalName: String? = null,
  override val title: String? = null,
  override val originalTitle: String? = null,
  override val posterPath: String? = null,
  override val backdropPath: String? = null,
  override val releaseDate: String? = null,
  override val firstAirDate: String? = null,
  override val profilePath: String? = null,
  val mediaType: String = MOVIE_MEDIA_TYPE,
  val listKnownFor: List<KnownForItem>? = null,
  val knownForDepartment: String? = null,
  val popularity: Double = 0.0,
  val id: Int,
  val adult: Boolean = false,
  val overview: String? = null,
  val originalLanguage: String? = null,
  val video: Boolean = false,
  val listGenreIds: List<Int>? = null,
  val voteAverage: Double = 0.0,
  val voteCount: Double = 0.0,
  val listOriginCountry: List<String?>? = null,
) : Imageble,
  Dateable,
  ProfileImageable,
  Titleable
