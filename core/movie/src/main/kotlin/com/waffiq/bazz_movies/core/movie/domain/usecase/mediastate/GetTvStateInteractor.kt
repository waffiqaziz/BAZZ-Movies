package com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate

import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTvStateInteractor @Inject constructor(
  private val getStateTvRepository: IMoviesRepository,
) : GetTvStateUseCase {

  override fun getTvState(sessionId: String, tvId: Int): Flow<Outcome<MediaState>> =
    getStateTvRepository.getTvState(sessionId, tvId)
}
