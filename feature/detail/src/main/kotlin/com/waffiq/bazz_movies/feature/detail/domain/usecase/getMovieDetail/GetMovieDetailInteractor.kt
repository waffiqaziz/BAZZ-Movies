package com.waffiq.bazz_movies.feature.detail.domain.usecase.getMovieDetail

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.toLink
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ReleaseDateHelper.getReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaKeywordsMapper.toMediaDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMovieDetailInteractor @Inject constructor(
  private val detailRepository: IDetailRepository,
) : GetMovieDetailUseCase {

  override fun getMovieDetail(movieId: Int, userRegion: String): Flow<Outcome<MediaDetail>> =
    detailRepository.getMovieDetail(movieId)
      .combine(detailRepository.getMovieKeywords(movieId.toString())) { detail, keywords ->
        when (detail) {
          is Outcome.Success -> {
            Outcome.Success(
              detail.data.toMediaDetail(
                releaseDateRegion = getReleaseDateRegion(detail.data, userRegion),
                mediaKeywords = (keywords as? Outcome.Success)?.data,
              ),
            )
          }

          is Outcome.Error -> Outcome.Error(detail.message)

          is Outcome.Loading -> Outcome.Loading
        }
      }

  override fun getMovieVideoLinks(movieId: Int): Flow<Outcome<String>> =
    detailRepository.getMovieTrailerLink(movieId).map { outcome ->
      when (outcome) {
        is Outcome.Success -> Outcome.Success(outcome.data.toLink())
        is Outcome.Error -> Outcome.Error(outcome.message)
        is Outcome.Loading -> Outcome.Loading
      }
    }

  override fun getMovieCredits(movieId: Int): Flow<Outcome<MediaCredits>> =
    detailRepository.getMovieCredits(movieId)

  override fun getMovieWatchProviders(
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
