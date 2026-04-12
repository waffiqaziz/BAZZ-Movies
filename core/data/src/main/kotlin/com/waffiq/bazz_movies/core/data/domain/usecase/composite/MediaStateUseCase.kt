package com.waffiq.bazz_movies.core.data.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import kotlinx.coroutines.flow.Flow

interface MediaStateUseCase {
  fun getMovieStateWithUser(movieId: Int): Flow<Outcome<MediaState>>
  fun getTvStateWithUser(tvId: Int): Flow<Outcome<MediaState>>
}
