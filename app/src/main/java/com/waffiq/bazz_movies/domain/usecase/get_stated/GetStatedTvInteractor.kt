package com.waffiq.bazz_movies.domain.usecase.get_stated

import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.utils.resultstate.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStatedTvInteractor @Inject constructor(
  private val getStatedTvRepository: IMoviesRepository
) : GetStatedTvUseCase {
  override suspend fun getStatedTv(sessionId: String, tvId: Int): Flow<NetworkResult<Stated>> =
    getStatedTvRepository.getStatedTv(sessionId, tvId)
}
