package com.waffiq.bazz_movies.core.network.data.remote.datasource.omdb

import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

fun interface OmdbRemoteDataSourceInterface {
  fun getOMDbDetails(imdbId: String): Flow<NetworkResult<OMDbDetailsResponse>>
}
