package com.waffiq.bazz_movies.core.movie.domain.usecase.getstated

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStatedMovieInteractor @Inject constructor(
  private val getStatedIMovieRepository: IMoviesRepository
) : GetMovieStateUseCase {
  override suspend fun getMovieState(
    sessionId: String,
    movieId: Int
  ): Flow<Outcome<MediaState>> =
    getStatedIMovieRepository.getStatedMovie(sessionId, movieId)
}
