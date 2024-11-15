package com.waffiq.bazz_movies.feature.detail.domain.model

import com.waffiq.bazz_movies.core.model.MovieTvCastItem

data class MovieTvCredits(
  val cast: List<MovieTvCastItem>,
  val id: Int? = null,
  val crew: List<MovieTvCrewItem>
)
