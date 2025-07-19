package com.waffiq.bazz_movies.core.movie.domain.usecase.getstated

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStatedTvInteractor @Inject constructor(
  private val getStatedTvRepository: IMoviesRepository
) : GetTvStateUseCase {
  override suspend fun getTvState(sessionId: String, tvId: Int): Flow<Outcome<MediaState>> =
    getStatedTvRepository.getStatedTv(sessionId, tvId)
}
