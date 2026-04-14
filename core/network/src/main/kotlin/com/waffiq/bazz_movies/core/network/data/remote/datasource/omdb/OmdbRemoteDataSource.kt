package com.waffiq.bazz_movies.core.network.data.remote.datasource.omdb

import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.OMDbApiService
import com.waffiq.bazz_movies.core.network.utils.helpers.SafeApiCallHelper.executeApiCall
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OmdbRemoteDataSource @Inject constructor(
  private val omDbApiService: OMDbApiService,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : OmdbRemoteDataSourceInterface {

  override fun getOMDbDetails(imdbId: String): Flow<NetworkResult<OMDbDetailsResponse>> =
    executeApiCall(
      apiCall = { omDbApiService.getOMDbDetails(imdbId) },
      ioDispatcher = ioDispatcher,
    )
}
