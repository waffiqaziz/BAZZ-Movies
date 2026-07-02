package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.toUsd
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreIdsToJoinName
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreToJoinString
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformToGenreIDs
import com.waffiq.bazz_movies.core.utils.LanguageHelper.getLanguageName
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvDetail
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
import com.waffiq.bazz_movies.feature.detail.utils.helpers.AgeRatingHelper.getAgeRating
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformDuration
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformTMDBScore
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.toLink
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ReleaseDateHelper.getReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.utils.mappers.WatchProvidersMapper.toWatchProvidersState

object BasicMediaDetailMapper {

  fun TvDetail.toMediaDetail(userRegion: String): MediaDetail =
    MediaDetail(
      id = id ?: 0,
      imdbId = externalIds?.imdbId.orEmpty(),
      ageRating = getAgeRating(this, userRegion),
      credits = credits,
      genre = transformListGenreToJoinString(listGenres),
      genreId = transformToGenreIDs(listGenres),
      keywords = keywords?.keywords,
      language = getLanguageName(originalLanguage),
      releaseDateRegion = getReleaseDateRegion(this),
      status = status,
      tmdbScore = getTransformTMDBScore(voteAverage),
      trailer = videos?.toLink(),
      watchProviders = watchProviders?.toWatchProvidersState(userRegion)
        ?: WatchProvidersUiState.Error("No watch providers available"),

      // tv related
      totalEpisodes = numberOfEpisodes,
      totalSeasons = numberOfSeasons,

      // updated data
      title = titleHandler(name, originalName),
      popularity = popularity?.toFloat() ?: 0f,
      backdrop = backdropPath.toString(),
      poster = posterPath.toString(),
      overview = overview.toString(),
      releaseDate = firstAirDate.orEmpty(),
    )

  fun MovieDetail.toMediaDetail(releaseDateRegion: ReleaseDateRegion): MediaDetail =
    MediaDetail(
      id = id ?: 0,
      imdbId = imdbId,
      ageRating = getAgeRating(this, releaseDateRegion.regionRelease),
      credits = credits,
      genre = transformListGenreToJoinString(listGenres), // for view
      genreId = transformToGenreIDs(listGenres),
      keywords = keywords?.keywords,
      language = getLanguageName(originalLanguage),
      releaseDateRegion = releaseDateRegion,
      status = status,
      tmdbScore = getTransformTMDBScore(voteAverage),
      trailer = videos?.toLink(),
      watchProviders = watchProviders?.toWatchProvidersState(releaseDateRegion.regionRelease)
        ?: WatchProvidersUiState.Error("No watch providers available"),

      // movie related
      budget = toUsd(budget),
      duration = getTransformDuration(runtime),
      revenue = toUsd(revenue),
      belongsToCollection = belongsToCollection,

      // updated data
      title = titleHandler(title, originalTitle),
      popularity = popularity?.toFloat() ?: 0f,
      backdrop = backdropPath.toString(),
      poster = posterPath.toString(),
      overview = overview.toString(),
      releaseDate = releaseDate.orEmpty(),
    )

  fun Favorite.refreshWith(data: MediaDetail): Favorite =
    copy(
      genre = transformListGenreIdsToJoinName(data.genreId.orEmpty()).orEmpty(),
      backDrop = data.backdrop,
      poster = data.poster,
      overview = data.overview,
      title = data.title,
      releaseDate = data.releaseDate,
      popularity = data.popularity.toDouble(),
      rating = data.tmdbScore?.toFloat() ?: 0f,
      lastUpdated = System.currentTimeMillis(),
    )
}
