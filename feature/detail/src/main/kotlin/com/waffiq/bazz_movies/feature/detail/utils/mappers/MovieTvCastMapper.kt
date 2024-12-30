package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.domain.MovieTvCastItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.castcrew.MovieTvCastItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.castcrew.MovieTvCrewItemResponse
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
