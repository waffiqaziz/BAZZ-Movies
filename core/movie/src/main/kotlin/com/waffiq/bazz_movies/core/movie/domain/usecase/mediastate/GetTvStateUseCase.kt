package com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate

import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import kotlinx.coroutines.flow.Flow

fun interface GetTvStateUseCase {
  fun getTvState(sessionId: String, tvId: Int): Flow<Outcome<MediaState>>
}
