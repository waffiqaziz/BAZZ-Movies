package com.waffiq.bazz_movies.core.movie.domain.usecase.getstated

import com.waffiq.bazz_movies.core.domain.Stated
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

interface GetStatedTvUseCase {
  suspend fun getStatedTv(sessionId: String, tvId: Int): Flow<NetworkResult<Stated>>
}
