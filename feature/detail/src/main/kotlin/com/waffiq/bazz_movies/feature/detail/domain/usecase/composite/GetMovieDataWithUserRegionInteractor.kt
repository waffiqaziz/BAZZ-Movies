package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getMovieDetail.GetMovieDetailUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetMovieDataWithUserRegionInteractor @Inject constructor(
  private val getMovieDetailUseCase: GetMovieDetailUseCase,
  private val userPrefUseCase: UserPrefUseCase,
) : GetMovieDataWithUserRegionUseCase {

  override suspend fun getMovieDetailWithUserRegion(movieId: Int): Flow<Outcome<MediaDetail>> =
    userPrefUseCase.getUserRegionPref().first().let { userRegion ->
      getMovieDetailUseCase.getMovieDetail(movieId, userRegion)
    }

  override suspend fun getMovieWatchProvidersWithUserRegion(movieId: Int): Flow<Outcome<WatchProvidersItem>> =
    userPrefUseCase.getUserRegionPref().first().let { userRegion ->
      getMovieDetailUseCase.getMovieWatchProviders(userRegion.uppercase(), movieId)
    }
}
