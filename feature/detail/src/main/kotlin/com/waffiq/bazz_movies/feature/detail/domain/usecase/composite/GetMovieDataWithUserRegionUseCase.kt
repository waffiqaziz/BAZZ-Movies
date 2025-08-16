package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import kotlinx.coroutines.flow.Flow

interface GetMovieDataWithUserRegionUseCase {
  suspend fun getMovieDetailWithUserRegion(movieId: Int): Flow<Outcome<MediaDetail>>
  suspend fun getMovieWatchProvidersWithUserRegion(movieId: Int): Flow<Outcome<WatchProvidersItem>>
}
