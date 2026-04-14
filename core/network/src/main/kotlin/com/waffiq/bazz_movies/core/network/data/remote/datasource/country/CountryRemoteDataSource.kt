package com.waffiq.bazz_movies.core.network.data.remote.datasource.country

import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.network.data.remote.responses.countryip.CountryIPResponse
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.CountryIPApiService
import com.waffiq.bazz_movies.core.network.utils.helpers.SafeApiCallHelper.executeApiCall
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CountryRemoteDataSource @Inject constructor(
  private val countryIPApiService: CountryIPApiService,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : CountryRemoteDataSourceInterface {

  override fun getCountryCode(): Flow<NetworkResult<CountryIPResponse>> =
    executeApiCall(
      apiCall = { countryIPApiService.getIP() },
      ioDispatcher = ioDispatcher,
    )
}
