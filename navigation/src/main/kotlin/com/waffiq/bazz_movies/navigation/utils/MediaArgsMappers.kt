package com.waffiq.bazz_movies.navigation.utils

import com.waffiq.bazz_movies.core.common.MediaType.Companion.fromValue
import com.waffiq.bazz_movies.core.common.value
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.navigation.MediaArgs

fun MediaArgs.toMediaItem() =
  MediaItem(
    name = name,
    originalName = originalName,
    title = title,
    originalTitle = originalTitle,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    firstAirDate = firstAirDate,
    overview = overview,
    originalLanguage = originalLanguage,
    listGenreIds = listGenreIds,
    mediaType = mediaType.value,
    id = id,
    video = video,
  )

fun MediaItem.toMediaArgs() =
  MediaArgs(
    name = name,
    originalName = originalName,
    title = title,
    originalTitle = originalTitle,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    firstAirDate = firstAirDate,
    overview = overview,
    originalLanguage = originalLanguage,
    listGenreIds = listGenreIds,
    mediaType = fromValue(mediaType),
    id = id,
    video = video,
  )
