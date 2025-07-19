package com.waffiq.bazz_movies.feature.detail.domain.usecase.getMovieDetail

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreToJoinString
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformToGenreIDs
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.utils.helpers.AgeRatingHelper.getAgeRating
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformDuration
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformTMDBScore
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.toLink
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ReleaseDateHelper.getReleaseDateRegion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDetailMovieInteractor @Inject constructor(
  private val detailRepository: IDetailRepository,
) : GetMovieDetailUseCase {

  override suspend fun getMovieDetail(
    movieId: Int,
    userRegion: String,
  ): Flow<Outcome<MediaDetail>> =
    detailRepository.getMovieDetail(movieId).map { outcome ->
      when (outcome) {
        is Outcome.Success -> {
          val releaseDateRegion = getReleaseDateRegion(outcome.data, userRegion)

          Outcome.Success(
            MediaDetail(
              id = outcome.data.id ?: 0,
              genre = transformListGenreToJoinString(outcome.data.listGenres), // for view
              genreId = transformToGenreIDs(outcome.data.listGenres),
              duration = getTransformDuration(outcome.data.runtime),
              imdbId = outcome.data.imdbId,
              ageRating = getAgeRating(outcome.data, releaseDateRegion.regionRelease),
              tmdbScore = getTransformTMDBScore(outcome.data.voteAverage),
              releaseDateRegion = releaseDateRegion
            )
          )
        }

        is Outcome.Error -> Outcome.Error(outcome.message)
        is Outcome.Loading -> Outcome.Loading
      }
    }

  override suspend fun getMovieVideoLinks(movieId: Int): Flow<Outcome<String>> =
    detailRepository.getMovieTrailerLink(movieId).map { outcome ->
      when (outcome) {
        is Outcome.Success -> Outcome.Success(outcome.data.toLink())
        is Outcome.Error -> Outcome.Error(outcome.message)
        is Outcome.Loading -> Outcome.Loading
      }
    }

  override suspend fun getMovieCredits(movieId: Int): Flow<Outcome<MediaCredits>> =
    detailRepository.getMovieCredits(movieId)

  override suspend fun getMovieWatchProviders(
    countryCode: String,
    movieId: Int,
  ): Flow<Outcome<WatchProvidersItem>> =
    detailRepository.getWatchProviders("movie", movieId).map { outcome ->
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

  override fun getMovieRecommendationPagingData(movieId: Int): Flow<PagingData<MediaItem>> =
    detailRepository.getMovieRecommendationPagingData(movieId)
}
