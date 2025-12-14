package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod.PostMethodUseCase
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class PostRateInteractor @Inject constructor(
  private val postMethodUseCase: PostMethodUseCase,
  private val userPrefUseCase: UserPrefUseCase,
) : PostRateUseCase {

  override fun postMovieRate(
    rating: Float,
    movieId: Int,
  ): Flow<Outcome<Post>> =
    userPrefUseCase.getUserToken()
      .take(1)
      .flatMapConcat { token ->
        postMethodUseCase.postMovieRate(
          sessionId = token,
          rating = rating,
          movieId = movieId
        )
      }

  override fun postTvRate(
    rating: Float,
    tvId: Int,
  ): Flow<Outcome<Post>> =
    userPrefUseCase.getUserToken()
      .take(1)
      .flatMapConcat { token ->
        postMethodUseCase.postTvRate(
          sessionId = token,
          rating = rating,
          tvId = tvId
        )
      }
}
