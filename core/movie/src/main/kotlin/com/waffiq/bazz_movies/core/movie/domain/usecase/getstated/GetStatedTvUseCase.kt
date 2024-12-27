package com.waffiq.bazz_movies.core.movie.domain.usecase.getstated

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Stated
import kotlinx.coroutines.flow.Flow

interface GetStatedTvUseCase {
  suspend fun getStatedTv(sessionId: String, tvId: Int): Flow<Outcome<Stated>>
}
