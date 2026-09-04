package com.waffiq.bazz_movies.feature.search.utils

import com.waffiq.bazz_movies.core.common.MediaType.Companion.fromValue
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.navigation.MediaArgs
import com.waffiq.bazz_movies.navigation.PersonArgs

object MultiSearchItemMapper {

  fun MultiSearchItem.toMediaArgs() =
    MediaArgs(
      posterPath = posterPath,
      backdropPath = backdropPath,
      firstAirDate = firstAirDate,
      releaseDate = releaseDate,
      overview = overview,
      title = title,
      name = name,
      originalTitle = originalTitle,
      originalName = originalName,
      mediaType = fromValue(mediaType),
      listGenreIds = listGenreIds,
      id = id,
    )

  fun MultiSearchItem.toPersonArgs() =
    PersonArgs(
      id = id,
      profilePath = profilePath,
      name = name,
      originalName = originalName,
    )
}
