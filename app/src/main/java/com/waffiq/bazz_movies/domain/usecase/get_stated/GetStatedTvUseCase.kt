package com.waffiq.bazz_movies.domain.usecase.get_stated

import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface GetStatedTvUseCase {
  suspend fun getStatedTv(sessionId: String, tvId: Int): Flow<NetworkResult<Stated>>
}