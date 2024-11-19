package com.waffiq.bazz_movies.core.movie.domain.usecase.getstated

import com.waffiq.bazz_movies.core.data.Stated
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

interface GetStatedMovieUseCase {
  suspend fun getStatedMovie(sessionId: String, movieId: Int): Flow<NetworkResult<Stated>>
}
