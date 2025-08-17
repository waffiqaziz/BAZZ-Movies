package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import kotlinx.coroutines.flow.Flow

interface GetTvDataWithUserRegionUseCase {
  suspend fun getTvDetailWithUserRegion(tvId: Int): Flow<Outcome<MediaDetail>>
  suspend fun getTvWatchProvidersWithUserRegion(tvId: Int): Flow<Outcome<WatchProvidersItem>>
}
