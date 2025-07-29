package com.waffiq.bazz_movies.core.movie.domain.usecase.getstated

import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import kotlinx.coroutines.flow.Flow

fun interface GetMovieStateUseCase {
  suspend fun getMovieState(sessionId: String, movieId: Int): Flow<Outcome<MediaState>>
}
