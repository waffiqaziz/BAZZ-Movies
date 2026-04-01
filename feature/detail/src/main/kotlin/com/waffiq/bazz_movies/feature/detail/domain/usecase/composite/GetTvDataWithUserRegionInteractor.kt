package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaKeywordsMapper.toMediaDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class GetTvDataWithUserRegionInteractor @Inject constructor(
  private val detailRepository: IDetailRepository,
  private val userRepository: IUserRepository,
) : GetTvDataWithUserRegionUseCase {

  private fun getRegion() = userRepository.getUserRegionPref().take(1)

  override fun getTvDetailWithUserRegion(tvId: Int): Flow<Outcome<MediaDetail>> =
    getRegion().flatMapConcat { region ->
      combine(
        detailRepository.getTvDetail(tvId),
        detailRepository.getTvKeywords(tvId.toString()),
        detailRepository.getTvExternalIds(tvId),
      ) { detail, keywords, externalIds ->
        when (detail) {
          is Outcome.Success -> Outcome.Success(
            detail.data.toMediaDetail(
              userRegion = region,
              mediaKeywords = (keywords as? Outcome.Success)?.data,
              externalIds = (externalIds as? Outcome.Success)?.data,
            ),
          )

          is Outcome.Error -> Outcome.Error(detail.message)

          is Outcome.Loading -> Outcome.Loading
        }
      }
    }

  override fun getTvWatchProvidersWithUserRegion(tvId: Int): Flow<Outcome<WatchProvidersItem>> =
    getRegion().flatMapConcat { region ->
      detailRepository.getWatchProviders("tv", tvId).map { outcome ->
        when (outcome) {
          is Outcome.Success -> {
            val countryProvider = outcome.data.results?.get(region)
            if (countryProvider != null) {
              Outcome.Success(countryProvider)
            } else {
              Outcome.Error("No watch provider found for country code: $region")
            }
          }

          is Outcome.Error -> Outcome.Error(outcome.message)

          is Outcome.Loading -> Outcome.Loading
        }
      }
    }
}
