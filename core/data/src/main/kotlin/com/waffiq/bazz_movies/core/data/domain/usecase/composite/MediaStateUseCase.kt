package com.waffiq.bazz_movies.core.data.domain.usecase.composite

import com.waffiq.bazz_movies.core.common.Outcome
import com.waffiq.bazz_movies.core.model.user.MediaState
import kotlinx.coroutines.flow.Flow

interface MediaStateUseCase {
  fun getMovieStateWithUser(movieId: Int): Flow<Outcome<MediaState>>
  fun getTvStateWithUser(tvId: Int): Flow<Outcome<MediaState>>
}
