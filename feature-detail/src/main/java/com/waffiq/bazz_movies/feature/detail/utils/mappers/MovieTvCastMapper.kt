package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.data.remote.responses.tmdb.detail_movie_tv.cast_crew.MovieTvCastItemResponse
import com.waffiq.bazz_movies.core.data.remote.responses.tmdb.detail_movie_tv.cast_crew.MovieTvCrewItemResponse
import com.waffiq.bazz_movies.core.domain.model.MovieTvCastItem
import com.waffiq.bazz_movies.feature.detail.domain.model.MovieTvCrewItem

object MovieTvCastMapper {

  fun MovieTvCastItemResponse.toMovieTvCastItem() = MovieTvCastItem(
    castId = castId,
    character = character,
    gender = gender,
    creditId = creditId,
    knownForDepartment = knownForDepartment,
    originalName = originalName,
    popularity = popularity,
    name = name,
    profilePath = profilePath,
    id = id,
    adult = adult,
    order = order,
  )

  fun MovieTvCrewItemResponse.toMovieTvCrewItem() = MovieTvCrewItem(
    gender = gender,
    creditId = creditId,
    knownForDepartment = knownForDepartment,
    originalName = originalName,
    popularity = popularity,
    name = name,
    profilePath = profilePath,
    id = id,
    adult = adult,
    department = department,
    job = job,
  )
}
