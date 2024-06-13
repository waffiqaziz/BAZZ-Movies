package com.waffiq.bazz_movies.utils.mappers

import com.waffiq.bazz_movies.data.remote.responses.tmdb.search.KnownForItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.search.ResultsItemSearchResponse
import com.waffiq.bazz_movies.domain.model.search.KnownForItem
import com.waffiq.bazz_movies.domain.model.search.ResultsItemSearch

object SearchMapper {
  fun ResultsItemSearchResponse.toResultItemSearch() = ResultsItemSearch(
    mediaType = mediaType,
    listKnownFor = listKnownFor?.map { it.toKnownForItem() },
    knownForDepartment = knownForDepartment,
    popularity = popularity,
    name = name,
    profilePath = profilePath,
    id = id,
    adult = adult,
    overview = overview,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    video = video,
    title = title,
    listGenreIds = listGenreIds,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
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