package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.BelongsToCollectionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.DetailCollectionsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.PartsResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponseItemValue
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.BelongsToCollection
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.DetailCollections
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.PartsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDates
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItemValue
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toGenresItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toMediaCredits
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toSpokenLanguagesItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toVideo
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaKeywordsMapper.toMediaKeywords
import com.waffiq.bazz_movies.feature.detail.utils.mappers.WatchProvidersMapper.toWatchProviders

object MovieMapper {

  fun DetailMovieResponse.toDetailMovie() =
    MovieDetail(
      originalLanguage = originalLanguage,
      imdbId = imdbId,
      video = video,
      title = title,
      backdropPath = backdropPath,
      credits = credits?.toMediaCredits(),
      revenue = revenue,
      listGenres = listGenresItemResponse?.map { it?.toGenresItem() },
      keywords = keywords?.toMediaKeywords(),
      popularity = popularity,
      releaseDates = releaseDatesResponse?.toReleaseDates(),
      listProductionCountriesItem = listProductionCountriesItemResponse?.map {
        it?.toProductionCountriesItem()
      },
      id = id,
      voteCount = voteCount,
      budget = budget,
      overview = overview,
      originalTitle = originalTitle,
      runtime = runtime,
      posterPath = posterPath,
      listSpokenLanguagesItem =
      listSpokenLanguagesItemResponse?.map { it?.toSpokenLanguagesItem() },
      listProductionCompaniesItem = listProductionCompaniesItemResponse?.map {
        it?.toProductionCompaniesItem()
      },
      releaseDate = releaseDate,
      voteAverage = voteAverage,
      belongsToCollection = belongsToCollectionResponse?.toBelongsToCollection(),
      tagline = tagline,
      adult = adult,
      homepage = homepage,
      status = status,
      videos = videos?.toVideo(),
      watchProviders = watchProviders?.toWatchProviders(),
    )

  private fun ReleaseDatesResponse.toReleaseDates() =
    ReleaseDates(
      listReleaseDatesItem = listReleaseDatesResponseItem?.map { it?.toReleaseDatesItem() },
    )

  private fun ReleaseDatesResponseItem.toReleaseDatesItem() =
    ReleaseDatesItem(
      iso31661 = iso31661,
      listReleaseDatesItemValue =
      listReleaseDateResponseItemValue?.map { it.toReleaseDatesItemValue() },
    )

  private fun ReleaseDatesResponseItemValue.toReleaseDatesItemValue() =
    ReleaseDatesItemValue(
      descriptors = descriptors,
      note = note,
      type = type,
      iso6391 = iso6391,
      certification = certification,
      releaseDate = releaseDate,
    )

  private fun BelongsToCollectionResponse.toBelongsToCollection() =
    BelongsToCollection(
      backdropPath = backdropPath,
      name = name,
      id = id,
      posterPath = posterPath,
    )

  fun DetailCollectionsResponse.toDetailCollections() =
    DetailCollections(
      backdropPath = backdropPath,
      overview = overview,
      originalLanguage = originalLanguage,
      originalName = originalName,
      name = name,
      parts = parts?.map { it?.toPartsItem() },
      id = id,
      posterPath = posterPath,
    )

  private fun PartsResponseItem.toPartsItem() =
    PartsItem(
      id = id,
      overview = overview,
      originalLanguage = originalLanguage,
      video = video,
      genreIds = genreIds,
      posterPath = posterPath,
      backdropPath = backdropPath,
      mediaType = mediaType,
      releaseDate = releaseDate,
      originalTitle = originalTitle,
      popularity = popularity,
      voteAverage = voteAverage,
      title = title,
      adult = adult,
      voteCount = voteCount,
    )

  fun PartsItem.toMediaItem() =
    MediaItem(
      id = id ?: 0,
      overview = overview,
      originalLanguage = originalLanguage,
      video = video == true,
      listGenreIds = genreIds,
      posterPath = posterPath,
      backdropPath = backdropPath,
      mediaType = mediaType ?: MOVIE_MEDIA_TYPE,
      releaseDate = releaseDate,
      originalName = originalTitle,
      popularity = popularity?.toDouble(),
      voteAverage = voteAverage,
      name = title,
      adult = adult == true,
      voteCount = voteCount ?: 0,
    )
}
