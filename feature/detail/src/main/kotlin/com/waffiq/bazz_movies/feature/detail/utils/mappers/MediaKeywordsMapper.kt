package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.MediaKeywordsResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.MovieKeywordsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.TvKeywordsResponse
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.toUsd
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreToJoinString
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformToGenreIDs
import com.waffiq.bazz_movies.core.utils.LanguageHelper.getLanguageName
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywords
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywordsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.utils.helpers.AgeRatingHelper.getAgeRating
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformDuration
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformTMDBScore
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ReleaseDateHelper.getReleaseDateRegion

object MediaKeywordsMapper {

  fun TvKeywordsResponse.toMediaKeywords(): MediaKeywords = MediaKeywords(
    id = id,
    keywords = keywords?.map { it?.toMediaKeywordsItem() }
  )

  fun MovieKeywordsResponse.toMediaKeywords(): MediaKeywords = MediaKeywords(
    id = id,
    keywords = keywords?.map { it?.toMediaKeywordsItem() }
  )

  private fun MediaKeywordsResponseItem.toMediaKeywordsItem(): MediaKeywordsItem =
    MediaKeywordsItem(
      id = id,
      name = name
    )

  fun TvDetail.toMediaDetail(
    userRegion: String,
    mediaKeywords: MediaKeywords?,
    externalIds: TvExternalIds?,
  ): MediaDetail = MediaDetail(
    id = id ?: 0,
    genre = transformListGenreToJoinString(listGenres),
    genreId = transformToGenreIDs(listGenres),
    imdbId = externalIds?.imdbId.orEmpty(),
    ageRating = getAgeRating(this, userRegion),
    tmdbScore = getTransformTMDBScore(voteAverage),
    releaseDateRegion = getReleaseDateRegion(this),
    status = status,
    budget = "-",
    revenue = "-",
    language = getLanguageName(originalLanguage),
    keywords = mediaKeywords?.keywords
  )

  fun MovieDetail.toMediaDetail(
    releaseDateRegion: ReleaseDateRegion,
    mediaKeywords: MediaKeywords?,
  ): MediaDetail = MediaDetail(
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
    keywords = mediaKeywords?.keywords
  )
}
