package com.waffiq.bazz_movies.core.domain.model.detail

import com.waffiq.bazz_movies.core.domain.model.person.MovieTvCastItem
import com.waffiq.bazz_movies.core.domain.model.person.MovieTvCrewItem

data class MovieTvCredits(
  val cast: List<MovieTvCastItem>,
  val id: Int? = null,
  val crew: List<MovieTvCrewItem>
)
