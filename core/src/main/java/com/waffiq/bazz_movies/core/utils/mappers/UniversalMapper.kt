package com.waffiq.bazz_movies.core.utils.mappers

import com.waffiq.bazz_movies.core.data.remote.responses.tmdb.ResultItemResponse
import com.waffiq.bazz_movies.core.domain.model.ResultItem

object UniversalMapper {

  fun ResultItemResponse.toResultItem(): ResultItem = ResultItem(
    firstAirDate = firstAirDate,
    overview = overview,
    originalLanguage = originalLanguage,
    listGenreIds = genreIds,
    posterPath = posterPath,
    backdropPath = backdropPath,
    mediaType = mediaType ?: "movie",
    originalName = originalName,
    popularity = popularity,
    voteAverage = voteAverage,
    name = name,
    id = id ?: 0,
    adult = adult ?: false,
    voteCount = voteCount ?: 0,
    originalTitle = originalTitle,
    video = video ?: false,
    title = title,
    releaseDate = releaseDate
  )
}
