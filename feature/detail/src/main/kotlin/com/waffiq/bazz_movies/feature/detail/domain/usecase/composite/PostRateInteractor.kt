package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod.PostMethodUseCase
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostRateInteractor @Inject constructor(
  private val postMethodUseCase: PostMethodUseCase,
  private val userPrefUseCase: UserPrefUseCase,
) : PostRateUseCase {

  override fun postMovieRate(
    rating: Float,
    movieId: Int,
  ): Flow<Outcome<Post>> = flow {
    val token = userPrefUseCase.getUser().first().token

    emitAll(
      postMethodUseCase.postMovieRate(
        sessionId = token,
        rating = rating,
        movieId = movieId
      )
    )
  }

  override fun postTvRate(
    rating: Float,
    tvId: Int,
  ): Flow<Outcome<Post>> = flow {
    val token = userPrefUseCase.getUser().first().token

    emitAll(
      postMethodUseCase.postTvRate(
        sessionId = token,
        rating = rating,
        tvId = tvId
      )
    )
  }
}
