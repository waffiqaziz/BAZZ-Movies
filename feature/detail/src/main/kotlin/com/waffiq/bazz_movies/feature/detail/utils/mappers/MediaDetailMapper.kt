package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.domain.GenresItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.GenresResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.ProductionCountriesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ProductionCompaniesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.SpokenLanguagesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia.VideoResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia.VideoResponseItem
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.SpokenLanguagesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.VideoItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaCreditsMapper.toMediaCastItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaCreditsMapper.toMediaCrewItem

object MediaDetailMapper {

  fun VideoResponse.toVideo() = Video(
    id = id,
    results = results.map { it.toVideoItem() }
  )

  private fun VideoResponseItem.toVideoItem() = VideoItem(
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

  fun MediaCreditsResponse.toMediaCredits() = MediaCredits(
    cast = cast.map { it.toMediaCastItem() },
    id = id,
    crew = crew.map { it.toMediaCrewItem() },
  )

  fun GenresResponseItem.toGenresItem() = GenresItem(
    name = name,
    id = id
  )

  fun SpokenLanguagesResponseItem.toSpokenLanguagesItem() = SpokenLanguagesItem(
    name = name,
    iso6391 = iso6391,
    englishName = englishName
  )

  fun ProductionCountriesResponseItem.toProductionCountriesItem() = ProductionCountriesItem(
    iso31661 = iso31661,
    name = name,
    type = type,
    iso6391 = iso6391,
    certification = certification
  )

  fun ProductionCompaniesResponseItem.toProductionCompaniesItem() = ProductionCompaniesItem(
    logoPath = logoPath,
    name = name,
    id = id,
    originCountry = originCountry
  )
}
