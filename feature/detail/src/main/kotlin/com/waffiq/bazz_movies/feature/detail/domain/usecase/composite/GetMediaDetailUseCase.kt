package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import kotlinx.coroutines.flow.Flow

interface GetMediaDetailUseCase {
  fun getMovieDetailWithUserRegion(movieId: Int): Flow<Outcome<MediaDetail>>
  fun getMovieWatchProvidersWithUserRegion(movieId: Int): Flow<Outcome<WatchProvidersItem>>
  fun getMovieCredits(movieId: Int): Flow<Outcome<MediaCredits>>

  fun getTvCredits(tvId: Int): Flow<Outcome<MediaCredits>>
  fun getTvDetailWithUserRegion(tvId: Int): Flow<Outcome<MediaDetail>>
  fun getTvWatchProvidersWithUserRegion(tvId: Int): Flow<Outcome<WatchProvidersItem>>
}
