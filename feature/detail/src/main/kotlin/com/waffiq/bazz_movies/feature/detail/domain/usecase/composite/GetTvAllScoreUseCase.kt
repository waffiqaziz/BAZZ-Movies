package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import kotlinx.coroutines.flow.Flow

interface GetTvAllScoreUseCase {
  fun getTvAllScore(tvId: Int): Flow<Outcome<OMDbDetails>>
}
