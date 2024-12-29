package com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailOmdb

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import kotlinx.coroutines.flow.Flow

interface GetDetailOMDbUseCase {
  suspend fun getDetailOMDb(imdbId: String): Flow<Outcome<OMDbDetails>>
}
