package com.waffiq.bazz_movies.core.mappers

import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem

object MediaItemMapper {

  fun MediaResponseItem.toMediaItem(): MediaItem = MediaItem(
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
