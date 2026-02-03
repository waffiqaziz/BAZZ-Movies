package com.waffiq.bazz_movies.feature.detail.domain.model

import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywordsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDateRegion

data class MediaDetail(
  val id: Int,
  val genre: String? = null,
  val genreId: List<Int>? = null,
  val duration: String? = null,
  val imdbId: String? = null,
  val ageRating: String? = null,
  val tmdbScore: String? = null,
  val releaseDateRegion: ReleaseDateRegion,
  val status: String? = null,
  val budget: String? = null,
  val revenue: String? = null,
  val language: String? = null,
  val keywords: List<MediaKeywordsItem?>? = null
)
