package com.waffiq.bazz_movies.core.database.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Favorite(
  val id: Int,
  val mediaId: Int,
  val mediaType: String,
  val genre: String,
  val backDrop: String,
  val poster: String,
  val overview: String,
  val title: String,
  val releaseDate: String,
  val popularity: Double,
  val rating: Float,
  val isFavorite: Boolean,
  val isWatchlist: Boolean
) : Parcelable
