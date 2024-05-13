package com.waffiq.bazz_movies.domain.model.detail

import com.waffiq.bazz_movies.data.remote.response.tmdb.MovieTvCastItemResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.MovieTvCrewItemResponse

data class MovieTvCredits(
  val cast: List<MovieTvCastItemResponse>,
  val id: Int? = null,
  val crew: List<MovieTvCrewItemResponse>
)
