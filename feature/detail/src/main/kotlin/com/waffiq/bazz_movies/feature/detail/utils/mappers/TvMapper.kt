package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.domain.GenresItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ContentRatingsItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ContentRatingsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.CreatedByItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.DetailTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ExternalIdResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.LastEpisodeToAirResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.NetworksItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.SeasonsItemResponse
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ContentRatings
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ContentRatingsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.CreatedByItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.LastEpisodeToAir
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.NetworksItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.SeasonsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toGenresItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toSpokenLanguagesItem

object TvMapper {

  fun DetailTvResponse.toTvDetail() = TvDetail(
    originalLanguage = originalLanguage,
    numberOfEpisodes = numberOfEpisodes,
    listNetworksItem = networksResponse?.map { it?.toNetworksItem() },
    type = type,
    backdropPath = backdropPath,
    listGenres = genres?.map { it?.toGenresItem() },
    popularity = popularity,
    listProductionCountriesItem = productionCountriesResponse?.map { it?.toProductionCountriesItem() },
    id = id,
    numberOfSeasons = numberOfSeasons,
    voteCount = voteCount,
    firstAirDate = firstAirDate,
    overview = overview,
    listSeasonsItem = seasonsResponse?.map { it?.toSeasonsItem() },
    listLanguages = languages,
    listCreatedByItem = createdByResponse?.map { it?.toCreatedByItem() },
    lastEpisodeToAir = lastEpisodeToAirResponse?.toLastEpisodeToAir(),
    posterPath = posterPath,
    listOriginCountry = originCountry,
    listSpokenLanguagesItem = spokenLanguagesResponse?.map { it?.toSpokenLanguagesItem() },
    listProductionCompaniesItem = productionCompaniesResponse?.map { it?.toProductionCompaniesItem() },
    originalName = originalName,
    voteAverage = voteAverage,
    name = name,
    tagline = tagline,
    listEpisodeRunTime = episodeRunTime,
    contentRatings = contentRatingsResponse?.toContentRatings(),
    adult = adult,
    nextEpisodeToAir = nextEpisodeToAir,
    inProduction = inProduction,
    lastAirDate = lastAirDate,
    homepage = homepage,
    status = status,
  )

  private fun ContentRatingsResponse.toContentRatings() = ContentRatings(
    contentRatingsItem = contentRatingsItemResponse?.map { it?.toContentRatingsItem() }
  )

  private fun ContentRatingsItemResponse.toContentRatingsItem() = ContentRatingsItem(
    descriptors = descriptors,
    iso31661 = iso31661,
    rating = rating,
  )

  private fun NetworksItemResponse.toNetworksItem() = NetworksItem(
    logoPath = logoPath,
    name = name,
    id = id,
    originCountry = originCountry
  )

  private fun SeasonsItemResponse.toSeasonsItem() = SeasonsItem(
    airDate = airDate,
    overview = overview,
    episodeCount = episodeCount,
    name = name,
    seasonNumber = seasonNumber,
    id = id,
    posterPath = posterPath
  )

  private fun CreatedByItemResponse.toCreatedByItem() = CreatedByItem(
    gender = gender,
    creditId = creditId,
    name = name,
    profilePath = profilePath,
    id = id
  )

  private fun LastEpisodeToAirResponse.toLastEpisodeToAir() = LastEpisodeToAir(
    productionCode = productionCode,
    airDate = airDate,
    overview = overview,
    episodeNumber = episodeNumber,
    episodeType = episodeType,
    showId = showId,
    voteAverage = voteAverage,
    name = name,
    seasonNumber = seasonNumber,
    runtime = runtime,
    id = id,
    stillPath = stillPath,
    voteCount = voteCount
  )

  fun ExternalIdResponse.toExternalTvID() = TvExternalIds(
    imdbId = imdbId,
    freebaseMid = freebaseMid,
    tvdbId = tvdbId,
    freebaseId = freebaseId,
    id = id,
    twitterId = twitterId,
    tvrageId = tvrageId,
    facebookId = facebookId,
    instagramId = instagramId
  )
}
