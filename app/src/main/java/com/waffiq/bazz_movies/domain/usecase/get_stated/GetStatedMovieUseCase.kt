package com.waffiq.bazz_movies.domain.usecase.get_stated

import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.utils.resultstate.NetworkResult
import kotlinx.coroutines.flow.Flow

interface GetStatedMovieUseCase {
  suspend fun getStatedMovie(sessionId: String, movieId: Int): Flow<NetworkResult<Stated>>
}
