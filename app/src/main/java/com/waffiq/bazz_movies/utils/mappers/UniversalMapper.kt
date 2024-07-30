package com.waffiq.bazz_movies.utils.mappers

import com.waffiq.bazz_movies.data.remote.responses.tmdb.ResultItemResponse
import com.waffiq.bazz_movies.domain.model.ResultItem

object UniversalMapper {

  fun ResultItemResponse.toResultItem(): ResultItem = ResultItem(
    firstAirDate = firstAirDate,
    overview = overview,
    originalLanguage = originalLanguage,
    listGenreIds = genreIds,
    posterPath = posterPath,
    backdropPath = backdropPath,
    mediaType = mediaType,
    originalName = originalName,
    popularity = popularity,
    voteAverage = voteAverage,
    name = name,
    id = id,
    adult = adult,
    voteCount = voteCount,
    originalTitle = originalTitle,
    video = video,
    title = title,
    releaseDate = releaseDate
  )
}