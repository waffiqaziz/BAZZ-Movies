package com.waffiq.bazz_movies.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Used as data class for guest user favorite n watchlist
 */
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
  val isWatchlist: Boolean,
) : Parcelable
