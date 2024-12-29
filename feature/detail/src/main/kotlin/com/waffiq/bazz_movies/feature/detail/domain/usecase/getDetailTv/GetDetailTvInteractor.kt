package com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailTv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreToJoinString
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformToGenreIDs
import com.waffiq.bazz_movies.feature.detail.domain.model.DetailMovieTvUsed
import com.waffiq.bazz_movies.feature.detail.domain.model.MovieTvCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ExternalTvID
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.utils.helpers.AgeRatingHelper.getAgeRating
import com.waffiq.bazz_movies.feature.detail.utils.helpers.DetailMovieTvHelper.getTransformTMDBScore
import com.waffiq.bazz_movies.feature.detail.utils.helpers.DetailMovieTvHelper.toLink
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ReleaseDateHelper.getReleaseDateRegion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDetailTvInteractor @Inject constructor(
  private val detailRepository: IDetailRepository
) : GetDetailTvUseCase {

  /** notes: for tv, imdb will null and get later using [getExternalTvId] **/
  override suspend fun getDetailTv(
    tvId: Int,
    userRegion: String
  ): Flow<Outcome<DetailMovieTvUsed>> =
    detailRepository.getDetailTv(tvId).map { outcome ->
      when (outcome) {
        is Outcome.Success -> {
          Outcome.Success(
            DetailMovieTvUsed(
              id = outcome.data.id ?: 0,
              genre = transformListGenreToJoinString(outcome.data.listGenres), // for view
              genreId = transformToGenreIDs(outcome.data.listGenres),
              duration = outcome.data.status, // for tv, duration set as status
              imdbId = "",
              ageRating = getAgeRating(outcome.data, userRegion),
              tmdbScore = getTransformTMDBScore(outcome.data.voteAverage),
              releaseDateRegion = getReleaseDateRegion(outcome.data)
            )
          )
        }

        is Outcome.Error -> Outcome.Error(outcome.message)
        is Outcome.Loading -> Outcome.Loading
      }
    }

  override suspend fun getExternalTvId(tvId: Int): Flow<Outcome<ExternalTvID>> =
    detailRepository.getExternalTvId(tvId)

  override suspend fun getTrailerLinkTv(tvId: Int): Flow<Outcome<String>> =
    detailRepository.getTrailerLinkTv(tvId).map { outcome ->
      when (outcome) {
        is Outcome.Success -> Outcome.Success(outcome.data.toLink())
        is Outcome.Error -> Outcome.Error(outcome.message)
        is Outcome.Loading -> Outcome.Loading
      }
    }

  override suspend fun getCreditTv(tvId: Int): Flow<Outcome<MovieTvCredits>> =
    detailRepository.getCreditTv(tvId)

  override fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>> =
    detailRepository.getPagingTvRecommendation(tvId)
}
