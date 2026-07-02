package com.waffiq.bazz_movies.feature.detail.domain.model.movie

import com.waffiq.bazz_movies.core.models.Imageble

data class PartsItem(
  val id: Int? = null,
  val title: String? = null,
  val originalTitle: String? = null,
  override val posterPath: String? = null,
  override val backdropPath: String? = null,
  val overview: String? = null,
  val originalLanguage: String? = null,
  val video: Boolean? = null,
  val genreIds: List<Int>? = null,
  val mediaType: String? = null,
  val releaseDate: String? = null,
  val popularity: Float? = null,
  val voteAverage: Float? = null,
  val adult: Boolean? = null,
  val voteCount: Int? = null,
) : Imageble
