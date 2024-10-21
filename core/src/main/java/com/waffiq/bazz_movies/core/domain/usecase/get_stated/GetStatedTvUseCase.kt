package com.waffiq.bazz_movies.core.domain.usecase.get_stated

import com.waffiq.bazz_movies.core.domain.model.Stated
import com.waffiq.bazz_movies.core.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

interface GetStatedTvUseCase {
  suspend fun getStatedTv(sessionId: String, tvId: Int): Flow<NetworkResult<Stated>>
}