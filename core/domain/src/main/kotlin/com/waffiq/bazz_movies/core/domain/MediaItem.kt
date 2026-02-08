package com.waffiq.bazz_movies.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaItem(
  val firstAirDate: String? = null,
  val overview: String? = null,
  val originalLanguage: String? = null,
  val listGenreIds: List<Int>? = null,
  val posterPath: String? = null,
  val backdropPath: String? = null,
  val mediaType: String = "movie",
  val originalName: String? = null,
  val popularity: Double? = 0.0,
  val voteAverage: Float? = 0f,
  val name: String? = null,
  val id: Int = 0,
  val adult: Boolean = false,
  val voteCount: Int = 0,
  val originalTitle: String? = null,
  val video: Boolean = false,
  val title: String? = null,
  val releaseDate: String? = null,
  val originCountry: List<String>? = null,
) : Parcelable
