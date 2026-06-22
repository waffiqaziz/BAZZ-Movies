package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import kotlinx.coroutines.flow.Flow

interface GetMediaDetailUseCase {
  fun getMovieDetailWithUserRegion(movieId: Int): Flow<Outcome<MediaDetail>>
  fun getTvDetailWithUserRegion(tvId: Int): Flow<Outcome<MediaDetail>>
}
