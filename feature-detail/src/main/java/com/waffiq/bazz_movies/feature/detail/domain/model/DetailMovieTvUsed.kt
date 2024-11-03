package com.waffiq.bazz_movies.feature.detail.domain.model

import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDateRegion

data class DetailMovieTvUsed(
  val id: Int,
  val genre: String? = null,
  val genreId: List<Int>? = null,
  val duration: String? = null,
  val imdbId: String? = null,
  val ageRating: String? = null,
  val tmdbScore: String? = null,
  val releaseDateRegion: ReleaseDateRegion,
)
