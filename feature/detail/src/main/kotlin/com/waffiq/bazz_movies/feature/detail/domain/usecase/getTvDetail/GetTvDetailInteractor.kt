package com.waffiq.bazz_movies.feature.detail.domain.usecase.getTvDetail

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.toLink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTvDetailInteractor @Inject constructor(private val detailRepository: IDetailRepository) :
  GetTvDetailUseCase {

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

  override fun getTvRecommendationPagingData(tvId: Int): Flow<PagingData<MediaItem>> =
    detailRepository.getTvRecommendationPagingData(tvId)
}
