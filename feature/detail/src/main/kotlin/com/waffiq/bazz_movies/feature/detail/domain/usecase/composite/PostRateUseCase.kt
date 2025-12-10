package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import kotlinx.coroutines.flow.Flow

interface PostRateUseCase {
  fun postMovieRate(rating: Float, movieId: Int): Flow<Outcome<Post>>
  fun postTvRate(rating: Float, tvId: Int): Flow<Outcome<Post>>
}
