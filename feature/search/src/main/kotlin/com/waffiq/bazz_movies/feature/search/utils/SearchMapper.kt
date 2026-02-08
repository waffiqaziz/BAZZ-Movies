package com.waffiq.bazz_movies.feature.search.utils

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.KnownForItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponseItem
import com.waffiq.bazz_movies.feature.search.domain.model.KnownForItem
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem

object SearchMapper {
  fun MultiSearchResponseItem.toMultiSearchItem() =
    MultiSearchItem(
      mediaType = mediaType ?: MOVIE_MEDIA_TYPE,
      listKnownFor = listKnownFor?.map { it.toKnownForItem() },
      knownForDepartment = knownForDepartment,
      popularity = popularity ?: 0.0,
      name = name,
      profilePath = profilePath,
      id = id ?: 0,
      adult = adult == true,
      overview = overview,
      originalLanguage = originalLanguage,
      originalTitle = originalTitle,
      video = video == true,
      title = title,
      listGenreIds = listGenreIds,
      posterPath = posterPath,
      backdropPath = backdropPath,
      releaseDate = releaseDate,
      voteAverage = voteAverage ?: 0.0,
      voteCount = voteCount ?: 0.0,
      firstAirDate = firstAirDate,
      listOriginCountry = listOriginCountry,
      originalName = originalName,
    )

  private fun KnownForItemResponse.toKnownForItem() =
    KnownForItem(
      overview = overview,
      originalLanguage = originalLanguage,
      originalTitle = originalTitle,
      video = video,
      title = title,
      genreIds = genreIds,
      posterPath = posterPath,
      backdropPath = backdropPath,
      releaseDate = releaseDate,
      mediaType = mediaType,
      popularity = popularity,
      voteAverage = voteAverage,
      id = id,
      adult = adult,
      voteCount = voteCount,
      firstAirDate = firstAirDate,
      originCountry = originCountry,
      originalName = originalName,
      name = name,
    )
}
