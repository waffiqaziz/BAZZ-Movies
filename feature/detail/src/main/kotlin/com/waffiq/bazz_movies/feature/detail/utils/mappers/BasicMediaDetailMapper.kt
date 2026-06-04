package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.toUsd
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreIdsToJoinName
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreToJoinString
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformToGenreIDs
import com.waffiq.bazz_movies.core.utils.LanguageHelper.getLanguageName
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywords
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.utils.helpers.AgeRatingHelper.getAgeRating
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformDuration
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformTMDBScore
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ReleaseDateHelper.getReleaseDateRegion

object BasicMediaDetailMapper {

  fun TvDetail.toMediaDetail(
    userRegion: String,
    mediaKeywords: MediaKeywords?,
    externalIds: TvExternalIds?,
  ): MediaDetail =
    MediaDetail(
      id = id ?: 0,
      genre = transformListGenreToJoinString(listGenres),
      genreId = transformToGenreIDs(listGenres),
      imdbId = externalIds?.imdbId.orEmpty(),
      ageRating = getAgeRating(this, userRegion),
      tmdbScore = getTransformTMDBScore(voteAverage),
      releaseDateRegion = getReleaseDateRegion(this),
      status = status,
      language = getLanguageName(originalLanguage),
      keywords = mediaKeywords?.keywords,
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

  fun MovieDetail.toMediaDetail(
    releaseDateRegion: ReleaseDateRegion,
    mediaKeywords: MediaKeywords?,
  ): MediaDetail =
    MediaDetail(
      id = id ?: 0,
      genre = transformListGenreToJoinString(listGenres), // for view
      genreId = transformToGenreIDs(listGenres),
      duration = getTransformDuration(runtime),
      imdbId = imdbId,
      ageRating = getAgeRating(this, releaseDateRegion.regionRelease),
      tmdbScore = getTransformTMDBScore(voteAverage),
      releaseDateRegion = releaseDateRegion,
      status = status,
      budget = toUsd(budget),
      revenue = toUsd(revenue),
      language = getLanguageName(originalLanguage),
      keywords = mediaKeywords?.keywords,

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
