package com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate

import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import kotlinx.coroutines.flow.Flow

fun interface GetMovieStateUseCase {
  fun getMovieState(sessionId: String, movieId: Int): Flow<Outcome<MediaState>>
}
