package com.waffiq.bazz_movies.utils.mappers

import com.waffiq.bazz_movies.data.remote.responses.tmdb.search.KnownForItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.search.ResultsItemSearchResponse
import com.waffiq.bazz_movies.domain.model.search.KnownForItem
import com.waffiq.bazz_movies.domain.model.search.ResultsItemSearch

object SearchMapper {
  fun ResultsItemSearchResponse.toResultItemSearch() = ResultsItemSearch(
    mediaType = mediaType ?: "movie",
    listKnownFor = listKnownFor?.map { it.toKnownForItem() },
    knownForDepartment = knownForDepartment,
    popularity = popularity ?: 0.0,
    name = name,
    profilePath = profilePath,
    id = id ?: 0,
    adult = adult ?: false,
    overview = overview,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    video = video ?: false,
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