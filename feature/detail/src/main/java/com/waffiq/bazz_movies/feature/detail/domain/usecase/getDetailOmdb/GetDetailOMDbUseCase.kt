package com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailOmdb

import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

interface GetDetailOMDbUseCase {
  suspend fun getDetailOMDb(imdbId: String): Flow<NetworkResult<OMDbDetails>>
}
