package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.common.Outcome
import com.waffiq.bazz_movies.core.model.user.PostResult
import kotlinx.coroutines.flow.Flow

interface PostRateUseCase {
  fun postMovieRate(rating: Float, movieId: Int): Flow<Outcome<PostResult>>
  fun postTvRate(rating: Float, tvId: Int): Flow<Outcome<PostResult>>
}
