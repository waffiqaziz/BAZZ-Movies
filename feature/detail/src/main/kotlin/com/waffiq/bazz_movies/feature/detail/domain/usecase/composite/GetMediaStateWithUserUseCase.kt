package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import kotlinx.coroutines.flow.Flow

interface GetMediaStateWithUserUseCase {
  suspend fun getMovieStateWithUser(movieId: Int): Flow<Outcome<MediaState>>
  suspend fun getTvStateWithUser(tvId: Int): Flow<Outcome<MediaState>>
}
