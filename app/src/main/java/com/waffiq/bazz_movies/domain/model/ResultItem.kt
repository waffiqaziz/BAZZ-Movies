package com.waffiq.bazz_movies.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultItem(
  val firstAirDate: String? = null,
  val overview: String? = null,
  val originalLanguage: String? = null,
  val genreIds: List<Int>? = null,
  val posterPath: String? = null,
  val backdropPath: String? = null,
  val mediaType: String? = null,
  val originalName: String? = null,
  val popularity: Double? = null,
  val voteAverage: Float? = null,
  val name: String? = null,
  val id: Int? = null,
  val adult: Boolean? = null,
  val voteCount: Int? = null,
  val originalTitle: String? = null,
  val video: Boolean? = null,
  val title: String? = null,
  val releaseDate: String? = null
): Parcelable
