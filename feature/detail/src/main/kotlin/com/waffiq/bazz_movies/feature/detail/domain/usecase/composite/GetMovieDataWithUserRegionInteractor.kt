package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getMovieDetail.GetMovieDetailUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class GetMovieDataWithUserRegionInteractor @Inject constructor(
  private val getMovieDetailUseCase: GetMovieDetailUseCase,
  private val userPrefUseCase: UserPrefUseCase,
) : GetMovieDataWithUserRegionUseCase {

  override fun getMovieDetailWithUserRegion(movieId: Int): Flow<Outcome<MediaDetail>> =
    userPrefUseCase.getUserRegionPref()
      .take(1)
      .flatMapConcat { userRegion ->
        getMovieDetailUseCase.getMovieDetail(movieId, userRegion)
      }

  override fun getMovieWatchProvidersWithUserRegion(movieId: Int): Flow<Outcome<WatchProvidersItem>> =
    userPrefUseCase.getUserRegionPref()
      .take(1)
      .flatMapConcat { userRegion ->
        getMovieDetailUseCase.getMovieWatchProviders(userRegion.uppercase(), movieId)
      }
}
