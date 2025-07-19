package com.waffiq.bazz_movies.core.movie.domain.usecase.getstated

import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import kotlinx.coroutines.flow.Flow

interface GetTvStateUseCase {
  suspend fun getTvState(sessionId: String, tvId: Int): Flow<Outcome<MediaState>>
}
