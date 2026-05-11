package com.waffiq.bazz_movies.feature.detail.domain.model

import com.waffiq.bazz_movies.core.models.MediaCastItem

data class MediaCredits(
  val cast: List<MediaCastItem>,
  val id: Int? = null,
  val crew: List<MediaCrewItem>,
)
