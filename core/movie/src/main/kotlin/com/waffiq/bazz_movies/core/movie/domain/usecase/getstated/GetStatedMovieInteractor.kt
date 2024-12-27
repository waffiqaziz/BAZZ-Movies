package com.waffiq.bazz_movies.core.movie.domain.usecase.getstated

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Stated
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStatedMovieInteractor @Inject constructor(
  private val getStatedIMovieRepository: IMoviesRepository
) : GetStatedMovieUseCase {
  override suspend fun getStatedMovie(
    sessionId: String,
    movieId: Int
  ): Flow<Outcome<Stated>> =
    getStatedIMovieRepository.getStatedMovie(sessionId, movieId)
}
