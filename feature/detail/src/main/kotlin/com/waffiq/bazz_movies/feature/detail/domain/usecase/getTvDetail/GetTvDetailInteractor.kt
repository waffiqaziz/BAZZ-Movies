package com.waffiq.bazz_movies.feature.detail.domain.usecase.getTvDetail

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.toLink
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaKeywordsMapper.toMediaDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTvDetailInteractor @Inject constructor(private val detailRepository: IDetailRepository) :
  GetTvDetailUseCase {

  override fun getTvDetail(tvId: Int, userRegion: String): Flow<Outcome<MediaDetail>> =
    combine(
      detailRepository.getTvDetail(tvId),
      detailRepository.getTvKeywords(tvId.toString()),
      detailRepository.getTvExternalIds(tvId),
    ) { detail, keywords, externalIds ->
      when (detail) {
        is Outcome.Success -> Outcome.Success(
          detail.data.toMediaDetail(
            userRegion = userRegion,
            mediaKeywords = (keywords as? Outcome.Success)?.data,
            externalIds = (externalIds as? Outcome.Success)?.data,
          ),
        )

        is Outcome.Error -> Outcome.Error(detail.message)

        is Outcome.Loading -> Outcome.Loading
      }
    }

  override fun getTvExternalIds(tvId: Int): Flow<Outcome<TvExternalIds>> =
    detailRepository.getTvExternalIds(tvId)

  override fun getTvTrailerLink(tvId: Int): Flow<Outcome<String>> =
    detailRepository.getTvTrailerLink(tvId).map { outcome ->
      when (outcome) {
        is Outcome.Success -> Outcome.Success(outcome.data.toLink())
        is Outcome.Error -> Outcome.Error(outcome.message)
        is Outcome.Loading -> Outcome.Loading
      }
    }

  override fun getTvCredits(tvId: Int): Flow<Outcome<MediaCredits>> =
    detailRepository.getTvCredits(tvId)

  override fun getTvWatchProviders(
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
