package com.waffiq.bazz_movies.domain.model.detail

import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.cast_crew.MovieTvCastItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.cast_crew.MovieTvCrewItemResponse

data class MovieTvCredits(
  val cast: List<MovieTvCastItemResponse>,
  val id: Int? = null,
  val crew: List<MovieTvCrewItemResponse>
)