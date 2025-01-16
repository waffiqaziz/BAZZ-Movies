package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.domain.GenresItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.GenresItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.ProductionCountriesItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.castcrew.MovieTvCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv.ProductionCompaniesItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv.SpokenLanguagesItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.videomedia.VideoItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.videomedia.VideoResponse
import com.waffiq.bazz_movies.feature.detail.domain.model.MovieTvCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.SpokenLanguagesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.VideoItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MovieTvCastMapper.toMovieTvCastItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MovieTvCastMapper.toMovieTvCrewItem

object DetailMovieTvMapper {

  fun VideoResponse.toVideo() = Video(
    id = id,
    results = results.map { it.toVideoItem() }
  )

  private fun VideoItemResponse.toVideoItem() = VideoItem(
    site = site,
    size = size,
    iso31661 = iso31661,
    name = name,
    official = official,
    id = id,
    publishedAt = publishedAt,
    type = type,
    iso6391 = iso6391,
    key = key
  )

  fun MovieTvCreditsResponse.toMovieTvCredits() = MovieTvCredits(
    cast = cast.map { it.toMovieTvCastItem() },
    id = id,
    crew = crew.map { it.toMovieTvCrewItem() },
  )

  fun GenresItemResponse.toGenresItem() = GenresItem(
    name = name,
    id = id
  )

  fun SpokenLanguagesItemResponse.toSpokenLanguagesItem() = SpokenLanguagesItem(
    name = name,
    iso6391 = iso6391,
    englishName = englishName
  )

  fun ProductionCountriesItemResponse.toProductionCountriesItem() = ProductionCountriesItem(
    iso31661 = iso31661,
    name = name,
    type = type,
    iso6391 = iso6391,
    certification = certification
  )

  fun ProductionCompaniesItemResponse.toProductionCompaniesItem() = ProductionCompaniesItem(
    logoPath = logoPath,
    name = name,
    id = id,
    originCountry = originCountry
  )
}
