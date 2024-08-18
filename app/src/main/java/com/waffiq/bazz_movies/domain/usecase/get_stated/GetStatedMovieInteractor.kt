package com.waffiq.bazz_movies.domain.usecase.get_stated

import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

class GetStatedMovieInteractor(
  private val getStatedIMovieRepository: IMoviesRepository
): GetStatedMovieUseCase {
  override suspend fun getStatedMovie(
    sessionId: String,
    movieId: Int
  ): Flow<NetworkResult<Stated>> =
    getStatedIMovieRepository.getStatedMovie(sessionId,movieId)
}