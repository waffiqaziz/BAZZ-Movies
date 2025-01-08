package com.waffiq.bazz_movies.core.mappers

import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.ResultItemResponse

object ResultItemMapper {

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
    adult = adult == true,
    voteCount = voteCount ?: 0,
    originalTitle = originalTitle,
    video = video == true,
    title = title,
    releaseDate = releaseDate,
    originCountry = originCountry
  )
}
