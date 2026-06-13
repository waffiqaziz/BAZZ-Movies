package com.waffiq.bazz_movies.feature.search.utils

import com.waffiq.bazz_movies.core.models.MediaCastItem
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem

object MultiSearchItemMapper {

  fun MultiSearchItem.toMediaItem() =
    MediaItem(
      posterPath = posterPath,
      backdropPath = backdropPath,
      firstAirDate = firstAirDate,
      releaseDate = releaseDate,
      overview = overview,
      title = title,
      name = name,
      originalTitle = originalTitle,
      originalName = originalName,
      mediaType = mediaType,
      listGenreIds = listGenreIds,
      id = id,
    )

  fun MultiSearchItem.toMediaCastItem() =
    MediaCastItem(
      id = id,
      profilePath = profilePath,
      name = name,
      originalName = originalName,
    )
}
