package com.waffiq.bazz_movies.core.movie.domain.usecase.get_stated

import com.waffiq.bazz_movies.core.model.Stated
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStatedMovieInteractor @Inject constructor(
  private val getStatedIMovieRepository: IMoviesRepository
) : GetStatedMovieUseCase {
  override suspend fun getStatedMovie(
    sessionId: String,
    movieId: Int
  ): Flow<NetworkResult<Stated>> =
    getStatedIMovieRepository.getStatedMovie(sessionId, movieId)
}
