package com.waffiq.bazz_movies.navigation

import android.os.Parcelable
import com.waffiq.bazz_movies.core.common.MediaType
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaArgs(
  val name: String? = null,
  val originalName: String? = null,
  val title: String? = null,
  val originalTitle: String? = null,
  val posterPath: String? = null,
  val backdropPath: String? = null,
  val releaseDate: String? = null,
  val firstAirDate: String? = null,
  val id: Int = 0,
  val mediaType: MediaType,
  val overview: String? = null,
  val originalLanguage: String? = null,
  val listGenreIds: List<Int>? = null,
  val video: Boolean = false,
) : Parcelable
