package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetMovieStateUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetTvStateUseCase
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMediaStateWithUserInteractor @Inject constructor(
  private val getMovieStateUseCase: GetMovieStateUseCase,
  private val getTvStateUseCase: GetTvStateUseCase,
  private val userPrefUseCase: UserPrefUseCase,
) : GetMediaStateWithUserUseCase {

  override fun getMovieStateWithUser(movieId: Int): Flow<Outcome<MediaState>> = flow {
    val token = userPrefUseCase.getUserToken().first()
    emitAll(getMovieStateUseCase.getMovieState(token, movieId))
  }

  override fun getTvStateWithUser(tvId: Int): Flow<Outcome<MediaState>> = flow {
    val token = userPrefUseCase.getUserToken().first()
    emitAll(getTvStateUseCase.getTvState(sessionId = token, tvId = tvId))
  }
}
