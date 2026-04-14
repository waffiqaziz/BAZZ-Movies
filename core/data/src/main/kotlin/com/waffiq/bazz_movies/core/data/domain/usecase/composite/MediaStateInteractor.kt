package com.waffiq.bazz_movies.core.data.domain.usecase.composite

import com.waffiq.bazz_movies.core.data.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.data.domain.repository.ITvRepository
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class MediaStateInteractor @Inject constructor(
  private val moviesRepository: IMoviesRepository,
  private val tvRepository: ITvRepository,
  private val userRepository: IUserRepository,
) : MediaStateUseCase {

  private fun getToken() = userRepository.getUserToken().take(1)

  override fun getMovieStateWithUser(movieId: Int): Flow<Outcome<MediaState>> =
    getToken().flatMapConcat { token ->
      moviesRepository.getMovieState(token, movieId)
    }

  override fun getTvStateWithUser(tvId: Int): Flow<Outcome<MediaState>> =
    getToken().flatMapConcat { token ->
      tvRepository.getTvState(sessionId = token, tvId = tvId)
    }
}
