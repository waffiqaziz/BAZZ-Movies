package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.data.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.data.domain.repository.ITvRepository
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.PostResult
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostRateInteractor @Inject constructor(
  private val moviesRepository: IMoviesRepository,
  private val tvRepository: ITvRepository,
  private val userRepository: IUserRepository,
) : PostRateUseCase {

  private suspend fun getToken() = userRepository.getUserToken().first()

  override fun postMovieRate(rating: Float, movieId: Int): Flow<Outcome<PostResult>> =
    flow {
      emitAll(moviesRepository.postMovieRate(getToken(), rating, movieId))
    }

  override fun postTvRate(rating: Float, tvId: Int): Flow<Outcome<PostResult>> =
    flow {
      emitAll(tvRepository.postTvRate(getToken(), rating, tvId))
    }
}
