package com.waffiq.bazz_movies.core.movie.domain.usecase.getstated

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Stated
import kotlinx.coroutines.flow.Flow

interface GetStatedMovieUseCase {
  suspend fun getStatedMovie(sessionId: String, movieId: Int): Flow<Outcome<Stated>>
}
