package com.waffiq.bazz_movies.domain.usecase.get_detail_omdb

import com.waffiq.bazz_movies.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.utils.resultstate.NetworkResult
import kotlinx.coroutines.flow.Flow

interface GetDetailOMDbUseCase {
  suspend fun getDetailOMDb(imdbId: String): Flow<NetworkResult<OMDbDetails>>
}
