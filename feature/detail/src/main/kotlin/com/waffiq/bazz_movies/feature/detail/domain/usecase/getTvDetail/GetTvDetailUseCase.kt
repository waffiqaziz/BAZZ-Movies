package com.waffiq.bazz_movies.feature.detail.domain.usecase.getTvDetail

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import kotlinx.coroutines.flow.Flow

interface GetTvDetailUseCase {
  suspend fun getTvDetail(tvId: Int, userRegion: String): Flow<Outcome<MediaDetail>>
  suspend fun getTvExternalIds(tvId: Int): Flow<Outcome<TvExternalIds>>
  suspend fun getTvCredits(tvId: Int): Flow<Outcome<MediaCredits>>
  suspend fun getTvTrailerLink(tvId: Int): Flow<Outcome<String>>
  suspend fun getTvWatchProviders(
    countryCode: String,
    tvId: Int,
  ): Flow<Outcome<WatchProvidersItem>>

  fun getTvRecommendationPagingData(tvId: Int): Flow<PagingData<MediaItem>>
}
