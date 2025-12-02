package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import kotlinx.coroutines.flow.Flow

interface GetMediaStateWithUserUseCase {
  fun getMovieStateWithUser(movieId: Int): Flow<Outcome<MediaState>>
  fun getTvStateWithUser(tvId: Int): Flow<Outcome<MediaState>>
}
