package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.AggregateCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.CastResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ContentRatingsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ContentRatingsResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.CreatedByResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.CrewResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.DetailTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ExternalIdResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.JobsResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.LastEpisodeToAirResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.NetworksResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.NextEpisodeToAirResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.RolesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.SeasonsResponseItem
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCastItem
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCrewItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ContentRatings
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ContentRatingsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.CreatedByItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.JobsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.LastEpisodeToAir
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.NetworksItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.NextEpisodeToAir
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.RolesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.SeasonsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toGenresItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toSpokenLanguagesItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toVideo
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaKeywordsMapper.toMediaKeywords
import com.waffiq.bazz_movies.feature.detail.utils.mappers.WatchProvidersMapper.toWatchProviders

@Suppress("TooManyFunctions")
object TvMapper {

  fun DetailTvResponse.toTvDetail() =
    TvDetail(
      originalLanguage = originalLanguage,
      numberOfEpisodes = numberOfEpisodes,
      listNetworksItem = networks?.map { it?.toNetworksItem() },
      type = type,
      backdropPath = backdropPath,
      credits = aggregateCredits?.toCredits(),
      listGenres = genres?.map { it?.toGenresItem() },
      keywords = keywords?.toMediaKeywords(),
      popularity = popularity,
      listProductionCountriesItem =
      productionCountries?.map { it?.toProductionCountriesItem() },
      id = id,
      numberOfSeasons = numberOfSeasons,
      voteCount = voteCount,
      firstAirDate = firstAirDate,
      overview = overview,
      listSeasonsItem = seasons?.map { it?.toSeasonsItem() },
      listLanguages = languages,
      listCreatedByItem = createdBy?.map { it?.toCreatedByItem() },
      lastEpisodeToAir = lastEpisodeToAir?.toLastEpisodeToAir(),
      posterPath = posterPath,
      listOriginCountry = originCountry,
      listSpokenLanguagesItem = spokenLanguages?.map { it?.toSpokenLanguagesItem() },
      listProductionCompaniesItem =
      productionCompanies?.map { it?.toProductionCompaniesItem() },
      originalName = originalName,
      voteAverage = voteAverage,
      name = name,
      tagline = tagline,
      listEpisodeRunTime = episodeRunTime,
      contentRatings = contentRatings?.toContentRatings(),
      adult = adult,
      nextEpisodeToAir = nextEpisodeToAir?.toNextEpisodeToAir(),
      inProduction = inProduction,
      lastAirDate = lastAirDate,
      homepage = homepage,
      status = status,
      externalIds = externalIds?.toExternalTvID(),
      videos = videos?.toVideo(),
      watchProviders = watchProviders?.toWatchProviders(),
    )

  private fun NetworksResponseItem.toNetworksItem() =
    NetworksItem(
      logoPath = logoPath,
      name = name,
      id = id,
      originCountry = originCountry,
    )

  fun AggregateCreditsResponse.toCredits() =
    MediaCredits(
      cast = cast?.map { it?.toMediaCastItem() ?: MediaCastItem() } ?: emptyList(),
      crew = crew?.map { it?.toMediaCrewItem() ?: MediaCrewItem() } ?: emptyList(),
    )

  fun CastResponseItem.toMediaCastItem() =
    MediaCastItem(
      totalEpisodeCount = totalEpisodeCount,
      gender = gender,
      knownForDepartment = knownForDepartment,
      originalName = originalName,
      popularity = popularity,
      roles = roles?.map { it?.toRolesItem() },
      character = roles?.firstOrNull()?.character,
      name = name,
      profilePath = profilePath,
      id = id,
      adult = adult,
      order = order,
    )

  fun RolesResponseItem.toRolesItem() =
    RolesItem(
      character = character,
      episodeCount = episodeCount,
      creditId = creditId,
    )

  fun CrewResponseItem.toMediaCrewItem() =
    MediaCrewItem(
      totalEpisodeCount = totalEpisodeCount,
      gender = gender,
      knownForDepartment = knownForDepartment,
      originalName = originalName,
      popularity = popularity,
      jobs = jobs?.map { it?.toJobsItem() },
      name = name,
      profilePath = profilePath,
      id = id,
      adult = adult,
      department = department,
    )

  fun JobsResponseItem.toJobsItem() =
    JobsItem(
      episodeCount = episodeCount,
      creditId = creditId,
      job = job,
    )

  private fun ContentRatingsResponse.toContentRatings() =
    ContentRatings(
      contentRatingsItem = contentRatings?.map { it?.toContentRatingsItem() },
    )

  private fun ContentRatingsResponseItem.toContentRatingsItem() =
    ContentRatingsItem(
      descriptors = descriptors,
      iso31661 = iso31661,
      rating = rating,
    )

  private fun SeasonsResponseItem.toSeasonsItem() =
    SeasonsItem(
      airDate = airDate,
      overview = overview,
      episodeCount = episodeCount,
      name = name,
      seasonNumber = seasonNumber,
      id = id,
      posterPath = posterPath,
    )

  private fun CreatedByResponseItem.toCreatedByItem() =
    CreatedByItem(
      gender = gender,
      creditId = creditId,
      name = name,
      profilePath = profilePath,
      id = id,
    )

  private fun LastEpisodeToAirResponse.toLastEpisodeToAir() =
    LastEpisodeToAir(
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
      voteCount = voteCount,
    )

  fun ExternalIdResponse.toExternalTvID() =
    TvExternalIds(
      imdbId = imdbId,
      freebaseMid = freebaseMid,
      tvdbId = tvdbId,
      freebaseId = freebaseId,
      id = id,
      twitterId = twitterId,
      tvrageId = tvrageId,
      facebookId = facebookId,
      instagramId = instagramId,
    )

  fun NextEpisodeToAirResponse.toNextEpisodeToAir() =
    NextEpisodeToAir(
      id = id,
      name = name,
      airDate = airDate,
    )
}
