package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getMovieDetail.GetMovieDetailUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMovieDataWithUserRegionInteractor @Inject constructor(
  private val getMovieDetailUseCase: GetMovieDetailUseCase,
  private val userPrefUseCase: UserPrefUseCase,
) : GetMovieDataWithUserRegionUseCase {

  override fun getMovieDetailWithUserRegion(movieId: Int): Flow<Outcome<MediaDetail>> = flow {
    val userRegion = userPrefUseCase.getUserRegionPref().first()
    emitAll(getMovieDetailUseCase.getMovieDetail(movieId, userRegion))
  }

  override fun getMovieWatchProvidersWithUserRegion(movieId: Int): Flow<Outcome<WatchProvidersItem>> =
    flow {
      val userRegion = userPrefUseCase.getUserRegionPref().first()
      emitAll(getMovieDetailUseCase.getMovieWatchProviders(userRegion.uppercase(), movieId))
    }
}
