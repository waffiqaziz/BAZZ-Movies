package com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailOmdb

import com.waffiq.bazz_movies.core.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.core.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

interface GetDetailOMDbUseCase {
  suspend fun getDetailOMDb(imdbId: String): Flow<NetworkResult<OMDbDetails>>
}
