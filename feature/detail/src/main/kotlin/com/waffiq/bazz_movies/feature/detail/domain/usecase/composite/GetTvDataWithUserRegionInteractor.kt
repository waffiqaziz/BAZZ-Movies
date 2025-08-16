package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getTvDetail.GetTvDetailUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetTvDataWithUserRegionInteractor @Inject constructor(
  private val getTvDetailUseCase: GetTvDetailUseCase,
  private val userPrefUseCase: UserPrefUseCase,
) : GetTvDataWithUserRegionUseCase {

  override suspend fun getTvDetailWithUserRegion(tvId: Int): Flow<Outcome<MediaDetail>> =
    userPrefUseCase.getUserRegionPref().flatMapLatest { userRegion ->
      getTvDetailUseCase.getTvDetail(tvId, userRegion)
    }

  override suspend fun getTvWatchProvidersWithUserRegion(tvId: Int): Flow<Outcome<WatchProvidersItem>> =
    userPrefUseCase.getUserRegionPref().flatMapLatest { userRegion ->
      getTvDetailUseCase.getTvWatchProviders(userRegion.uppercase(), tvId)
    }
}
