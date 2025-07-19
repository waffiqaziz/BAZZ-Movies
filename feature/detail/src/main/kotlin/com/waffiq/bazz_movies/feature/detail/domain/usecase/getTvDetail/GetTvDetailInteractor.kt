package com.waffiq.bazz_movies.feature.detail.domain.usecase.getTvDetail

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreToJoinString
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformToGenreIDs
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.utils.helpers.AgeRatingHelper.getAgeRating
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformTMDBScore
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.toLink
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ReleaseDateHelper.getReleaseDateRegion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTvDetailInteractor @Inject constructor(
  private val detailRepository: IDetailRepository,
) : GetTvDetailUseCase {

  /** notes: for tv, imdb will null and get later using [getTvExternalIds] **/
  override suspend fun getTvDetail(
    tvId: Int,
    userRegion: String,
  ): Flow<Outcome<MediaDetail>> =
    detailRepository.getTvDetail(tvId).map { outcome ->
      when (outcome) {
        is Outcome.Success -> {
          Outcome.Success(
            MediaDetail(
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

  override suspend fun getTvExternalIds(tvId: Int): Flow<Outcome<TvExternalIds>> =
    detailRepository.getTvExternalIds(tvId)

  override suspend fun getTvTrailerLink(tvId: Int): Flow<Outcome<String>> =
    detailRepository.getTvTrailerLink(tvId).map { outcome ->
      when (outcome) {
        is Outcome.Success -> Outcome.Success(outcome.data.toLink())
        is Outcome.Error -> Outcome.Error(outcome.message)
        is Outcome.Loading -> Outcome.Loading
      }
    }

  override suspend fun getTvCredits(tvId: Int): Flow<Outcome<MediaCredits>> =
    detailRepository.getTvCredits(tvId)

  override suspend fun getTvWatchProviders(
    countryCode: String,
    tvId: Int,
  ): Flow<Outcome<WatchProvidersItem>> =
    detailRepository.getWatchProviders("tv", tvId).map { outcome ->
      when (outcome) {
        is Outcome.Success -> {
          val countryProvider = outcome.data.results?.get(countryCode)
          if (countryProvider != null) {
            Outcome.Success(countryProvider)
          } else {
            Outcome.Error("No watch provider found for country code: $countryCode")
          }
        }

        is Outcome.Error -> Outcome.Error(outcome.message)
        is Outcome.Loading -> Outcome.Loading
      }
    }

  override fun getTvRecommendationPagingData(tvId: Int): Flow<PagingData<MediaItem>> =
    detailRepository.getTvRecommendationPagingData(tvId)
}
