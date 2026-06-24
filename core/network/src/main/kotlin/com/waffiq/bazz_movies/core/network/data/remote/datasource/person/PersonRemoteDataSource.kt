package com.waffiq.bazz_movies.core.network.data.remote.datasource.person

import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.PersonApiService
import com.waffiq.bazz_movies.core.network.utils.helpers.SafeApiCallHelper.executeApiCall
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRemoteDataSource @Inject constructor(
  private val personApiService: PersonApiService,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : PersonRemoteDataSourceInterface {

  override fun getPersonDetails(id: Int): Flow<NetworkResult<DetailPersonResponse>> =
    executeApiCall(
      apiCall = { personApiService.getPersonDetails(id) },
      ioDispatcher = ioDispatcher,
    )
}
