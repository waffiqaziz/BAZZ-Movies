package com.waffiq.bazz_movies.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaItem(
  override val name: String? = null,
  override val originalName: String? = null,
  override val title: String? = null,
  override val originalTitle: String? = null,
  override val posterPath: String? = null,
  override val backdropPath: String? = null,
  override val releaseDate: String? = null,
  override val firstAirDate: String? = null,
  val overview: String? = null,
  val originalLanguage: String? = null,
  val listGenreIds: List<Int>? = null,
  val mediaType: String = "movie",
  val popularity: Double? = 0.0,
  val voteAverage: Float? = 0f,
  val id: Int = 0,
  val adult: Boolean = false,
  val voteCount: Int = 0,
  val video: Boolean = false,
  val originCountry: List<String>? = null,
) : Dateable,
  Imageble,
  Parcelable,
  Titleable
