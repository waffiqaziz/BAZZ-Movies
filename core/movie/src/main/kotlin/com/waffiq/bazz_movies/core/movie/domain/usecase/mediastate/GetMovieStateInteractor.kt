package com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate

import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieStateInteractor @Inject constructor(
  private val getStateMovieRepository: IMoviesRepository,
) : GetMovieStateUseCase {

  override suspend fun getMovieState(sessionId: String, movieId: Int): Flow<Outcome<MediaState>> =
    getStateMovieRepository.getMovieState(sessionId, movieId)
}
