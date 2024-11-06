package com.waffiq.bazz_movies.core.utils.mappers

import com.waffiq.bazz_movies.core.data.remote.responses.tmdb.search.KnownForItemResponse
import com.waffiq.bazz_movies.core.data.remote.responses.tmdb.search.ResultsItemSearchResponse
import com.waffiq.bazz_movies.core.domain.model.search.KnownForItem
import com.waffiq.bazz_movies.core.domain.model.search.ResultsItemSearch
import com.waffiq.bazz_movies.core.utils.common.Constants.MOVIE_MEDIA_TYPE

object SearchMapper {
  fun ResultsItemSearchResponse.toResultItemSearch() = ResultsItemSearch(
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
    originalName = originalName
  )

  private fun KnownForItemResponse.toKnownForItem() = KnownForItem(
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
    name = name
  )
}
