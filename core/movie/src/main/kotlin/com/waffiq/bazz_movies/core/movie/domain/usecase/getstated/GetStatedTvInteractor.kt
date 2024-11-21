package com.waffiq.bazz_movies.core.movie.domain.usecase.getstated

import com.waffiq.bazz_movies.core.data.Stated
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStatedTvInteractor @Inject constructor(
  private val getStatedTvRepository: IMoviesRepository
) : GetStatedTvUseCase {
  override suspend fun getStatedTv(sessionId: String, tvId: Int): Flow<NetworkResult<Stated>> =
    getStatedTvRepository.getStatedTv(sessionId, tvId)
}
