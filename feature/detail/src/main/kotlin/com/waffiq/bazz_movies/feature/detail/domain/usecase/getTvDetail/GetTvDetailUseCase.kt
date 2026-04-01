package com.waffiq.bazz_movies.feature.detail.domain.usecase.getTvDetail

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import kotlinx.coroutines.flow.Flow

interface GetTvDetailUseCase {
  fun getTvExternalIds(tvId: Int): Flow<Outcome<TvExternalIds>>
  fun getTvCredits(tvId: Int): Flow<Outcome<MediaCredits>>
  fun getTvTrailerLink(tvId: Int): Flow<Outcome<String>>
  fun getTvRecommendationPagingData(tvId: Int): Flow<PagingData<MediaItem>>
}
